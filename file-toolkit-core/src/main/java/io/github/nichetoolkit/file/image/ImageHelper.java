package io.github.nichetoolkit.file.image;

import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.error.ImageReadException;
import io.github.nichetoolkit.file.error.ImageTransferException;
import io.github.nichetoolkit.file.error.ImageWriteException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * <code>ImageHelper</code>
 * <p>The image helper class.</p>
 * @author Cyan (snow22314@outlook.com)
 * @since Jdk1.8
 */
public class ImageHelper {

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param outputStream {@link java.io.OutputStream} <p>The output stream parameter is <code>OutputStream</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.io.OutputStream
     * @see  io.github.nichetoolkit.file.error.ImageWriteException
     * @throws ImageWriteException {@link io.github.nichetoolkit.file.error.ImageWriteException} <p>The image write exception is <code>ImageWriteException</code> type.</p>
     */
    public static void write(BufferedImage bufferedImage, OutputStream outputStream) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, FileConstants.DEFAULT_IMAGE_SUFFIX, outputStream);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param imagePath {@link java.lang.String} <p>The image path parameter is <code>String</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.lang.String
     * @see  io.github.nichetoolkit.file.error.ImageWriteException
     * @throws ImageWriteException {@link io.github.nichetoolkit.file.error.ImageWriteException} <p>The image write exception is <code>ImageWriteException</code> type.</p>
     */
    public static void write(BufferedImage bufferedImage, String imagePath) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, FileConstants.DEFAULT_IMAGE_SUFFIX, new File(imagePath));
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    /**
     * <code>write</code>
     * <p>The write method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @param file {@link java.io.File} <p>The file parameter is <code>File</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.io.File
     * @see  io.github.nichetoolkit.file.error.ImageWriteException
     * @throws ImageWriteException {@link io.github.nichetoolkit.file.error.ImageWriteException} <p>The image write exception is <code>ImageWriteException</code> type.</p>
     */
    public static void write(BufferedImage bufferedImage, File file) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, FileConstants.DEFAULT_IMAGE_SUFFIX, file);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
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
     * @see  io.github.nichetoolkit.file.error.ImageWriteException
     * @throws ImageWriteException {@link io.github.nichetoolkit.file.error.ImageWriteException} <p>The image write exception is <code>ImageWriteException</code> type.</p>
     */
    public static void write(BufferedImage bufferedImage, String formatName, OutputStream outputStream) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, formatName, outputStream);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
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
     * @see  io.github.nichetoolkit.file.error.ImageWriteException
     * @throws ImageWriteException {@link io.github.nichetoolkit.file.error.ImageWriteException} <p>The image write exception is <code>ImageWriteException</code> type.</p>
     */
    public static void write(BufferedImage bufferedImage, String formatName, String imagePath) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, formatName, new File(imagePath));
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
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
     * @see  io.github.nichetoolkit.file.error.ImageWriteException
     * @throws ImageWriteException {@link io.github.nichetoolkit.file.error.ImageWriteException} <p>The image write exception is <code>ImageWriteException</code> type.</p>
     */
    public static void write(BufferedImage bufferedImage, String formatName, File file) throws ImageWriteException {
        try {
            ImageIO.write(bufferedImage, formatName, file);
        } catch (IOException exception) {
            throw new ImageWriteException(exception.getMessage());
        }
    }

    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param inputStream {@link java.io.InputStream} <p>The input stream parameter is <code>InputStream</code> type.</p>
     * @see  java.io.InputStream
     * @see  java.awt.image.BufferedImage
     * @see  io.github.nichetoolkit.file.error.ImageReadException
     * @return  {@link java.awt.image.BufferedImage} <p>The read return object is <code>BufferedImage</code> type.</p>
     * @throws ImageReadException {@link io.github.nichetoolkit.file.error.ImageReadException} <p>The image read exception is <code>ImageReadException</code> type.</p>
     */
    public static BufferedImage read(InputStream inputStream) throws ImageReadException {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException exception) {
            throw new ImageReadException(exception.getMessage());
        }
    }

    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param imagePath {@link java.lang.String} <p>The image path parameter is <code>String</code> type.</p>
     * @see  java.lang.String
     * @see  java.awt.image.BufferedImage
     * @see  io.github.nichetoolkit.file.error.ImageReadException
     * @return  {@link java.awt.image.BufferedImage} <p>The read return object is <code>BufferedImage</code> type.</p>
     * @throws ImageReadException {@link io.github.nichetoolkit.file.error.ImageReadException} <p>The image read exception is <code>ImageReadException</code> type.</p>
     */
    public static BufferedImage read(String imagePath) throws ImageReadException {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException exception) {
            throw new ImageReadException(exception.getMessage());
        }
    }


    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param file {@link java.io.File} <p>The file parameter is <code>File</code> type.</p>
     * @see  java.io.File
     * @see  java.awt.image.BufferedImage
     * @see  io.github.nichetoolkit.file.error.ImageReadException
     * @return  {@link java.awt.image.BufferedImage} <p>The read return object is <code>BufferedImage</code> type.</p>
     * @throws ImageReadException {@link io.github.nichetoolkit.file.error.ImageReadException} <p>The image read exception is <code>ImageReadException</code> type.</p>
     */
    public static BufferedImage read(File file) throws ImageReadException {
        try {
            return ImageIO.read(file);
        } catch (IOException exception) {
            throw new ImageReadException(exception.getMessage());
        }
    }


    /**
     * <code>read</code>
     * <p>The read method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  java.io.InputStream
     * @see  io.github.nichetoolkit.file.error.ImageTransferException
     * @return  {@link java.io.InputStream} <p>The read return object is <code>InputStream</code> type.</p>
     * @throws ImageTransferException {@link io.github.nichetoolkit.file.error.ImageTransferException} <p>The image transfer exception is <code>ImageTransferException</code> type.</p>
     */
    public static InputStream read(BufferedImage bufferedImage) throws ImageTransferException {
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream;
        try {
            imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(bufferedImage, FileConstants.DEFAULT_IMAGE_SUFFIX, imageOutputStream);
            inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException exception) {
            throw new ImageTransferException(exception.getMessage());
        }
        return inputStream;
    }

    /**
     * <code>bytes</code>
     * <p>The bytes method.</p>
     * @param bufferedImage {@link java.awt.image.BufferedImage} <p>The buffered image parameter is <code>BufferedImage</code> type.</p>
     * @see  java.awt.image.BufferedImage
     * @see  io.github.nichetoolkit.file.error.ImageTransferException
     * @return byte <p>The bytes return object is <code>byte</code> type.</p>
     * @throws ImageTransferException {@link io.github.nichetoolkit.file.error.ImageTransferException} <p>The image transfer exception is <code>ImageTransferException</code> type.</p>
     */
    public static byte[] bytes(BufferedImage bufferedImage) throws ImageTransferException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream;
        try {
            imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(bufferedImage, FileConstants.DEFAULT_IMAGE_SUFFIX, imageOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            throw new ImageTransferException(exception.getMessage());
        }
    }

}
