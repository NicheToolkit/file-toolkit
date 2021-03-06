package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.configure.FileServiceProperties;
import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.error.FileErrorStatus;
import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.helper.FileServerHelper;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.model.FileRequest;
import io.github.nichetoolkit.file.video.VideoHttpRequestHandler;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.github.nichetoolkit.rest.identity.IdentityUtils;
import io.github.nichetoolkit.rest.util.*;
import io.github.nichetoolkit.rice.RestId;
import io.github.nichetoolkit.rice.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * <p>FileServiceImpl</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
public abstract class FileSupperService implements FileService {

    @Autowired
    protected FileServiceProperties serviceProperties;

    @Autowired
    protected FileIndexService fileIndexService;

    @Autowired
    protected FileChunkService fileChunkService;
    
    protected VideoHttpRequestHandler videoHttpRequestHandler;
    
    @PostConstruct
    public void initVideoHttpRequestHandler() {
        videoHttpRequestHandler = ContextUtils.getBean(VideoHttpRequestHandler.class);
    }

    @Override
    public void remove(FileFilter fileFilter) throws RestException {
        if (GeneralUtils.isEmpty(fileFilter)) {
            return;
        }
        RestPage<? extends RestId<String>> restPage;
        if (fileFilter.isChunk()) {
            restPage = fileChunkService.queryAllWithFilter(fileFilter);
        } else {
            restPage = fileIndexService.queryAllWithFilter(fileFilter);
        }
        List<String> fileIdList = restPage.getItems().stream().map(RestId::getId).distinct().collect(Collectors.toList());
        if (GeneralUtils.isNotEmpty(fileIdList)) {
            removeAll(fileIdList);
            if (fileFilter.isChunk()) {
                if (fileFilter.isDelete()) {
                    fileChunkService.deleteAll(fileIdList);
                } else {
                    fileChunkService.removeAll(fileIdList);
                }
            }  else {
                if (fileFilter.isDelete()) {
                    fileIndexService.deleteAll(fileIdList);
                } else {
                    fileIndexService.removeAll(fileIdList);
                }
            }
        }

    }

    abstract public void removeAll(List<String> fileIdList)  throws RestException;

    @Override
    @Async
    public void remove(String fileId, Boolean chunk, Boolean delete, Boolean rename) throws RestException {
        if (GeneralUtils.isEmpty(fileId)) {
            return;
        }
        if (chunk) {
            if (delete) {
                fileChunkService.deleteById(fileId);
            } else {
                fileChunkService.removeById(fileId);
            }
        } else {
            if (delete) {
                fileIndexService.deleteById(fileId);
            } else {
                fileIndexService.removeById(fileId);
            }
        }
        if (rename) {
            renameById(fileId, fileId.concat("_del"));
        }
        removeById(fileId);
    }

    abstract public void renameById(String fileId,String rename)  throws RestException;

    abstract public void removeById(String fileId)  throws RestException;

    abstract public InputStream getById(String fileId)  throws RestException;

    abstract public void putById(String fileId,InputStream inputStream)  throws RestException;

    abstract public void margeById(String fileId, Collection<String> fileIdList)  throws RestException;

    @Override
    public void download(File file, String filename, String contentType, HttpServletRequest request, HttpServletResponse response) throws RestException {
        try (FileInputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            log.info("file size: {}", file.length());
            response.addHeader(FileConstants.CONTENT_DISPOSITION_HEADER, FileConstants.ATTACHMENT_FILENAME_VALUE + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.addHeader(FileConstants.CONTENT_LENGTH_HEADER, "" + file.length());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(contentType);
            StreamUtils.write(outputStream, inputStream);
        } catch (IOException exception) {
            log.error("the file service download has error: {}", exception.getMessage());
            throw new FileErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR);
        }
    }

    @Override
    public void download(FileIndex fileIndex, String filename, String contentType, Boolean preview, FileType fileType, HttpServletRequest request, HttpServletResponse response) throws RestException {
        response.setContentType(contentType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (fileType == FileType.VIDEO && preview) {
            request.setAttribute(VideoHttpRequestHandler.VIDEO_FILE, fileIndex);
            try {
                videoHttpRequestHandler.handleRequest(request,response);
            } catch (ServletException | IOException exception) {
                log.error("the file service download has error: {}", exception.getMessage());
                throw new FileErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR);
            }
        } else {
            try (InputStream inputStream = getById(fileIndex.getId());
                 ServletOutputStream outputStream = response.getOutputStream()) {
                response.addHeader(FileConstants.CONTENT_DISPOSITION_HEADER, FileConstants.ATTACHMENT_FILENAME_VALUE + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
                response.addHeader(FileConstants.CONTENT_LENGTH_HEADER, String.valueOf(fileIndex.getFileSize()));
                StreamUtils.write(outputStream, inputStream);
            } catch (IOException exception) {
                log.error("the file service download has error: {}", exception.getMessage());
                throw new FileErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR);
            }
        }

    }

    @Override
    public void download(FileIndex fileIndex, String filename, Boolean preview, FileType fileType, HttpServletRequest request, HttpServletResponse response) throws RestException {
        MediaType mediaType = FileServerHelper.parseContentType(filename);
        if (!preview) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        download(fileIndex, filename, mediaType.toString(), preview, fileType, request, response);
    }

    @Override
    public void download(FileFilter fileFilter, HttpServletRequest request, HttpServletResponse response) throws RestException {
        List<FileIndex> fileIndices;
        if (fileFilter.isChunk()) {
            RestPage<FileChunk> fileChunkRestPage = fileChunkService.queryAllWithFilter(fileFilter);
            FileServerHelper.checkRestPage(fileChunkRestPage);
            List<FileChunk> fileChunks = fileChunkRestPage.getItems();
            List<String> fileIndexIds = fileChunks.stream().map(FileChunk::getFileId).distinct().collect(Collectors.toList());
            List<FileIndex> fileIndexList = fileIndexService.queryAll(fileIndexIds);
            if (GeneralUtils.isNotEmpty(fileIndexList)) {
                Map<String, List<FileChunk>> fileChunkMap = fileChunks.stream().collect(Collectors.groupingBy(FileChunk::getFileId));
                for (FileIndex fileIndex : fileIndexList) {
                    List<FileChunk> chunkList = fileChunkMap.get(fileIndex.getId());
                    if (GeneralUtils.isNotEmpty(chunkList)) {
                        fileIndex.setFileChunks(chunkList);
                    }
                }
            }
            fileIndices = fileIndexList;
        } else {
            RestPage<FileIndex> fileIndexRestPage = fileIndexService.queryAllWithFilter(fileFilter);
            FileServerHelper.checkRestPage(fileIndexRestPage);
            fileIndices = fileIndexRestPage.getItems()
                    .stream().filter(FileIndex::getIsFinish).collect(Collectors.toList());
        }

        if (GeneralUtils.isEmpty(fileIndices)) {
            log.warn("the file slice upload has not finish !");
            throw new FileErrorException(FileErrorStatus.FILE_NO_FINISH_ERROR);
        }
        String tempPath = FileUtils.createPath(serviceProperties.getTempPath());
        String randomPath = FileUtils.createPath(tempPath, GeneralUtils.uuid());

        if (fileIndices.size() == 1) {
            FileIndex fileIndex = fileIndices.get(0);
            if (!fileFilter.isChunk()) {
                String filename = fileIndex.getAlias().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
                if (fileFilter.isOriginal()) {
                    filename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
                }
                download(fileIndex, filename, true, fileIndex.getFileType(),request, response);
                return;
            }
        }
        List<File> fileList = new ArrayList<>();
        if (fileFilter.isChunk()) {
            FileServerHelper.buildChunkFiles(fileIndices, fileFilter, randomPath, fileList);
        } else {
            FileServerHelper.buildIndexFiles(fileIndices, fileFilter, randomPath, fileList);
        }
        String filename = DateUtils.format(DateUtils.today(), "yyyyMMdd_HHmmss");
        File zipFile = ZipUtils.zipFiles(randomPath, filename, fileList);
        MediaType mediaType = FileServerHelper.parseContentType(filename);
        download(zipFile, zipFile.getName(), mediaType.toString(), request, response);
        FileUtils.clear(randomPath);
    }

    @Override
    public void fileDownload(String fileId, Boolean chunk, Boolean preview, Boolean original,HttpServletRequest request, HttpServletResponse response) throws RestException {
        FileIndex fileIndex;
        if (chunk) {
            FileChunk fileChunk = fileChunkService.queryById(fileId);
            if (GeneralUtils.isEmpty(fileChunk)) {
                log.warn("the file service query result is empty!");
                throw new FileErrorException(FileErrorStatus.FILE_NO_FOUND_ERROR);
            }
            String fileIndexId = fileChunk.getFileId();
            fileIndex = fileIndexService.queryById(fileIndexId);
            fileIndex.setFileChunk(fileChunk);
        } else {
            fileIndex = fileIndexService.queryById(fileId);
        }
        if (GeneralUtils.isEmpty(fileIndex)) {
            log.warn("the file service query result is empty!");
            throw new FileErrorException(FileErrorStatus.FILE_NO_FOUND_ERROR);
        }
        if (!fileIndex.getIsFinish()) {
            log.warn("the file slice upload has not finish !");
            throw new FileErrorException(FileErrorStatus.FILE_NO_FINISH_ERROR);
        }
        String filename = fileIndex.getAlias().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
        if (chunk) {
            FileChunk fileChunk = fileIndex.getFileChunk();
            if (original) {
                filename = fileIndex.getFilename().concat("_").concat(String.valueOf(fileChunk.getChunkIndex())).concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
            } else {
                filename = fileIndex.getAlias().concat("_").concat(String.valueOf(fileChunk.getChunkIndex())).concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
            }
        } else if (original) {
            filename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
        }
        download(fileIndex, filename, preview, fileIndex.getFileType(), request, response);
    }

    @Override
    public void imageDownload(String fileId, Boolean preview, Boolean original,HttpServletRequest request, HttpServletResponse response) throws RestException {
        String filename = fileId.concat(FileConstants.SUFFIX_REGEX).concat(FileConstants.IMAGE_JPG_SUFFIX);
        FileIndex fileIndex = null;
        if (original) {
            fileIndex = fileIndexService.queryById(fileId);
            if (GeneralUtils.isEmpty(fileIndex)) {
                log.warn("the image service query result is empty!");
                throw new RestException(FileErrorStatus.FILE_NO_FOUND_ERROR);
            }
            filename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
        }
        if (GeneralUtils.isEmpty(fileIndex)) {
            fileIndex = new FileIndex(fileId);
        }
        download(fileIndex, filename, preview, FileType.IMAGE, request, response);
    }

    @Override
    public FileIndex upload(MultipartFile file, FileRequest fileRequest) throws RestException {
        FileIndex createIndex = FileServerHelper.createFileIndex(file, fileRequest.toIndex());
        return upload(createIndex);
    }

    @Override
    public FileIndex upload(FileIndex fileIndex) throws RestException {
        if (GeneralUtils.isEmpty(fileIndex)) {
            throw new RestException(FileErrorStatus.FILE_INDEX_IS_NULL);
        }
        String tempPath = FileUtils.createPath(serviceProperties.getTempPath());
        String randomPath = FileUtils.createPath(tempPath, GeneralUtils.uuid());
        if (fileIndex.getIsCondense()) {
            if (fileIndex.getFileType() == FileType.IMAGE) {
                FileServerHelper.condenseImage(randomPath,fileIndex);
            } else {
                FileServerHelper.condenseFile(randomPath,fileIndex);
            }
        }
        String fileId = fileIndex.getId();
        if (GeneralUtils.isEmpty(fileIndex.getId())) {
            fileId = IdentityUtils.generateString();
            fileIndex.setId(fileId);
        }
        putById(fileId,fileIndex.inputStream());
        FileUtils.clear(randomPath);
        checkFileIndex(fileIndex);
        return fileIndexService.save(fileIndex);
    }

    @Override
    public FileIndex indexUpload(FileIndex fileIndex) throws RestException {
        String name = fileIndex.getName();
        FileIndex uploadInterrupt = fileIndexService.queryByNameWithUploadInterrupt(name);
        if (GeneralUtils.isNotEmpty(uploadInterrupt)) {
            return uploadInterrupt;
        } else {
            checkFileIndex(fileIndex);
            return fileIndexService.save(fileIndex);
        }
    }

    @Override
    @Async
    public Future<FileIndex> chunkUpload(MultipartFile file, String contentRange, FileRequest fileRequest) throws RestException {
        FileIndex fileChunkIndex = FileServerHelper.createFileChunk(fileRequest, contentRange);
        FileIndex fileIndex = FileServerHelper.createFileChunk(file, fileChunkIndex);
        FileChunk uploadChunk = fileChunkService.save(fileIndex.getFileChunk());
        putById(uploadChunk.getId(), uploadChunk.inputStream());
        fileIndex.setFileChunk(uploadChunk);
        if (GeneralUtils.isEmpty(fileIndex.getFileChunks())) {
            fileIndex.setFileChunks(new ArrayList<>());
        }
        List<FileChunk> fileChunks = fileIndex.getFileChunks();
        fileChunks.add(uploadChunk);
        fileIndex.setCurrentIndex(uploadChunk.getChunkIndex());
        if ((uploadChunk.getIsLastChunk() || uploadChunk.getChunkIndex().equals(fileIndex.getSliceSize())) && fileIndex.getIsMerge()) {
            List<String> sources = fileChunks.stream().map(RestId::getId).collect(Collectors.toList());
            fileIndex.setIsFinish(true);
            margeById(fileIndex.getId(), sources);
            checkFileIndex(fileIndex);
            fileIndexService.save(fileIndex);
        }
        return AsyncResult.forValue(fileIndex);
    }

    private void checkFileIndex(FileIndex fileIndex) {
        if (GeneralUtils.isEmpty(fileIndex.getIsCondense())) {
            fileIndex.setIsCondense(false);
        }
        if (GeneralUtils.isEmpty(fileIndex.getIsFinish())) {
            fileIndex.setIsFinish(true);
        }
        if (GeneralUtils.isEmpty(fileIndex.getIsSlice())) {
            fileIndex.setIsSlice(false);
        }
        if (GeneralUtils.isEmpty(fileIndex.getSliceSize())) {
            fileIndex.setSliceSize(0);
        }
        if (GeneralUtils.isEmpty(fileIndex.getIsMerge())) {
            fileIndex.setIsMerge(true);
        }
    } 
    
    
}
