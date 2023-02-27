package io.github.nichetoolkit.file.service.impl;

import io.github.nichetoolkit.file.configure.FileCommonProperties;
import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.error.FileErrorStatus;
import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.helper.FileServiceHelper;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.model.FileRequest;
import io.github.nichetoolkit.file.service.*;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    protected FileCommonProperties commonProperties;

    @Autowired
    protected FileIndexService fileIndexService;

    @Autowired
    protected FileChunkService fileChunkService;

    @Autowired
    protected FileHandleService fileHandleService;

    @Autowired
    protected AsyncFileService asyncFileService;

    @Autowired
    protected VideoHttpRequestHandler videoHttpRequestHandler;

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
            asyncFileService.removeAll(fileIdList);
            if (fileFilter.isChunk()) {
                if (fileFilter.isDelete()) {
                    fileChunkService.deleteAll(fileIdList);
                } else {
                    fileChunkService.removeAll(fileIdList);
                }
            } else {
                if (fileFilter.isDelete()) {
                    fileIndexService.deleteAll(fileIdList);
                } else {
                    fileIndexService.removeAll(fileIdList);
                }
            }
        }

    }


    @Override
    public void remove(String fileId, Boolean chunk, Boolean delete, Boolean rename) throws RestException {
        if (GeneralUtils.isEmpty(fileId)) {
            return;
        }
        boolean isExist = false;
        if (chunk) {
            FileChunk fileChunk = fileChunkService.queryById(fileId);
            isExist = GeneralUtils.isNotEmpty(fileChunk);
            if (delete) {
                fileChunkService.deleteById(fileId);
            } else {
                fileChunkService.removeById(fileId);
            }
        } else {
            FileIndex fileIndex = fileIndexService.queryById(fileId);
            isExist = GeneralUtils.isNotEmpty(fileIndex);
            if (delete) {
                fileIndexService.deleteById(fileId);
            } else {
                fileIndexService.removeById(fileId);
            }
        }
        if (isExist) {
            if (rename) {
                asyncFileService.renameById(fileId, fileId.concat("_del"));
            }
            asyncFileService.removeById(fileId);
        }
    }


    @Override
    public void download(File file, String filename, String contentType, HttpServletRequest request, HttpServletResponse response) throws RestException {
        try (FileInputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            log.info("file size: {}", file.length());
            response.addHeader(FileConstants.CONTENT_DISPOSITION_HEADER, FileConstants.ATTACHMENT_FILENAME_VALUE + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
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
                videoHttpRequestHandler.handleRequest(request, response);
            } catch (ServletException | IOException exception) {
                log.error("the file service download has error: {}", exception.getMessage());
                throw new FileErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR);
            }
        } else {
            try (InputStream inputStream = asyncFileService.getById(fileIndex.getId());
                 ServletOutputStream outputStream = response.getOutputStream()) {
                response.addHeader(FileConstants.CONTENT_DISPOSITION_HEADER, FileConstants.ATTACHMENT_FILENAME_VALUE + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
                StreamUtils.write(outputStream, inputStream);
            } catch (IOException exception) {
                log.error("the file service download has error: {}", exception.getMessage());
                throw new FileErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR);
            }
        }

    }

    @Override
    public void download(FileIndex fileIndex, String filename, Boolean preview, FileType fileType, HttpServletRequest request, HttpServletResponse response) throws RestException {
        MediaType mediaType = FileServiceHelper.parseContentType(filename);
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
            FileServiceHelper.checkRestPage(fileChunkRestPage);
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
            FileServiceHelper.checkRestPage(fileIndexRestPage);
            fileIndices = fileIndexRestPage.getItems()
                    .stream().filter(FileIndex::getIsFinish).collect(Collectors.toList());
        }

        if (GeneralUtils.isEmpty(fileIndices)) {
            log.warn("the file slice upload has not finish !");
            throw new FileErrorException(FileErrorStatus.FILE_NO_FINISH_ERROR);
        }
        String tempPath = FileUtils.createPath(commonProperties.getTempPath());
        String randomPath = FileUtils.createPath(tempPath, GeneralUtils.uuid());

        if (fileIndices.size() == 1) {
            FileIndex fileIndex = fileIndices.get(0);
            if (!fileFilter.isChunk()) {
                String filename = fileIndex.getAlias().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
                if (fileFilter.isOriginal()) {
                    filename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
                }
                download(fileIndex, filename, true, fileIndex.getFileType(), request, response);
                return;
            }
        }
        List<File> fileList = new ArrayList<>();
        if (fileFilter.isChunk()) {
            FileServiceHelper.buildChunkFiles(fileIndices, fileFilter, randomPath, fileList);
        } else {
            FileServiceHelper.buildIndexFiles(fileIndices, fileFilter, randomPath, fileList);
        }
        String filename = DateUtils.format(DateUtils.today(), "yyyyMMdd_HHmmss");
        File zipFile = ZipUtils.zipFiles(randomPath, filename, fileList);
        MediaType mediaType = FileServiceHelper.parseContentType(filename);
        download(zipFile, zipFile.getName(), mediaType.toString(), request, response);
        FileUtils.clear(randomPath);
    }

    @Override
    public void fileDownload(String fileId, Boolean chunk, Boolean preview, Boolean original, HttpServletRequest request, HttpServletResponse response) throws RestException {
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
    public void imageDownload(String fileId, Boolean preview, Boolean original, HttpServletRequest request, HttpServletResponse response) throws RestException {
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
        FileIndex createIndex = FileServiceHelper.createFileIndex(file, fileRequest.toIndex());
        return upload(createIndex);
    }

    @Override
    public FileIndex upload(FileIndex fileIndex) throws RestException {
        if (GeneralUtils.isEmpty(fileIndex)) {
            throw new RestException(FileErrorStatus.FILE_INDEX_IS_NULL);
        }
        if (fileIndex.getIsAutograph() != null && fileIndex.getIsAutograph() && fileIndex.getFileType() == FileType.IMAGE) {
            fileHandleService.autographImage(fileIndex);
        }
        if (fileIndex.getIsCondense()) {
            if (fileIndex.getFileType() == FileType.IMAGE) {
                fileHandleService.condenseImage(fileIndex);
            } else {
                fileHandleService.condenseFile(fileIndex);
            }
        }
        String fileId = fileIndex.getId();
        asyncFileService.putById(fileId, fileIndex.inputStream());
        fileIndexService.save(fileIndex);
        checkFileIndex(fileIndex);
        return fileIndex;
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
    public FileIndex chunkUpload(MultipartFile file, String contentRange, FileRequest fileRequest) throws RestException {
        FileIndex fileChunkIndex = FileServiceHelper.createFileChunk(fileRequest, contentRange);
        FileIndex fileIndex = FileServiceHelper.createFileChunk(file, fileChunkIndex);
        FileChunk uploadChunk = fileChunkService.save(fileIndex.getFileChunk());
        asyncFileService.putById(uploadChunk.getId(), uploadChunk.inputStream());
        fileIndex.setFileChunk(uploadChunk);
        if (GeneralUtils.isEmpty(fileIndex.getFileChunks())) {
            fileIndex.setFileChunks(new ArrayList<>());
        }
        List<FileChunk> fileChunks = fileIndex.getFileChunks();
        fileChunks.add(uploadChunk);
        fileIndex.setCurrentIndex(uploadChunk.getChunkIndex());
        if ((uploadChunk.getIsLastChunk() || uploadChunk.getChunkIndex().equals(fileIndex.getSliceSize())) && fileIndex.getIsMerge()) {
            fileIndex.setIsFinish(true);
            List<String> sources = fileIndex.getFileChunks().stream().map(RestId::getId).collect(Collectors.toList());
            asyncFileService.margeById(fileIndex.getId(), sources);
            checkFileIndex(fileIndex);
            fileIndexService.save(fileIndex);
        }
        return fileIndex;
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
