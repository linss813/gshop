package com.linss.gshop.controller;

import com.linss.gshop.entity.GoodsType;
import com.linss.gshop.service.GoodsTypeService;
import com.linss.gshop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/goodstype")
public class GoodsTypeController {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @GetMapping("/getAllGoodsType")
    public List<GoodsType> getAllGoodsType() {
        return goodsTypeService.getAllGoodsType();
    }

    @GetMapping("/getGoodsTypeById/{tid}")
    public GoodsType getGoodsTypeById(@PathVariable Integer tid) {
        return goodsTypeService.getGoodsTypeById(tid);
    }

    @PostMapping("/addGoodsType")
    public String addGoodsType(@RequestBody GoodsType goodsType) {
        try {
            return goodsTypeService.addGoodsType(goodsType);
        } catch (Exception e) {
            return "添加失败: " + e.getMessage();
        }
    }

    @PutMapping("/updateGoodsType")
    public String updateGoodsType(@RequestBody GoodsType goodsType) {
        try {
            return goodsTypeService.updateGoodsType(goodsType);
        } catch (Exception e) {
            return "更新失败: " + e.getMessage();
        }
    }

    @DeleteMapping("/deleteGoodsType")
    public String deleteGoodsType(@RequestParam Integer tid) {
        try {
            return goodsTypeService.deleteGoodsType(tid);
        } catch (Exception e) {
            return "删除失败: " + e.getMessage();
        }
    }





}
