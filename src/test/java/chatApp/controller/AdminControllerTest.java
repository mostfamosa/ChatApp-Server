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
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminControllerTest {
    @Autowired
    AdminController adminController;
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

    public String enterDB_activate() {
        ActiveUser activeUser = new ActiveUser("dana2@gmail.com", "123456789", "saray", "1234");
        activateRepository.save(activeUser);
        return activeUser.getCode();
    }
}