package io.github.nichetoolkit.file.video;

import io.github.nichetoolkit.file.model.FileIndex;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>FileInputStreamResource</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public abstract class VideoInputStreamResource extends InputStreamResource {
    private FileIndex fileIndex;

    public VideoInputStreamResource(FileIndex fileIndex) {
        super(fileIndex.inputStream());
        this.fileIndex = fileIndex;
    }

    public VideoInputStreamResource(InputStream inputStream, FileIndex fileIndex) {
        super(inputStream);
        this.fileIndex = fileIndex;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean isReadable() {
        return fileIndex.getIsFinish();
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public abstract long contentLength() throws IOException;

    @Override
    public abstract long lastModified() throws IOException;

    @Override
    public String getFilename() {
        return fileIndex.getName();
    }

    @Override
    public boolean equals(Object other) {
        return fileIndex.equals(other);
    }

    @Override
    public String getDescription() {
        return " file input stream resource [" + super.getDescription() + "]";
    }

}
