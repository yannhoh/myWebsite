package ch.mywebsite.yannhoh.user;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

enum Role {
    ADMIN, USER;
}

@Entity
@Table(name = "accounts")
public class User {

    @org.springframework.data.annotation.Id
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private Date createdOn;

    protected User() {}
    //Every new Users' role is set as "USER"
    public User(String username, String password, String email) {
        role = Role.USER;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdOn = Date.valueOf(LocalDate.now());
    }

    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public String getEmail() {
        return email;
    }

    public Role getRole() { return role; }

    public void setId(Long id) {
        //this.id = id;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

}
