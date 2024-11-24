package io.github.nichetoolkit.file.image;

import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.error.ImageReadException;
import io.github.nichetoolkit.file.error.ImageTransferException;
import io.github.nichetoolkit.file.error.ImageWriteException;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rest.util.IoStreamUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <code>ImageUtils</code>
 * <p>The image utils class.</p>
 * @see  lombok.extern.slf4j.Slf4j
 * @author Cyan (snow22314@outlook.com)
 * @since Jdk1.8
 */
@Slf4j
public class ImageUtils {

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param outputStream {@link java.io.OutputStream} <p>The output stream parameter is <code>OutputStream</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.io.OutputStream
     */
    public static void write(BufferedImage bufferedImage, OutputStream outputStream) {
        try {
            ImageHelper.write(bufferedImage, outputStream);
        } catch (ImageWriteException exception) {
            log.error("It is has an error during bufferedImage to write as outputStream.", exception);
            GeneralUtils.printStackTrace(exception);
        }
    }

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param imagePath {@link java.lang.String} <p>The image path parameter is <code>String</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.lang.String
     */
    public static void write(BufferedImage bufferedImage, String imagePath) {
        try {
            ImageHelper.write(bufferedImage, imagePath);
        } catch (ImageWriteException exception) {
            log.error("It is has an error during bufferedImage to write as file!", exception);
            GeneralUtils.printStackTrace(exception);
        }
    }

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param file {@link java.io.File} <p>The file parameter is <code>File</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.io.File
     */
    public static void write(BufferedImage bufferedImage, File file) {
        try {
            ImageHelper.write(bufferedImage,  FileConstants.DEFAULT_IMAGE_SUFFIX, file);
        } catch (ImageWriteException exception) {
            log.error("It is has an error during bufferedImage to write as file!", exception);
            GeneralUtils.printStackTrace(exception);
        }
    }


    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param formatName {@link java.lang.String} <p>The format name parameter is <code>String</code> type.</p>
     * @param outputStream {@link java.io.OutputStream} <p>The output stream parameter is <code>OutputStream</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.lang.String
     * @see  java.io.OutputStream
     */
    public static void write(BufferedImage bufferedImage, String formatName, OutputStream outputStream) {
        try {
            ImageHelper.write(bufferedImage, formatName, outputStream);
        } catch (ImageWriteException exception) {
            log.error("It is has an error during bufferedImage to write as outputStream!", exception);
            GeneralUtils.printStackTrace(exception);
        }
    }

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param formatName {@link java.lang.String} <p>The format name parameter is <code>String</code> type.</p>
     * @param imagePath {@link java.lang.String} <p>The image path parameter is <code>String</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.lang.String
     */
    public static void write(BufferedImage bufferedImage, String formatName, String imagePath) {
        try {
            ImageHelper.write(bufferedImage, formatName, new File(imagePath));
        } catch (ImageWriteException exception) {
            log.error("It is has an error during bufferedImage to write as file!", exception);
            GeneralUtils.printStackTrace(exception);
        }
    }

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param formatName {@link java.lang.String} <p>The format name parameter is <code>String</code> type.</p>
     * @param file {@link java.io.File} <p>The file parameter is <code>File</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.lang.String
     * @see  java.io.File
     */
    public static void write(BufferedImage bufferedImage, String formatName, File file) {
        try {
            ImageHelper.write(bufferedImage, formatName, file);
        } catch (ImageWriteException exception) {
            log.error("It is has an error during bufferedImage to read as file!", exception);
            GeneralUtils.printStackTrace(exception);
        }
    }

    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param inputStream {@link java.io.InputStream} <p>The input stream parameter is <code>InputStream</code> type.</p>
     * @see  java.io.InputStream
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The read return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage read(InputStream inputStream) {
        try {
            return ImageHelper.read(inputStream);
        } catch (ImageReadException exception) {
            log.error("It is has an error during inputStream to read as BufferedImage!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param imagePath {@link java.lang.String} <p>The image path parameter is <code>String</code> type.</p>
     * @see  java.lang.String
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The read return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage read(String imagePath) {
        try {
            return ImageHelper.read(imagePath);
        } catch (ImageReadException exception) {
            log.error("It is has an error during file to read as BufferedImage!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }


    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param file {@link java.io.File} <p>The file parameter is <code>File</code> type.</p>
     * @see  java.io.File
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The read return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage read(File file) {
        try {
            return ImageHelper.read(file);
        } catch (ImageReadException exception) {
            log.error("It is has an error during file to write as BufferedImage!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.io.InputStream
     * @return  {@link java.io.InputStream} <p>The read return object is <code>InputStream</code> type.</p>
     */
    public static InputStream read(BufferedImage bufferedImage) {
        try {
            return ImageHelper.read(bufferedImage);
        } catch (ImageTransferException exception) {
            log.error("It is has an error during bufferedImage to transfer as inputStream!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>bytes</code>
     * <p>The bytes method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @return byte <p>The bytes return object is <code>byte</code> type.</p>
     */
    public static byte[] bytes(BufferedImage bufferedImage) {
        try {
            return ImageHelper.bytes(bufferedImage);
        } catch (ImageTransferException exception) {
            log.error("It is has an error during bufferedImage to transfer as inputStream!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>bytes</code>
     * <p>The bytes method.</p>
     * @param file {@link java.io.File} <p>The file parameter is <code>File</code> type.</p>
     * @see  java.io.File
     * @return byte <p>The bytes return object is <code>byte</code> type.</p>
     */
    public static byte[] bytes(File file) {
        try {
            return IoStreamUtils.bytes(Files.newInputStream(file.toPath()));
        } catch (IOException exception) {
            log.error("It is has an error during file to transfer as byte!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>scale</code>
     * <p>The scale method.</p>
     * @param scale {@link java.lang.Double} <p>The scale parameter is <code>Double</code> type.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.lang.Double
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scale(Double scale, BufferedImage bufferedImage) {
        try {
            return Thumbnails.of(bufferedImage).scale(scale)
                    .outputFormat(FileConstants.IMAGE_PNG_SUFFIX)
                    .outputQuality(1d).asBufferedImage();
        } catch (IOException exception) {
            log.error("It is has an error during bufferedImage to scale as bufferedImage!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>scale</code>
     * <p>The scale method.</p>
     * @param scale {@link java.lang.Double} <p>The scale parameter is <code>Double</code> type.</p>
     * @param inputStream {@link java.io.InputStream} <p>The input stream parameter is <code>InputStream</code> type.</p>
     * @see  java.lang.Double
     * @see  java.io.InputStream
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scale(Double scale, InputStream inputStream) {
        try {
            return Thumbnails.of(inputStream).scale(scale)
                    .outputFormat(FileConstants.IMAGE_PNG_SUFFIX)
                    .outputQuality(1d).asBufferedImage();
        } catch (IOException exception) {
            log.error("It is has an error during inputStream to scale as bufferedImage!", exception);
            GeneralUtils.printStackTrace(exception);
            return null;
        }
    }

    /**
     * <code>scale</code>
     * <p>The scale method.</p>
     * @param width {@link java.lang.Integer} <p>The width parameter is <code>Integer</code> type.</p>
     * @param height {@link java.lang.Integer} <p>The height parameter is <code>Integer</code> type.</p>
     * @param inputStream {@link java.io.InputStream} <p>The input stream parameter is <code>InputStream</code> type.</p>
     * @see  java.lang.Integer
     * @see  java.io.InputStream
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scale(Integer width, Integer height, InputStream inputStream) {
        BufferedImage bufferedImage = read(inputStream);
        return scale(width, height, bufferedImage);
    }

    /**
     * <code>scale</code>
     * <p>The scale method.</p>
     * @param width {@link java.lang.Integer} <p>The width parameter is <code>Integer</code> type.</p>
     * @param height {@link java.lang.Integer} <p>The height parameter is <code>Integer</code> type.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.lang.Integer
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scale(Integer width, Integer height, BufferedImage bufferedImage) {
        double scale = 1.0d;
        if (GeneralUtils.isNotEmpty(bufferedImage)) {
            int imageWidth = bufferedImage.getWidth();
            int imageHeight = bufferedImage.getHeight();
            if (GeneralUtils.isNotEmpty(width)) {
                scale = ((double) width / (double) imageWidth >= 1.0D) ? scale : ((double) width / (double) imageWidth);
                log.debug("The image keep width is {} to scale: {}", width, scale);
            } else if (GeneralUtils.isNotEmpty(height)) {
                scale = ((double) height / (double) imageHeight >= 1.0D) ? scale : ((double) height / (double) imageHeight);
                log.debug("The image keep height is {} to scale: {}", height, scale);
            }
            return scale(scale, bufferedImage);
        }
        return null;
    }

    /**
     * <code>scaleWidth</code>
     * <p>The scale width method.</p>
     * @param width {@link java.lang.Integer} <p>The width parameter is <code>Integer</code> type.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.lang.Integer
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale width return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scaleWidth(Integer width, BufferedImage bufferedImage) {
        return scale(width, null, bufferedImage);
    }

    /**
     * <code>scaleHeight</code>
     * <p>The scale height method.</p>
     * @param height {@link java.lang.Integer} <p>The height parameter is <code>Integer</code> type.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.lang.Integer
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale height return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scaleHeight(Integer height, BufferedImage bufferedImage) {
        return scale(null, height, bufferedImage);
    }

    /**
     * <code>scaleWidth</code>
     * <p>The scale width method.</p>
     * @param width {@link java.lang.Integer} <p>The width parameter is <code>Integer</code> type.</p>
     * @param inputStream {@link java.io.InputStream} <p>The input stream parameter is <code>InputStream</code> type.</p>
     * @see  java.lang.Integer
     * @see  java.io.InputStream
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale width return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scaleWidth(Integer width, InputStream inputStream) {
        return scale(width, null, inputStream);
    }

    /**
     * <code>scaleHeight</code>
     * <p>The scale height method.</p>
     * @param height {@link java.lang.Integer} <p>The height parameter is <code>Integer</code> type.</p>
     * @param inputStream {@link java.io.InputStream} <p>The input stream parameter is <code>InputStream</code> type.</p>
     * @see  java.lang.Integer
     * @see  java.io.InputStream
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The scale height return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage scaleHeight(Integer height, InputStream inputStream) {
        return scale(null, height, inputStream);
    }

    /**
     * <code>rgbR</code>
     * <p>The rgb r method.</p>
     * @param rgb int <p>The rgb parameter is <code>int</code> type.</p>
     * @return int <p>The rgb r return object is <code>int</code> type.</p>
     */
    public static int rgbR(int rgb) {
        return (rgb & 0xff0000) >> 16;
    }

    /**
     * <code>rgbG</code>
     * <p>The rgb g method.</p>
     * @param rgb int <p>The rgb parameter is <code>int</code> type.</p>
     * @return int <p>The rgb g return object is <code>int</code> type.</p>
     */
    public static int rgbG(int rgb) {
        return (rgb & 0xff0000) >> 16;
    }

    /**
     * <code>rgbB</code>
     * <p>The rgb b method.</p>
     * @param rgb int <p>The rgb parameter is <code>int</code> type.</p>
     * @return int <p>The rgb b return object is <code>int</code> type.</p>
     */
    public static int rgbB(int rgb) {
        return (rgb & 0xff0000) >> 16;
    }

    /**
     * <code>typeImage</code>
     * <p>The type image method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param type int <p>The type parameter is <code>int</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The type image return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage typeImage(BufferedImage bufferedImage, int type) {
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        BufferedImage typeImage = new BufferedImage(width, height, type);
        Graphics2D graphics2D = (Graphics2D) typeImage.getGraphics();
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
        graphics2D.dispose();
        return typeImage;
    }

    /**
     * <code>rgbaImage</code>
     * <p>The rgba image method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The rgba image return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage rgbaImage(BufferedImage bufferedImage) {
        return typeImage(bufferedImage, BufferedImage.TYPE_4BYTE_ABGR);
    }

    /**
     * <code>binaryImage</code>
     * <p>The binary image method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The binary image return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage binaryImage(BufferedImage bufferedImage) {
        return typeImage(bufferedImage, BufferedImage.TYPE_BYTE_BINARY);
    }

    /**
     * <code>autograph</code>
     * <p>The autograph method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param backgroundRgb int <p>The background rgb parameter is <code>int</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The autograph return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage autograph(BufferedImage bufferedImage, int backgroundRgb) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage rgbaImage = rgbaImage(bufferedImage);
        int alpha;
        List<Integer> xCoordinates = new ArrayList<>();
        List<Integer> yCoordinates = new ArrayList<>();
        for (int x = rgbaImage.getMinX(); x < width; x++) {
            for (int y = rgbaImage.getMinY(); y < height; y++) {
                int contentRgb = rgbaImage.getRGB(x, y);
                if (contentRgb != backgroundRgb) {
                    xCoordinates.add(x);
                    yCoordinates.add(y);
                }
                int contentR = rgbR(contentRgb);
                int contentG = rgbG(contentRgb);
                int contentB = rgbB(contentRgb);
                int backgroundR = rgbR(backgroundRgb);
                int backgroundG = rgbG(backgroundRgb);
                int backgroundB = rgbB(backgroundRgb);
                /* a为色差范围值，渐变色边缘处理，数值需要具体测试，50左右的效果比较可以 */
                int a = 45;
                if (Math.abs(backgroundR - contentR) < a && Math.abs(backgroundG - contentG) < a && Math.abs(backgroundB - contentB) < a) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                contentRgb = (alpha << 24) | (contentRgb & 0x00ffffff);
                rgbaImage.setRGB(x, y, contentRgb);
            }
        }
        List<Integer> xCoordinateList = xCoordinates.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Integer> yCoordinateList = yCoordinates.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        xCoordinateList.sort(Comparator.comparingInt(value -> value));
        yCoordinateList.sort(Comparator.comparingInt(value -> value));
        int minX = xCoordinateList.get(0);
        int maxX = xCoordinateList.get(xCoordinateList.size() - 1);
        int minY = yCoordinateList.get(0);
        int maxY = yCoordinateList.get(yCoordinateList.size() - 1);
        int subWidth = maxX - minX;
        int subHeight = maxY - minY;
        return rgbaImage.getSubimage(minX, minY, subWidth, subHeight);
    }

    /**
     * <code>autograph</code>
     * <p>The autograph method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @return  {@link java.awt.image.BufferedImage} <p>The autograph return object is <code>BufferedImage</code> type.</p>
     */
    public static BufferedImage autograph(BufferedImage bufferedImage) {
        return autograph(bufferedImage, -1);
    }
}
