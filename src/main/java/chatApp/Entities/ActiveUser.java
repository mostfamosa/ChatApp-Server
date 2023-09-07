package chatApp.Entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activeUser")
public class ActiveUser {

    private String email;
    private String password;
    private String nickName;
    @Column(unique = true)
    private String code;

    public ActiveUser() {

    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    public ActiveUser(String email, String password, String nickName, String code) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.code = code;
    }

    public ActiveUser(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "activeUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", code='" + code + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubmitedUser)) return false;
        SubmitedUser that = (SubmitedUser) o;
        return Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getNickName(), that.getNickName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassword(), getNickName());
    }
}