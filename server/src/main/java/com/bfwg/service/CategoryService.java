package com.bfwg.service;

import com.bfwg.model.Category;
import com.bfwg.model.Transaction;
import com.bfwg.model.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void save(Category category);
    void deleteAll();
}
