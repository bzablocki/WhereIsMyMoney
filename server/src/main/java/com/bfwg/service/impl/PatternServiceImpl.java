package com.bfwg.service.impl;

import com.bfwg.model.Category;
import com.bfwg.model.Pattern;
import com.bfwg.model.User;
import com.bfwg.model.UserPattern;
import com.bfwg.repository.CategoryRepository;
import com.bfwg.repository.PatternRepository;
import com.bfwg.repository.UserPatternRepository;
import com.bfwg.service.CategoryService;
import com.bfwg.service.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fan.jin on 2016-10-15.
 */

@Transactional
@Service
public class PatternServiceImpl implements PatternService {

    private final PatternRepository patternRepository;
    private final UserPatternRepository userPatternRepository;


    @Autowired
    public PatternServiceImpl(PatternRepository patternRepository, UserPatternRepository userPatternRepository) {
        this.patternRepository = patternRepository;
        this.userPatternRepository = userPatternRepository;
    }

    @Override
    public void save(Pattern pattern) {
        patternRepository.save(pattern);
    }


    @Override
    public void deleteAll() {
        patternRepository.deleteAll();
    }

    @Override
    public List<Pattern> findAll(User user) {
        List<UserPattern> allUserPatterns = userPatternRepository.findAllByUser(user);
        return allUserPatterns.stream().map(UserPattern::getPattern).collect(Collectors.toList());
    }
}
