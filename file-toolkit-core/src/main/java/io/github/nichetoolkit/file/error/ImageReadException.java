package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestError;
import io.github.nichetoolkit.rest.RestStatus;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;

/**
 * <p>ImageReadException</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class ImageReadException extends FileErrorException {
    public ImageReadException() {
        super(ImageErrorStatus.IMAGE_READ_ERROR);
    }

    public ImageReadException(RestStatus status) {
        super(status, RestError.error(status));
    }

    public ImageReadException(String message) {
        super(ImageErrorStatus.IMAGE_READ_ERROR, RestError.error(ImageErrorStatus.IMAGE_READ_ERROR, message));
    }

    public ImageReadException(String resource, String message) {
        super(ImageErrorStatus.IMAGE_READ_ERROR, RestError.error(resource, ImageErrorStatus.IMAGE_READ_ERROR, message));
    }

    @Override
    public ImageReadException get() {
        return new ImageReadException();
    }
}
