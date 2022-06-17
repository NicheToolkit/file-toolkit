package io.github.nichetoolkit.file.configure;

import io.github.nichetoolkit.file.video.MinioHttpRequestHandler;
import io.github.nichetoolkit.file.video.VideoHttpRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>FileMinioStarterAutoConfigure</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Configuration
@org.mybatis.spring.annotation.MapperScan("io.github.nichetoolkit.file.mapper")
@MapperScan("io.github.nichetoolkit.file.mapper")
@ComponentScan(basePackages = {"io.github.nichetoolkit.file"})
public class FileMinioStarterAutoConfigure {
    public FileMinioStarterAutoConfigure() {
        log.debug("================= file-minio-starter-auto-config initiated ！ ===================");
    }

    @Bean
    @ConditionalOnMissingBean(value = VideoHttpRequestHandler.class)
    public VideoHttpRequestHandler minioHttpRequestHandler() {
        return new MinioHttpRequestHandler();
    }
}
