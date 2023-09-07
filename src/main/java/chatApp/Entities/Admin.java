package chatApp.Entities;

import javax.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin() {
    }

    public Admin(Builder builder) {
        super(builder);
    }

    public void muteUser(User user) {
        user.setIsMuted(true);
    }

    public void unMuteUser(User user) {
        user.setIsMuted(false);
    }
}
