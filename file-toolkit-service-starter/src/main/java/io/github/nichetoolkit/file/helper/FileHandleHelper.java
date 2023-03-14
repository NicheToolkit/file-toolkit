package io.github.nichetoolkit.file.helper;

import io.github.nichetoolkit.file.error.FileErrorStatus;
import io.github.nichetoolkit.file.handle.FileStoreService;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.github.nichetoolkit.rest.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>FileHandleHelper</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Component
public class FileHandleHelper implements InitializingBean {

    @Resource(type = FileStoreService.class)
    private FileStoreService fileStoreService;

    private static FileHandleHelper INSTANCE = null;

    public static FileHandleHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }

    public static FileStoreService fileStoreService() {
        return INSTANCE.fileStoreService;
    }

    public static void writeFile(String objectName, String itemFilePath) throws RestException {
        try (InputStream inputStream = INSTANCE.fileStoreService.getById(objectName)) {
            StreamUtils.write(itemFilePath, inputStream);
        } catch (IOException exception) {
            log.error("the file service download has error: {}", exception.getMessage());
            throw new FileErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR);
        }
    }
}
