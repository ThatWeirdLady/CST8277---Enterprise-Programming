package org.ac.cst8277.hailey.jennifer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Member {
    @Id
    private String id;
    private String username;
    private String password;

    public Member() {
    }

    public Member(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
