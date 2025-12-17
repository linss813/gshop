package com.linss.gshop.service;

import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Goods;

import java.util.List;

public interface GoodsService {

    PageInfo<Goods> getAllGoods(int pageNum, int pageSize);

    Goods getGoodsById(Integer gid);

    // 根据商品名称搜索商品-模糊搜索
    PageInfo<Goods> getGoodsByName(String name, int pageNum, int pageSize);

    PageInfo<Goods> getGoodsByCid(Integer cid, int pageNum, int pageSize);

    PageInfo<Goods> getGoodsByTid(List<Integer> tids, int pageNum, int pageSize);

    String addGoods(Goods goods);

    String updateGoods(Goods goods);

    String deleteGoods(Integer gid);

    void updateStock(Goods goods);

    int getAllGoodsCount();

    List<Goods> getWarningGoods();
}


