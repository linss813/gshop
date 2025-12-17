package com.linss.gshop.controller;

import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Goods;
import com.linss.gshop.service.FileUploadService;
import com.linss.gshop.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping(value = "/goods")
@CrossOrigin
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${image.base.url}")
    private String imageBaseUrl;


    @GetMapping("/getAllGoods")
    public PageInfo<Goods> getAllGoods(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        return goodsService.getAllGoods(pageNum, pageSize);
    }

    @GetMapping("/getAllGoodsCount")
    public int getAllGoodsCount() {
        return goodsService.getAllGoodsCount();
    }

    @GetMapping("/getGoodsById/{gid}")
    public Goods getGoodsById(@PathVariable Integer gid) {
        return goodsService.getGoodsById(gid);
    }

    @GetMapping("/getGoodsByName")
    public PageInfo<Goods> getGoodsByName(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String name) {
        return goodsService.getGoodsByName(name, pageNum, pageSize);
    }

    @GetMapping("/getGoodsByCid")
    public PageInfo<Goods> getGoodsByCid(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Integer cid) {
        return goodsService.getGoodsByCid(cid, pageNum, pageSize);
    }

    @GetMapping("/getByTid")
    public PageInfo<Goods> getGoodsByTid(
            @RequestParam("tids") @Param("tids") List<Integer> tids, // 明确指定参数名称
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return goodsService.getGoodsByTid(tids, pageNum, pageSize);
    }

    @GetMapping("/getWarningGoods")
    public List<Goods> getWarningGoods() {
        return goodsService.getWarningGoods();
    }

    @PostMapping(value = "/addGoods", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addGoods(@RequestBody Goods goods) {
        System.out.println("Received Goods: " + goods);
        return goodsService.addGoods(goods);
    }

    @PutMapping(value = "/updateGoods", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateGoods(@RequestBody Goods goods) {
        return goodsService.updateGoods(goods);
    }

    @DeleteMapping("/deleteGoods/{gid}")
    public String deleteGoods(@PathVariable Integer gid) {
        return goodsService.deleteGoods(gid);
    }

    @PostMapping("/file/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = fileUploadService.uploadFile(file);
        if (fileName != null) {
            String imageUrl = imageBaseUrl + fileName;
            return "{\"url\": \"" + imageUrl + "\"}";
        }
        return "{\"error\": \"上传失败\"}";
    }

}

