package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.rest.RestException;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * <p>AsyncFileService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public abstract class AsyncFileService {
    @Async
    abstract public void removeAll(List<String> fileIdList) throws RestException;

    @Async
    abstract public void renameById(String fileId, String rename) throws RestException;

    @Async
    abstract public void removeById(String fileId) throws RestException;

    abstract public InputStream getById(String fileId) throws RestException;

    @Async
    abstract public void putById(String fileId, InputStream inputStream) throws RestException;

    @Async
    abstract public void margeById(String fileId, Collection<String> fileIdList) throws RestException;

}
