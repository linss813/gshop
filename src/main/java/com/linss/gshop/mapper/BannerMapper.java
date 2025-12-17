package com.linss.gshop.mapper;

import com.linss.gshop.entity.Banner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper {

    List<Banner> getAllBanner();

    Banner getBannerById(Integer bid);

    int addBanner(Banner banner);

    int updateBanner(Banner banner);

    int deleteBanner(Integer bid);
}
