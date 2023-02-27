package io.github.nichetoolkit.file.service.impl;

import io.github.nichetoolkit.file.configure.FileCommonProperties;
import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.file.error.FileErrorStatus;
import io.github.nichetoolkit.file.helper.FileServiceHelper;
import io.github.nichetoolkit.file.helper.ImageHelper;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.service.FileHandleService;
import io.github.nichetoolkit.file.util.ImageUtils;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.error.natives.FileErrorException;
import io.github.nichetoolkit.rest.identity.IdentityUtils;
import io.github.nichetoolkit.rest.util.FileUtils;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rest.util.StreamUtils;
import io.github.nichetoolkit.rest.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>FileHandleServiceImpl</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Service
public class FileHandleServiceImpl implements FileHandleService {

    @Autowired
    protected FileCommonProperties commonProperties;
    @Autowired
    private FileCommonProperties serviceProperties;

    @Async
    @Override
    public void autographImage(FileIndex fileIndex) throws RestException {
        String tempPath = FileUtils.createPath(commonProperties.getTempPath());
        String cachePath = FileUtils.createPath(tempPath, fileIndex.getId());
        String randomPath = FileUtils.createPath(cachePath, GeneralUtils.uuid());
        String filename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(FileConstants.IMAGE_PNG_SUFFIX);
        String filePath = randomPath.concat(File.separator).concat(filename);
        File file = new File(filePath);
        if (file.exists()) {
            FileUtils.delete(filePath);
        }
        try(InputStream inputStream = fileIndex.inputStream()) {
            BufferedImage bufferedImage = ImageUtils.read(inputStream);
            BufferedImage binaryImage = ImageUtils.binaryImage(bufferedImage);
            BufferedImage autographImage = ImageUtils.autograph(binaryImage);
            ImageUtils.write(autographImage, file);
        } catch (IOException exception) {
            log.error("the image file has error during autograph: {}", exception.getMessage());
            throw new FileErrorException(FileErrorStatus.FILE_IMAGE_CONDENSE_ERROR);
        }
        byte[] bytes = ImageUtils.bytes(file);
        fileIndex.setBytes(bytes);
        FileUtils.delete(filePath);
        FileUtils.clearFile(randomPath);
        FileUtils.clear(cachePath);
    }

    @Async
    @Override
    public void condenseImage(FileIndex fileIndex) throws RestException {
        String tempPath = FileUtils.createPath(commonProperties.getTempPath());
        String cachePath = FileUtils.createPath(tempPath, fileIndex.getId());
        String randomPath = FileUtils.createPath(cachePath, GeneralUtils.uuid());
        Long imageFileSize;
        Double imageFileQuality = 1.0d;
        Double imageFileScale = 1.0d;
        String filename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(FileConstants.IMAGE_PNG_SUFFIX);
        String filePath;
        File file;
        Integer width = fileIndex.getWidth();
        Integer height = fileIndex.getHeight();
        filePath = randomPath.concat(File.separator).concat(filename);
        file = new File(filePath);
        do {
            if (file.exists()) {
                FileUtils.delete(filePath);
            }
            try(InputStream inputStream = fileIndex.inputStream()) {
                Thumbnails.of(inputStream).scale(imageFileScale).outputFormat(FileConstants.IMAGE_PNG_SUFFIX).outputQuality(imageFileQuality).toFile(filePath);
                BufferedImage bufferedImage = ImageHelper.read(file);
                int imageWidth = bufferedImage.getWidth();
                int imageHeight = bufferedImage.getHeight();
                if (GeneralUtils.isNotEmpty(width) && GeneralUtils.isNotEmpty(height)) {
                    Thumbnails.of(inputStream).size(width, height).outputFormat(FileConstants.IMAGE_PNG_SUFFIX).outputQuality(imageFileQuality).toFile(filePath);
                } else if (GeneralUtils.isNotEmpty(width) || GeneralUtils.isNotEmpty(height)) {
                    if (GeneralUtils.isNotEmpty(width)) {
                        imageFileScale = ((double) width / (double) imageWidth >= 1.0D) ? imageFileScale : ((double) width / (double) imageWidth);
                    } else {
                        imageFileScale = ((double) height / (double) imageHeight >= 1.0D) ? imageFileScale : ((double) height / (double) imageHeight);
                    }
                    Thumbnails.of(inputStream).scale(imageFileScale).outputFormat(FileConstants.IMAGE_PNG_SUFFIX).outputQuality(imageFileQuality).toFile(filePath);
                } else {
                    Thumbnails.of(inputStream).scale(imageFileScale).outputFormat(FileConstants.IMAGE_PNG_SUFFIX).outputQuality(imageFileQuality).toFile(filePath);
                }
            } catch (IOException exception) {
                log.error("the image file has error during condensing: {}", exception.getMessage());
                throw new FileErrorException(FileErrorStatus.FILE_IMAGE_CONDENSE_ERROR);
            }
            imageFileSize = file.length();
            if (imageFileQuality.equals(serviceProperties.getMinImageQuality())) {
                imageFileScale += -0.1d;
            } else {
                imageFileQuality += -0.1d;
            }
        } while (imageFileSize > serviceProperties.getMaxImageSize()
                && imageFileQuality > serviceProperties.getMinImageQuality()
                && imageFileScale > serviceProperties.getMinImageScale());
        if (GeneralUtils.isNotEmpty(width) && GeneralUtils.isNotEmpty(height)) {
            fileIndex.addProperty(FileConstants.IMAGE_CONDENSE_WIDTH_PROPERTY, width);
            fileIndex.addProperty(FileConstants.IMAGE_CONDENSE_HEIGHT_PROPERTY, height);
        } else {
            fileIndex.addProperty(FileConstants.IMAGE_CONDENSE_SCALE_PROPERTY, imageFileScale);
        }
        fileIndex.addProperty(FileConstants.IMAGE_CONDENSE_QUALITY_PROPERTY, imageFileQuality);
        FileServiceHelper.buildProperties(filename, file.length(), FileConstants.IMAGE_PNG_SUFFIX, fileIndex);
        FileServiceHelper.buildMd5(file, fileIndex);
        FileUtils.delete(filePath);
        FileUtils.clearFile(randomPath);
        FileUtils.clear(cachePath);
    }

    @Async
    @Override
    public void condenseFile(FileIndex fileIndex) throws RestException {
        String tempPath = FileUtils.createPath(commonProperties.getTempPath());
        String cachePath = FileUtils.createPath(tempPath, fileIndex.getId());
        String randomPath = FileUtils.createPath(cachePath, GeneralUtils.uuid());
        String filename = fileIndex.getName();
        String zipFilename = fileIndex.getFilename().concat(FileConstants.SUFFIX_REGEX).concat(FileConstants.FILE_ZIP_SUFFIX);
        String filePath = randomPath.concat(File.separator).concat(filename);
        File file = FileUtils.createFile(filePath);
        try(InputStream inputStream = fileIndex.inputStream()) {
            StreamUtils.write(file, inputStream);
        } catch (IOException exception) {
            log.error("the file has error during autograph: {}", exception.getMessage());
            throw new FileErrorException(FileErrorStatus.FILE_CONDENSE_ERROR);
        }
        File zipFile = ZipUtils.zipFile(randomPath, zipFilename, file);
        FileServiceHelper.buildProperties(zipFilename, zipFile.length(), FileConstants.FILE_ZIP_SUFFIX, fileIndex);
        if (fileIndex.getIsMd5()) {
            FileServiceHelper.buildMd5(zipFile, fileIndex);
        }
        FileUtils.delete(filePath);
        FileUtils.clearFile(randomPath);
        FileUtils.clear(cachePath);
    }
}
