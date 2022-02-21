package io.github.nichetoolkit.file.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * <p>FileServiceProperties</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Component
@ConfigurationProperties(prefix = "nichetoolkit.file.service")
public class FileServiceProperties {
    private String tempPath = "/data/server/file/temp";
    private Long maxFileSize = 100 * 1024 * 1024L;
    private Long maxImageSize = 100 * 1024L;
    private Double minImageQuality = 0.5d;
    private Double minImageScale = 0.5d;

    public FileServiceProperties() {
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Long getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(Long maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public Double getMinImageQuality() {
        return minImageQuality;
    }

    public void setMinImageQuality(Double minImageQuality) {
        this.minImageQuality = minImageQuality;
    }

    public Double getMinImageScale() {
        return minImageScale;
    }

    public void setMinImageScale(Double minImageScale) {
        this.minImageScale = minImageScale;
    }

}
