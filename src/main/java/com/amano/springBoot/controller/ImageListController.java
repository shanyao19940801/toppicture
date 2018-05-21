package com.amano.springBoot.controller;

import com.amano.springBoot.controller.input.LoveInput;
import com.amano.springBoot.controller.output.Msg;
import com.amano.springBoot.entity.ImageList;
import com.amano.springBoot.service.ImageListService;
import com.amano.springBoot.service.LoveService;
import com.amano.springBoot.service.ValidationService;
import com.amano.springBoot.util.ErrorCode;
import com.amano.springBoot.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/image")
@CrossOrigin
public class ImageListController {

    @Autowired
    ValidationService validationService;

    @Autowired
    LoveService loveService;

    @Resource
    ImageListService imageListService;

    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public Msg getImageList(HttpServletRequest request) {
        String sIndex = request.getParameter("pageIndex");
        String sSize = request.getParameter("pageSize");
        String sType = request.getParameter("type");
        if(sIndex == null || sSize == null) {
            return ResultUtil.error(1,"参数错误");
        }
        int pageIndex = Integer.parseInt(sIndex);
        int pageSize = Integer.parseInt(sSize);
        int type = 0;
        if(sType != null) {
           type = Integer.parseInt(sType);
        }

        return ResultUtil.success(imageListService.getImageList(pageIndex, pageSize, type));
    }

    @RequestMapping(value = "/addList", method = RequestMethod.POST)
    public Msg addImageList(@RequestBody List<ImageList> imageListList) {
        System.out.println("addList post receive " + imageListList.toString());
        if(imageListList.size() == 0) {
            return ResultUtil.error(2,"list不能为空");
        }
        imageListService.addImageList(imageListList);
        return ResultUtil.success();

    }

    @RequestMapping(value = "/getPages", method = RequestMethod.GET)
    public Msg getPages(@RequestParam(value="type",defaultValue="0", required =false) int type ,
                        @RequestParam(value="pageSize",defaultValue="10", required =false) int pageSize ) {
        System.out.println("getPages post receive type" + type +"<<");
        System.out.println("getPages post receive pageSize" + pageSize +"<<");
        return ResultUtil.success(imageListService.getPages(type, pageSize ));

    }

    @RequestMapping(value = "/deleteList",  method = RequestMethod.POST)
    public Msg deleteImageList(@RequestBody List<String> list){
        System.out.println("id  is :"+list.toString());
        if(list ==null||list.size() ==0) {
            return ResultUtil.error(2, "ID不能为空！");
        }
        imageListService.dellist(list);
        return ResultUtil.success();
    }

    @PostMapping(value = "/love")
    public Msg love(@RequestBody @Valid LoveInput loveInput, BindingResult bindingResult){

        validationService.checkBindingResult(bindingResult, ErrorCode.VALIDATE_ERROR);

        return loveService.love(loveInput);
    }
}