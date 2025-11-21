import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageStore {

    private final List<String> messageIDs = new ArrayList<>();
    private final List<String> hashes = new ArrayList<>();
    private final List<String> recipients = new ArrayList<>();
    private final List<String> messages = new ArrayList<>();
    private final List<String> statuses = new ArrayList<>();

    // Add a record explicitly
    public void addMessage(String id, String recipient, String messageText, String status, String hash) {
        messageIDs.add(id == null ? "" : id);
        hashes.add(hash == null ? "" : hash);
        recipients.add(recipient == null ? "" : recipient);
        messages.add(messageText == null ? "" : messageText);
        statuses.add(status == null ? "" : status.toUpperCase());
    }

    // Load JSON file via JsonMessageReader (manual parser)
    public void loadFromJson(String filePath) {
        List<JsonMessageReader.SimpleMessage> list = JsonMessageReader.readMessages(filePath);
        for (JsonMessageReader.SimpleMessage sm : list) {
            String id = (sm.id == null || sm.id.isEmpty()) ? generateFallbackId() : sm.id;
            String hash = generateHashForJSON(id, sm.message);
            addMessage(id, sm.recipient, sm.message, sm.status, hash);
        }
    }

    // Fallback id generator (simple incremental)
    private String generateFallbackId() {
        return "J" + (messageIDs.size() + 1);
    }

    private String generateHashForJSON(String id, String message) {
        String firstTwo = id.length() >= 2 ? id.substring(0, 2) : "XX";
        String first = "";
        String last = "";
        if (message != null && !message.isEmpty()) {
            String[] parts = message.trim().split("\\s+");
            first = parts[0];
            last = parts[parts.length - 1];
        }
        return (firstTwo + ":" + messageIDs.size() + ":" + first + last).toUpperCase();
    }

    // a. Display sender and recipient for sent messages (sender parameter)
    public List<String> displaySenderAndRecipientForSent(String sender) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < statuses.size(); i++) {
            if ("SENT".equalsIgnoreCase(statuses.get(i))) {
                out.add(sender + " -> " + recipients.get(i));
            }
        }
        return out;
    }

    // b. Longest sent message
    public Optional<String> getLongestSentMessage() {
        String longest = null;
        for (int i = 0; i < statuses.size(); i++) {
            if ("SENT".equalsIgnoreCase(statuses.get(i))) {
                String m = messages.get(i);
                if (m != null && (longest == null || m.length() > longest.length())) {
                    longest = m;
                }
            }
        }
        return Optional.ofNullable(longest);
    }

    // c. Search by message ID
    public Optional<String> searchByMessageID(String id) {
        int idx = messageIDs.indexOf(id);
        if (idx == -1) return Optional.empty();
        return Optional.of(String.format("Recipient: %s Message: %s", recipients.get(idx), messages.get(idx)));
    }

    // d. Search by recipient
    public List<String> searchByRecipient(String recipient) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            if (recipient != null && recipient.equals(recipients.get(i))) {
                out.add(messages.get(i));
            }
        }
        return out;
    }

    // e. Delete by hash
    public boolean deleteByHash(String hash) {
        int idx = hashes.indexOf(hash);
        if (idx == -1) return false;
        // remove aligned entries
        messageIDs.remove(idx);
        hashes.remove(idx);
        recipients.remove(idx);
        messages.remove(idx);
        statuses.remove(idx);
        return true;
    }

    // f. Generate report of sent messages
    public String generateSentMessagesReport() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statuses.size(); i++) {
            if ("SENT".equalsIgnoreCase(statuses.get(i))) {
                sb.append(String.format("MessageID: %s | Hash: %s | Recipient: %s | Message: %s%n",
                        messageIDs.get(i), hashes.get(i), recipients.get(i), messages.get(i)));
            }
        }
        return sb.toString();
    }

    // full report (all messages)
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== QuickChat Message Report ===\n");
        for (int i = 0; i < messageIDs.size(); i++) {
            sb.append("ID: ").append(messageIDs.get(i))
              .append(" | Recipient: ").append(recipients.get(i))
              .append(" | Status: ").append(statuses.get(i))
              .append("\nMessage: ").append(messages.get(i))
              .append("\nHash: ").append(hashes.get(i))
              .append("\n-------------------------------------\n");
        }
        return sb.toString();
    }

    // helper for tests
    public void clearAll() {
        messageIDs.clear();
        hashes.clear();
        recipients.clear();
        messages.clear();
        statuses.clear();
    }
}
