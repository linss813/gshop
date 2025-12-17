package com.linss.gshop.service;

import com.linss.gshop.entity.GoodsType;

import java.util.List;

public interface GoodsTypeService {

    List<GoodsType> getAllGoodsType();

    GoodsType getGoodsTypeById(Integer tid);

    String addGoodsType(GoodsType goodsType);

    String updateGoodsType(GoodsType goodsType);

    String deleteGoodsType(Integer tid);


//    GoodsType addGoodsType(GoodsType goodsType);
//
//    GoodsType updateGoodsType(GoodsType goodsType);
//
//    GoodsType deleteGoodsType(Integer id);

}
