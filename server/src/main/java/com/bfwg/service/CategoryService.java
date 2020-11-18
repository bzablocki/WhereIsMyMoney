package com.bfwg.service;

import com.bfwg.model.Category;
import com.bfwg.model.Transaction;
import com.bfwg.model.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    void save(Category category);
    void deleteAll();
    Category findUnknownCategory();
}
