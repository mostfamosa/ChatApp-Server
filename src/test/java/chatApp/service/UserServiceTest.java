package chatApp.service;

import chatApp.Entities.Guest;
import chatApp.Entities.Response;
import chatApp.Entities.SubmitedUser;
import chatApp.Entities.User;
import chatApp.repository.GuestRepository;
import chatApp.repository.UserRepository;
import chatApp.util.EmailActivation;
import chatApp.util.ValidationUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    GuestRepository guestRepository;
    @InjectMocks
    UserService userService;
    private Map<String, SubmitedUser> useresCode = new HashMap<>();

    @Test
    void addGuestToDbTest() {
        Guest GGG = new Guest("saraa");
        userService.addGuest(GGG);
        ArgumentCaptor<Guest> guestArgumentCaptor = ArgumentCaptor.forClass(Guest.class);
        verify(guestRepository).save(guestArgumentCaptor.capture());
        Guest Guest = guestArgumentCaptor.getValue();

        assertThat(Guest).isEqualTo(GGG);
    }

    @Test
    void getAllGuestTest() {
        userService.getAllGuests();
        verify(guestRepository).findAll();

        assertTrue(guestRepository.findAll().equals(userService.getAllGuests()));
    }

    @Test
    void getGuestListTest() {
        userService.getGuestList();
        verify(guestRepository).findAll();
    }


    @Test
    void loginUserBadTest() throws NoSuchAlgorithmException {
        SubmitedUser user1 = new SubmitedUser("danagmail.com", "123456", "Saraysar");

        assertNull(userService.login(user1));
    }
    @Test
    void addTakenNickNameGuestToDbTest() {
        Guest GGG = new Guest("saraaaaaa");
        given(guestRepository.findByNickName(GGG.getNickName())).willReturn(GGG);

        assertThatThrownBy(() -> userService.addGuest(GGG)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(String.format("Nickname %s exists in guests table", GGG.getNickName()));
    }

    @Test
    void getUseresCodeTest() {
        SubmitedUser user = new SubmitedUser("saraysara@gmail.com", "123", "sasasa");
        useresCode.put("1", user);

        assertNotNull(userService.getUseresCode());
    }
}