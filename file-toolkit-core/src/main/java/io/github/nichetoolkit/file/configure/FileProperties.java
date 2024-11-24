package io.github.nichetoolkit.file.configure;

import io.github.nichetoolkit.file.constant.FileConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "nichetoolkit.file")
public class FileProperties {
    private String rootPath = toRootPath();
    private String tempPath = toTempPath();
    private Long maxSize = 100 * 1024 * 1024L;

    public String getRootPath() {
        String trim = this.rootPath.toLowerCase().trim();
        if (!trim.endsWith(FileConstants.FILE_ROOT_PREFIX) || !trim.contains(FileConstants.FILE_ROOT_PREFIX)) {
            if (!trim.endsWith(File.separator)) {
                return trim.concat(File.separator).concat(FileConstants.FILE_ROOT_PREFIX);
            } else {
                return trim.concat(FileConstants.FILE_ROOT_PREFIX);
            }
        }
        return trim;
    }

    public String getTempPath() {
        String trim = this.tempPath.toLowerCase().trim();
        return concatRoot(trim);
    }

    public String getTempPath(String uuid) {
        String trim = this.tempPath.toLowerCase().trim();
        return concatRoot(trim).concat(File.separator).concat(uuid);
    }
    
    private String concatRoot(String path) {
        String rootPath = getRootPath();
        if (!path.startsWith(rootPath) || !path.contains(rootPath)) {
            return rootPath.concat(path);
        }
        return path;
    }

    public static String toRootPath() {
        return File.separator.concat(FileConstants.DATA_PREFIX).concat(File.separator).concat(FileConstants.FILE_ROOT_PREFIX);
    }

    public static String toTempPath() {
        return File.separator.concat(FileConstants.TEMP_PREFIX).concat(File.separator);
    }

}
