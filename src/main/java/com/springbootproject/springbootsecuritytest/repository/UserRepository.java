package com.springbootproject.springbootsecuritytest.repository;

import com.springbootproject.springbootsecuritytest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
