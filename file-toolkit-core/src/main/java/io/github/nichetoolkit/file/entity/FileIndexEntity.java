package io.github.nichetoolkit.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.rest.util.BeanUtils;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rest.util.JsonUtils;
import io.github.nichetoolkit.rice.IdModel;
import io.github.nichetoolkit.rice.RiceInfoEntity;
import io.github.nichetoolkit.rice.enums.OperateType;
import io.github.nichetoolkit.rice.helper.PropertyHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>FileIndexEntity</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */

@Data
@TableName("file_index")
@EqualsAndHashCode(callSuper = false)
public class FileIndexEntity extends RiceInfoEntity<FileIndexEntity, FileIndex> {
    /** 用户id */
    private String userId;
    /** 文件源名称 */
    private String originalFilename;
    /** 文件名称 */
    private String filename;
    /** 文件操作 */
    private Integer operate;
    /** 文件名别名 */
    private String alias;
    /** 文件扩展名 */
    private String suffix;
    /** 文件md5校验 */
    private String fileMd5;
    /** 文件操作 */
    private Long fileSize;
    /** 文件类型 */
    private Integer fileType;
    /** 是否完成 */
    private Boolean isFinish;
    /** 是否压缩 */
    private Boolean isCondense;
    /** 是否分片 */
    private Boolean isSlice;
    /** 分片大小 */
    private Integer sliceSize;
    /** 是否分片 */
    private Boolean isMerge;
    /** 文件存储hash校验 */
    private String etag;
    /** 文件存储版本 */
    private String versionId;
    /** 文件存储请求头 */
    private String headers;
    /** 文件存储属性 */
    private String properties;

    public FileIndexEntity() {
    }

    public FileIndexEntity(String id) {
        super(id);
    }

    public FileIndex toModel() {
        FileIndex model = new FileIndex();
        BeanUtils.copyNonullProperties(this, model);
        if (GeneralUtils.isNotEmpty(this.userId)) {
            IdModel<String> user = new IdModel<>(this.userId);
            model.setUser(user);
        }
        model.setName(this.originalFilename);
        model.setFileType(FileType.parseKey(this.fileType));
        model.setProperties(PropertyHelper.toPropertiesList(this.properties));
        model.setHeaders(JsonUtils.parseMapList(this.headers, String.class, String.class));
        model.setOperateType(OperateType.parseKey(this.operate));
        return model;
    }
}
