package com.amano.springBoot.service;

import com.amano.springBoot.dao.ImageDetailRepo;
import com.amano.springBoot.entity.ImageDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageDetailService {

    @Autowired
    ImageDetailRepo imageDetailRepo;

    @Transactional
    public List<ImageDetail> getImageDetail(String id, String flag) {
        return imageDetailRepo.getImageDetail(id, flag);
    }

    @Transactional
    public void addImageList(List<ImageDetail> imageDetailList) {
        imageDetailRepo.addImageDetail(imageDetailList);
    }


}
