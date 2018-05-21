package com.amano.springBoot.controller;

import com.amano.springBoot.controller.output.Msg;
import com.amano.springBoot.controller.output.WiselyResponse;
import com.amano.springBoot.entity.User;
import com.amano.springBoot.service.UserService;
import com.amano.springBoot.service.ValidationService;
import com.amano.springBoot.service.WebSocketService;
import com.amano.springBoot.util.EncrypAES;
import com.amano.springBoot.util.ResultUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;


@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {

    private static final Log logger = LogFactory.getLog(UserController.class);
    private EncrypAES aesEncryp = new EncrypAES();

    @Autowired
    ValidationService validationService;

    @Resource
    UserService userService;

    @Resource
    WebSocketService webSocketService;

    @PersistenceContext
    EntityManager entityManager;

    public UserController() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Msg register(User req) {
        String username = req.getUsername();
        String password = req.getPassword();
        String mail = req.getMail();
        logger.info("name is " + username + "pass is " + password + "mail is" + mail);
        if (username == null || password == null || mail == null) {
            return ResultUtil.error(1, "参数错误");
        }
        return userService.register(username, password, mail);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public Msg active(User req) {
        String code = req.getCode();
        if (code == null) {
            return ResultUtil.error(1, "参数错误");
        }
        if (userService.active(code)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(5, "激活码失效");
        }
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public Msg version(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie[] cookies = request.getCookies();
        HttpSession ses = request.getSession();
        Object count=ses.getServletContext().getAttribute("count");
        HashMap map = new HashMap();
        String uName = "guest";
        map.put("onlineCount",count);
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberMe")) {
                    try {
                        String remberMe = new String(aesEncryp.Decryptor(aesEncryp.hexStr2ByteArr(cookie.getValue())));
                        String[] arr = remberMe.split("\\|");
                        logger.info("version remberMe is --->" + remberMe);
                        HttpSession session = request.getSession();
                        session.setAttribute("uname", arr[0]);
                        session.setAttribute("upass", arr[1]);
                        uName = arr[0];
                    }catch (Exception e){
                        Cookie a = new Cookie("rememberMe","");
                        response.addCookie(a);
                    }
                }
            }
        }
        String dbcount = userService.dbCount(uName);
        int sumCount = Integer.parseInt(dbcount.split("\\|")[0]);
        int userLoginCount = Integer.parseInt(dbcount.split("\\|")[1]);
        map.put("dbCount",sumCount);
        webSocketService.sendMsg(new WiselyResponse("version",map));
        map.put("name",uName);
        map.put("selfCount",userLoginCount);
        return ResultUtil.success(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Msg login(User req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = req.getUsername();
        String pass = req.getPassword();
        if (name == null || pass == null) {
            return ResultUtil.error(1, "参数错误");
        }
        int re = userService.login(name, pass);
        switch (re) {
            case 0:
                return ResultUtil.error(6, "用户名不存在!");
            case -1:
                return ResultUtil.error(7, "密码错误!请重新输入!");
            case -2:
                return ResultUtil.error(8, "该用户邮箱尚未激活!");
            case 1:
                HttpSession session = request.getSession();
                String sessionId = session.getId();
                session.setAttribute("uname", name);
                session.setAttribute("upass", pass);
//                session.setMaxInactiveInterval(300);
                //判断session是不是新创建的
                if (session.isNew()) {
                    logger.info("session创建成功，session的id是：" + sessionId);
                } else {
                    logger.info("服务器已经存在该session了，session的id是：" + sessionId);
                }
                //创建cookie
//                String MD5Cookies = MD5Util.string2MD5(name + "|" + pass);
                String remberMe =  aesEncryp.byteArr2HexStr(aesEncryp.Encrytor(name + "|" + pass));
                logger.info("nama and pass is  " + name + "|" + pass);
                logger.info("create remberMe " + remberMe);
                Cookie cookie = new Cookie("rememberMe", remberMe);
                cookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(cookie);
                return ResultUtil.success();
            default:
                return ResultUtil.error(1, "default error!");
        }
    }

    @Value("${activeAddress}")
    private String address;

    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    public Msg alipay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //商户订单号，需要保证不重复
        String out_trade_no = UUID.randomUUID().toString().replace("-", "");
        //订单金额
        String amount = request.getParameter("amount");
        //订单标题
        String subject = request.getParameter("subject");
        //商户门店编号
        String store_id = "CX_101";
        //交易超时时间
        String timeout_express = "30";
        String re = userService.createOrder(out_trade_no, amount, subject, store_id, timeout_express);
        if (!re.equals("-1")) {
            re = re.split("web")[1];
            return ResultUtil.success(address + re);
        } else {
            return ResultUtil.error(1, "创建订单失败");
        }
    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public String notif(HttpServletRequest request) {
        logger.info("收到通知！！！！");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        logger.info("订单号：" + out_trade_no);
        logger.info("交易状态：" + trade_status);
        return "success";
    }
}