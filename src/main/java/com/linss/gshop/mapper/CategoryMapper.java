package com.linss.gshop.mapper;

import com.linss.gshop.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {

    List<Category> getAllCategory();

    Category getCategoryById(Integer cid);

    int addCategory(Category category);

    int updateCategory(Category category);

    int deleteCategory(Integer cid);

    List<Map<String, Object>> getCategoryCounts();

    @Select("SELECT * FROM goods WHERE cid=#{cid}")
    Collection<Object> getAllGoodsByCategoryId(Integer cid);
}
