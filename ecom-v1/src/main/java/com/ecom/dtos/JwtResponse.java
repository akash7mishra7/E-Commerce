package com.ecom.dtos;

import java.util.Set;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer userId;
    private String name;
    private Set<String> roles;

    public JwtResponse() {}

    public JwtResponse(String token, Integer uid, String name, Set<String> roles) {
        this.token = token;
        this.userId = uid;
        this.name = name;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}
