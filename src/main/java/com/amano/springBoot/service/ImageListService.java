package com.amano.springBoot.service;

import com.amano.springBoot.dao.ImageListRepo;
import com.amano.springBoot.entity.ImageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageListService {
    @Autowired
    ImageListRepo imageListRepo;

    @Transactional
    public List<ImageList> getImageList(int pageIndex, int pageSize, int type) {
        return imageListRepo.getImageList(pageIndex, pageSize, type);
    }

    @Transactional
    public void addImageList(List<ImageList> imageListList) {
        imageListRepo.addImageList(imageListList);
    }

    public String getPages(int type, int pageSize) {
        return imageListRepo.getPages(type, pageSize);
    }

    @Transactional
    public ImageList dellist(List<String> ids){
        return imageListRepo.dellist(ids);
    }
}
