package chatApp.controller;

import chatApp.Entities.*;
import chatApp.repository.ActivateRepository;
import chatApp.repository.GuestRepository;
import chatApp.repository.UserRepository;
import chatApp.util.ValidationUtils;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;
    @Autowired
    ChatController chatController;
    @Autowired
    AuthenticationController authenticationController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    ActivateRepository activateRepository;
    private static final SubmitedUser goodGuest = new SubmitedUser(" ", " ", "Saray");
    private static final SubmitedUser goodUser = new SubmitedUser("saraysara1996@gmail.com", "123456789", "Saray");

    @AfterEach
    void setup() throws NoSuchAlgorithmException {
        User myUser = new User.Builder("saraysara1996@gmail.com", ValidationUtils.secretPassword("123456789"), "Saray").build();

        userRepository.delete(myUser);
        guestRepository.deleteUserByNickName(goodGuest.getNickName());
    }
    public void enterDB() throws NoSuchAlgorithmException {
        User myUser = new User.Builder("saraysara1996@gmail.com", ValidationUtils.secretPassword("123456789"), "Saray").build();

        userRepository.save(myUser);
    }

    public String enterDB_activate()  {
        ActiveUser activeUser = new ActiveUser("dana2@gmail.com", "123456789", "saray", "1234");
        activateRepository.save(activeUser);
        return activeUser.getCode();
    }
    @Test
    public void saveProfile_Successfully() throws NoSuchAlgorithmException {
        SubmitedUser user = new SubmitedUser("saraysara1996@gmail.com", "123456789", "Saray");
        User myUser = new User.Builder(user.getEmail(), ValidationUtils.secretPassword(user.getPassword()), user.getNickName()).build();
        ResponseEntity<String> r = userController.saveProfile(myUser);

        assertEquals(200, r.getStatusCodeValue(), "new profile saved successfully!");
    }

    @Test
    public void saveProfile_Not_Successfully_BadNickName() throws NoSuchAlgorithmException {
        SubmitedUser user = new SubmitedUser("saraysara1996@gmail.com", "123456789", "S");
        User myUser = new User.Builder(user.getEmail(), ValidationUtils.secretPassword(user.getPassword()), user.getNickName()).build();

        assertThrows(IllegalArgumentException.class, () -> userController.saveProfile(myUser));
    }

    @Test
    public void getGuestList_NotNull_Successfully() {
        authenticationController.loginGuest(goodGuest);
        ResponseEntity<List<Guest>> result = userController.getGuestList();

        assertNotNull(result.getBody());
    }

    @Test
    public void getGuestList_Null_Successfully() {
        ResponseEntity<List<Guest>> result = userController.getGuestList();

        assertTrue(result.getBody().isEmpty());
    }

    @Test
    public void getUserByToken1_Successfully() throws NoSuchAlgorithmException {
        Object result = authenticationController.login(goodUser).getBody();
        String myx = "{\"token\" : \"" + result.toString() + "\"}";
        ResponseEntity<User> r = userController.getUserByToken1(myx);

        assertEquals(200, r.getStatusCodeValue());
    }

    @Test
    @Disabled
    public void getUserById_Successfully() throws NoSuchAlgorithmException {
        enterDB();
        User myUser = new User.Builder("saraysara1996@gmail.com", ValidationUtils.secretPassword("123456789"), "Saray").build();
        String id = String.valueOf(myUser.getId());

        assertNotNull(userController.getUserById(id));
    }

    @Test
    public void getUserList_Successfully() {
        ResponseEntity<List<User>> r = userController.getUserList();

        assertEquals(200, r.getStatusCodeValue());
    }

    @Test
    public void AllMessageHistoryMainChat_Successfully() {
        RecievedMessage recievedMessage = new RecievedMessage("sara", "hi");
        chatController.sendPlainMessage(recievedMessage);
        ResponseEntity<List<ChatMessage>> newList = userController.AllMessageHistoryMainChat("0");

        assertNotNull(newList.getBody());
    }

    @Test
    public void AllMessageHistoryMainChat_Successfully_NotNull() {
        RecievedMessage recievedMessage = new RecievedMessage("sara", "hi");
        chatController.sendPlainMessage(recievedMessage);
        ResponseEntity<List<ChatMessage>> newList = userController.AllMessageHistoryMainChat("0");

        assertEquals(200, newList.getStatusCodeValue());
    }
}
