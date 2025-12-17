package com.linss.gshop.service;

import com.linss.gshop.entity.Banner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {

    List<Banner> getAllBanner();

    Banner getBannerById(Integer bid);

    String addBanner(Banner banner);

    String updateBanner(Banner banner);

    String deleteBanner(Integer bid);

}


