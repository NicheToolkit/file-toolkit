package io.github.nichetoolkit.file.service;


import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rice.service.FilterService;

/**
 * <p>FileIndexService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileIndexService extends FilterService<String, FileIndex, FileFilter> {

    /**
     * 通过id集合查询单个
     * @param name 对象id
     * @return M 查询的对象
     * @throws RestException 模块异常
     */
    FileIndex queryByNameWithUploadInterrupt(String name) throws RestException;

    /**
     * 文件分片上传结束
     * @param id id
     * @throws RestException 模块异常
     */
    void finishSliceUpload(String id, Integer sliceSize) throws RestException;

}
