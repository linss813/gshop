package com.linss.gshop.mapper;

import com.linss.gshop.entity.GoodsType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsTypeMapper {

    public List<GoodsType> getAllGoodsType();

    public GoodsType getGoodsTypeById(Integer tid);

    public int addGoodsType(GoodsType goodsType);

    public int updateGoodsType(GoodsType goodsType);

    public int deleteGoodsType(Integer tid);




//    public GoodsType addGoodsType(GoodsType goodsType);
//    public GoodsType updateGoodsType(GoodsType goodsType);
//    public GoodsType deleteGoodsType(Integer id);

}


