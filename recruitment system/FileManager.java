
package rms;


import java.io.*;
import java.util.ArrayList;

public class FileManager {

    // Save an ArrayList to a binary file
    public static <T> void saveToFile(ArrayList<T> list, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Load an ArrayList from a binary file
    public static <T> ArrayList<T> loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Append an object to a binary file
    public static <T> void appendToFile(T object, String filePath) {
        try (AppendingObjectOutputStream oos = new AppendingObjectOutputStream(new FileOutputStream(filePath, true))) {
            oos.writeObject(object);
        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
        }
    }

    // Custom ObjectOutputStream to avoid overwriting file header
    static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
