package io.github.nichetoolkit.file.mapper;

import io.github.nichetoolkit.file.entity.FileIndexEntity;
import io.github.nichetoolkit.rice.RiceInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface FileIndexMapper extends RiceInfoMapper<FileIndexEntity>, Mapper<FileIndexEntity> {
    /**
     * 查询上传中断文件
     * @param name 文件源名称
     * @return T 查询的数据
     */
    FileIndexEntity findByNameWithUploadInterrupt(@Param("name") String name);

    /**
     * 实体更新
     * @param id 实体id集合
     * @return Integer SQL影响行数（成功为1）
     */
    Integer finishSliceUpload(@Param("id") String id, @Param("sliceSize") Integer sliceSize);

}
