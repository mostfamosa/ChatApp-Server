package chatApp.service;

import chatApp.Entities.*;
import chatApp.repository.ActivateRepository;
import chatApp.repository.GuestRepository;
import chatApp.repository.UserRepository;
import chatApp.util.EmailActivation;
import chatApp.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private ActivateRepository activateRepository;
    private Map<Integer, String> usersTokens;
    private Map<String, String> guestsTokens;

    private Map<String, User> registeredUsers = new HashMap<>();
    public Map<String, SubmitedUser> useresCode = new HashMap<>();

    public Map<String, SubmitedUser> getUseresCode() {
        return useresCode;
    }

    public UserService(UserRepository userRepository, GuestRepository guestRepository) {
        this.userRepository = userRepository;
        this.guestRepository = guestRepository;
        usersTokens = new HashMap<>();
        guestsTokens = new HashMap<>();
    }

    /**
     * Function checks if user is registered in the db or not .
     *
     * @param user
     * @return
     */
    public boolean isUserRegistered(SubmitedUser user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new IllegalArgumentException(String.format("Email \" %s \" is Already Exist!", user.getEmail()));

        return false;
    }

    /**
     * Adds a user to the database if it has a unique email
     *
     * @param user - the user's data
     * @return a saved user with it's generated id
     * @throws IllegalArgumentException when the provided email already exists
     */
    public Response saveProfile(User user) throws IllegalArgumentException {

        if (!ValidationUtils.validateName(user.getNickName()))
            throw new IllegalArgumentException(String.format("Nickname \" %s \" is not valid!", user.getNickName()));
        if (!ValidationUtils.validateName(user.getFirstName()))
            throw new IllegalArgumentException(String.format("First Name \" %s \" is not valid!", user.getFirstName()));
        if (!ValidationUtils.validateName(user.getLastName()))
            throw new IllegalArgumentException(String.format("Last Name \" %s \" is not valid!", user.getLastName()));

        user.setPassword(userRepository.findByEmail(user.getEmail()).getPassword());
        user.setRole(userRepository.findByEmail(user.getEmail()).getRole());
        user.setIsMuted(userRepository.findByEmail(user.getEmail()).getIsMuted());
        User u = userRepository.findByEmail(user.getEmail());
        userRepository.delete(u);
        userRepository.save(user);
        userRepository.updateId(u.getId(), u.getEmail());

        return new Response(200, "new profile saved successfully!");
    }

    /**
     *
     * @param code that the user get in the mail with the link
     * @return response 200 if the function found him on the activate DB
     * @throws NoSuchAlgorithmException when secret password not valid
     */
    public Response enterUserToDB(String code) throws NoSuchAlgorithmException, IllegalArgumentException {
        ActiveUser user = activateRepository.findByCode(code);

        if (user == null)
            throw new IllegalArgumentException(String.format("Your Code is not Valid!"));

        User myUser = new User.Builder(user.getEmail(), ValidationUtils.secretPassword(user.getPassword()), user.getNickName()).build();

        if (userRepository.save(myUser) != null) {
            EmailActivation.sendSuccessRegisterationMessageToUser(user);
            activateRepository.delete(user);
            return new Response(200, "User is registered successfully");
        }

        return null;
    }

    /**
     * Function that gets all the users of the db .
     *
     * @return
     */
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    /**
     * Function to add guest to the db after validations .
     *
     * @param SubmittedGuest
     * @return
     * @throws IllegalArgumentException
     */
    public String addGuest(Guest SubmittedGuest) throws IllegalArgumentException {
        if (guestRepository.findByNickName(SubmittedGuest.getNickName()) != null)
            throw new IllegalArgumentException(String.format("Nickname %s exists in guests table", SubmittedGuest.getNickName()));

        Guest savedGuest = guestRepository.save(SubmittedGuest);

        if (savedGuest != null) {
            String token = ValidationUtils.generateRandomToken();
            guestsTokens.put(savedGuest.getNickName(), token);
            return token;
        }

        return null;
    }

    /**
     * Function to let user make login operation .
     *
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IllegalArgumentException
     */
    public String login(SubmitedUser user) throws NoSuchAlgorithmException, IllegalArgumentException {
        if (isUserValid(user)) {
            int userId = userRepository.findByEmail(user.getEmail()).getId();
            String token = ValidationUtils.generateRandomToken();
            usersTokens.put(userId, token);
            userRepository.updateUserSetStatusForId("online", userId);
            return token;
        }
        return null;
    }

    /**
     * Function that checks if user is valid - has an email in db and a fit password .
     *
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IllegalArgumentException
     */
    private boolean isUserValid(SubmitedUser user) throws NoSuchAlgorithmException, IllegalArgumentException {
        return isUserExistedAndPasswordIsFit(user);
    }

    /**
     * Helper to the function that check validation of the user .
     *
     * @param user
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchAlgorithmException
     */
    private boolean isUserExistedAndPasswordIsFit(SubmitedUser user) throws IllegalArgumentException, NoSuchAlgorithmException {
        User myUser = userRepository.findByEmail(user.getEmail());

        if (myUser == null)
            return false;

        else if (myUser.getPassword().equals(ValidationUtils.secretPassword(user.getPassword())))
            return true;

        return false;
    }


    /**
     * Registered user sends his token , if its available we could make logout for him , otherwise we send an error message .
     * Guest sends his name , so we delete him from Guests DB .
     * If the token is numeric it means it's a registered user ,else if it's not , so he is a guest and it's his name .
     *
     * @param token
     * @return
     */
    public boolean logout(String token) {

        String myToken = ValidationUtils.convertToken(token, Token.class);

        if (ValidationUtils.isNumeric(myToken)) {
            User user = getUserByToken(myToken);
            if (usersTokens.get(user.getId()).equals(myToken)) {
                usersTokens.remove(user.getId());
                userRepository.updateUserSetStatusForId("offline", user.getId());
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Function that deletes guest from the db , and deletes his token .
     *
     * @param token
     * @return
     */
    public boolean logoutGuest(String token) {

        String myToken = ValidationUtils.convertToken(token, Token.class);

        for (String nickName : guestsTokens.keySet()) {
            if (guestsTokens.get(nickName).equals(myToken)) {
                int delresult = guestRepository.deleteUserByNickName(nickName);
                if (delresult > 0) {
                    guestsTokens.remove(nickName);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * get list of all the guests .
     *
     * @return
     */
    public List<Guest> getGuestList() {
        return guestRepository.findAll();
    }

    /**
     * get list of all the users .
     *
     * @return
     */
    public List<User> getUserList() {
        List<User> userList = userRepository.findAll();
        List<User> res = new ArrayList<>();

        for (User u : userList)
            if (!Objects.equals(u.getStatus(), "offline"))
                res.add(u);

        return res;
    }

    /**
     * get user of db by his token if existed .
     *
     * @param token
     * @return
     */
    public User getUserByToken(String token) {
        Integer myid = -1;

        for (Integer key : usersTokens.keySet())
            if (usersTokens.get(key).equals(token))
                myid = key;

        User result = userRepository.findUserById(myid);
        return result;
    }

    /**
     * mute user .
     *
     * @param token
     * @param id
     * @return
     */
    public Response mute(String token, String id) {
        if (isAdmin(token)) {
            int i = Integer.parseInt(id);
            userRepository.mute(i);
            return new Response(200, "User is muted!");
        }

        return new Response(404, "Can't mute user!");
    }

    /**
     * unmute guest .
     *
     * @param token
     * @param nickName
     * @return
     */
    public Response muteGuest(String token, String nickName) {
        if (isAdmin(token)) {
            guestRepository.mute(nickName);
            return new Response(200, "User is muted!");
        }

        return new Response(404, "Can't mute user!");
    }

    /**
     * unMute user .
     *
     * @param token
     * @param id
     * @return
     */
    public Response unMute(String token, String id) {
        if (isAdmin(token)) {
            int i = Integer.parseInt(id);
            userRepository.unMute(i);
            return new Response(200, "User is un muted!");
        }

        return new Response(404, "Can't unmute user!");
    }

    /**
     * unmute guest .
     *
     * @param token
     * @param nickName
     * @return
     */
    public Response unmuteGuest(String token, String nickName) {
        if (isAdmin(token)) {
            guestRepository.unMute(nickName);
            return new Response(200, "User is un muted!");
        }

        return new Response(404, "Can't unmute user!");
    }

    /**
     * check if user is admin .
     *
     * @param token
     * @return
     */
    private boolean isAdmin(String token) {

        String mytoken = ValidationUtils.convertToken(token, Token.class);
        User user = getUserByToken(mytoken);

        if (user.getRole() == 1)
            return true;

        return false;
    }

    /**
     * general function to check is user/guest is muted .
     *
     * @param token
     * @return
     */
    public boolean isUserMuted(String token) {
        if (isRegisteredUserMuted(token) || isGuestMuted(token))
            return true;

        return false;
    }

    /**
     * checks if registered user is muted or not .
     *
     * @param token
     * @return
     */
    private boolean isRegisteredUserMuted(String token) {
        User user = getUserByToken(token);

        if (user != null)
            if (user.getIsMuted())
                return true;

        return false;
    }

    /**
     * checks if guest is muted or not .
     *
     * @param token
     * @return
     */
    private boolean isGuestMuted(String token) {
        for (String nickName : guestsTokens.keySet()) {
            if (guestsTokens.get(nickName).equals(token)) {
                Guest guest = guestRepository.findByNickName(nickName);
                if (guest.isMuted())
                    return true;
            }
        }
        return false;
    }

    /**
     * get user from db by his id .
     *
     * @param id
     * @return
     */
    public User getUserById(String id) {
        String myId = ValidationUtils.convertToken(id, Token.class);
        User result = userRepository.findUserById(Integer.valueOf(myId));
        return result;
    }

}
