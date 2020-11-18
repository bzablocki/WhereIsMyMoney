package com.bfwg.service;

import com.bfwg.model.Pattern;
import com.bfwg.model.User;

import java.util.List;

public interface PatternService {
    void save(Pattern pattern);
    void deleteAll();
    List<Pattern> findAll(User user);
}
