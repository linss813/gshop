package com.linss.gshop.service;

import com.linss.gshop.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> getAllCategory();

    Category getCategoryById(Integer cid);

    String addCategory(Category category);

    String updateCategory(Category category);

    String deleteCategory(Integer cid);

    List<Map<String, Object>> getCategoryCounts();
}
