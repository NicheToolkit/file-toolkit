package io.github.nichetoolkit.file.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.rest.util.BeanUtils;
import io.github.nichetoolkit.rice.RiceIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>FileChunkEntity</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */

@Data
@TableName("file_chunk")
@EqualsAndHashCode(callSuper = false)
public class FileChunkEntity extends RiceIdEntity<FileChunkEntity,FileChunk> {
    /** 文件id */
    private String fileId;
    /** 分片序列 */
    private Integer chunkIndex;
    /** 分片序列 */
    private Long chunkSize;
    /** 分片开始 */
    private Long chunkStart;
    /** 分片结束 */
    private Long chunkEnd;
    /** 分片结束 */
    private String chunkMd5;
    /** 最后分片 */
    private Boolean isLastChunk;
    /** 分片创建时间 */
    private Date chunkTime;
    /** 分片开始时间 */
    private Date startTime;
    /** 分片结束时间 */
    private Date endTime;

    public FileChunkEntity() {
    }

    public FileChunkEntity(String id) {
        super(id);
    }

    public FileChunk toModel() {
        FileChunk model = new FileChunk();
        BeanUtils.copyNonullProperties(this, model);
        return model;
    }
}
