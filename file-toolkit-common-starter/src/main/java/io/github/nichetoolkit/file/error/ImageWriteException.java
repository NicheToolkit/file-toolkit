package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestError;
import io.github.nichetoolkit.rest.RestStatus;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;

/**
 * <p>ImageWriteException</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class ImageWriteException extends FileErrorException {
    public ImageWriteException() {
        super(ImageErrorStatus.IMAGE_WRITE_ERROR);
    }

    public ImageWriteException(RestStatus status) {
        super(status, RestError.error(status));
    }

    public ImageWriteException(String message) {
        super(ImageErrorStatus.IMAGE_WRITE_ERROR, RestError.error(ImageErrorStatus.IMAGE_WRITE_ERROR, message));
    }

    public ImageWriteException(String resource, String message) {
        super(ImageErrorStatus.IMAGE_WRITE_ERROR, RestError.error(resource, ImageErrorStatus.IMAGE_WRITE_ERROR, message));
    }

    @Override
    public ImageWriteException get() {
        return new ImageWriteException();
    }
}
