package io.github.nichetoolkit.file.service.impl;

import io.github.nichetoolkit.file.configure.FileCommonProperties;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.service.*;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.util.FileUtils;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rice.RestId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>FileUploadServiceImpl</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

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

    @Async
    @Override
    public void uploadFileIndex(FileIndex fileIndex) throws RestException {
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
    }

    @Async
    @Override
    public void uploadChunk(FileIndex fileIndex) throws RestException {
        FileChunk uploadChunk = fileChunkService.save(fileIndex.getFileChunk());
        asyncFileService.putById(uploadChunk.getId(), uploadChunk.inputStream());
        fileIndex.setFileChunk(uploadChunk);
    }

    @Async
    @Override
    public void lastChunk(FileIndex fileIndex) throws RestException {
        List<String> sources = fileIndex.getFileChunks().stream().map(RestId::getId).collect(Collectors.toList());
        asyncFileService.margeById(fileIndex.getId(), sources);
        fileIndexService.save(fileIndex);
    }

}
