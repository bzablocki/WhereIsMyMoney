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

        Category c1 = new Category();
        c1.setName("groceries");
        categoryService.save(c1);

        Pattern p1 = new Pattern();
        p1.setCategory(c1);
        p1.setPattern("%Albert%Heijn");
        patternService.save(p1);

        user.getPatterns().add(p1);
        userService.update(user);

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
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
