package com.linss.gshop.service.impl;

import com.linss.gshop.entity.Banner;
import com.linss.gshop.mapper.BannerMapper;
import com.linss.gshop.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<Banner> getAllBanner() {
        return bannerMapper.getAllBanner();
    }

    @Override
    public Banner getBannerById(Integer bid) {
        return bannerMapper.getBannerById(bid);
    }

    @Override
    public String addBanner(Banner banner) {
       if (bannerMapper.addBanner(banner) > 0) {
           return "success";
       } else {
           return "error";
       }

    }

    @Override
    public String updateBanner(Banner banner) {
        if (bannerMapper.updateBanner(banner) > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public String deleteBanner(Integer bid) {
        if (bannerMapper.deleteBanner(bid) > 0) {
            return "success";
        } else {
            return "error";
        }
    }

}