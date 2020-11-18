package com.bfwg.service.impl;

import com.bfwg.model.UserPattern;
import com.bfwg.repository.UserPatternRepository;
import com.bfwg.service.UserPatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by fan.jin on 2016-10-15.
 */

@Service
public class UserPatternServiceImpl implements UserPatternService {

    private final UserPatternRepository userPatternRepository;


    @Autowired
    public UserPatternServiceImpl(UserPatternRepository userPatternRepository) {
        this.userPatternRepository = userPatternRepository;
    }

    @Override
    public void save(UserPattern userPattern) {
        userPatternRepository.save(userPattern);
    }

//    @Transactional
    @Override
    public void deleteAll() {
        userPatternRepository.deleteAll();
    }
}
