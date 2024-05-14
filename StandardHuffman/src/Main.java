import jdk.jfr.Unsigned;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {

    private static Map<Character, Integer> buildFrequencyMap(String stream) {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char ch : stream.toCharArray()) { // convert string to array of char then iterate on it 
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1); // Returns the value to which the specified key is 
                                                                // mapped, or defaultValue if this map contains no mapping for the key.
        }

        return frequencyMap;
    }
    // now we have map contain each char and it's frequency 
    //Build Tree with code for every symbol
    private static Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) { // for each entry in map
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }
        // now we put each item with it's frequency in priority queue 
        // build huffman tree
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll(); // poll get node and rmove it (peak get item but not remove it)
            Node right = priorityQueue.poll(); // poll here will get the smallest frequency element 

            Node parent = new Node('\0', left.frequency + right.frequency); // nys node
            parent.left = left;
            parent.right = right;

            priorityQueue.offer(parent);
        }

        return priorityQueue.poll(); // root node ( the only remaining node) (summision for all tree )
        // returning node (root) that have lefft and right and frequency (summision frequency)  
        // and compare functoin  
    }

    private static Map<Character, String> generateHuffmanCodes(Node root, String currentCode) {
        Map<Character, String> huffmanCodes = new HashMap<>();// string is the code '010....'
        generateCodes(root, currentCode, huffmanCodes);
        return huffmanCodes;
    }

    // recursion function 
    private static void generateCodes(Node node, String currentCode, Map<Character, String> huffmanCodes) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                huffmanCodes.put(node.data, currentCode);
            }

            generateCodes(node.left, currentCode + "0", huffmanCodes);
            generateCodes(node.right, currentCode + "1", huffmanCodes);
        }
    }

    public static void Compression(String inputFileName, String outputFileName) {
        Scanner input = null;
        try {
            input = new Scanner(new FileInputStream(inputFileName));
        } catch (FileNotFoundException e) {
            // File not found, display an error message
            JOptionPane.showMessageDialog(null, "File not found: " + inputFileName + "\nPlease Enter a valid file name and try again!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        // Check if the file is empty
        if (!input.hasNextLine()) {
            JOptionPane.showMessageDialog(null, "File is empty: " + inputFileName + " Try again!", "Error", JOptionPane.ERROR_MESSAGE);
            input.close();
            return;
            // JOptionPane.showMessageDialog(...): Displays a graphical message dialog (a pop-up window) using Java's Swing library.
            // In this case, it shows an error message indicating that the file is empty . The parameters for the method include the
            // message, window title ("Error"), message type (error), and the parent component (in this case, null means it's a standalone message dialog).
            // if file not exist it will be display error also but in not our case
        }

        // from here we ensure that teh file is not empty and have next line
        String s = input.nextLine(); // our txt to decode (string)
        int n = s.length(); // number of characters
        Map<Character, Integer> frequencyMap = buildFrequencyMap(s); // send the string to our function to build map (char,freq) 


        int sizeAfter = 0;
        try (FileOutputStream outputStream = new FileOutputStream(outputFileName)) {
            String encodedString = "";
            // byte int : integer stored in 1 byte as a size
            byte symbolsNumber = 0;    // number all char 
            byte uniqueSymbols = 0;     // number each unique symbol we have
            HashMap<Integer, Byte> symbolWithCode = new HashMap<>();  // byte represent the code
            // associates an integer (representing a symbol) with a byte (representing the Huffman code for that symbol). This map will
            // be used to store the mapping between symbols and their Huffman codes.
            HashMap<Integer, Integer> codeLength = new HashMap<>();  // second integer represent the length of code
            // associates an integer (representing a symbol) with an integer (representing the length of its Huffman code)


            // building the Huffman tree
            Node root = buildHuffmanTree(frequencyMap);

            Map<Character, String> huffmanCodes = generateHuffmanCodes(root, ""); // map(char,it's_code)
            // first byte in the file
            uniqueSymbols = (byte) huffmanCodes.size(); // numof leaves
            // number of unique codes(chars) in bytes // leaves size (all codes) 

            for(Map.Entry<Character, String> charCodeAndCode : huffmanCodes.entrySet()) { // each char and it's code

                // frequency each symbol
                symbolsNumber += frequencyMap.get(charCodeAndCode.getKey()); // key is the char // get it's frequency
                // get frequency for each char in huffman tree               // to get num all chars

                int symbolByte = (charCodeAndCode.getKey() & 0xFF); // represent char(key , our symbol) as byte int 
                // & 0xFF ensure that it is in byte
                byte CodeByte = stringToByte(charCodeAndCode.getValue()); // get code(string) then convert to byte
                // converting the Huffman code (a string of '0's and '1's) to a byte using the stringToByte method.
                symbolWithCode.put(symbolByte, CodeByte);  
                // map(int,byte) represent char,code  -> final result

                codeLength.put(symbolByte, charCodeAndCode.getValue().length()); 
                // store code lengthes associated with each symbol(byte)
                // for symbol(stored as byte) store the length of it's code(stored as byte)

                sizeAfter += charCodeAndCode.getValue().length() * frequencyMap.get(charCodeAndCode.getKey());
                // length of code for the key(char) * frequencies for this key (length for all codes for all chars )
            }

            // encode the stream from the same input after building the codes
            for (int i = 0; i < n; i++) { // n string size
                char key = s.charAt(i);
                encodedString += huffmanCodes.get(key); // push the code for each char in input string
            }

            // reverse the order of code to be from root to leave ( correct order)
            encodedString = new StringBuilder(encodedString).reverse().toString();

            // convert to binary ( string to bytes )
            Vector<Byte> byteArray = convertToByte(encodedString);
            // go through each char and convert it to byte

            // write to file
            outputStream.write(uniqueSymbols);

            outputStream.write(symbolsNumber);
            // symbol code length
            for (Map.Entry<Integer, Byte> x : symbolWithCode.entrySet()) {
                outputStream.write(x.getKey()); // symbol
                outputStream.write(x.getValue()); // code
                outputStream.write(codeLength.get(x.getKey())); // codeLength
            }
            
            for (Byte aByte : byteArray) {
                outputStream.write(aByte);
            }

            JOptionPane.showMessageDialog(null, "Size After Compression: " + sizeAfter + " bits", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //input.close();
    }

    private static byte stringToByte(String s) {
        byte res = 0;
        // begin from right to begin from lowst order 
        // dosm't affect bec we make power as order multiplication
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '1') {
                res += (byte) Math.pow(2, s.length() - 1 - i);// string shlud be char (maximum 8 bits)
            }
        }
        return res;
    }

    // converting for array ( all encoded stream )
    public static Vector<Byte> convertToByte(String input) {
        Vector<Byte> res = new Vector<>();
        int i = input.length() - 1;   // start from right to left
        while (i >= 0) { // for each char in encoded string 
            String temp = "";
            // put the current char in 8 digits
            for (int j = 0; j < 8; j++) { // for each current char 
                if (i < 0) 
                //  If the index i is less than 0 (meaning we've processed all character bits), paddng the remaining bits with zeros 
                // to complete gorup of 8 
                    temp = '0' + temp;
                else
                    temp = input.charAt(i--) + temp;
            }
            res.add(stringToByte(temp)); // convert the code for the current char and convert it to byte nad add it to res
        }
        return res;
    }

    public static String byteToString(byte value) {
        // return string like stored in bin file
        int mask = 128; // 8 bits 1000 0000 
        String res = "";
        // to take each char
        // byte is a number represented in 8 bits so shifteing will manipulate each bit
        while (mask > 0) {
            if ((value & mask) == 0)
                res += "0";
            else
                res += "1";
            mask >>= 1;
        }
        return res;
    }
    // input.close() ;
    public static void Decompression(String inputFileName, String outputFileName) throws IOException {
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(inputFileName));
        } catch (FileNotFoundException e) {
            // File not found, display an error message
            JOptionPane.showMessageDialog(null, "File not found: " + inputFileName + "\nPlease Enter a valid file name and try again!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        HashMap<String, Character> decode = new HashMap<>(); // codes(strings) -> original char
        byte[] buff = new byte[1]; // want to read one byte from buffer (the number of unique symbols)
        byte numberOfUniqueSymbols = 0, numOfSymbols = 0;

        int read = input.read(buff); // read first byte
        if (read != -1) // more save to read non-empty file
            numberOfUniqueSymbols = buff[0]; // first byte

        read = input.read(buff); // read second byte
        if (read != -1)
            numOfSymbols = buff[0];// number all symbols

        for (int i = 0; i < numberOfUniqueSymbols; i++) {
            // read code
            read = input.read(buff); // read byte
            Character code = (char) buff[0]; 
            // the code is the char representation of this byte

            //  read key
            read = input.read(buff);
            String key = byteToString(buff[0]);// the(symbol) char as binary representatoin in string

            // read size
            read = input.read(buff);
            key = key.substring(key.length() - buff[0]);
            // ensures that key retains only the actual bits representing the Huffman code.
            decode.put(key, code); // code(as string):original_letter as binary string
        }
        String encoded = "";

        // read stream bytes
        read = input.read(buff);
        while (read != -1) {
            encoded = byteToString(buff[0]) + encoded;
            read = input.read(buff);
        }
        encoded = new StringBuilder(encoded).reverse().toString(); // after toString the encoded now become codes as strings not bytes
        String temp = ""; // to complete the entire huffman code
        String decoded = "";
        int temp_counter = 0, loop_counter = 0;
        while (loop_counter < encoded.length() && temp_counter < numOfSymbols) {
            temp += encoded.charAt(loop_counter); // if the current code isn't complete we will continue to adding bits
            if (decode.containsKey(temp)) {
                decoded += decode.get(temp);
                temp = "";
                // decode the entire temp if it included in decuded stream (all codes) then clear it again
                temp_counter++;
            }
            loop_counter++;
        }
        try {
            PrintWriter printWriter = new PrintWriter(outputFileName);
            printWriter.println(decoded);
            printWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "File Decompressed", "Information", JOptionPane.INFORMATION_MESSAGE);
        input.close();
    }

    
    // main with gui
    public static void main(String[] args) {


        JFrame frame_ = new JFrame("Huffman App");
        JLabel label_1, label_2;
        label_1 = new JLabel("Input File Name");
        label_1.setBounds(50, 20, 350, 25);
        final JTextField input1 = new JTextField();
        input1.setBounds(190, 25, 150, 25);

        label_2 = new JLabel("Output File Name");
        label_2.setBounds(50, 90, 350, 25);
        final JTextField input2 = new JTextField();
        input2.setBounds(190, 70, 150, 25);

        JButton button1 = new JButton("Compress as Standard Huffman");
        button1.setBounds(100, 150, 250, 30);
        JButton button2 = new JButton("Decompress from Standard Huffman compressoin");
        button2.setBounds(100, 200, 350, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Compression(input1.getText(), input2.getText());
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
                    Decompression(input1.getText(), input2.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
        frame_.add(button1);
        frame_.add(button2);
        frame_.add(input1);
        frame_.add(input2);
        frame_.setSize(800, 700);
        frame_.setLayout(null);
        frame_.setVisible(true);
        frame_.getContentPane().setBackground(Color.green);

    }
}

