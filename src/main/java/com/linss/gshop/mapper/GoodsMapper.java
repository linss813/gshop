package com.linss.gshop.mapper;

import com.linss.gshop.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<Goods> getAllGoods();

    Goods getGoodsById(Integer gid);

    List<Goods> getGoodsByName(String name);

    List<Goods> getGoodsByCid(Integer cid);

    List<Goods> getGoodsByTid(@Param("tids") List<Integer> tids);

    void addGoods(Goods goods);

    void updateGoods(Goods goods);

    void deleteGoods(Integer gid);

    void updateStock(Goods goods);

    int getAllGoodsCount();

    List<Goods> getWarningGoods();
}
