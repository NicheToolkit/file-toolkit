package io.github.nichetoolkit.file.constant;

public interface FileConstants {

    String FILE_ROOT_PREFIX = "nichetoolkit.file";
    String DATA_PREFIX = "data";
    String TEMP_PREFIX = "temp";

    String DEFAULT_IMAGE_SUFFIX = "png";

    String[] IMAGE_SUFFIX = new String[]
            {"jpg", "jpeg", "png", "bpm", "gif", "svg", "icon", "tfw", "psd", "tif", "tiff", "raw", "tag"};
    String[] DOCUMENT_SUFFIX = new String[]
            {"txt", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "vsdx", "eapx"};
    String[] VIDEO_SUFFIX = new String[]
            {"mp3", "mp4", "avi", "mkv", "rmvb", "rm", "asf", "wmv", "mov", "raw", "tag"};
    String[] EXECUTABLE_SUFFIX = new String[]{"sh", "bat", "exe", "py"};

    String[] COMPRESSED_SUFFIX = new String[]{"rar", "tar", "zip", "jar", "war"};


    String CONTENT_RANGE_HEADER = "Content-Range";
    String CONTENT_RANGE_BYTES_HEADER = "bytes ";
    String CONTENT_RANGE_RANGE_REGEX = "-";
    String CONTENT_RANGE_SIZE_REGEX = "/";

    String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
    String ATTACHMENT_FILENAME_VALUE = "attachment; filename=";

    String SUFFIX_REGEX = ".";
    String IMAGE_JPG_SUFFIX = "jpg";
    String IMAGE_PNG_SUFFIX = "png";

    String FILE_ZIP_SUFFIX = "zip";

    String IMAGE_CONDENSE_WIDTH_PROPERTY = "condenseWidth";
    String IMAGE_CONDENSE_HEIGHT_PROPERTY = "condenseHeight";
    String IMAGE_CONDENSE_QUALITY_PROPERTY = "condenseQuality";
    String IMAGE_CONDENSE_SCALE_PROPERTY = "condenseScale";
    String ORIGINAL_SUFFIX_PROPERTY = "originalSuffix";
    String ORIGINAL_SIZE_PROPERTY = "originalSize";
    String ORIGINAL_MD5_PROPERTY = "originalMd5";
    String ORIGINAL_NAME_PROPERTY = "originalName";

}
