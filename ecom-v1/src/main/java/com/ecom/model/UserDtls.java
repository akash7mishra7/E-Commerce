package com.ecom.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String mobileNumber;

    private String email;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String password;

    private String profileImage;

    private String role;

    // ✅ defaults for security flags
    @Column(nullable = false)
    private Boolean isEnable = true;

    @Column(nullable = false)
    private Boolean accountNonLocked = true;

    private Integer failedAttempt = 0;

    private Date lockTime;

    private String resetToken;

    // Optional safeguard for nulls
    public boolean getAccountNonLockedSafe() {
        return accountNonLocked != null ? accountNonLocked : true;
    }

    public boolean getIsEnableSafe() {
        return isEnable != null ? isEnable : true;
    }
}
