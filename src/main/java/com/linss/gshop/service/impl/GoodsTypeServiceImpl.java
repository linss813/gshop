package com.linss.gshop.service.impl;

import com.linss.gshop.entity.GoodsType;
import com.linss.gshop.mapper.GoodsTypeMapper;
import com.linss.gshop.service.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public List<GoodsType> getAllGoodsType() {
        return goodsTypeMapper.getAllGoodsType();
    }

    @Override
    public GoodsType getGoodsTypeById(Integer tid) {
        return goodsTypeMapper.getGoodsTypeById(tid);
    }

    @Override
    public String addGoodsType(GoodsType goodsType) {
        goodsTypeMapper.addGoodsType(goodsType);
        return "添加成功";
    }

    @Override
    public String updateGoodsType(GoodsType goodsType) {
        if (goodsTypeMapper.getGoodsTypeById(goodsType.getTid()) == null) {
            return "不存在";
        }
        goodsTypeMapper.updateGoodsType(goodsType);
        return "更新成功";
    }

    @Override
    public String deleteGoodsType(Integer tid) {
        if (goodsTypeMapper.getGoodsTypeById(tid) == null) {
            return "不存在";
        }
        goodsTypeMapper.deleteGoodsType(tid);
        return "删除成功";
    }

}
