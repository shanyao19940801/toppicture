package com.amano.springBoot.dao;

import com.amano.springBoot.entity.ImageDetail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImageDetailRepo {
    private static final Log logger = LogFactory.getLog(ImageDetailRepo.class);

    @PersistenceContext
    EntityManager entityManager;

    public List<ImageDetail> getImageDetail(String id, String flag) {

        StringBuffer sql = new StringBuffer( "SELECT i FROM ImageDetail i where i .imageListId=" + id);
        List<ImageDetail> resultList;
        if (flag.equals("0")){
            resultList = entityManager.createQuery(sql.toString())
                    .setFirstResult(0).setMaxResults(5)
                    .getResultList();
        }else{
            resultList = entityManager.createQuery(sql.toString())
                    .setFirstResult(0)
                    .getResultList();
        }
        if (resultList.size() == 0) {
            return null;
        }
        return resultList;
    }

    public void addImageDetail(List<ImageDetail> imageDetailList) {
        try {
            for (int i = 0; i < imageDetailList.size(); i++) {
                entityManager.persist(imageDetailList.get(i));
            }
        } catch (Exception e) {
            logger.debug("exxxxx");
            e.printStackTrace();
        }
        logger.info("addImageList over length is " + imageDetailList.size());
    }


}
