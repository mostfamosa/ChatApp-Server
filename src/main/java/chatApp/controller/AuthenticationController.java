package chatApp.controller;

import chatApp.Entities.Guest;
import chatApp.Entities.Response;
import chatApp.Entities.SubmitedUser;
import chatApp.Entities.ActiveUser;
import chatApp.service.ActivateService;
import chatApp.service.UserService;
import chatApp.util.EmailActivation;
import chatApp.util.ValidationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {
    private static Logger logger = LogManager.getLogger(AuthenticationController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private ActivateService activateService;

    /**
     * @param user details
     * @return check validations and return the token of the user if he succeeded to log in
     * @throws IllegalArgumentException validations problems
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@RequestBody SubmitedUser user) throws IllegalArgumentException, NoSuchAlgorithmException {
        String token = "";

        if (ValidationUtils.loginUserValidation(user))
            token = userService.login(user);

        if (token == null || token.isEmpty())
            throw new IllegalArgumentException(String.format("email or password is not correct !"));

        return ResponseEntity.ok(token);
    }


    /**
     * @param user details of guest details
     * @return guest in the body response if he logged in and pass the validations.
     * @throws IllegalArgumentException validations problems
     */
    @RequestMapping(value = "loginGuest", method = RequestMethod.POST)
    public ResponseEntity<Object> loginGuest(@RequestBody SubmitedUser user) throws IllegalArgumentException {

        if (ValidationUtils.guestValidation(user)) {
            Guest guest = new Guest(user.getNickName());
            return ResponseEntity.status(HttpStatus.OK).body(userService.addGuest(guest));
        }

        throw new IllegalArgumentException(String.format("Nickname \" %s \" is not valid!", user.getNickName()));
    }


    /**
     * @param user details
     * @return check the user details, validation, and send an email with code,
     * then, keep him on map until he activates his email.
     * @throws IllegalArgumentException if there is a validated problems
     */
    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody SubmitedUser user) throws IllegalArgumentException {
        if (!ValidationUtils.validateEmail(user.getEmail()))
            throw new IllegalArgumentException(String.format("Email \" %s \" is not valid!", user.getEmail()));

        if (!ValidationUtils.validatePassword(user.getPassword()))
            throw new IllegalArgumentException(String.format("Password \" %s \" is not valid!", user.getPassword()));

        if (!ValidationUtils.validateName(user.getNickName()))
            throw new IllegalArgumentException(String.format("Nickname \" %s \" is not valid!", user.getNickName()));


        if (!userService.isUserRegistered(user)) {

            String myCode = ValidationUtils.generateRandomToken();

            ActiveUser activeU = new ActiveUser(user.getEmail(), user.getPassword(), user.getNickName(), myCode);

            EmailActivation.sendEmailWithGenerateCode(myCode, activeU);

            activateService.keepOnDB(activeU);

            return ResponseEntity
                    .status(200)
                    .body("added to dataBase for activation users");
        }

        return ResponseEntity
                .status(400)
                .body("User is already existed !");

    }

    /**
     * logout for guest router .
     * @param token
     * @return
     */

    @RequestMapping(value = "logoutGuest", method = RequestMethod.POST)
    public ResponseEntity<String> logoutGuest(@RequestBody String token) {
        if (!userService.logoutGuest(token))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("some error !");

        return ResponseEntity.ok("logout done successfully");
    }

    /**
     * @param token of the online user
     * @return response 200 if he found the token in the map, he log out the user. else error
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity<String> logout(@RequestBody String token) {

        if (!userService.logout(token))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("some error !");

        return ResponseEntity.ok("logout done successfully");
    }


    /**
     * @param code that we send to the mail.
     * @return if the code is right the func enter the user to the users' repo/
     */
    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public ResponseEntity<Object> activateEmail(@RequestParam String code) throws NoSuchAlgorithmException {

        Response response = userService.enterUserToDB(code);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getMessage());
    }

}
