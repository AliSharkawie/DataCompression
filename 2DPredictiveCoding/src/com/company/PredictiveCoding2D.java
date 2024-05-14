package com.company;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class PredictiveCoding2D {
    public static int[][] compressImage(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] compressedImage = new int[width][height];

        // Iterate over each pixel
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Get the RGB values of the current pixel
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Apply linear predictive coding (LPC) to calculate the predicted pixel value
                int predictedPixel = predictPixel(compressedImage, x, y);

                // Calculate the difference (prediction error) between the actual and predicted pixel values
                int errorRed = red - predictedPixel;
                int errorGreen = green - predictedPixel;
                int errorBlue = blue - predictedPixel;

                // Store the prediction error as the compressed pixel value
                compressedImage[x][y] = (errorRed << 16) | (errorGreen << 8) | errorBlue;
            }
        }

        return compressedImage;
    }

    
    public static BufferedImage decompressImage(int[][] compressedImage) {
        int width = compressedImage.length;
        int height = compressedImage[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Iterate over each compressed pixel
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Get the prediction error values from the compressed pixel
                int compressedPixel = compressedImage[x][y];
                int errorRed = (compressedPixel >> 16) & 0xFF;
                int errorGreen = (compressedPixel >> 8) & 0xFF;
                int errorBlue = compressedPixel & 0xFF;

                // Calculate the predicted pixel value
                int predictedPixel = predictPixel(compressedImage, x, y);

                // Calculate the actual pixel value using the predicted value and the prediction error
                int red = clamp(predictedPixel + errorRed);
                int green = clamp(predictedPixel + errorGreen);
                int blue = clamp(predictedPixel + errorBlue);

                // Set the RGB values of the decompressed pixel
                int rgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }

   
    private static int predictPixel(int[][] image, int x, int y) {
        int predictedPixel = 0;
        int count = 0;

        for (int dx = -1; dx <= 0; dx++) {
            for (int dy = -1; dy <= 0; dy++) {
                if (isValidPixel(image, x + dx, y + dy)) {
                    int compressedPixel = image[x + dx][y + dy];
                    int pixelValue = (compressedPixel >> 16) & 0xFF;
                    predictedPixel += pixelValue;
                    count++;
                }
            }
        }

        if (count != 0) {
            predictedPixel /= count;
        }

        return predictedPixel;
    }

    
    private static boolean isValidPixel(int[][] image, int x, int y) {
        int width = image.length;
        int height = image[0].length;
        return x >= 0 && x < width && y >= 0 && y < height;
    }

   
    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}

