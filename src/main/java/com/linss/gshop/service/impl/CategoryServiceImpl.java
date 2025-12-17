package com.linss.gshop.service.impl;

import com.linss.gshop.entity.Category;
import com.linss.gshop.mapper.CategoryMapper;
import com.linss.gshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategory() {
        return categoryMapper.getAllCategory();
    }

    @Override
    public Category getCategoryById(Integer cid) {
        return categoryMapper.getCategoryById(cid);
    }

    @Override
    public String addCategory(Category category) {
        categoryMapper.addCategory(category);
        return "添加成功";
    }

    @Override
    public String updateCategory(Category category) {
        if (categoryMapper.getCategoryById(category.getCid()) == null){
            return "目标不存在";
        }
        categoryMapper.updateCategory(category);
        return "修改成功";
    }

    @Override
    public String deleteCategory(Integer cid) {
        //判断是否存在商品
        if (categoryMapper.getAllGoodsByCategoryId(cid).size() > 0){
            return "该类别下有商品，请先删除商品";
        }
        categoryMapper.deleteCategory(cid);
        return "删除成功";
    }

    @Override
    public List<Map<String, Object>> getCategoryCounts() {
        return categoryMapper.getCategoryCounts();
    }
}
