package com.amano.springBoot.dao;

import com.amano.springBoot.entity.ImageList;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ImageListRepo {
    //springboot会默认自动将数据源中的配置注入,用法与hibernate中sessionFactory生成的session类似。以后使用多数据源时会详细解释
    @PersistenceContext
    EntityManager entityManager;

    public List<ImageList> getImageList(int pageIndex, int pageSize, int type) {
        System.out.println("getImageList pageIndex is " + pageIndex + " pageSize is " + pageSize + " type is:" + type +"<<" );
        String sType = "";
        if(type != 0) {
            sType = " where i .type=" + type;
        }
        StringBuffer sql = new StringBuffer( "SELECT i FROM ImageList i" + sType + " order by id  desc");

        List<ImageList> resultList = entityManager.createQuery(sql.toString())
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        if (resultList.size() == 0) {
            return null;
        }
        System.out.println("list" + resultList.toString() );
        return  resultList;
    }

    public void addImageList(List<ImageList> imageListList) {
        try {
            for(int i= 0; i<imageListList.size(); i++) {
                ImageList imageList = imageListList.get(i);
                entityManager.persist(imageList);
            }
        } catch (Exception e) {
            System.out.print("exxxxx");
            e.printStackTrace();
        }
        System.out.print("addImageList over length is " + imageListList.size());
    }

    public String getPages(int type, int pageSize) {
        System.out.println("getPages type is " + type + " pageSize is " + pageSize + "<<" );
        String sType = "";
        if(type != 0) {
            sType = " where i .type=" + type;
        }
        StringBuffer sql = new StringBuffer( "SELECT COUNT(i) FROM ImageList i" + sType);

        List list = entityManager.createQuery(sql.toString()).getResultList();
        int pages =Integer.parseInt(list.get(0).toString()) / pageSize;
        return  pages + "";
    }

    public ImageList dellist(List<String> ids){
        for(int i = 0;i<ids.size();i++){
            String id = ids.get(i);
            ImageList tempList = entityManager.find(ImageList.class,id);
            if (tempList == null||tempList.getId() == null) {
                System.out.println("此ID"+id+"不存在");
            }else{
                entityManager.remove(tempList);
                deleteImageDetail(id);
            }
        }
        return null;
    }

    /**
     * 删除detail表中字段image_list_id为id的数据
     * @param id
     */
    public void deleteImageDetail(String id){
        Query query = entityManager.createNativeQuery("DELETE FROM image_detail  where image_list_id="+id);
        int delnum = query.executeUpdate();
        System.out.println("删除detail记录条数：" + delnum);
    }
}