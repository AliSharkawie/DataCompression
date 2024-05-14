package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        JFrame frame_ = new JFrame("Image Compress App");
        JLabel label_1, label_2;

        label_1 = new JLabel("input Image Path");
        label_1.setBounds(50, 20, 300, 25);
        final JTextField input1 = new JTextField();
        input1.setBounds(190, 25, 150, 25);

        label_2 = new JLabel("Enter compressed Name");
        label_2.setBounds(50, 70, 300, 25);
        final JTextField input2 = new JTextField();
        input2.setBounds(190, 70, 150, 25);

        JButton button1 = new JButton("Compression");
        button1.setBounds(190, 250, 150, 30);
        JButton button2 = new JButton("deCompression");
        button2.setBounds(190, 300, 150, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int[][] compressedImage = PredictiveCoding2D.compressImage(input1.getText());
                    // Save the compressed data to a file
                    saveCompressedData(compressedImage, input2.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Load the compressed data from the file
                    int[][] compressedData = loadCompressedData(input2.getText());

                    // Decompression: Pass the compressed data to your decompression function
                    BufferedImage decompressedImage = PredictiveCoding2D.decompressImage(compressedData);

                    // Save the decompressed image to a file
                    saveImageToFile(decompressedImage, "decompressed_" + input1.getText());
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame_.add(label_1);
        frame_.add(input1);
        frame_.add(label_2);
        frame_.add(input2);
        frame_.add(button1);
        frame_.add(button2);

        frame_.setSize(400, 400);
        frame_.setLayout(null);
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_.setVisible(true);
    }

    private static void saveCompressedData(int[][] compressedData, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName + ".dat"))) {
            oos.writeObject(compressedData);
            System.out.println("Compressed data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[][] loadCompressedData(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName + ".dat"))) {
            return (int[][]) ois.readObject();
        }
    }

    private static void saveImageToFile(BufferedImage image, String fileName) {
        try {
            File outputFile = new File(fileName + ".png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Decompressed image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
