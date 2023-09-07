package chatApp.controller;


import chatApp.Entities.Response;
import chatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    /**
     * mute router .
     *
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "mute", method = RequestMethod.PUT)
    public ResponseEntity<Object> mute(@RequestBody String token, @RequestParam String id) {
        Response response = userService.mute(token, id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getMessage());
    }

    /**
     * unmute router .
     *
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "unmute", method = RequestMethod.PUT)
    public ResponseEntity<Object> unMute(@RequestBody String token, @RequestParam String id) {
        Response response = userService.unMute(token, id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getMessage());
    }

    /**
     * mute guests router .
     *
     * @param token
     * @param nickName
     * @return
     */
    @RequestMapping(value = "muteGuest", method = RequestMethod.PUT)
    public ResponseEntity<Object> muteGuest(@RequestBody String token, @RequestParam String nickName) {
        Response response = userService.muteGuest(token, nickName);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getMessage());
    }

    /**
     * unmute guests router .
     *
     * @param token
     * @param nickName
     * @return
     */
    @RequestMapping(value = "unmuteGuest", method = RequestMethod.PUT)
    public ResponseEntity<Object> unmuteGuest(@RequestBody String token, @RequestParam String nickName) {
        Response response = userService.unmuteGuest(token, nickName);
        return ResponseEntity
                .status(response.getStatus())
                .body(response.getMessage());
    }
}
