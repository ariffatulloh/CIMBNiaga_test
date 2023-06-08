package com.example.cimbniaga_test.model;
import javax.persistence.*;

@Entity
public class UserAuth {
    //set id auto increment

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;

    public UserAuth() {
    }

    public UserAuth(String username, String password ) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
          return username;
     }
     public void setUsername(String username) {
          this.username = username;
     }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
