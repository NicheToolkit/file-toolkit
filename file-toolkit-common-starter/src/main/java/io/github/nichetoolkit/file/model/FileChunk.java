package io.github.nichetoolkit.file.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nichetoolkit.file.entity.FileChunkEntity;
import io.github.nichetoolkit.rest.util.BeanUtils;
import io.github.nichetoolkit.rice.RiceIdModel;
import io.github.nichetoolkit.rice.enums.OperateType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

/**
 * <p>FileChunk</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileChunk extends RiceIdModel<FileChunk, FileChunkEntity> implements Comparator<FileChunk>, Comparable<FileChunk> {
    /** 文件id */
    private String fileId;
    /** 分片序列 */
    private Integer chunkIndex;
    /** 分片大小 */
    private Long chunkSize;
    /** 起始位置 */
    private Long chunkStart;
    /** 终止位置 */
    private Long chunkEnd;
    /** 分片MD5校验 */
    private String chunkMd5;
    /** 文件操作 */
    protected OperateType operateType = OperateType.NONE;
    /** 分片状态 */
    private Boolean isLastChunk = false;
    /** 分片创建时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date chunkTime;
    /** 分片开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /** 分片结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JsonIgnore
    private byte[] bytes;

    @JsonIgnore
    public ByteArrayInputStream inputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public FileChunk() {
    }

    public FileChunk(String id) {
        super(id);
    }

    public FileChunkEntity toEntity() {
        FileChunkEntity entity = new FileChunkEntity();
        BeanUtils.copyNonullProperties(this, entity);
        entity.setOperate(Optional.ofNullable(this.operateType).map(OperateType::getKey).orElse(OperateType.NONE.getKey()));
        return entity;
    }

    @Override
    public int compare(FileChunk source, FileChunk target) {
        return Integer.compare(source.getChunkIndex(), target.getChunkIndex());
    }

    @Override
    public int compareTo(@NonNull FileChunk target) {
        return Integer.compare(this.getChunkIndex(), target.getChunkIndex());
    }
}
