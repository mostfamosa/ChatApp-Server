package chatApp.Entities;

import java.util.Objects;

public class SubmitedUser {

    private String email;
    private String password;
    private String nickName;

    public SubmitedUser() {

    }

    public SubmitedUser(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
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
        return "SubmitedUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
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
