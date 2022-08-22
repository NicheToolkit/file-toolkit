package io.github.nichetoolkit.file.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.rest.util.BeanUtils;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rice.IdModel;
import io.github.nichetoolkit.rice.helper.PropertyHelper;
import io.github.nichetoolkit.rice.jsonb.Property;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileRequest implements Serializable {
    /** 上传用户 */
    protected String userId;

    protected String fileId;

    protected String name;

    /** 文件名别名 */
    protected String alias;

    /** 文件字节大小 */
    protected Long fileSize;

    /** 文件类型 */
    protected Integer fileType;

    /** 文件类型 */
    protected String properties;

    /** 图片宽度 */
    protected Integer width;

    /** 图片高度 */
    protected Integer height;

    /** 是否分片 */
    protected Boolean isSlice;

    /** 总分片数 */
    protected Integer sliceSize;

    /** 是否合并分片 */
    protected Boolean isMerge;

    /** 是否压缩 */
    protected Boolean isCondense;

    public FileIndex toIndex() {
        FileIndex fileIndex = new FileIndex(fileId);
        BeanUtils.copyNonullProperties(this,fileIndex);
        fileIndex.setFileType(FileType.parseKey(this.fileType));
        if (GeneralUtils.isNotEmpty(this.properties)) {
            Map<String, String> propertiesMap = PropertyHelper.toPropertiesMap(this.properties);
            List<Property> properties = PropertyHelper.toPropertiesList(propertiesMap);
            if (GeneralUtils.isNotEmpty(properties)) {
                fileIndex.setProperties(properties);
            }
        }
        if (GeneralUtils.isNotEmpty(this.userId)) {
            IdModel<String> user = new IdModel<>(this.userId);
            fileIndex.setUser(user);
        }
        return fileIndex;
    }
}
