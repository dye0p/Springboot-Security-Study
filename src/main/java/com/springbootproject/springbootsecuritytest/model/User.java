package com.springbootproject.springbootsecuritytest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    private String provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public User(Timestamp createDate, String email, String password, String provider, String providerId, String role, String username) {
        this.createDate = createDate;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.username = username;
    }
}

