package io.github.nichetoolkit.file.error;

import io.github.nichetoolkit.rest.RestStatus;
import lombok.Getter;

/**
 * <p>FileErrorStatus</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Getter
public enum FileErrorStatus implements RestStatus {

    /** file error */
    SERVICE_DOWNLOAD_ERROR(11000, "下载服务发生错误"),
    FILE_NO_FOUND_ERROR(11001, "文件未找到"),
    FILE_NO_FINISH_ERROR(11002, "文件未上传完成或为合并，无法下载"),
    CONTENT_RANGE_IS_NULL(11003, "分片上传请求头Content-Range解析为空"),
    FILE_ID_IS_NULL(11004, "文件编号不能为空"),
    FILE_TOO_LARGE_ERROR(11005, "文件过大"),
    FILE_READ_STREAM_ERROR(11006, "文件数据流读取错误"),
    FILE_READ_BYTE_NULL(11007, "文件数据读取为空"),
    FILE_READ_BYTE_ERROR(11008, "文件数据读取错误"),
    FILE_CONDENSE_ERROR(11009, "文件压缩错误"),
    FILE_IMAGE_CONDENSE_ERROR(11010, "图片压缩错误"),
    FILE_SUFFIX_UNSUPPORTED_ERROR(11011, "文件扩展名不支持"),
    FILE_CHUNK_PARAM_ERROR(11012, "文件分片参数错误"),
    FILE_CHUNK_PARAM_INVALID(11013, "文件分片参数无效"),
    FILE_INDEX_IS_NULL(11014, "文件信息数据为空"),
    FILE_INDEX_NAME_IS_NULL(11015, "文件名称不能为空"),
    FILE_INDEX_SIZE_IS_NULL(11016, "文件大小不能为空"),
    FILE_INDEX_SLICE_SIZE_IS_NULL(11017, "文件分片数量不能为空"),
    FILE_CHUNK_IS_NULL(11018, "文件分片数据不能为空"),
    FILE_CHUNK_FILE_ID_NULL(11019, "分片的文件编号不能为空"),
    FILE_CHUNK_CHUNK_INDEX_IS_NULL(11020, "分片文件的序列不能为空"),
    ;

    private final Integer status;
    private final String message;

    FileErrorStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
