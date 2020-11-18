package com.bfwg.repository;

import com.bfwg.model.Category;
import com.bfwg.model.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternRepository extends JpaRepository<Pattern, Long> {

}

