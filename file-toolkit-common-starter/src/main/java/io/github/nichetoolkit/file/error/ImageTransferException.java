package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestError;
import io.github.nichetoolkit.rest.RestStatus;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;

/**
 * <p>ImageTransferException</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class ImageTransferException extends FileErrorException {
    public ImageTransferException() {
        super(ImageErrorStatus.IMAGE_TRANSFER_ERROR);
    }

    public ImageTransferException(RestStatus status) {
        super(status, RestError.error(status));
    }

    public ImageTransferException(String message) {
        super(ImageErrorStatus.IMAGE_TRANSFER_ERROR, RestError.error(ImageErrorStatus.IMAGE_TRANSFER_ERROR, message));
    }

    public ImageTransferException(String resource, String message) {
        super(ImageErrorStatus.IMAGE_TRANSFER_ERROR, RestError.error(resource, ImageErrorStatus.IMAGE_TRANSFER_ERROR, message));
    }

    @Override
    public ImageTransferException get() {
        return new ImageTransferException();
    }
}
