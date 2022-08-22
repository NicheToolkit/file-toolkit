package io.github.nichetoolkit.file.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nichetoolkit.file.entity.FileIndexEntity;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.rest.util.BeanUtils;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rest.util.JsonUtils;
import io.github.nichetoolkit.rice.RestId;
import io.github.nichetoolkit.rice.RiceInfoModel;
import io.github.nichetoolkit.rice.enums.OperateType;
import io.github.nichetoolkit.rice.helper.PropertyHelper;
import io.github.nichetoolkit.rice.jsonb.Property;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.Headers;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>FileIndex</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileIndex extends RiceInfoModel<FileIndex, FileIndexEntity> {

    /** 上传用户 */
    protected RestId<String> user;

    /** 文件名 */
    protected String filename;

    /** 文件名别名 */
    protected String alias;

    /** 文件扩展名 */
    protected String suffix;

    /** 文件md5校验 */
    protected String fileMd5;

    /** 文件字节大小 */
    protected Long fileSize;

    /** 文件类型 */
    protected FileType fileType = FileType.IMAGE;

    /** 文件操作 */
    protected OperateType operateType = OperateType.NONE;

    /** 文件附带属性参数 */
    protected List<Property> properties;

    /** 是否压缩 */
    protected Boolean isCondense;

    /** 当前上传是否结束  */
    protected Boolean isFinish;

    /** 文件附带属性参数 */
    protected Map<String, List<String>> headers;

    /** 文件名别名 */
    protected String etag;

    /** 是否分片 */
    private Boolean isMd5;

    /** 是否分片 */
    private Boolean isSlice;

    /** 总分片数 */
    private Integer sliceSize;

    /** 是否合并分片 */
    private Boolean isMerge;

    /** 文件分块对象 */
    private List<FileChunk> fileChunks;

    /** 当前分片上传 */
    private Integer currentIndex;

    /** 文件分块对象 */
    private FileChunk fileChunk;

    /** 图片宽度 */
    private Integer width;

    /** 图片高度 */
    private Integer height;

    @JsonIgnore
    protected byte[] bytes;

    @JsonIgnore
    protected MultipartFile file;


    public FileIndex() {
    }

    public FileIndex(String id) {
        super(id);
    }

    @JsonIgnore
    public InputStream inputStream() {
        if (GeneralUtils.isNotEmpty(this.file)){
            try {
                return this.file.getInputStream();
            } catch (IOException ignored) {
            }
        } else {
            return new ByteArrayInputStream(this.bytes);
        }
        return null;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        addProperty("alias",alias);
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        addProperty("suffix",suffix);
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
        addProperty("fileMd5",fileMd5);
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        addProperty("fileSize",fileSize);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        addProperty("name", name);
    }

    @JsonIgnore
    public void resetHeaders(Headers headers) {
        this.headers = headers.toMultimap();
    }

    public void addProperty(@NonNull String name, Object value) {
        if (GeneralUtils.isNotEmpty(value)) {
            if (GeneralUtils.isEmpty(this.properties)) {
                this.properties = new ArrayList<>();
            } else {
                this.properties.remove(new Property(name));
            }
            this.properties.add(new Property(name,value));
        }
    }

    @Override
    public FileIndexEntity toEntity() {
        FileIndexEntity entity = new FileIndexEntity();
        BeanUtils.copyNonullProperties(this,entity);
        entity.setUserId(Optional.ofNullable(this.user).map(RestId::getId).orElse(null));
        entity.setOriginalFilename(this.name);
        entity.setFileType(Optional.ofNullable(this.fileType).map(FileType::getKey).orElse(null));
        entity.setProperties(PropertyHelper.toPropertiesJson(this.properties));
        entity.setHeaders(JsonUtils.parseJson(this.headers));
        entity.setOperate(Optional.ofNullable(this.operateType).map(OperateType::getKey).orElse(OperateType.NONE.getKey()));
        return entity;
    }
}
