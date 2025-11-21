import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MessageStoreTest {

    private MessageStore store;

    @BeforeEach
    public void setUp() {
        store = new MessageStore();
        store.addMessage("m1", "+27111111111", "Short", "SENT", "H1");
        store.addMessage("m2", "+27222222222", "This is longer text", "STORED", "H2");
        store.addMessage("m3", "+27111111111", "Another", "DISREGARDED", "H3");
    }

    @Test
    public void testSearchByRecipient() {
        List<String> hits = store.searchByRecipient("+27111111111");
        assertTrue(hits.contains("Short"));
        assertTrue(hits.contains("Another"));
    }

    @Test
    public void testSearchById() {
        Optional<String> out = store.searchByMessageID("m2");
        assertTrue(out.isPresent());
        assertTrue(out.get().contains("This is longer text"));
    }

    @Test
    public void testDeleteByHash() {
        boolean del = store.deleteByHash("H2");
        assertTrue(del);
        Optional<String> out = store.searchByMessageID("m2");
        assertFalse(out.isPresent());
    }

    @Test
    public void testGenerateReport() {
        String rep = store.generateReport();
        assertTrue(rep.contains("m1"));
        assertTrue(rep.contains("m2"));
    }

    @Test
    public void testLongestSentMessage() {
        store.addMessage("m4", "+27333333333", "A long sent message example", "SENT", "H4");
        Optional<String> longest = store.getLongestSentMessage();
        assertTrue(longest.isPresent());
        assertEquals("A long sent message example", longest.get());
    }
}

