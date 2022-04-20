package io.github.nichetoolkit.file.configure;

import io.github.nichetoolkit.file.minio.MinioUtils;
import io.github.nichetoolkit.file.service.AsyncFileService;
import io.github.nichetoolkit.file.service.impl.MinioFileService;
import io.github.nichetoolkit.file.video.MinioHttpRequestHandler;
import io.github.nichetoolkit.file.video.VideoHttpRequestHandler;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
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
@org.mybatis.spring.annotation.MapperScan("io.github.nichetoolkit.file.mapper")
@MapperScan("io.github.nichetoolkit.file.mapper")
@ComponentScan(basePackages = {"io.github.nichetoolkit.file"})
public class FileMinioAutoConfigure {

    public FileMinioAutoConfigure() {
        log.debug("================= file-minio-auto-config initiated ！ ===================");
    }

    @Bean
    @ConditionalOnMissingBean(value = MinioClient.class)
    public MinioClient minioClient(FileMinioProperties minioProperties) {
        return MinioUtils.createMinioClient(minioProperties);
    }

    @Bean
    @ConditionalOnMissingBean(value = MinioUtils.class)
    public MinioUtils minioUtils(MinioClient minioClient, FileMinioProperties minioProperties) {
        return new MinioUtils(minioClient, minioProperties);
    }

    @Bean
    @ConditionalOnMissingBean(value = VideoHttpRequestHandler.class)
    public VideoHttpRequestHandler minioHttpRequestHandler() {
        return new MinioHttpRequestHandler();
    }
}
