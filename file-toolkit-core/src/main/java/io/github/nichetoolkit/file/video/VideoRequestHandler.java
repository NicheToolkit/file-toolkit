package io.github.nichetoolkit.file.video;

import io.github.nichetoolkit.file.BulkModel;
import io.github.nichetoolkit.file.constant.FileConstants;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>VideoHttpRequestHandler</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public abstract class VideoRequestHandler extends ResourceHttpRequestHandler {

    @Override
    protected Resource getResource(HttpServletRequest request) throws IOException {
        final BulkModel fileIndex = (BulkModel) request.getAttribute(FileConstants.VIDEO_FILE_HEADER);
        return videoInputStreamResource(fileIndex);
    }

    abstract public VideoResource videoInputStreamResource(BulkModel fileIndex) throws IOException;
}
