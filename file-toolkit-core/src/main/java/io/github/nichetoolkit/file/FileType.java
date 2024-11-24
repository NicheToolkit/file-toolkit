package io.github.nichetoolkit.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.rest.RestKey;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Getter
public enum FileType implements RestKey<String> {
    IMAGE("IMAGE", FileConstants.IMAGE_SUFFIX),
    DOCUMENT("DOCUMENT", FileConstants.DOCUMENT_SUFFIX),
    VIDEO("VIDEO", FileConstants.VIDEO_SUFFIX),
    EXECUTABLE("EXECUTABLE", FileConstants.EXECUTABLE_SUFFIX),
    COMPRESSED("COMPRESSED", FileConstants.COMPRESSED_SUFFIX),
    UNKNOWN( "UNKNOWN", new String[0]),
    ;

    private final String key;
    private final String[] types;

    FileType(String key, String[] types) {
        this.key = key;
        this.types = types;
    }

    @JsonValue
    @Override
    public String getKey() {
        return this.key;
    }

    @JsonCreator
    public static FileType parseKey(@NonNull String key) {
        FileType typeEnum = RestKey.parseKey(FileType.class, key);
        return Optional.ofNullable(typeEnum).orElse(FileType.UNKNOWN);
    }
}
