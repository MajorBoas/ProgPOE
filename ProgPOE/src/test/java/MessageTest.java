import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testGenerateIdAndHash() {
        Message m = new Message();
        String id = m.generateMessageID();
        assertNotNull(id);
        assertTrue(id.length() >= 10);

        String hash = m.createMessageHash(1, "Hello world");
        assertNotNull(hash);
        assertTrue(hash.contains(":1:"));
    }

    @Test
    public void testRecipientValidation() {
        Message m = new Message();
        assertTrue(m.checkRecipientCell("+27123456789"));
        assertFalse(m.checkRecipientCell("0712345678"));
    }

    @Test
    public void testMessageLengthValidation() {
        Message m = new Message();
        String ok = m.checkMessageLength("short msg");
        assertEquals("Message ready to send.", ok);
        String longMsg = "x".repeat(260);
        String tooLong = m.checkMessageLength(longMsg);
        assertTrue(tooLong.contains("Message exceeds"));
    }
}

