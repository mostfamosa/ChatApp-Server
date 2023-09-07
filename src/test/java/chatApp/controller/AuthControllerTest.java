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

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthControllerTest {
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

    public String enterDB_activate() throws NoSuchAlgorithmException {
        ActiveUser activeUser = new ActiveUser("dana2@gmail.com", "123456789", "saray", "1234");
        activateRepository.save(activeUser);
        return activeUser.getCode();
    }

    @Test
    public void login_User_Successfully() throws NoSuchAlgorithmException { //auth //good test
        enterDB();
        ResponseEntity<Object> r = authenticationController.login(goodUser);

        assertTrue(r.getStatusCodeValue() == 200);
    }

    @Test
    public void login_User_Not_Successfully_UserNotFound() { //auth //email not exist
        SubmitedUser user = new SubmitedUser("notExit@gmail.com", "123456789", "Saray");

        assertThrows(IllegalArgumentException.class, () -> authenticationController.login(user), String.format("email or password is not correct !"));
    }

    @Test
    public void loginGuest_Successfully() { //auth //good test
        ResponseEntity<Object> r = authenticationController.loginGuest(goodGuest);
        assertTrue(r.getStatusCodeValue() == 200);
    }

    @Test
    public void loginGuest_Not_Successfully_NotValidName() { //auth //name to short
        SubmitedUser user = new SubmitedUser(" ", " ", "S");

        assertThrows(IllegalArgumentException.class, () -> authenticationController.loginGuest(user), String.format("Nickname \" %s \" is not valid!", user.getNickName()));
    }

    @Test
    public void createUser_Not_Successfully_BadEmail() { //auth
        SubmitedUser user = new SubmitedUser("badEmail", "1234567890", "Saray");

        assertThrows(IllegalArgumentException.class, () -> authenticationController.createUser(user), String.format("Email \" %s \" is not valid!", user.getEmail()));
    }

    @Test
    public void createUser_Not_Successfully_BadPassword() { //auth
        SubmitedUser user = new SubmitedUser("saraysara1996@gmail.com", "1", "Saray");

        assertThrows(IllegalArgumentException.class, () -> authenticationController.createUser(user), String.format("Password \" %s \" is not valid!", user.getPassword()));
    }

    @Test
    public void createUser_Not_Successfully_BadNickName() { //auth
        SubmitedUser user = new SubmitedUser("saraysara1996@gmail.com", "1234567890", "s");

        assertThrows(IllegalArgumentException.class, () -> authenticationController.createUser(user), String.format("Nickname \" %s \" is not valid!", user.getNickName()));
    }

    @Test
    public void createUser_Successfully() { //auth
        SubmitedUser user = new SubmitedUser("sarayshlomi1@gmail.com", "1234567890", "Sara");
        ResponseEntity<String> r = authenticationController.createUser(user);

        assertTrue(r.getStatusCodeValue() == 200);
    }

    @Test
    @Disabled
    public void createUser_Not_Successfully_UserExist() throws NoSuchAlgorithmException {
        enterDB();
        SubmitedUser user = new SubmitedUser("saraysara1996@gmail.com", "123456789", "Saray");

        assertThrows(IllegalArgumentException.class, () -> authenticationController.createUser(user));
    }

    @Test
    public void logoutGuest_Successfully() {
        System.out.println(goodGuest);
        Object resulte = authenticationController.loginGuest(goodGuest).getBody();
        System.out.println(resulte);
        String myx = "{\"token\" : \"" + resulte.toString() + "\"}";
        ResponseEntity<String> r = authenticationController.logoutGuest(myx);

        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    public void logoutGuest_Not_Successfully() {
        authenticationController.loginGuest(goodGuest).getBody();
        String myx = "{\"token\" : \"" + "NotExistToken" + "\"}";
        ResponseEntity<String> r = authenticationController.logoutGuest(myx);

        assertEquals("some error !", r.getBody());
    }

    @Test
    public void logoutUser_Successfully() throws NoSuchAlgorithmException {
        Object resulte = authenticationController.login(goodUser).getBody();
        String myx = "{\"token\" : \"" + resulte.toString() + "\"}";
        ResponseEntity<String> r = authenticationController.logout(myx);

        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    public void logoutUser_Not_Successfully() throws NoSuchAlgorithmException {
        authenticationController.login(goodUser);
        String myx = "{\"token\" : \"" + "NotExistToken" + "\"}";
        ResponseEntity<String> r = authenticationController.logout(myx);

        assertEquals("some error !", r.getBody());
    }

    @Test
    public void activateEmail_Successfully() throws NoSuchAlgorithmException {
        String code = enterDB_activate();
        ResponseEntity<Object> result = authenticationController.activateEmail(code);

        assertEquals(200, result.getStatusCodeValue());
    }
}