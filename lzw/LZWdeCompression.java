import java.io.*;
import java.util.HashMap;


public class LZWdeCompression {
    public static void main(String[] args) {
        
        String compressedFile = "compressed.txt";
        String decompressedFile = "decompressed.txt";

        // Decompression
        decompress(compressedFile, decompressedFile);
    }

    public static void decompress(String inputFileName, String outputFileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            HashMap<Integer, String> dictionary = new HashMap<>();
            int nextCode = 128;

            for (int i = 0; i < 128; i++) {
                dictionary.put(i, "" + (char) i);
            }

            String current = "";
            String previous = "";
            int code;

            String[] tokens = reader.readLine().split(" ");

            for (String token : tokens) {
                code = Integer.parseInt(token);

                if (!dictionary.containsKey(code)) {
                    dictionary.put(code, previous + current.charAt(0));
                }

                writer.write(dictionary.get(code));
                if (!previous.isEmpty()) {
                    dictionary.put(nextCode++, previous + dictionary.get(code).charAt(0));
                }
                previous = dictionary.get(code);
                current = dictionary.get(code);
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
