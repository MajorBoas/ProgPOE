import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class MainClass {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Register register = new Register();
        Login login = new Login();
        Message message = new Message();
        MessageStore store = new MessageStore();

        System.out.println("=== Registration ===");
        String username, password, cell;

        do {
            System.out.print("Enter username: ");
            username = sc.nextLine();
            if (!register.checkUserName(username)) {
                System.out.println("Username not correct: must contain '_' and be <= 5 chars.");
            }
        } while (!register.checkUserName(username));

        do {
            System.out.print("Enter password: ");
            password = sc.nextLine();
            if (!register.checkPasswordComplexity(password)) {
                System.out.println("Password invalid: need >=8 chars, 1 uppercase, 1 digit, 1 special.");
            }
        } while (!register.checkPasswordComplexity(password));

        do {
            System.out.print("Enter cell phone (include +countrycode): ");
            cell = sc.nextLine();
            if (!register.checkCellPhoneNumber(cell)) {
                System.out.println("Cell number incorrect.");
            }
        } while (!register.checkCellPhoneNumber(cell));

        System.out.println(register.registerUser(username, password, cell));

        login.setRegisteredDetails(register.getRegisteredUsername(),
                register.getRegisteredPassword(), register.getRegisteredCell());

        System.out.println("\n=== Login ===");
        System.out.print("Enter username: ");
        String loginU = sc.nextLine();
        System.out.print("Enter password: ");
        String loginP = sc.nextLine();

        boolean ok = login.loginUser(loginU, loginP);
        System.out.println(login.returnLoginStatus(ok));

        if (ok) {
            String recipient = "";
            do {
                recipient = JOptionPane.showInputDialog("Enter recipient cell (e.g. +27123456789):");
                if (!message.checkRecipientCell(recipient)) {
                    JOptionPane.showMessageDialog(null, "Invalid recipient number.");
                }
            } while (!message.checkRecipientCell(recipient));
            message.setRecipient(recipient);

            String msgText = "";
            do {
                msgText = JOptionPane.showInputDialog("Enter your message (max 250 chars):");
                String check = message.checkMessageLength(msgText);
                if (check.contains("exceeds")) {
                    JOptionPane.showMessageDialog(null, check);
                } else {
                    message.setMessageText(msgText);
                    break;
                }
            } while (true);

            // ID and hash
            String id = message.generateMessageID();
            String hash = message.createMessageHash(Message.returnTotalMessages() + 1, message.getMessageText());

            // Action choice
            String[] options = {"Send", "Store", "Disregard"};
            int action = JOptionPane.showOptionDialog(
                    null,
                    "What do you want to do with this message?",
                    "Choose Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            String status = "NONE";
            switch (action) {
                case 0:
                    status = "SENT";
                    message.setSendStatus("Message successfully sent.");
                    Message.incrementMessageCount();
                    break;
                case 1:
                    status = "STORED";
                    message.setSendStatus("Message successfully stored.");
                    break;
                case 2:
                    status = "DISREGARDED";
                    message.setSendStatus("Message discarded.");
                    break;
                default:
                    message.setSendStatus("No action selected.");
            }

            // Add to store
            store.addMessage(id, recipient, message.getMessageText(), status, hash);

            // Show details
            message.printMessageDetails();

            // Optional: if you have a messages.json file, you may load it:
            // store.loadFromJson("messages.json");

            // Console report
            System.out.println(store.generateReport());

            // Part 3 examples (search / longest / delete)
            List<String> sentPairs = store.displaySenderAndRecipientForSent(register.getRegisteredCell());
            System.out.println("Sent pairs (sender->recipient): " + sentPairs);

            Optional<String> longest = store.getLongestSentMessage();
            System.out.println("Longest sent message: " + longest.orElse("No sent messages"));

            // search by id example:
            Optional<String> found = store.searchByMessageID(id);
            System.out.println(found.orElse("Not found"));

            // delete by hash example:
            boolean deleted = store.deleteByHash(hash);
            System.out.println("Deleted by hash? " + deleted);

            JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
        }

        sc.close();
    }
}
