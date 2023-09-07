package chatApp.util;

import chatApp.Entities.ActiveUser;
import chatApp.Entities.SubmitedUser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static chatApp.util.EmailActivation.sendEmailWithGenerateCode;
import static chatApp.util.EmailActivation.sendSuccessRegisterationMessageToUser;

class EmailActivationTest {

    @Test
    void sendSuccessRegisterationMessageToUserTest() {
        ActiveUser user1 = new ActiveUser("saraysara1996@gmail.com", "sdzavrbtbdfg", "Sarayy");
        sendSuccessRegisterationMessageToUser(user1);
    }

    @Test
    void sendEmailWithGenerateCodeT() {
        ActiveUser user1 = new ActiveUser("saraysara1996@gmail.com", "sdsvwwvsdzfg", "Sarayy");
        sendEmailWithGenerateCode("123", user1);
    }
}