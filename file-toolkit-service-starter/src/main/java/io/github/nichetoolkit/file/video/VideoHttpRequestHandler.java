package io.github.nichetoolkit.file.video;

import io.github.nichetoolkit.file.model.FileIndex;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>VideoHttpRequestHandler</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public abstract class VideoHttpRequestHandler extends ResourceHttpRequestHandler {

    public final static String VIDEO_FILE = "VIDEO-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) throws IOException {
        final FileIndex fileIndex = (FileIndex) request.getAttribute(VIDEO_FILE);
        return videoInputStreamResource(fileIndex);
    }

    abstract public VideoInputStreamResource videoInputStreamResource(FileIndex fileIndex) throws IOException;
}
