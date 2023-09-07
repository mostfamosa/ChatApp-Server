package chatApp.util;
import chatApp.Entities.SubmitedUser;
import chatApp.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import static chatApp.util.ValidationUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {
    private static Logger logger = LogManager.getLogger(ValidationUtilsTest.class.getName());


    @Test
    void validateName_ReturnTrue() {
        String name = "Sarayy";
        assertTrue(validateName(name));
    }

    @Test
    void validateName_ReturnFalse() {
        String name = "s";
        assertFalse(validateName(name));
    }

    @Test
    void validateCurrentEmail_ReturnTrue() {
        String email = "saraysara1996@gmail.com";
        assertTrue(validateEmail(email));
    }

    @Test
    void validateBadEmail_ReturnFalse() {
        String email = "saraysara1996gmail.com";
        logger.info("email");
        assertFalse(validateEmail(email));
    } //TODO:update the real validation, and then this test will be work:)

    @Test
    void validateCurrentPassword_ReturnTrue() {
        String password = "sdzfg12345";
        assertTrue(validatePassword(password));
    }

    @Test
    void validateShortPassword_ReturnFalse() {
        String password = "1";
        assertFalse(validatePassword(password));
    }

    @Test
    void registrationUserValidationCurrent_ReturnTrue() {
        SubmitedUser user1 = new SubmitedUser("saraysara1996@gmail.com", "sdz12345fg", "Sarayy");
        assertTrue(registrationUserValidation(user1));
    }

    @Test
    void registrationUserValidationFail_ReturnFalse() {
        SubmitedUser user1 = new SubmitedUser("saraysara1996gmail.com", "sg", "Sarayy");
        assertFalse(registrationUserValidation(user1));
    }

    @Test
    void goodLoginUserValidation_ReturnTrue() {
        SubmitedUser user1 = new SubmitedUser("saraysara1996@gmail.com", "sddwdcdvdzfg", "Sarayy");
        assertTrue(loginUserValidation(user1));
    }

    @Test
    void badLoginUserValidation_ReturnFalse() {
        SubmitedUser user1 = new SubmitedUser("saraysara1996gmail.com", "fg", "Sarayy");
        assertFalse(loginUserValidation(user1));
    }

    @Test
    void goodGuestValidation_ReturnTrue() {
        SubmitedUser user1 = new SubmitedUser(" ", " ", "Sarayy");
        assertTrue(guestValidation(user1));
    }

    @Test
    void badGuestValidation_ReturnFalse() {
        SubmitedUser user1 = null;
        assertFalse(guestValidation(user1));
    }

    @Test
    void isNumeric_returnTrue() {
        String str = "12345";
        assertTrue(isNumeric(str));
    }

    @Test
    void isNumeric_returnFalse() {
        String str = "";
        assertFalse(isNumeric(str));
    }

    @Test
    void returnGenerateRandomToken() {
        assertNotNull(generateRandomToken());
    }

    @Test
    void ReturnSecretPasswordT() throws NoSuchAlgorithmException {
        String str = "123";
        assertNotNull(secretPassword(str));
    }
}