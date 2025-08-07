package DomainModel.Users;

import BusinessLogic.AuthService.PasswordUtils;

import java.time.LocalDate;
import java.util.Objects;

public abstract class User {
    private int id;
    private String username;
    private String passwordHash;
    private String name;
    private String surname;
    private String mail;
    private String phoneNumber;
    private LocalDate birthDate;

    public User() {}

    public User(int id, User user) {
        this.id = id;
        this.username = user.username;
        this.passwordHash = user.passwordHash;
        this.name = user.name;
        this.surname = user.surname;
        this.mail = user.mail;
        this.phoneNumber = user.phoneNumber;
        this.birthDate = user.birthDate;
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.passwordHash = user.passwordHash;
        this.name = user.name;
        this.surname = user.surname;
        this.mail = user.name;
        this.phoneNumber = user.phoneNumber;
        this.birthDate = user.birthDate;
    }

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return this.passwordHash;
    }

    public void setPassword(String newPlainPassword) {
        this.passwordHash = PasswordUtils.hashPassword(newPlainPassword);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj)
            return true;

        if (obj == null || getClass() !=obj.getClass())
            return false;

        User user = (User) obj;
        return id == user.getId() &&
                Objects.equals(username, user.getUsername()) &&
                Objects.equals(name, user.getName()) &&
                Objects.equals(surname, user.getSurname()) &&
                Objects.equals(mail, user.getMail()) &&
                Objects.equals(phoneNumber, user.getPhoneNumber()) &&
                birthDate.equals(user.getBirthDate());
    }
}
