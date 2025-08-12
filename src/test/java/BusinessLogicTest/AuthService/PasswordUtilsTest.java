package BusinessLogicTest.AuthService;

import BusinessLogic.AuthService.PasswordUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilsTest {

    @Test
    void testPasswordHashing() {
        String plainPassword = "password";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);
        assertNotEquals(plainPassword, hashedPassword);
        assertTrue(PasswordUtils.checkPassword(plainPassword, hashedPassword));
        String wrongPassword = "wrongPassword";
        assertFalse(PasswordUtils.checkPassword(wrongPassword, hashedPassword));
    }

}
