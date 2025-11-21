import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple manual JSON reader for small arrays of objects.
 * Each object expected to include keys: id, recipient, message, status
 */
public class JsonMessageReader {

    public static class SimpleMessage {
        public String id;
        public String recipient;
        public String message;
        public String status;

        public SimpleMessage(String id, String recipient, String message, String status) {
            this.id = id;
            this.recipient = recipient;
            this.message = message;
            this.status = status;
        }
    }

    public static List<SimpleMessage> readMessages(String filePath) {
        List<SimpleMessage> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String id = "", recipient = "", message = "", status = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"id\"") || line.startsWith("\"messageId\"")) {
                    id = extractValue(line);
                } else if (line.startsWith("\"recipient\"")) {
                    recipient = extractValue(line);
                } else if (line.startsWith("\"message\"")) {
                    message = extractValue(line);
                } else if (line.startsWith("\"status\"")) {
                    status = extractValue(line);
                }

                if (line.contains("}")) {
                    if (!id.isEmpty() || !message.isEmpty()) {
                        list.add(new SimpleMessage(id, recipient, message, status));
                    }
                    id = recipient = message = status = "";
                }
            }
        } catch (IOException e) {
            System.out.println("JsonMessageReader: error reading file: " + e.getMessage());
        }
        return list;
    }

    private static String extractValue(String line) {
        int colon = line.indexOf(':');
        if (colon == -1) return "";
        int firstQuote = line.indexOf('"', colon + 1);
        if (firstQuote == -1) return "";
        int secondQuote = line.indexOf('"', firstQuote + 1);
        if (secondQuote == -1) return "";
        return line.substring(firstQuote + 1, secondQuote);
    }
}
