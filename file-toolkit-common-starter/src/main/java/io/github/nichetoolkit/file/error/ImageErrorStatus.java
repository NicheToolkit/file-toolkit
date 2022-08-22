package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestStatus;
import lombok.Getter;

/**
 * <p>FileErrorStatus</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Getter
public enum ImageErrorStatus implements RestStatus {

    /** image error */
    IMAGE_FILE_ERROR(11100, "图片文件错误"),
    IMAGE_READ_ERROR(11101, "图片文件读取错误"),
    IMAGE_WRITE_ERROR(11102, "图片文件写入错误"),
    IMAGE_TRANSFER_ERROR(11103, "图片文件流式类型转换错误"),
    ;

    private final Integer status;
    private final String message;

    ImageErrorStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
