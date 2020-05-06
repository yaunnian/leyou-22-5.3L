package com.leyou.entity;

public class UserInfo1 {
    private Long id;

    private String username;

    public UserInfo1() {
    }

    public UserInfo1(Long id, String username) {
        this.id = id;
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "UserInfo1{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
