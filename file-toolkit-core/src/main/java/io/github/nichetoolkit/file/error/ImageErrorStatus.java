package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestStatus;
import lombok.Getter;

@Getter
public enum ImageErrorStatus implements RestStatus {
    IMAGE_FILE_ERROR(11100, "it has encountered an error with image file"),
    IMAGE_READ_ERROR(11101, "it has encountered an error during the image file reading"),
    IMAGE_WRITE_ERROR(11102, "it has encountered an error during the image file writing"),
    IMAGE_TRANSFER_ERROR(11103, "it has encountered an error during the image file transferring"),
    ;

    private final Integer status;
    private final String message;

    ImageErrorStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
