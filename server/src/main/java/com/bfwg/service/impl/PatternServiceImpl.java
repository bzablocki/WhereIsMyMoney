package com.bfwg.service.impl;

import com.bfwg.model.Category;
import com.bfwg.model.Pattern;
import com.bfwg.repository.CategoryRepository;
import com.bfwg.repository.PatternRepository;
import com.bfwg.service.CategoryService;
import com.bfwg.service.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fan.jin on 2016-10-15.
 */

@Service
public class PatternServiceImpl implements PatternService {

    private final PatternRepository patternRepository;


    @Autowired
    public PatternServiceImpl(PatternRepository patternRepository) {
        this.patternRepository = patternRepository;
    }

    @Override
    public void save(Pattern pattern) {
        patternRepository.save(pattern);
    }

    @Override
    public void deleteAll() {
        patternRepository.deleteAll();
    }
}
