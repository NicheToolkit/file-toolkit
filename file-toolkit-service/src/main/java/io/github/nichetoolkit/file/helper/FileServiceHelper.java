package io.github.nichetoolkit.file.helper;

import io.github.nichetoolkit.file.configure.FileCommonProperties;
import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.entity.FileIndexEntity;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.error.FileErrorStatus;
import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.model.FileRequest;
import io.github.nichetoolkit.file.service.FileChunkService;
import io.github.nichetoolkit.file.service.FileIndexService;
import io.github.nichetoolkit.file.util.Md5Utils;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.github.nichetoolkit.rest.identity.IdentityUtils;
import io.github.nichetoolkit.rest.util.*;
import io.github.nichetoolkit.rice.RestPage;
import io.github.nichetoolkit.rice.helper.PropertyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>FileServerHelper</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Component
public class FileServiceHelper implements InitializingBean {

    @Autowired
    private FileCommonProperties commonProperties;

    @Autowired
    private FileIndexService fileIndexService;

    @Autowired
    private FileChunkService fileChunkService;

    private static FileServiceHelper INSTANCE = null;

    public static FileServiceHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }

    public static void checkRestPage(RestPage restPage) throws RestException {
        if (GeneralUtils.isEmpty(restPage) || GeneralUtils.isEmpty(restPage.getItems())) {
            log.warn("the file service query result is empty!");
            throw new FileErrorException(FileErrorStatus.FILE_NO_FOUND_ERROR);
        }
    }

    public static void buildProperties(String filename, long size, String suffix, FileIndex fileIndex) {
        fileIndex.addProperty(FileConstants.ORIGINAL_SUFFIX_PROPERTY, fileIndex.getSuffix());
        fileIndex.setSuffix(suffix);
        fileIndex.addProperty(FileConstants.ORIGINAL_NAME_PROPERTY, fileIndex.getName());
        fileIndex.setName(filename);
        fileIndex.addProperty(FileConstants.ORIGINAL_SIZE_PROPERTY, fileIndex.getFileSize());
        fileIndex.setFileSize(size);
        fileIndex.addProperty(FileConstants.ORIGINAL_MD5_PROPERTY, fileIndex.getFileMd5());
    }


    public static void buildChunkFiles(List<FileIndex> fileIndices, FileFilter fileFilter, String randomPath, List<File> fileList) throws RestException {
        for (FileIndex fileIndex : fileIndices) {
            List<FileChunk> fileChunks = fileIndex.getFileChunks();
            for (FileChunk fileChunk : fileChunks) {
                String itemFilename = fileIndex.getAlias().concat("_").concat(String.valueOf(fileChunk.getChunkIndex())).concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
                if (fileFilter.isOriginal()) {
                    itemFilename = fileIndex.getFilename().concat("_").concat(String.valueOf(fileChunk.getChunkIndex())).concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
                }
                String itemFilePath = randomPath.concat(File.separator).concat(itemFilename);
                FileHandleHelper.writeFile(fileChunk.getId(), itemFilePath);
                fileList.add(new File(itemFilePath));
            }
        }
    }


    public static void buildIndexFiles(List<FileIndex> fileIndices, FileFilter fileFilter, String randomPath, List<File> fileList) throws RestException {
        for (FileIndex fileIndex : fileIndices) {
            if (fileIndex.getFileSize() > INSTANCE.commonProperties.getMaxFileSize()) {
                log.warn("the file size is too large, id: {}, size: {}", fileIndex.getId(), fileIndex.getFileSize());
                throw new FileErrorException(FileErrorStatus.FILE_TOO_LARGE_ERROR);
            }
            String itemFilename = fileIndex.getId().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
            if (fileFilter.isOriginal()) {
                itemFilename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(fileIndex.getSuffix());
            }
            String itemFilePath = randomPath.concat(File.separator).concat(itemFilename);
            FileHandleHelper.writeFile(fileIndex.getId(), itemFilePath);
            fileList.add(new File(itemFilePath));
        }
    }

    public static FileIndex createFileIndex(FileIndex fileIndex) throws RestException {
        if (GeneralUtils.isEmpty(fileIndex)) {
            log.warn("the file index is null!");
            throw new FileErrorException(FileErrorStatus.FILE_INDEX_IS_NULL);
        }
        String originalFilename = fileIndex.getName();
        if (GeneralUtils.isEmpty(originalFilename)) {
            log.warn("the name of file index is null!");
            throw new FileErrorException(FileErrorStatus.FILE_INDEX_NAME_IS_NULL);
        }
        if (GeneralUtils.isEmpty(fileIndex.getFileSize())) {
            log.warn("the size of file index is null!");
            throw new FileErrorException(FileErrorStatus.FILE_INDEX_SIZE_IS_NULL);
        }
        String filename = FileUtils.filename(originalFilename);
        if (GeneralUtils.isEmpty(fileIndex.getFilename())) {
            fileIndex.setFilename(filename);
        }
        if (GeneralUtils.isEmpty(fileIndex.getAlias())) {
            fileIndex.setAlias(String.valueOf(System.currentTimeMillis()));
        }
        String suffix = FileUtils.suffix(originalFilename);
        if (GeneralUtils.isEmpty(fileIndex.getSuffix())) {
            fileIndex.setSuffix(suffix);
        }
        FileType fileType = parseType(suffix);
        fileIndex.setFileType(fileType);
        if (GeneralUtils.isEmpty(fileIndex.getIsCondense())) {
            if (fileType == FileType.IMAGE) {
                fileIndex.setIsCondense(true);
            } else {
                fileIndex.setIsCondense(false);
            }
        }
        fileIndex.setIsSlice(true);
        fileIndex.setCurrentIndex(0);
        fileIndex.setIsFinish(false);
        fileIndex.setCreateTime(new Date());
        return fileIndex;
    }

    public static void buildMd5(MultipartFile file, FileIndex fileIndex) throws RestException {
        try {
            buildMd5(file.getInputStream(), fileIndex);
        } catch (IOException exception) {
            log.error("the file read with bytes has error, filename: {}, error: {}", fileIndex.getName(), exception.getMessage());
            throw new FileErrorException(FileErrorStatus.FILE_READ_BYTE_ERROR);
        }
    }

    public static void buildMd5(File file, FileIndex fileIndex) throws RestException {
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            buildMd5(fileInputStream, fileIndex);
        } catch (IOException exception) {
            log.error("the file read with bytes has error, filename: {}, error: {}", fileIndex.getName(), exception.getMessage());
            throw new FileErrorException(FileErrorStatus.FILE_READ_BYTE_ERROR);
        }
    }

    public static void buildMd5(InputStream inputStream, FileIndex fileIndex) throws RestException {
        byte[] bytes = StreamUtils.bytes(inputStream);
        buildMd5(bytes, fileIndex);
    }

    public static void buildMd5(byte[] bytes, FileIndex fileIndex) throws RestException {
        if (GeneralUtils.isEmpty(bytes)) {
            log.error("the file read with bytes is null, filename: {}", fileIndex.getName());
            throw new FileErrorException(FileErrorStatus.FILE_READ_BYTE_NULL);
        }
        fileIndex.setBytes(bytes);
        String md5 = Md5Utils.md5(bytes);
        fileIndex.setFileMd5(md5);
    }

    public static void buildMd5(MultipartFile file, FileChunk fileChunk) throws RestException {
        try {
            buildMd5(file.getInputStream(), fileChunk);
        } catch (IOException exception) {
            log.error("the chunk read with bytes has error, chunk index: {}, error: {}", fileChunk.getChunkIndex(), exception.getMessage());
            throw new FileErrorException(FileErrorStatus.FILE_READ_BYTE_ERROR);
        }
    }

    public static void buildMd5(File file, FileChunk fileChunk) throws RestException {
        try {
            buildMd5(new FileInputStream(file), fileChunk);
        } catch (IOException exception) {
            log.error("the chunk read with bytes has error, chunk index: {}, error: {}", fileChunk.getChunkIndex(), exception.getMessage());
            throw new FileErrorException(FileErrorStatus.FILE_READ_BYTE_ERROR, exception.getMessage());
        }
    }

    public static void buildMd5(InputStream inputStream, FileChunk fileChunk) throws RestException {
        byte[] bytes = StreamUtils.bytes(inputStream);
        buildMd5(bytes, fileChunk);
    }

    public static void buildMd5(byte[] bytes, FileChunk fileChunk) throws RestException {
        if (GeneralUtils.isEmpty(bytes)) {
            log.error("the chunk read with bytes is null, chunk index: {}", fileChunk.getChunkIndex());
            throw new FileErrorException(FileErrorStatus.FILE_READ_BYTE_NULL);
        }
        fileChunk.setBytes(bytes);
        String md5 = Md5Utils.md5(bytes);
        fileChunk.setChunkMd5(md5);
    }

    public static FileIndex createFileChunk(MultipartFile multipartFile, FileIndex fileIndex) throws RestException {
        if (GeneralUtils.isEmpty(fileIndex)) {
            log.warn("the file index is null!");
            throw new FileErrorException(FileErrorStatus.FILE_INDEX_IS_NULL);
        }
        FileChunk fileChunk = fileIndex.getFileChunk();
        if (GeneralUtils.isEmpty(fileIndex)) {
            log.warn("the file chunk is null!");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_IS_NULL);
        }
        if (GeneralUtils.isEmpty(fileChunk.getFileId())) {
            String fileIndexId = fileIndex.getId();
            if (GeneralUtils.isEmpty(fileIndexId)) {
                log.error("the param of 'fileId' for file chunk is null! ");
                throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
            }
            fileChunk.setFileId(fileIndexId);
        }
        FileIndex queryFileIndex = INSTANCE.fileIndexService.queryById(fileIndex.getId());
        if (GeneralUtils.isEmpty(queryFileIndex)) {
            log.warn("the file service query result is empty!");
            throw new FileErrorException(FileErrorStatus.FILE_INDEX_IS_NULL);
        }
        queryFileIndex.setFileChunk(fileIndex.getFileChunk());
        queryFileIndex.setSliceSize(fileIndex.getSliceSize());
        fileIndex = queryFileIndex;
        if (GeneralUtils.isEmpty(fileChunk.getChunkIndex())) {
            log.error("the param of 'chunkIndex' for file chunk is null! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        Long chunkSize = fileChunk.getChunkSize();
        if (GeneralUtils.isEmpty(chunkSize)) {
            chunkSize = multipartFile.getSize();
            fileChunk.setChunkSize(multipartFile.getSize());
        }
        Long chunkStart = fileChunk.getChunkStart();
        if (chunkStart == null) {
            log.error("the param of 'chunkStart' for file chunk is null! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        Long chunkEnd = fileChunk.getChunkEnd();
        if (GeneralUtils.isEmpty(chunkEnd)) {
            log.error("the param of 'chunkEnd' for file chunk is null! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        if (chunkEnd <= chunkStart || chunkSize != chunkEnd - chunkStart) {
            log.error("the param of 'chunkEnd' or 'chunkStart' or 'chunkSize' for file chunk is invalid! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_INVALID);
        }
        buildMd5(multipartFile, fileChunk);

        fileChunk.setChunkTime(new Date());
        if (fileChunk.getChunkIndex() == 1) {
            fileChunk.setStartTime(new Date());
        }
        if (fileChunk.getIsLastChunk() || fileChunk.getChunkIndex().equals(fileIndex.getSliceSize())) {
            fileChunk.setEndTime(new Date());
        }
        return fileIndex;

    }

    public static FileIndex createFileChunk(FileRequest fileRequest, String contentRange) throws RestException {
        FileIndex fileIndex = fileRequest.toIndex();
        if (GeneralUtils.isEmpty(contentRange)) {
            log.error("the header of 'Content-Range' for request is null! ");
            throw new RestException(FileErrorStatus.CONTENT_RANGE_IS_NULL);
        }
        String chunkString = contentRange.trim().replaceAll(FileConstants.CONTENT_RANGE_BYTES_HEADER, "");
        String[] splitRange = chunkString.split(FileConstants.CONTENT_RANGE_RANGE_REGEX);
        Long chunkStart = PropertyHelper.toLong(splitRange[0]);
        if (chunkStart == null) {
            log.error("the header of 'Content-Range' start value for request is null! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        if (GeneralUtils.isEmpty(splitRange[1])) {
            log.error("the header of 'Content-Range' for request is error! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        String[] splitSize = splitRange[1].split(FileConstants.CONTENT_RANGE_SIZE_REGEX);
        Long chunkEnd = PropertyHelper.toLong(splitSize[0]);
        if (chunkEnd == null) {
            log.error("the header of 'Content-Range' end value for request is null! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        Long fileSize = PropertyHelper.toLong(splitSize[1]);
        if (fileSize == null) {
            log.error("the header of 'Content-Range' size value for request is null! ");
            throw new FileErrorException(FileErrorStatus.FILE_CHUNK_PARAM_ERROR);
        }
        FileChunk fileChunk = new FileChunk();
        fileChunk.setFileId(fileIndex.getId());
        Long chunkSize = chunkEnd - chunkStart;
        Long sliceSize = fileSize / chunkSize;
        Long chunkIndex = chunkEnd / chunkSize;
        if (fileSize % chunkSize != 0) {
            sliceSize += 1;
        }
        fileIndex.setSliceSize(sliceSize.intValue());
        fileChunk.setChunkIndex(chunkIndex.intValue());
        fileChunk.setChunkSize(chunkSize);
        fileChunk.setChunkStart(chunkStart);
        fileChunk.setChunkEnd(chunkEnd);
        if (chunkEnd.equals(fileSize)) {
            fileChunk.setIsLastChunk(true);
        }
        fileIndex.setFileChunk(fileChunk);
        return fileIndex;
    }

    public static FileIndex createFileIndex(MultipartFile multipartFile, FileIndex fileIndex) throws RestException {
        if (GeneralUtils.isEmpty(fileIndex)) {
            fileIndex = new FileIndex();
        }
        if (GeneralUtils.isEmpty(fileIndex.getId())) {
            String fileId = IdentityUtils.generateString();
            fileIndex.setId(fileId);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        if (GeneralUtils.isNotEmpty(originalFilename)) {
            originalFilename=originalFilename.trim().toLowerCase();
        }
        fileIndex.setName(originalFilename);
        String tempPath = FileUtils.createPath(INSTANCE.commonProperties.getTempPath());
        String cachePath = FileUtils.createPath(tempPath, fileIndex.getId());
        File file = FileUtils.cacheFile(cachePath, multipartFile);
        fileIndex.setFile(file);
        String filename = FileUtils.filename(originalFilename);
        if (GeneralUtils.isEmpty(fileIndex.getFilename())) {
            fileIndex.setFilename(filename);
        }
        if (GeneralUtils.isEmpty(fileIndex.getAlias())) {
            fileIndex.setAlias(String.valueOf(System.currentTimeMillis()));
        }
        String suffix = FileUtils.suffix(originalFilename);
        if (GeneralUtils.isEmpty(fileIndex.getSuffix())) {
            fileIndex.setSuffix(suffix);
        }
        fileIndex.setFileSize(multipartFile.getSize());
        FileType fileType = parseType(suffix);
        if (GeneralUtils.isEmpty(fileIndex.getIsSlice())) {
            fileIndex.setIsSlice(false);
        }
        fileIndex.setFileType(fileType);
        if (GeneralUtils.isEmpty(fileIndex.getIsCondense())) {
            if (fileType == FileType.IMAGE) {
                fileIndex.setIsCondense(true);
            } else {
                fileIndex.setIsCondense(false);
            }
        }
        if (GeneralUtils.isEmpty(fileIndex.getIsMd5())) {
            if (fileType == FileType.IMAGE) {
                fileIndex.setIsMd5(true);
            } else {
                fileIndex.setIsMd5(false);
            }
        }
        if (!fileIndex.getIsSlice()) {
            fileIndex.setSliceSize(0);
        }
        if (fileIndex.getIsMd5()) {
            buildMd5(file, fileIndex);
        }
        fileIndex.setCreateTime(new Date());
        return fileIndex;
    }

    public static MediaType parseContentType(String filename) throws RestException {
        Optional<MediaType> mediaTypeOptional = MediaTypeFactory.getMediaType(filename);
        return mediaTypeOptional.orElse(MediaType.APPLICATION_OCTET_STREAM);
    }


    public static FileType parseType(String suffix) throws RestException {
        if (GeneralUtils.isEmpty(suffix)) {
            return FileType.UNKNOWN;
        }
        for (String type : FileType.IMAGES_DICT) {
            if (suffix.equals(type)) {
                return FileType.IMAGE;
            }
        }
        for (String type : FileType.DOCUMENTS_DICT) {
            if (suffix.equals(type)) {
                return FileType.DOCUMENT;
            }
        }
        for (String type : FileType.RARS_DICT) {
            if (suffix.equals(type)) {
                return FileType.RAR;
            }
        }
        // 如果是可执行程序的，如脚本的话，禁传
        for (String type : FileType.EXES_DICT) {
            if (suffix.equals(type)) {
                // return FileType.EXE;
                throw new FileErrorException(FileErrorStatus.FILE_SUFFIX_UNSUPPORTED_ERROR, suffix);
            }
        }
        for (String type : FileType.VIDEOS_DICT) {
            if (suffix.equals(type)) {
                return FileType.VIDEO;
            }
        }
        return FileType.OTHER;
    }

    public static void buildChunks(List<FileIndexEntity> entityList, Collection<FileIndex> modelList) throws RestException {
        if (GeneralUtils.isEmpty(modelList)) {
            return;
        }
        List<String> fileIds = entityList.stream().filter(FileIndexEntity::getIsSlice).map(FileIndexEntity::getId).distinct().collect(Collectors.toList());
        if (GeneralUtils.isNotEmpty(fileIds)) {
            List<FileChunk> fileChunks = INSTANCE.fileChunkService.queryAllByFileIds(fileIds);
            if (GeneralUtils.isNotEmpty(fileChunks)) {
                Map<String, List<FileChunk>> fileChunkMap = fileChunks.stream().collect(Collectors.groupingBy(FileChunk::getFileId));
                for (FileIndex fileIndex : modelList) {
                    if (fileIndex.getIsSlice()) {
                        String fileIndexId = fileIndex.getId();
                        List<FileChunk> fileChunkList = fileChunkMap.get(fileIndexId);
                        if (GeneralUtils.isNotEmpty(fileChunkList)) {
                            Collections.sort(fileChunkList);
                            fileIndex.setFileChunks(fileChunkList);
                            FileChunk fileChunk = fileChunkList.get(fileChunkList.size() - 1);
                            fileIndex.setFileChunk(fileChunk);
                            fileIndex.setCurrentIndex(fileChunk.getChunkIndex());
                        }

                    }
                }
            }
        }
    }

}
