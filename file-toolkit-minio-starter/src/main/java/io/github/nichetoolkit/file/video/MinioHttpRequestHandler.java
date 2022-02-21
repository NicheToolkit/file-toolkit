package io.github.nichetoolkit.file.video;

import io.github.nichetoolkit.file.minio.MinioUtils;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.minio.GetObjectResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * <p>MinioHttpReq</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Component
public class MinioHttpRequestHandler extends VideoHttpRequestHandler {

    @Override
    public MinioInputStreamResource videoInputStreamResource(FileIndex fileIndex) throws IOException {
        try {
            GetObjectResponse getObjectResponse = MinioUtils.getObject(fileIndex.getId());
            return new MinioInputStreamResource(getObjectResponse,fileIndex);
        } catch (FileErrorException exception) {
            throw new IOException(exception.getMessage(),exception);
        }

    }
}
