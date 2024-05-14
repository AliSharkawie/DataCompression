import java.io.*;
import java.util.HashMap;

public class LZWCompression {
    public static void main(String[] args) {
        String inputFile = "input.txt";
        String compressedFile = "compressed.txt";

        // Compression
        compress(inputFile, compressedFile);
    }

    public static void compress(String inputFileName, String outputFileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            HashMap<String, Integer> dictionary = new HashMap<>();
            int nextCode = 128;

            for (int i = 0; i < 128; i++) {
                dictionary.put("" + (char) i, i);
            }

            String current = "";
            int code;

            while (true) {
                int character = reader.read();
                if (character == -1) {
                    if (current.length() > 0) {
                        code = dictionary.get(current);
                        writer.write(code + " ");
                    }
                    break;
                }

                char ch = (char) character;
                String temp = current + ch;
                if (dictionary.containsKey(temp)) {
                    current = temp;
                } else {
                    code = dictionary.get(current);
                    writer.write(code + " ");
                    if (nextCode < 4096) {
                        dictionary.put(temp, nextCode++);
                    }
                    current = "" + ch;
                }
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
