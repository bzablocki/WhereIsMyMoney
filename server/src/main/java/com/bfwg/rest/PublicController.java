package com.bfwg.rest;

import com.bfwg.model.Category;
import com.bfwg.model.Pattern;
import com.bfwg.model.User;
import com.bfwg.model.UserPattern;
import com.bfwg.service.CategoryService;
import com.bfwg.service.PatternService;
import com.bfwg.service.UserPatternService;
import com.bfwg.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fan.jin on 2017-05-08.
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final PatternService patternService;
    private final UserPatternService userPatternService;

    @Autowired
    public PublicController(UserService userService,
                            CategoryService categoryService,
                            PatternService patternService,
                            UserPatternService userPatternService
    ) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.patternService = patternService;
        this.userPatternService = userPatternService;
    }

    @RequestMapping(method = GET, value = "/foo")
    public Map<String, String> getFoo() {
        Map<String, String> fooObj = new HashMap<>();
        fooObj.put("foo", "bar");
        return fooObj;
    }




    @RequestMapping(path = "/getInitCategories", method = GET)
    public ResponseEntity<Boolean> getInitCategories() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Category> existingCategories = categoryService.findAll();

        Category groceriesCategory = getDBCategory(existingCategories, "groceries");
        Category sportCategory = getDBCategory(existingCategories, "sport");
        Category salaryCategory = getDBCategory(existingCategories, "salary");

        List<Pattern> existingPatterns = patternService.findAll(user);
        Pattern ahPattern = getDBPattern(existingPatterns, groceriesCategory, "%Albert%Heijn%");
        Pattern dirkPattern = getDBPattern(existingPatterns, groceriesCategory, "%DIRK%VDBROEK%");
        Pattern sportPattern = getDBPattern(existingPatterns, sportCategory, "%BOLDER%NEOLIET%");
        Pattern salaryPattern = getDBPattern(existingPatterns, salaryCategory, "%CYGNIFY%");

        user.getPatterns().add(ahPattern);
        user.getPatterns().add(dirkPattern);
        user.getPatterns().add(sportPattern);
        user.getPatterns().add(salaryPattern);
        userService.update(user);

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    private Pattern getDBPattern(List<Pattern> existingPatterns, Category category, String patternName) {
        Optional<Pattern> matchedCategoryInDBOpt = existingPatterns.stream()
                .filter(pattern -> pattern.getPattern().equals(patternName))
                .findAny();

        Pattern pattern = null;
        if (!matchedCategoryInDBOpt.isPresent()) {
            Pattern patternToSave = new Pattern();
            patternToSave.setPattern(patternName);
            patternToSave.setCategory(category);
            patternService.save(patternToSave);
            pattern = patternToSave;
        }
        return pattern;
    }

    private Category getDBCategory(List<Category> existingCategories, String categoryName) {
        Optional<Category> matchedCategoryInDBOpt = existingCategories.stream()
                .filter(category -> category.getName().equals(categoryName))
                .findAny();

        Category category = null;
        if (!matchedCategoryInDBOpt.isPresent()) {
            Category categoryToSave = new Category();
            categoryToSave.setName(categoryName);
            categoryService.save(categoryToSave);
            category = categoryToSave;
        }
        return category;
    }


    @RequestMapping(path = "/getDeleteAllCategories", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> getDeleteAllCategories() {
        userPatternService.deleteAll();
        patternService.deleteAll();
        categoryService.deleteAll();

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }


}
