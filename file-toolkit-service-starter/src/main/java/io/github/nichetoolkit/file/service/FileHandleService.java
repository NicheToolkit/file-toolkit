package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.rest.RestException;

/**
 * <p>FileHandleService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileHandleService {

    void autographImage(FileIndex fileIndex) throws RestException;

    void condenseImage(FileIndex fileIndex) throws RestException;

    void condenseFile(FileIndex fileIndex) throws RestException;
}
