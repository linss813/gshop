package com.linss.gshop.controller;

import com.linss.gshop.entity.Banner;
import com.linss.gshop.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/banner")
@CrossOrigin
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/getAllBanner")
    public List<Banner> getAllBanner() {
        return bannerService.getAllBanner();
    }

    @GetMapping("/getBannerById/{bid}")
    public Banner getBannerById(@PathVariable Integer bid) {
        return bannerService.getBannerById(bid);
    }

    @PostMapping(value = "/addBanner")
    public String addBanner(@RequestBody Banner banner) {
        return bannerService.addBanner(banner);
    }

    @PutMapping(value = "/updateBanner")
    public String updateBanner(@RequestBody Banner banner) {
        return bannerService.updateBanner(banner);
    }

    @DeleteMapping("/deleteBanner/{bid}")
    public String deleteBanner(@PathVariable Integer bid) {
        return bannerService.deleteBanner(bid);
    }

}
