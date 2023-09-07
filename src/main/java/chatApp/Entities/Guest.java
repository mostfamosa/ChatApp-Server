package chatApp.Entities;

import javax.persistence.*;

@Entity
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String nickName;
    private boolean isMuted;

    public Guest() {

    }

    public Guest(String nickName) {
        this.nickName = nickName;
        this.isMuted = false;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", muted=" + isMuted +
                '}';
    }
}
