package io.github.nichetoolkit.file.util;

import io.github.nichetoolkit.file.error.ImageReadException;
import io.github.nichetoolkit.file.error.ImageWriteException;
import io.github.nichetoolkit.file.helper.ImageHelper;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>ImageUtils</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
public class ImageUtils {

    public static void write(BufferedImage bufferedImage, OutputStream outputStream) {
        try {
            ImageHelper.write(bufferedImage,outputStream);
        } catch (ImageWriteException exception) {
            log.error("An error occurred during bufferedImage to write as outputStream!", exception);
            exception.printStackTrace();
        }
    }

    public static void write(BufferedImage bufferedImage, String imagePath) {
        try {
            ImageHelper.write(bufferedImage,imagePath);
        } catch (ImageWriteException exception) {
            log.error("An error occurred during bufferedImage to write as file!", exception);
            exception.printStackTrace();
        }
    }

    public static void write(BufferedImage bufferedImage, File file) {
        try {
            ImageHelper.write(bufferedImage, "png", file);
        } catch (ImageWriteException exception) {
            log.error("An error occurred during bufferedImage to write as file!", exception);
            exception.printStackTrace();
        }
    }


    public static void write(BufferedImage bufferedImage, String formatName, OutputStream outputStream) {
        try {
            ImageHelper.write(bufferedImage, formatName, outputStream);
        } catch (ImageWriteException exception) {
            log.error("An error occurred during bufferedImage to write as outputStream!", exception);
            exception.printStackTrace();
        }
    }

    public static void write(BufferedImage bufferedImage, String formatName, String imagePath) {
        try {
            ImageHelper.write(bufferedImage, formatName, new File(imagePath));
        } catch (ImageWriteException exception) {
            log.error("An error occurred during bufferedImage to write as file!", exception);
            exception.printStackTrace();
        }
    }

    public static void write(BufferedImage bufferedImage, String formatName, File file) {
        try {
            ImageHelper.write(bufferedImage, formatName, file);
        } catch (ImageWriteException exception) {
            log.error("An error occurred during bufferedImage to read as file!", exception);
            exception.printStackTrace();
        }
    }

    public static BufferedImage read(InputStream inputStream) {
        try {
            return ImageHelper.read(inputStream);
        } catch (ImageReadException exception) {
            log.error("An error occurred during inputStream to read as BufferedImage!", exception);
            exception.printStackTrace();
            return null;
        }
    }

    public static BufferedImage read(String imagePath) {
        try {
            return ImageHelper.read(imagePath);
        } catch (ImageReadException exception) {
            log.error("An error occurred during file to read as BufferedImage!", exception);
            exception.printStackTrace();
            return null;
        }
    }


    public static BufferedImage read(File file) {
        try {
            return ImageHelper.read(file);
        } catch (ImageReadException exception) {
            log.error("An error occurred during file to write as BufferedImage!", exception);
            exception.printStackTrace();
            return null;
        }
    }

    public static int rgbR(int rgb) {
        return (rgb & 0xff0000) >>16;
    }

    public static int rgbG(int rgb) {
        return (rgb & 0xff0000) >>16;
    }

    public static int rgbB(int rgb) {
        return (rgb & 0xff0000) >>16;
    }

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

    public static BufferedImage abgrImage(BufferedImage bufferedImage) {
        return typeImage(bufferedImage,BufferedImage.TYPE_4BYTE_ABGR);
    }

    public static BufferedImage binaryImage(BufferedImage bufferedImage) {
        return typeImage(bufferedImage,BufferedImage.TYPE_BYTE_BINARY);
    }

    public static BufferedImage autograph(BufferedImage bufferedImage,int backgroundRgb) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage abgrImage = abgrImage(bufferedImage);
        int alpha;
        List<Integer> xCoordinates = new ArrayList<>();
        List<Integer> yCoordinates = new ArrayList<>();
        for(int x = abgrImage.getMinX(); x < width; x++) {
            for(int y = abgrImage.getMinY(); y < height; y++) {
                int contentRgb = abgrImage.getRGB(x, y);
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
                /** a为色差范围值，渐变色边缘处理，数值需要具体测试，50左右的效果比较可以 */
                int a = 45;
                if(Math.abs(backgroundR - contentR) < a && Math.abs(backgroundG - contentG) < a && Math.abs(backgroundB-contentB) < a ) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                contentRgb = (alpha << 24) | (contentRgb & 0x00ffffff);
                abgrImage.setRGB( x, y, contentRgb);
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
        return abgrImage.getSubimage(minX, minY, subWidth, subHeight);
    }

    public static BufferedImage autograph(BufferedImage bufferedImage) {
        return autograph(bufferedImage,-1);
    }
}
