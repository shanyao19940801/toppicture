package com.amano.springBoot.controller;

import com.amano.springBoot.controller.input.DanmakuInput;
import com.amano.springBoot.controller.input.LoveInput;
import com.amano.springBoot.controller.output.Msg;
import com.amano.springBoot.entity.Danmaku;
import com.amano.springBoot.entity.ImageDetail;
import com.amano.springBoot.service.DanmakuService;
import com.amano.springBoot.service.ImageDetailService;
import com.amano.springBoot.service.LoveService;
import com.amano.springBoot.service.ValidationService;
import com.amano.springBoot.util.Access;
import com.amano.springBoot.util.ErrorCode;
import com.amano.springBoot.util.ResultUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/image")
@CrossOrigin
public class ImageDetailController {
    @Autowired
    ValidationService validationService;

    @Resource
    ImageDetailService imageDetailService;

    @Autowired
    DanmakuService danmakuService;


    private static final Log logger = LogFactory.getLog(ImageDetailController.class);

    @Access
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    public Msg getImageDetail(HttpServletRequest request) {
        logger.info("getImageDetail开始");
        String id = request.getParameter("id");
        String flag = request.getAttribute("flag").toString();
        if(id == null) {
            return ResultUtil.error(3,"id不能为空");
        }
        return ResultUtil.success(imageDetailService.getImageDetail(id,flag));
    }

    @RequestMapping(value = "/addDetail",  method = RequestMethod.POST)
    public Msg addImageDetail(@RequestBody List<ImageDetail> imageDetailList) {
        logger.info("addImageDetail开始");
        if(imageDetailList.size() == 0) {
            return ResultUtil.error(2,"list不能为空");
        }
        imageDetailService.addImageList(imageDetailList);
        return ResultUtil.success();
    }

    @PostMapping(value = "/danmaku")
    public Msg danmaku(@RequestBody @Valid DanmakuInput input, BindingResult bindingResult){

        validationService.checkBindingResult(bindingResult, ErrorCode.VALIDATE_ERROR);

        return danmakuService.comment(input);
    }


}