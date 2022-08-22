package io.github.nichetoolkit.file.helper;

import io.github.nichetoolkit.file.error.ImageReadException;
import io.github.nichetoolkit.file.error.ImageWriteException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>ImageHelper</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class ImageHelper {

    public static void write(BufferedImage bufferedImage, OutputStream outputStream) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    public static void write(BufferedImage bufferedImage, String imagePath) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, "png", new File(imagePath));
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    public static void write(BufferedImage bufferedImage, File file) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }


    public static void write(BufferedImage bufferedImage, String formatName, OutputStream outputStream) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, formatName, outputStream);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    public static void write(BufferedImage bufferedImage, String formatName, String imagePath) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, formatName, new File(imagePath));
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    public static void write(BufferedImage bufferedImage, String formatName, File file) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, formatName, file);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    public static BufferedImage read(InputStream inputStream) throws ImageReadException {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException exception) {
            throw new ImageReadException(exception.getMessage());
        }
    }

    public static BufferedImage read(String imagePath) throws ImageReadException {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException exception) {
            throw new ImageReadException(exception.getMessage());
        }
    }


    public static BufferedImage read(File file) throws ImageReadException {
        try {
            return ImageIO.read(file);
        } catch (IOException exception) {
            throw new ImageReadException(exception.getMessage());
        }
    }


}
