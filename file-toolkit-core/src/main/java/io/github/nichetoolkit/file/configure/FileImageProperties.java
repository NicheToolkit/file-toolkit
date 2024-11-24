package io.github.nichetoolkit.file.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "nichetoolkit.file.image")
public class FileImageProperties {
    private Long maxSize = 100 * 1024L;
    private Double minQuality = 0.5d;
    private Double minScale = 0.5d;
}
