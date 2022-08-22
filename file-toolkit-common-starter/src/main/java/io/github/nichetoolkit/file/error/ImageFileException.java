package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestError;
import io.github.nichetoolkit.rest.RestErrorStatus;
import io.github.nichetoolkit.rest.RestStatus;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;

/**
 * <p>ImageFileException</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class ImageFileException extends FileErrorException {
    public ImageFileException() {
        super(ImageErrorStatus.IMAGE_FILE_ERROR);
    }

    public ImageFileException(RestStatus status) {
        super(status, RestError.error(status));
    }

    public ImageFileException(String message) {
        super(ImageErrorStatus.IMAGE_FILE_ERROR, RestError.error(ImageErrorStatus.IMAGE_FILE_ERROR, message));
    }

    public ImageFileException(String resource, String message) {
        super(ImageErrorStatus.IMAGE_FILE_ERROR, RestError.error(resource, ImageErrorStatus.IMAGE_FILE_ERROR, message));
    }

    @Override
    public ImageFileException get() {
        return new ImageFileException();
    }
}
