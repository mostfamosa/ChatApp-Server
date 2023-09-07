package chatApp.Entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickName;


    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private int age;
    private String description;
    private String status;
    private boolean isMuted;
    private boolean isPrivate;
    private int role;
    private String imgUrl;


    public static class Builder {

        private int id;
        private final String email;
        private final String password;
        private final String nickName;

        private String firstName = "firstName";
        private String lastName = "lastName";
        private LocalDate birthDate = LocalDate.now();
        private int age = 0;
        private String description = "description";
        private String status = "offline";
        private int role = 0;
        private String imgUrl = "https://bootdey.com/img/Content/avatar/avatar7.png";
        private boolean isMuted = false;
        private boolean isPrivate = false;


        public Builder(String email, String password, String nickName) {
            this.email = email;
            this.password = password;
            this.nickName = nickName;
        }


        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder role(int role) {
            this.role = role;
            return this;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder isMuted(boolean isMuted) {
            this.isMuted = isMuted;
            return this;
        }

        public Builder isPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public User() {

    }

    User(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.nickName = builder.nickName;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.age = builder.age;
        this.description = builder.description;
        this.status = builder.status;
        this.role = builder.role;
        this.imgUrl = builder.imgUrl;
        this.isPrivate= builder.isPrivate;
    }

    public int getId() {
        return id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIsMuted(boolean isMuted) {
        this.isMuted = isMuted;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public boolean getIsMuted() {
        return isMuted;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && getAge() == user.getAge() && isMuted == user.isMuted && getRole() == user.getRole() && getEmail().equals(user.getEmail()) && getPassword().equals(user.getPassword()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getNickName(), user.getNickName()) && Objects.equals(getBirthDate(), user.getBirthDate()) && Objects.equals(getDescription(), user.getDescription()) && Objects.equals(getStatus(), user.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getFirstName(), getLastName(), getNickName(), getBirthDate(), getAge(), getDescription(), getStatus(), isMuted, getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + age +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", isMuted=" + isMuted +
                ", role=" + role +
                ", imgUrl=" + imgUrl +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
