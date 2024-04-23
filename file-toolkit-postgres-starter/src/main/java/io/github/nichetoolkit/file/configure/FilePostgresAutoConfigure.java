package io.github.nichetoolkit.file.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>FilePostgresAutoConfigure</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"io.github.nichetoolkit.file"})
@MapperScan(basePackages = {"io.github.nichetoolkit.file.mapper"})
public class FilePostgresAutoConfigure {

    public FilePostgresAutoConfigure() {
        log.debug("================= file-postgres-auto-config initiated ！ ===================");
    }
}
