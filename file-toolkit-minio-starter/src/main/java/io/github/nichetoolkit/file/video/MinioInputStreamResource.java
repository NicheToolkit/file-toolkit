package io.github.nichetoolkit.file.video;

import io.github.nichetoolkit.file.minio.MinioUtils;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.minio.StatObjectResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>MinioFileResource</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class MinioInputStreamResource extends VideoInputStreamResource {
    private StatObjectResponse statObject;

    public MinioInputStreamResource(InputStream inputStream, FileIndex fileIndex) throws IOException {
        super(inputStream, fileIndex);
        try {
            this.statObject = MinioUtils.statObject(fileIndex.getId());
        } catch (FileErrorException exception) {
            throw new IOException(exception.getMessage(), exception);
        }
    }

    @Override
    public long contentLength() throws IOException {
        return statObject.size();
    }

    @Override
    public long lastModified() throws IOException {
        return statObject.lastModified().toEpochSecond();
    }

}
