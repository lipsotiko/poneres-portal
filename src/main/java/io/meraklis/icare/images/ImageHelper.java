package io.meraklis.icare.images;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static io.meraklis.icare.helpers.Helpers.tmpFile;

public class ImageHelper {

    public static File trimAndScale(File input) {
        if (input == null) {
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(input);
            BufferedImage scaled = scale(image, .5);

            File output = tmpFile(".png");
            ImageIO.write(scaled, "PNG", output);
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage trim(File input) {
        try {
            BufferedImage img = ImageIO.read(input);
            int startWidth = getStartWidth(img) * -1;
            int startHeight = getStartHeight(img) * -1;

            int width = getEndWidth(img) + startWidth;
            int height = getEndHeight(img) + startHeight;

            BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = newImg.createGraphics();
            g.drawImage(img, startWidth, startHeight, null);
            return newImg;
        } catch (IOException e) {
            throw new RuntimeException("Problem reading image", e);
        }
    }

    private static void applyBorder(BufferedImage image) {
        int borderColor = 0x99FF0000;
        for (int i = 0; i < image.getWidth(); i++) {
            image.setRGB(i, image.getHeight() - 1, borderColor);
        }

        for (int i = 0; i < image.getWidth(); i++) {
            image.setRGB(i, 1, borderColor);
        }

        for (int i = 0; i < image.getHeight(); i++) {
            image.setRGB(1, i, borderColor);
        }

        for (int i = 0; i < image.getHeight(); i++) {
            image.setRGB(image.getWidth() - 1, i, borderColor);
        }
    }

    private static BufferedImage scale(BufferedImage image, double percent) {
        int w = (int) (image.getWidth() * percent);
        int h = (int) (image.getHeight() * percent);
        return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, w, h, Scalr.OP_ANTIALIAS);
    }

    private static int getStartWidth(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int startWidth = image.getWidth();

        for (int h = 0; h < height; h++) {
            for (int w = width - 1; w >= 0; w--) {
                if (image.getRGB(w, h) != 0 && w < startWidth) {
                    startWidth = w;
                    break;
                }
            }
        }

        return startWidth;
    }

    private static int getStartHeight(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int startHeight = image.getHeight();

        for (int w = 0; w < width; w++) {
            for (int h = height - 1; h >= 0; h--) {
                if (image.getRGB(w, h) != 0 && h < startHeight) {
                    startHeight = h;
                    break;
                }
            }
        }

        return startHeight;
    }

    private static int getEndWidth(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int trimmedWidth = 0;

        for (int h = 0; h < height; h++) {
            for (int w = width - 1; w >= 0; w--) {
                if (image.getRGB(w, h) != 0 && w > trimmedWidth) {
                    trimmedWidth = w;
                    break;
                }
            }
        }

        return trimmedWidth;
    }

    private static int getEndHeight(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int trimmedHeight = 0;

        for (int w = 0; w < width; w++) {
            for (int h = height - 1; h >= 0; h--) {
                if (image.getRGB(w, h) != 0 && h > trimmedHeight) {
                    trimmedHeight = h;
                    break;
                }
            }
        }

        return trimmedHeight;
    }
}
