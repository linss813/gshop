package com.linss.gshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linss.gshop.entity.Goods;
import com.linss.gshop.entity.Review;
import com.linss.gshop.mapper.GoodsMapper;
import com.linss.gshop.mapper.ReviewMapper;
import com.linss.gshop.service.GoodsService;
import com.linss.gshop.service.RedisCacheService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Resource
    private RedisCacheService redisCacheService;

    private static final String GOODS_CACHE_KEY_PREFIX = "goods:";

    @Override
    public PageInfo<Goods> getAllGoods(int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询数据
        List<Goods> goodsList = goodsMapper.getAllGoods();
        // 返回分页结果
        return new PageInfo<>(goodsList);
    }

    @Override
    public Goods getGoodsById(Integer gid) {
        String cacheKey = GOODS_CACHE_KEY_PREFIX + gid;
        // 从缓存中获取商品信息
        Goods goods = (Goods) redisCacheService.get(cacheKey);
        if (goods == null) {
            // 如果缓存中没有，则从数据库中查询
            goods = goodsMapper.getGoodsById(gid);
            if (goods != null) {
                // 将查询结果放入缓存，设置过期时间为 30 分钟
                redisCacheService.set(cacheKey, goods, 1800);
            }
        }
        return goods;
    }

    @Override
    public PageInfo<Goods> getGoodsByName(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsMapper.getGoodsByName(name);
        return new PageInfo<>(goodsList);
    }


    @Override
    public PageInfo<Goods> getGoodsByCid(Integer cid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsMapper.getGoodsByCid(cid);
        return new PageInfo<>(goodsList);
    }

    @Override
    public PageInfo<Goods> getGoodsByTid(@Param("tids") List<Integer> tids, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsMapper.getGoodsByTid(tids);
        return new PageInfo<>(goodsList);
    }

    @Override
    public String addGoods(Goods goods) {
        if (goods.getIsShelved() == null) {
            goods.setIsShelved(false);
        }
        goodsMapper.addGoods(goods);
        // 添加商品后清除缓存
        redisCacheService.delete(GOODS_CACHE_KEY_PREFIX + goods.getGid());
        return "添加成功";
    }

    @Override
    public String updateGoods(Goods goods) {
        if (goodsMapper.getGoodsById(goods.getGid()) == null){
            return "商品不存在";
        }
        goodsMapper.updateGoods(goods);
        // 更新商品后清除缓存
        redisCacheService.delete(GOODS_CACHE_KEY_PREFIX + goods.getGid());
        return "修改成功";
    }

    @Override
    public String deleteGoods(Integer gid) {
        if (goodsMapper.getGoodsById(gid) == null){
            return "商品不存在";
        }
        goodsMapper.deleteGoods(gid);
        // 删除商品后清除缓存
        redisCacheService.delete(GOODS_CACHE_KEY_PREFIX + gid);
        return "删除成功";
    }

    @Override
    public void updateStock(Goods goods) {
        goodsMapper.updateStock(goods);
        // 更新库存后清除缓存
        redisCacheService.delete(GOODS_CACHE_KEY_PREFIX + goods.getGid());
    }

    @Override
    public int getAllGoodsCount() {
        return goodsMapper.getAllGoodsCount();
    }

    @Override
    public List<Goods> getWarningGoods() {
        return goodsMapper.getWarningGoods();
    }
}