package io.github.nichetoolkit.file.service;


import io.github.nichetoolkit.file.FileFilter;
import io.github.nichetoolkit.file.PartModel;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rice.service.FilterService;
import io.github.nichetoolkit.rice.service.extend.RemoveService;

import java.util.Collection;
import java.util.List;

/**
 * <p>FileChunkService</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public interface FileChunkService extends FilterService<String,String, PartModel, FileFilter>, RemoveService<String,String> {

    /**
     * 通过id查询实体
     * @param fileId     文件id
     * @param chunkIndex 分片序号
     * @return T 查询的数据
     */
    PartModel queryByFileIdAndChunkIndex(String fileId, Integer chunkIndex) throws RestException;

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    PartModel queryByFileIdFirstChunk(String fileId) throws RestException;

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    PartModel queryByFileIdLastChunk(String fileId) throws RestException;

    /**
     * 通过id集合查询所有
     * @param fileId 对象id
     * @return List<M> 查询的数据
     * @throws RestException 模块异常
     */
    List<PartModel> queryAllByFileId(String fileId) throws RestException;

    /**
     * 通过id集合查询所有
     * @param fileIds 对象id集合
     * @return List<M> 查询的数据
     * @throws RestException 模块异常
     */
    List<PartModel> queryAllByFileIds(Collection<String> fileIds) throws RestException;

}
