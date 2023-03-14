package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.handle.FileStoreService;
import io.github.nichetoolkit.file.minio.MinioUtils;
import io.github.nichetoolkit.rest.RestException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * <p>MinioFileService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Service
public class MinioFileService extends FileStoreService {
    @Override
    @Async
    public void removeAll(List<String> fileIdList) throws RestException {
        MinioUtils.removeObjects(fileIdList);
    }

    @Override
    @Async
    public void renameById(String fileId, String rename) throws RestException {
        MinioUtils.copyObject(fileId, rename);
    }

    @Override
    @Async
    public void removeById(String fileId) throws RestException {
        MinioUtils.removeObject(fileId);
    }

    @Override
    public InputStream getById(String fileId) throws RestException {
        return MinioUtils.getObject(fileId);
    }

    @Override
    @Async
    public void putById(String fileId, InputStream inputStream) throws RestException {
        MinioUtils.putObject(fileId, inputStream);
    }

    @Override
    @Async
    public void margeById(String fileId, Collection<String> fileIdList) throws RestException {

    }
}
