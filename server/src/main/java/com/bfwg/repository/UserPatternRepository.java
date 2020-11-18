package com.bfwg.repository;

import com.bfwg.model.User;
import com.bfwg.model.UserPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPatternRepository extends JpaRepository<UserPattern, Long> {
    List<UserPattern> findAllByUser(User user);
}

