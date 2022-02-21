package io.github.nichetoolkit.file.service;


import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rice.RestPage;
import io.github.nichetoolkit.rice.filter.IdFilter;
import io.github.nichetoolkit.rice.service.FilterService;

import java.util.Collection;
import java.util.List;

/**
 * <p>FileChunkService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileChunkService extends FilterService<String, FileChunk, FileFilter> {

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @param chunkIndex 分片序号
     * @return T 查询的数据
     */
    FileChunk queryByFileIdAndChunkIndex(String fileId, Integer chunkIndex) throws RestException;

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    FileChunk queryByFileIdFirstChunk(String fileId) throws RestException;

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    FileChunk queryByFileIdLastChunk(String fileId) throws RestException;

    /**
     * 通过id集合查询所有
     * @param fileId 对象id
     * @return List<M> 查询的数据
     * @throws RestException 模块异常
     */
    List<FileChunk> queryAllByFileId(String fileId) throws RestException;

    /**
     * 通过id集合查询所有
     * @param fileIds 对象id集合
     * @return List<M> 查询的数据
     * @throws RestException 模块异常
     */
    List<FileChunk> queryAllByFileIds(Collection<String> fileIds) throws RestException;

}
