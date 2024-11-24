package io.github.nichetoolkit.file.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>FileCommonAutoConfigure</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"io.github.nichetoolkit.file"})
public class FileCommonAutoConfigure {

    public FileCommonAutoConfigure() {
        log.debug("================= file-common-auto-config initiated ！ ===================");
    }
}
