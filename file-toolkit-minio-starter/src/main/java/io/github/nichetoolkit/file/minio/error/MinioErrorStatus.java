package io.github.nichetoolkit.file.minio.error;

import io.github.nichetoolkit.rest.RestStatus;
import lombok.Getter;

/**
 * <p>MinioErrorStatus</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Getter
public enum MinioErrorStatus implements RestStatus {

    /** Minio error */
    MINIO_SERVER_ERROR(11100, "Minio服务器错误"),
    MINIO_CONFIG_ERROR(110101, "Minio配置错误"),
    MINIO_BUCKET_POLICY_ERROR(11102, "Minio存储桶策略错误"),
    MINIO_MAKE_BUCKET_ERROR(11103, "Minio创建存储桶错误"),
    MINIO_REMOVE_BUCKET_ERROR(11104, "Minio获取移除存储桶错误"),
    MINIO_LIST_ALL_BUCKETS_ERROR(11105, "Minio获取存储桶列表错误"),
    MINIO_STAT_OBJECT_ERROR(11106, "Minio获取对象状态错误"),
    MINIO_GET_ALL_OBJECTS_ERROR(11107, "Minio获取所有对象错误"),
    MINIO_GET_OBJECT_ERROR(11108, "Minio获取对象错误"),
    MINIO_PUT_OBJECT_ERROR(11109, "Minio添加对象错误"),
    MINIO_PUT_FOLDER_ERROR(11110, "Minio添加文件夹错误"),
    MINIO_COMPOSE_OBJECT_ERROR(11111, "Minio组合对象错误"),
    MINIO_UPLOAD_OBJECT_ERROR(11112, "Minio上传对象错误"),
    MINIO_UPLOAD_SNOWBALL_OBJECT_ERROR(11113, "Minio雪球上传对象错误"),
    MINIO_COPY_OBJECT_ERROR(11114, "Minio拷贝对象错误"),
    MINIO_REMOVE_OBJECT_ERROR(11115, "Minio移除对象错误"),
    MINIO_PRESIGNED_OBJECT_URL_ERROR(11116, "Minio获取对象访问链接错误"),
    MINIO_PRESIGNED_ALL_OBJECT_URL_ERROR(11117, "Minio获取所有对象访问链接错误"),
    ;

    private final Integer status;
    private final String message;

    MinioErrorStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
