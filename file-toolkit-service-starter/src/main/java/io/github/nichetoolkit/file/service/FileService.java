package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.model.FileRequest;
import io.github.nichetoolkit.rest.RestException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.concurrent.Future;

/**
 * <p>FileService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileService {

    /**
     * 文件移除
     * @param fileFilter 过滤器删除
     * @throws RestException 服务异常
     */
    void remove(FileFilter fileFilter) throws RestException;

    /**
     * 文件移除
     * @param fileId 文件id
     * @param chunk  是否是分片
     * @param delete 是否真实删除
     * @param rename 是否重命名删除
     * @throws RestException 服务异常
     */
    void remove(String fileId, Boolean chunk, Boolean delete, Boolean rename) throws RestException;

    /**
     * 下载文件
     * @param file        文件数据
     * @param filename    文件名称
     * @param contentType contentType
     * @param response    返回体
     * @throws RestException 服务异常
     */
    void download(File file, String filename, String contentType, HttpServletRequest request, HttpServletResponse response) throws RestException;

    /**
     * 下载文件
     * @param fileIndex   文件id
     * @param filename    文件名称
     * @param contentType contentType
     * @param response    返回体
     * @throws RestException 服务异常
     */
    void download(FileIndex fileIndex, String filename, String contentType, Boolean preview, FileType fileType, HttpServletRequest request, HttpServletResponse response) throws RestException;

    /**
     * @param fileIndex 文件id
     * @param filename  文件名称
     * @param preview   是否预览图片
     * @param response  返回体
     * @throws RestException 服务异常
     */
    void download(FileIndex fileIndex, String filename, Boolean preview, FileType fileType, HttpServletRequest request, HttpServletResponse response) throws RestException;

    /**
     * 下载多文件
     * @param fileFilter 查询过滤器
     * @param response   返回体
     * @throws RestException 服务异常
     */
    void download(FileFilter fileFilter, HttpServletRequest request, HttpServletResponse response) throws RestException;

    /**
     * 下载文件
     * @param fileId   文件id
     * @param chunk    是否下载分片文件
     * @param preview  是否预览图片
     * @param original 是否原始命名
     * @param response 返回体
     * @throws RestException 服务异常
     */
    void fileDownload(String fileId, Boolean chunk, Boolean preview, Boolean original, HttpServletRequest request, HttpServletResponse response) throws RestException;

    /**
     * 下载图片
     * @param fileId   文件id
     * @param preview  是否预览图片
     * @param original 是否原始命名
     * @param response 返回体
     * @throws RestException 服务异常
     */
    void imageDownload(String fileId, Boolean preview, Boolean original, HttpServletRequest request, HttpServletResponse response) throws RestException;

    /**
     * 上传文件
     * @param file        文件数据
     * @param fileRequest 文件信息
     * @return FileIndex
     * @throws RestException 服务异常
     */
    FileIndex upload(MultipartFile file, FileRequest fileRequest) throws RestException;

    /**
     * 上传文件
     * @param fileIndex 文件信息
     * @return FileIndex
     * @throws RestException 服务异常
     */
    FileIndex upload(FileIndex fileIndex) throws RestException;

    /**
     * 预分片上传文件
     * @param fileIndex 文件信息
     * @return FileIndex
     * @throws RestException 服务异常
     */
    FileIndex indexUpload(FileIndex fileIndex) throws RestException;

    /**
     * 分片上传
     * @param file         分片文件数据
     * @param contentRange 分片header
     * @param fileRequest  文件信息
     * @return FileIndex
     * @throws RestException 服务异常
     */
    Future<FileIndex> chunkUpload(MultipartFile file, String contentRange, FileRequest fileRequest) throws RestException;

}
