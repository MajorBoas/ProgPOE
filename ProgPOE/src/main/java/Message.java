import java.util.Random;
import javax.swing.JOptionPane;

public class Message {

    private static int totalMessagesSent = 0;

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sendStatus;

    public boolean checkMessageID(String id) {
        return id != null && id.matches("\\d{1,10}");
    }

    public boolean checkRecipientCell(String cell) {
        if (cell == null) return false;
        return cell.matches("^\\+\\d{9,12}$");
    }

    public String checkMessageLength(String msg) {
        if (msg == null) return "Message cannot be empty.";
        if (msg.length() > 250) {
            int extra = msg.length() - 250;
            return "Message exceeds 250 characters by " + extra + ", please reduce size.";
        }
        return "Message ready to send.";
    }

    public String generateMessageID() {
        Random rand = new Random();
        long id = (long) (1000000000L + rand.nextDouble() * 8999999999L);
        messageID = String.valueOf(id);
        return messageID;
    }

    // msgNumber is used for hash formatting (pass Message.returnTotalMessages()+1 when creating)
    public String createMessageHash(int msgNumber, String msgText) {
        if (messageID == null) generateMessageID();
        if (msgText == null || msgText.trim().isEmpty()) return "INVALID MESSAGE";

        String firstTwo = messageID.substring(0, 2);
        String[] words = msgText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        messageHash = (firstTwo + ":" + msgNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    // returns status keyword ("SENT"/"STORED"/"DISREGARDED")
    public String sendMessageOptionInteractive() {
        String[] options = {"1.Send Message", "2.Store Message", "3.Disregard Message"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose what to do with this message:",
                "Send Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0:
                sendStatus = "SENT";
                totalMessagesSent++;
                break;
            case 1:
                sendStatus = "STORED";
                break;
            case 2:
                sendStatus = "DISREGARDED";
                break;
            default:
                sendStatus = "NONE";
                break;
        }
        return sendStatus;
    }

    // non-interactive setter for status
    public void setSendStatus(String status) {
        if (status == null) return;
        this.sendStatus = status.toUpperCase();
    }

    public void printMessageDetails() {
        String info = "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText
                + "\nStatus: " + sendStatus;
        JOptionPane.showMessageDialog(null, info, "Message Details", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static void incrementMessageCount() {
        totalMessagesSent++;
    }

    // getters/setters
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getSendStatus() { return sendStatus; }
}
