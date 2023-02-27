package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.model.FileRequest;
import io.github.nichetoolkit.rest.RestException;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>FileUploadService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileUploadService {

    void uploadFileIndex(FileIndex fileIndex) throws RestException;

    void uploadChunk(FileIndex fileIndex) throws RestException;

    void lastChunk(FileIndex fileIndex) throws RestException;
}
