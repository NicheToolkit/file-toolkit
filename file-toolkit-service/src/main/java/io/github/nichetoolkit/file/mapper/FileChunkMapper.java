package io.github.nichetoolkit.file.mapper;

import io.github.nichetoolkit.file.entity.FileChunkEntity;
import io.github.nichetoolkit.rice.RiceIdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

@Component
public interface FileChunkMapper extends RiceIdMapper<FileChunkEntity>, Mapper<FileChunkEntity> {

    /**
     * 通过id查询实体
     * @param fileId     文件id
     * @param chunkIndex 分片序号
     * @return T 查询的数据
     */
    FileChunkEntity findByFileIdAndChunkIndex(@Param("fileId") String fileId, @Param("chunkIndex") Integer chunkIndex);


    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    FileChunkEntity findByFileIdFirstChunk(@Param("fileId") String fileId);

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    FileChunkEntity findByFileIdLastChunk(@Param("fileId") String fileId);

    /**
     * 通过id查询实体
     * @param fileId 文件id
     * @return T 查询的数据
     */
    List<FileChunkEntity> findAllByFileId(@Param("fileId") String fileId);

    /**
     * 通过id查询实体
     * @param fileIds 文件id集合
     * @return T 查询的数据
     */
    List<FileChunkEntity> findAllByFileIds(@Param("fileIds") Collection<String> fileIds);


}
