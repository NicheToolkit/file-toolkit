package io.github.nichetoolkit.file.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.nichetoolkit.rest.RestValue;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * <p>FileType</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public enum FileType implements RestValue<Integer,String> {
    /**
     * JPG、PNG、PDF、TIFF、SWF
     */
    IMAGE(1,"图片"),
    /**
     * TXT、DOC、XLS、PPT、DOCX、XLSX、PPTX
     */
    DOCUMENT(2,"文档"),
    /**
     * FLV、RMVB、MP4、MVB
     */
    VIDEO(3,"视频"),

    /**
     * SH、EXE、BAT
     */
    EXE(4,"可执行文件"),

    /**
     * RAR、ZIP、TAR、JAR、WAR
     */
    RAR(5,"压缩文件"),

    /**
     * 其他文件类型
     */
    OTHER(99,"其他文件"),

    /**
     * 未知文件类型
     */
    UNKNOWN(999,"未知文件"),
    ;

    private final Integer key;
    private final String value;

    public static final String[] IMAGES_DICT = new String[]
            {"jpg", "jpeg", "png", "bpm", "gif", "svg", "icon", "tfw", "psd", "tif", "tiff", "raw", "tag"};
    public static final String[] DOCUMENTS_DICT = new String[]
            {"txt", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "vsdx", "eapx"};
    public static final String[] VIDEOS_DICT = new String[]
            {"mp3", "mp4", "avi", "mkv", "rmvb", "rm", "asf", "wmv", "mov", "raw", "tag"};
    public static final String[] EXES_DICT = new String[]{"sh", "bat", "exe", "py"};

    public static final String[] RARS_DICT = new String[]{"rar", "tar", "zip", "jar", "war"};

    FileType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    @JsonValue
    public Integer getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static FileType parseKey(@NonNull Integer key) {
        FileType typeEnum = RestValue.parseKey(FileType.class, key);
        return Optional.ofNullable(typeEnum).orElse(FileType.OTHER);
    }

    public static FileType parseValue(@NonNull String value) {
        FileType typeEnum = RestValue.parseValue(FileType.class, value);
        return Optional.ofNullable(typeEnum).orElse(FileType.OTHER);
    }
}
