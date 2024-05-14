package com.company;

import javax.swing.*;

import org.w3c.dom.css.RGBColor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Scanner input =new Scanner(System.in);
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        JFrame frame_ = new JFrame("Image Compress App");
        JLabel label_1, label_2 ,label_3,label_4;

        label_1 = new JLabel("Enter Vector Height");
        label_1.setBounds(50, 20, 300, 25);
        final JTextField input1 = new JTextField();
        input1.setBounds(190, 25, 150, 25);

        label_2 = new JLabel("Enter Vector Width");
        label_2.setBounds(50, 70, 300, 25);
        final JTextField input2 = new JTextField();
        input2.setBounds(190, 70, 150, 25);

        label_3 = new JLabel("Enter CodeBook Size");
        label_3.setBounds(50, 120, 300, 25);
        final JTextField input3 = new JTextField();
        input3.setBounds(190, 120, 150, 25);

        label_4 = new JLabel("Enter Image name");
        label_4.setBounds(50, 180, 300, 25);
        final JTextField input4 = new JTextField();
        input4.setBounds(190, 180, 150, 25);

        JButton button1 = new JButton("Compression");
        button1.setBounds(190, 250, 150, 30);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    VectorQuantization.originalImage = VectorQuantization.readImage(input4.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                VectorQuantization.vectorHeight = Integer.parseInt(input1.getText());
                VectorQuantization.vectorWidth = Integer.parseInt(input2.getText());
                VectorQuantization.numOfCodebook = Integer.parseInt(input3.getText());
                VectorQuantization.Compression();
                VectorQuantization.writeToFile("codes.txt");
                try {
                    VectorQuantization.readFromFile("codes.txt");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                VectorQuantization.Decompression();
                VectorQuantization.writeImage(VectorQuantization.collectedImage, "out2.jpg");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);


            }
        });

        frame_.add(label_1);
        frame_.add(label_2);
        frame_.add(label_3);
        frame_.add(label_4);
        frame_.add(button1);
        frame_.add(input1);
        frame_.add(input2);
        frame_.add(input3);
        frame_.add(input4);

        frame_.setSize(500, 600);
        frame_.setLayout(null);
        frame_.setVisible(true);
        frame_.setBackground(Color.GREEN);




    }
}
