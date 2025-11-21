import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    @Test
    public void testValidRegister() {
        Register r = new Register();
        String res = r.registerUser("bo_as", "Test@123", "+27123456789");
        assertEquals("User successfully registered.", res);
    }

    @Test
    public void testInvalidUsername() {
        Register r = new Register();
        String res = r.registerUser("boas", "Test@123", "+27123456789");
        assertEquals("Username is not correctly formatted", res);
    }

    @Test
    public void testInvalidPassword() {
        Register r = new Register();
        String res = r.registerUser("bo_as", "test", "+27123456789");
        assertEquals("Password does not meet complexity", res);
    }

    @Test
    public void testInvalidCell() {
        Register r = new Register();
        String res = r.registerUser("bo_as", "Test@123", "0723456789");
        assertEquals("Cell number is not correct", res);
    }
}

