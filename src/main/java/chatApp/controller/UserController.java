package chatApp.controller;

import chatApp.Entities.*;
import chatApp.service.ChatService;
import chatApp.service.UserService;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class UserController {
    private static Logger logger = LogManager.getLogger(UserController.class.getName());
    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;


    /**
     * @param user details.
     * @return update the user profile.
     * @throws IllegalArgumentException if one of the details not valid.
     */
    @RequestMapping(value = "saveProfile", method = RequestMethod.POST)
    public ResponseEntity<String> saveProfile(@RequestBody User user) throws IllegalArgumentException {

        Response response = userService.saveProfile(user);
        Gson g = new Gson();
        return ResponseEntity
                .status(response.getStatus())
                .body(g.toJson(response.getMessage()));
    }


    /**
     * @return list of all the guest users from the repo.
     */
    @RequestMapping(value = "onlineGuestUsers", method = RequestMethod.GET)
    public ResponseEntity<List<Guest>> getGuestList() {
        List<Guest> mylist = userService.getGuestList();
        return ResponseEntity.status(HttpStatus.OK).body(mylist);
    }


    /**
     * @param token of online user
     * @return the user from the map of the online users in the service
     */
    @RequestMapping(value = "userByToken", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByToken1(@RequestParam String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByToken(token));
    }

    /**
     * @param id of the user
     * @return search the user by id in the repo, and return the user in the response body
     */
    @RequestMapping(value = "userById", method = RequestMethod.POST)
    public ResponseEntity<User> getUserById(@RequestBody String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    /**
     * @return list of 'online' user from the user repo.
     */
    @RequestMapping(value = "onlineUsers", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUserList() {

        List<User> mylist = userService.getUserList().stream().filter(user -> user.getStatus() != "offline")
                .sorted(Comparator.comparing(User::getRole))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(mylist);
    }

    /**
     * @param chatId The number of the chat
     * @return All the message history of this chat
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<List<ChatMessage>> AllMessageHistoryMainChat(@RequestParam String chatId) {

        List<ChatMessage> newList = chatService.getAllMessagesByChatId(chatId);
        logger.info(newList);

        return ResponseEntity.status(HttpStatus.OK).body(newList);
    }

}


