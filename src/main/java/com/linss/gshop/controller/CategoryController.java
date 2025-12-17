package com.linss.gshop.controller;

import com.linss.gshop.entity.Category;
import com.linss.gshop.service.CategoryService;
import com.linss.gshop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getAllCategory")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/getCategoryById/{cid}")
    public Category getCategoryById(@PathVariable Integer cid) {
            return categoryService.getCategoryById(cid);
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestBody Category category){
        try {
            return categoryService.addCategory(category);
        } catch (Exception e) {
            return "添加失败:" + e.getMessage();
        }
    }

    @PutMapping("/updateCategory")
    public String updateCategory(@RequestBody Category category) {
        try {
            return categoryService.updateCategory(category);
        } catch (Exception e) {
            return "更新失败:" + e.getMessage();
        }
    }

    @DeleteMapping("/deleteCategory/{cid}")
    public String deleteCategory(@PathVariable Integer cid) {
        try {
            return categoryService.deleteCategory(cid);
        } catch (Exception e) {
            return "删除失败:" + e.getMessage();
        }
    }

    @GetMapping("/getCategoryCounts")
    public List<Map<String, Object>> getCategoryCounts() {
        return categoryService.getCategoryCounts();
    }
}
