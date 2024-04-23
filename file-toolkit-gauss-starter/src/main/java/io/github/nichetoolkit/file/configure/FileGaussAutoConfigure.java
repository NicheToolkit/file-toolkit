package io.github.nichetoolkit.file.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>FileServiceAutoConfigure</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"io.github.nichetoolkit.file"})
@MapperScan(basePackages = {"io.github.nichetoolkit.file.mapper"})
public class FileGaussAutoConfigure {

    public FileGaussAutoConfigure() {
        log.debug("================= file-gauss-auto-config initiated ！ ===================");
    }
}
