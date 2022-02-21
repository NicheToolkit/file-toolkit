package io.github.nichetoolkit.file.minio;

import io.github.nichetoolkit.file.configure.FileMinioProperties;
import io.github.nichetoolkit.file.minio.error.MinioErrorStatus;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.github.nichetoolkit.rest.error.natives.ServiceErrorException;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * <p>MinioUtils</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
public class MinioUtils {

    private MinioClient minioClient;

    private String defaultBucket;

    private static MinioUtils INSTANCE = null;

    public static MinioUtils getInstance() {
        return INSTANCE;
    }

    public static MinioClient getMinioClient() {
        return INSTANCE.minioClient;
    }

    public static String getDefaultBucket() {
        return INSTANCE.defaultBucket;
    }

    @Autowired
    public MinioUtils(MinioClient minioClient, FileMinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.defaultBucket = minioProperties.getBucketName();
    }

    @PostConstruct
    public void initMinioClient() throws ServiceErrorException {
        INSTANCE = this;
        initDefaultBucket(this.minioClient, this.defaultBucket);
    }


    public static MinioClient createMinioClient(FileMinioProperties minioProperties) {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    /**
     * MinioClient 初始化bucket方法
     * @param bucketName minio配置文件
     * @return MinioClient MinioClient
     * @throws ServiceErrorException ServiceErrorException
     */
    public static void initDefaultBucket(MinioClient minioClient, String bucketName) throws ServiceErrorException {
        try {
            boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isBucketExists) {
                log.warn("the minio bucket will be created because of it is no found!  bucket: {}", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            log.info("the minio bucket is found! bucket: {}", bucketName);
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException exception) {
            log.error("the minio server connect has error: {}, bucket: {}", exception.getMessage(), bucketName);
            throw new ServiceErrorException(MinioErrorStatus.MINIO_CONFIG_ERROR);
        }
    }

    /**
     * 获取存储桶策略
     * @return JSONObject
     * @throws ServiceErrorException ServiceErrorException
     */
    public static String bucketPolicy() throws ServiceErrorException {
        return bucketPolicy(INSTANCE.defaultBucket);
    }

    /**
     * 获取存储桶策略
     * @param bucketName 存储桶名称
     * @return JSONObject
     * @throws ServiceErrorException ServiceErrorException
     */
    public static String bucketPolicy(String bucketName) throws ServiceErrorException {
        try {
            return INSTANCE.minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server get bucket policy has error, bucket: {}, error: {}", bucketName, exception.getMessage());
            throw new ServiceErrorException(MinioErrorStatus.MINIO_BUCKET_POLICY_ERROR);
        }
    }

    /**
     * 获取全部数据桶bucket
     * @return List<Bucket>
     * @throws ServiceErrorException ServiceErrorException
     */
    public static List<Bucket> listBuckets() throws ServiceErrorException {
        try {
            return INSTANCE.minioClient.listBuckets();
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server get all buckets has error: {}", exception.getMessage());
            throw new ServiceErrorException(MinioErrorStatus.MINIO_LIST_ALL_BUCKETS_ERROR);
        }
    }

    /**
     * 根据bucketName获取数据桶信息
     * @return Optional<Bucket> Bucket
     * @throws ServiceErrorException ServiceErrorException
     */
    public static Optional<Bucket> getBucket() throws ServiceErrorException {
        return getBucket(INSTANCE.defaultBucket);
    }

    /**
     * 根据bucketName获取数据桶信息
     * @param bucketName 数据桶
     * @return Optional<Bucket> Bucket
     * @throws ServiceErrorException ServiceErrorException
     */
    public static Optional<Bucket> getBucket(String bucketName) throws ServiceErrorException {
        return listBuckets().stream().filter(bucket -> bucket.name().equals(bucketName)).findFirst();
    }

    /**
     * 切换Bucket默认数据桶
     * @param bucketName 数据桶
     */
    public static void switchBucket(String bucketName) throws ServiceErrorException {
        INSTANCE.defaultBucket = bucketName;
        initDefaultBucket(INSTANCE.minioClient, bucketName);
    }

    /**
     * 创建Bucket数据桶
     * @param bucketName 数据桶
     * @throws ServiceErrorException ServiceErrorException
     */
    public static void makeBucket(String bucketName) throws ServiceErrorException {
        try {
            INSTANCE.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server make bucket has error, bucket: {}, error: {}", bucketName, exception.getMessage());
            throw new ServiceErrorException(MinioErrorStatus.MINIO_MAKE_BUCKET_ERROR);
        }
    }

    /**
     * 根据bucketName删除数据桶信息
     * @param bucketName 数据桶
     * @throws ServiceErrorException ServiceErrorException
     */
    public static void removeBucket(String bucketName) throws ServiceErrorException {
        try {
            INSTANCE.minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server remove bucket has error, bucket: {}, error: {}", bucketName, exception.getMessage());
            throw new ServiceErrorException(MinioErrorStatus.MINIO_REMOVE_BUCKET_ERROR);
        }
    }


    /**
     * 获取文件信息
     * @param objectName 对象名称
     * @return StatObjectResponse
     * @throws FileErrorException FileErrorException
     */
    public static StatObjectResponse statObject(String objectName) throws FileErrorException {
        return statObject(INSTANCE.defaultBucket, objectName);
    }

    /**
     * 获取文件信息
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @return StatObjectResponse
     * @throws FileErrorException FileErrorException
     */
    public static StatObjectResponse statObject(String bucketName, String objectName) throws FileErrorException {
        try {
            return INSTANCE.minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server stat object has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_STAT_OBJECT_ERROR);
        }
    }

    /**
     * 判断文件是否存在
     * @param objectName 对象名称
     * @return boolean
     */
    public static boolean isObjectExist(String objectName) {
        return isObjectExist(INSTANCE.defaultBucket, objectName);
    }

    /**
     * 判断文件是否存在
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @return boolean
     */
    public static boolean isObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            statObject(bucketName, objectName);
        } catch (FileErrorException ignored) {
            exist = false;
        }
        return exist;
    }

    /**
     * 判断文件夹是否存在
     * @param objectName 对象名称
     * @return boolean
     */
    public static boolean isFolderExist(String objectName) {
        return isFolderExist(INSTANCE.defaultBucket, objectName);
    }

    /**
     * 判断文件夹是否存在
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @return boolean
     */
    public static boolean isFolderExist(String bucketName, String objectName) {
        boolean exist = false;
        Iterable<Result<Item>> resultIterable = listObjects(bucketName, objectName, false);
        if (GeneralUtils.isNotEmpty(resultIterable)) {
            try {
                for (Result<Item> result : resultIterable) {
                    Item item = result.get();
                    if (item.isDir() && objectName.equals(item.objectName())) {
                        exist = true;
                    }
                }
            } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException ignored) {
            }
        }
        return exist;
    }

    /**
     * 根据文件前缀查询文件
     * @param prefix    文件前缀或文件名
     * @param recursive 是否递归查询
     * @return List<Item>
     */
    public static Iterable<Result<Item>> listObjects(String prefix, boolean recursive) {
        return listObjects(INSTANCE.defaultBucket, prefix, recursive);
    }

    /**
     * 根据文件前缀查询文件
     * @param bucketName 数据桶
     * @param prefix     文件前缀或文件名
     * @param recursive  是否递归查询
     * @return List<Item>
     */
    public static Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive) {
        return INSTANCE.minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
    }


    /**
     * 根据文件前缀查询文件
     * @param prefix    文件前缀或文件名
     * @param recursive 是否递归查询
     * @return List<Item>
     * @throws FileErrorException FileErrorException
     */
    public static List<Item> allObjects(String prefix, boolean recursive) throws FileErrorException {
        return allObjects(INSTANCE.defaultBucket, prefix, recursive);
    }

    /**
     * 根据文件前缀查询文件
     * @param bucketName 数据桶
     * @param prefix     文件前缀或文件名
     * @param recursive  是否递归查询
     * @return List<Item>
     * @throws FileErrorException FileErrorException
     */
    public static List<Item> allObjects(String bucketName, String prefix, boolean recursive) throws FileErrorException {
        List<Item> items = new ArrayList<>();
        Iterable<Result<Item>> resultIterable = listObjects(bucketName, prefix, recursive);
        if (GeneralUtils.isNotEmpty(resultIterable)) {
            try {
                for (Result<Item> itemResult : resultIterable) {
                    items.add(itemResult.get());
                }
            } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException exception) {
                log.error("the minio server get all object by prefix has error, prefix: {}, bucket: {}, error: {}", prefix, bucketName, exception.getMessage());
                throw new FileErrorException(MinioErrorStatus.MINIO_GET_ALL_OBJECTS_ERROR);
            }
        }
        return items;
    }

    /**
     * 获取对象文件流
     * @param objectName 对象名称
     * @return InputStream
     * @throws FileErrorException FileErrorException
     */
    public static GetObjectResponse getObject(String objectName) throws FileErrorException {
        return getObject(INSTANCE.defaultBucket, objectName);
    }

    /**
     * 获取对象文件流
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @return InputStream
     * @throws FileErrorException FileErrorException
     */
    public static GetObjectResponse getObject(String bucketName, String objectName) throws FileErrorException {
        try {
            return INSTANCE.minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server get object has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_GET_OBJECT_ERROR);
        }
    }

    /**
     * 对象文件流断点续传
     * @param objectName 对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return InputStream
     * @throws FileErrorException FileErrorException
     */
    public GetObjectResponse getObject(String objectName, long offset, long length) throws FileErrorException {
        return getObject(INSTANCE.defaultBucket, objectName, offset, length);
    }

    /**
     * 对象文件流断点续传
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return InputStream
     * @throws FileErrorException FileErrorException
     */
    public GetObjectResponse getObject(String bucketName, String objectName, long offset, long length) throws FileErrorException {
        try {
            return INSTANCE.minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName)
                    .offset(offset).length(length).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server get object with offset and length has error, range: [ {} , {} ], object: {}, bucket: {} error: {}", offset, length, objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_GET_OBJECT_ERROR);
        }
    }

    /**
     * 通过MultipartFile，上传文件
     * @param file        MultipartFile
     * @param objectName  对象名称
     * @param contentType contentType
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putObject(MultipartFile file, String objectName, String contentType) throws FileErrorException {
        return putObject(INSTANCE.defaultBucket, file, objectName, contentType);
    }

    /**
     * 通过MultipartFile，上传文件
     * @param bucketName  数据桶
     * @param file        MultipartFile
     * @param objectName  对象名称
     * @param contentType contentType
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putObject(String bucketName, MultipartFile file, String objectName, String contentType) throws FileErrorException {
        try {
            InputStream inputStream = file.getInputStream();
            return INSTANCE.minioClient.putObject(PutObjectArgs.builder().bucket(bucketName)
                    .object(objectName).contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server put object has error, contentType: {}, object: {}, bucket: {}, error: {}", contentType, objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_PUT_OBJECT_ERROR);
        }
    }

    /**
     * 上传本地文件
     * @param objectName 对象名称
     * @param fileName   文件名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putObject(String objectName, String fileName) throws FileErrorException {
        return putObject(INSTANCE.defaultBucket, objectName, fileName);
    }

    /**
     * 上传本地文件
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   文件名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putObject(String bucketName, String objectName, String fileName) throws FileErrorException {
        try {
            return INSTANCE.minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(fileName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server upload object has error, fileName: {}, object: {}, bucket: {}, error: {}", fileName, objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_PUT_OBJECT_ERROR);
        }
    }


    /**
     * 通过流上传文件
     * @param objectName  对象名称
     * @param inputStream 文件流数据
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putObject(String objectName, InputStream inputStream) throws FileErrorException {
        return putObject(INSTANCE.defaultBucket, objectName, inputStream);
    }

    /**
     * 通过流上传文件
     * @param bucketName  存储桶
     * @param objectName  对象名称
     * @param inputStream 文件流数据
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putObject(String bucketName, String objectName, InputStream inputStream) throws FileErrorException {
        try {
            return INSTANCE.minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(inputStream, inputStream.available(), -1).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server put object has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_PUT_OBJECT_ERROR);
        }
    }

    /**
     * 创建文件夹或目录
     * @param objectName 对象名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putFolder(String objectName) throws FileErrorException {
        return putFolder(INSTANCE.defaultBucket, objectName);
    }

    /**
     * 创建文件夹或目录
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse putFolder(String bucketName, String objectName) throws FileErrorException {
        try {
            return INSTANCE.minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server put folder has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_PUT_OBJECT_ERROR);
        }
    }

    /**
     * 通过组合成新的文件
     * @param bucketName     数据桶
     * @param objectName     对象名称
     * @param composeSources 组合源数据
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse composeObject(String bucketName, String objectName, Collection<ComposeSource> composeSources) throws FileErrorException {
        try {
            return INSTANCE.minioClient.composeObject(ComposeObjectArgs.builder().bucket(bucketName)
                    .object(objectName).sources(new ArrayList<>(composeSources)).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server compose object has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_COMPOSE_OBJECT_ERROR);
        }
    }


    /**
     * 通过组合成新的文件
     * @param objectName 对象名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse composeObject(String objectName, Collection<String> sources) throws FileErrorException {
        return composeObject(INSTANCE.defaultBucket, objectName, Collections.singletonMap(INSTANCE.defaultBucket, sources));
    }

    /**
     * 通过组合成新的文件
     * @param objectName 对象名称
     * @param sourcesMap 组合源数据
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse composeObject(String objectName, Map<String, Collection<String>> sourcesMap) throws FileErrorException {
        return composeObject(INSTANCE.defaultBucket, objectName, sourcesMap);
    }

    /**
     * 通过组合成新的文件
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @param sourcesMap 组合源数据
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse composeObject(String bucketName, String objectName, Map<String, Collection<String>> sourcesMap) throws FileErrorException {
        List<ComposeSource> composeSources = new ArrayList<>();
        sourcesMap.forEach((sourceBucketName, sourceObjectNames) -> {
            sourceObjectNames.forEach(sourceObjectName -> {
                composeSources.add(ComposeSource.builder().bucket(sourceBucketName).object(sourceObjectName).build());
            });
        });
        return composeObject(bucketName, objectName, composeSources);
    }

    /**
     * 上传文件
     * @param objectName 对象名称
     * @param filename 文件名
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse uploadObject(String objectName, String filename) throws FileErrorException {
        return uploadObject(objectName,filename,0L);
    }

    /**
     * 上传文件
     * @param objectName 对象名称
     * @param filename 文件名
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse uploadObject(String objectName, String filename,long partSize) throws FileErrorException {
        return uploadObject(INSTANCE.defaultBucket,objectName,filename,partSize);
    }

    /**
     * 上传文件
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @param filename 文件名
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse uploadObject(String bucketName, String objectName, String filename) throws FileErrorException {
        return uploadObject(bucketName,objectName,filename,0L);
    }

    /**
     * 上传文件
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @param filename 文件名
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse uploadObject(String bucketName, String objectName, String filename, long partSize) throws FileErrorException {
        try {
            return INSTANCE.minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName)
                    .object(objectName).filename(filename,partSize).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server upload object has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_UPLOAD_OBJECT_ERROR);
        }
    }

    /**
     * 上传文件
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @param objects 对象名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse uploadSnowballObjects(String bucketName, String objectName, Collection<SnowballObject> objects) throws FileErrorException {
        try {
            return INSTANCE.minioClient.uploadSnowballObjects(UploadSnowballObjectsArgs.builder().bucket(bucketName)
                    .object(objectName).objects(objects).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server upload snowball objects has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_UPLOAD_SNOWBALL_OBJECT_ERROR);
        }
    }

    /**
     * 拷贝文件
     * @param sourceObjectName 源对象名称
     * @param targetObjectName 目标对象名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse copyObject(String sourceObjectName, String targetObjectName) throws FileErrorException {
        return copyObject(INSTANCE.defaultBucket, sourceObjectName, INSTANCE.defaultBucket, targetObjectName);
    }

    /**
     * 拷贝文件
     * @param sourceBucketName 源数据桶
     * @param sourceObjectName 源对象名称
     * @param targetBucketName 目标数据桶
     * @param targetObjectName 目标对象名称
     * @return ObjectWriteResponse
     * @throws FileErrorException FileErrorException
     */
    public static ObjectWriteResponse copyObject(String sourceBucketName, String sourceObjectName, String targetBucketName, String targetObjectName) throws FileErrorException {
        try {
            return INSTANCE.minioClient.copyObject(CopyObjectArgs.builder().source(CopySource.builder().bucket(sourceBucketName).object(sourceObjectName).build())
                    .bucket(targetBucketName).object(targetObjectName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server copy object has error, target object: {}, target bucket: {}, source object: {}, source bucket: {}, error: {}", targetObjectName, targetBucketName, sourceObjectName, sourceBucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_COPY_OBJECT_ERROR);
        }
    }

    /**
     * 删除文件
     * @param objectName 对象名称
     * @throws FileErrorException FileErrorException
     */
    public static void removeObject(String objectName) throws FileErrorException {
        removeObject(INSTANCE.defaultBucket, objectName);
    }

    /**
     * 删除文件
     * @param bucketName 数据桶
     * @param objectName 对象名称
     * @throws FileErrorException FileErrorException
     */
    public static void removeObject(String bucketName, String objectName) throws FileErrorException {
        try {
            INSTANCE.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server remove object has error, object: {}, bucket: {}, error: {}", objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_REMOVE_OBJECT_ERROR);
        }
    }

    /**
     * 批量删除文件
     * @param objectNames 对象名称集合
     * @throws FileErrorException FileErrorException
     */
    public static void removeObjects(Collection<String> objectNames) throws FileErrorException {
        removeObjects(INSTANCE.defaultBucket, objectNames);
    }

    /**
     * 批量删除文件
     * @param bucketName  数据桶
     * @param objectNames 对象名称集合
     * @throws FileErrorException FileErrorException
     */
    public static void removeObjects(String bucketName, Collection<String> objectNames) throws FileErrorException {
        for (String objectName : objectNames) {
            removeObject(bucketName, objectName);
        }
    }

    /**
     * 获取文件外链
     * @param objectName 文件名称
     * @param expire     过期时间 <=7 秒级
     * @return String
     * @throws FileErrorException FileErrorException
     */
    public static String objectUrl(String objectName, Integer expire) throws FileErrorException {
        return objectUrl(INSTANCE.defaultBucket, objectName, expire);
    }

    /**
     * 获取文件外链
     * @param bucketName 数据桶
     * @param objectName 文件名称
     * @param expire     过期时间 <=7 秒级
     * @return String
     * @throws FileErrorException FileErrorException
     */
    public static String objectUrl(String bucketName, String objectName, Integer expire) throws FileErrorException {
        try {
            return INSTANCE.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expire).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server presigned object url has error, expire: {}, object: {}, bucket: {}, error: {}", expire, objectName, bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_PRESIGNED_OBJECT_URL_ERROR);
        }
    }

    /**
     * 获取所有文件外链
     * @return Map<String, String>
     * @throws FileErrorException FileErrorException
     */
    public static Map<String, String> objectUrls() throws FileErrorException {
        return objectUrls(INSTANCE.defaultBucket);
    }

    /**
     * 获取所有文件外链
     * @param bucketName 数据桶
     * @return Map<String, String>
     * @throws FileErrorException FileErrorException
     */
    public static Map<String, String> objectUrls(String bucketName) throws FileErrorException {
        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusDays(7));
        try {
            return INSTANCE.minioClient.getPresignedPostFormData(policy);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException exception) {
            log.error("the minio server presigned object urls has error, bucket: {}, error: {}", bucketName, exception.getMessage());
            throw new FileErrorException(MinioErrorStatus.MINIO_PRESIGNED_ALL_OBJECT_URL_ERROR);
        }
    }

    /**
     * url编码转成UTF8
     * @param url url
     * @return String
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String urlDecode(String url) throws UnsupportedEncodingException {
        String uri = url.trim().replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        return URLDecoder.decode(uri, "UTF-8");
    }

}
