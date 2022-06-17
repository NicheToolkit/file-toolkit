package io.github.nichetoolkit.file.configure;

import io.github.nichetoolkit.file.minio.MinioUtils;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>FileMinioUtilsAutoConfigure</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"io.github.nichetoolkit.file"})
public class FileMinioUtilsAutoConfigure {

    public FileMinioUtilsAutoConfigure() {
        log.debug("================= file-minio-utils-auto-config initiated ！ ===================");
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

}
