package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.BulkModel;
import io.github.nichetoolkit.rest.RestException;

/**
 * <p>FileHandleService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileHandleService {

    void autographImage(BulkModel fileIndex) throws RestException;

    void condenseImage(BulkModel fileIndex) throws RestException;

    void condenseFile(BulkModel fileIndex) throws RestException;
}
