package com.amano.springBoot.dao;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.amano.springBoot.controller.output.Msg;
import com.amano.springBoot.entity.Love;
import com.amano.springBoot.entity.User;
import com.amano.springBoot.util.QRcodeService;
import com.amano.springBoot.util.ResultUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by beyondLi on 2017/7/25.
 */
@Repository
public class UserRepo {
    //springboot会默认自动将数据源中的配置注入,用法与hibernate中sessionFactory生成的session类似。以后使用多数据源时会详细解释
    @PersistenceContext
    EntityManager entityManager;

    private static final Log logger = LogFactory.getLog(UserRepo.class);

    public Msg register(String user, String password, String mail) {
        int result = 1;
        //判断邮箱是否已经被注册
        StringBuffer mailCheck = new StringBuffer("SELECT w FROM User w WHERE w.mail='"+mail+"'");
        List listMail = entityManager.createQuery(mailCheck.toString()).getResultList();
        StringBuffer nameCheck = new StringBuffer("SELECT n FROM User n WHERE n.username='"+user+"'");
        List listName = entityManager.createQuery(nameCheck.toString()).getResultList();
        if (listMail.size() != 0) {
            logger.info("该邮箱已被注册");
            result = 0;
        }else if (listName.size()!=0){
            logger.info("该用户名已被注册");
            result = -2;
        }else{
            String code= UUID.randomUUID().toString().replace("-", "");
            //添加用户
            Query query = entityManager.createNativeQuery("INSERT INTO user(username,password,mail,staus,code,count) " +
                    "VALUES('"+ user +"','"+ password+"','"+mail+"',0,'"+code+"',0)");
            query.executeUpdate();
            //向用户发送激活邮件
            if(sendMail(mail,code)){
                logger.info("邮件发送成功！！！");
            }else{
                logger.info("邮件发送失败！！！");
                result = -1;
            }
        }

        switch (result) {
            case 0:
                return ResultUtil.error(2, "该邮箱已被注册");
            case -2:
                return ResultUtil.error(3, "该用户名已被注册");
            case -1:
                return ResultUtil.error(4, "邮件发送失败");
            case 1:
                return ResultUtil.success();
            default:
                return null;
        }

    }
    /**
     * 发送激活邮件
     * @param to 收件人邮箱地址
     * @param code 激活码
     */
    @Value("${mailFrom}")
    private String account;
    @Value("${mailKey}")
    private String word;
    @Value("${activeAddress}")
    private String address;
    public boolean sendMail(String to, String code) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp" );
            props.setProperty("mail.smtp.host", "smtp.qq.com");
            props.setProperty("mail.smtp.auth", "true");
            final String smtpPort = "465";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);
            Session mailSession = Session.getInstance(props);

            MimeMessage msg = new MimeMessage(mailSession);
            //From: 发件人
            msg.setFrom(new InternetAddress(account));
            //To: 收件人
            msg.addRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
            //主题
            msg.setSubject("激活邮件");
            //正文
            msg.setContent("<h1>此邮件为官方激活邮件！请点击下面链接完成激活操作！</h1><h3><a href='"+address+"/user/active?code="+code+"'>"+code+"</a></h3>","text/html;charset=UTF-8");
            //保存
            msg.saveChanges();

            Transport transport = mailSession.getTransport();
            transport.connect(account, word);
            //发送
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return false;
        }
        return true;
    }

    /**
     * 激活用户
     * @param code 用户激活码
     * @return 是否激活成功
     */
    @Transactional
    public Boolean active(String code){
        StringBuffer exit = new StringBuffer("SELECT w FROM User w WHERE w.code='"+code+"'");
        List list = entityManager.createQuery(exit.toString()).getResultList();
        System.out.println(list.toString());
        if (list.size() != 0){
            Query query = entityManager.createNativeQuery("UPDATE user " +
                    "SET staus=1 WHERE code='"+code+"'");
            int delnum = query.executeUpdate();
            logger.info("更新记录条数|激活成功：" + delnum);
            return true;
        }else{
            logger.info("没有记录|激活失败");
            return false;
        }
    }

    public int login(String name, String pass) {
        logger.info("用户登录开始!");
        StringBuffer sql = new StringBuffer("SELECT i FROM User i " +
                "WHERE i.username='"+name+"' OR mail='"+name+"'");
        List<User> user = entityManager.createQuery(sql.toString()).getResultList();
        if (user.size()>0){
            for (User uu:user){
                String passWord = uu.getPassword();
                String staus = uu.getStaus();
                if (passWord.equals(pass)){
                    if (staus.equals("1")) {
                        logger.info("登录成功!");
                        return 1;
                    }else {
                        logger.info("该用户未激活！");
                        return -2;
                    }
                }
            }
            logger.info("密码错误！");
            return -1;
        }else {
            logger.info("用户名不存在！");
            return 0;
        }
    }
    @Transactional
    public String dbCount(String uName) {
        logger.info("总数统计开始!");
        StringBuffer query = new StringBuffer("SELECT i FROM User i WHERE i.username='"+uName+"'");
        StringBuffer queryadmin = new StringBuffer("SELECT i FROM User i WHERE username='admin'");
        List<User> res = entityManager.createQuery(query.toString()).getResultList();
        List<User> admin = entityManager.createQuery(queryadmin.toString()).getResultList();
        long dbcount = 0;
        long sumCount = 1;
        if (res.size()>0){
            logger.info("res is :"+res.toString());
            logger.info(res.get(0).toString());
            logger.info(res.get(0).getCount());
            dbcount = res.get(0).getCount();
            dbcount += 1;
            Query updateGuest = entityManager.createNativeQuery("UPDATE user SET count='"+dbcount+"" +
                    "'WHERE username='"+uName+"'");
            updateGuest.executeUpdate();
            logger.info("更新成功!用户"+uName+"访问次数"+dbcount);
        }else{
            Query insert = entityManager.createNativeQuery("INSERT INTO user (username,password,staus,count)" +
                    "VALUES ('"+uName+"','admin','1',1)");
            int insertNum = insert.executeUpdate();
            logger.info("生成用户"+uName+"，次数"+insertNum);
        }
        // 总数
        if (admin.size()>0){
            sumCount = admin.get(0).getCount();
            sumCount++;
            Query updateGuest = entityManager.createNativeQuery("UPDATE user SET count='"+sumCount+"" +
                    "'WHERE username='admin'");
            updateGuest.executeUpdate();
            logger.info("更新成功!总访问次数"+sumCount);
        }else {
            Query insertAdmin = entityManager.createNativeQuery("INSERT INTO user (username,password,staus,count)" +
                    "VALUES ('admin','guest','1',1)");
            int Num = insertAdmin.executeUpdate();
            logger.info("生成admin，次数"+Num);
        }
        logger.info("总数统计结束!");
        return sumCount+"|"+dbcount;
    }

    public Boolean love(String userName, String imageId, String flag) {
        if (flag.equals("-1")){
            //取消

        }else if (flag.equals("1")){
            //增加
            Query add = entityManager.createNativeQuery("INSERT INTO love (username,)" +
                    "VALUES ('admin','guest','1',1)");
        }else {
            //查询
            StringBuffer query = new StringBuffer("SELECT i FROM Love i WHERE i.username='"+userName+"'");
            List<Love> loves = entityManager.createQuery(query.toString()).getResultList();
        }
        return true;
    }

    @Value("${ALIPAY_PUBLIC_KEY}")
    private String alipayPublicKey;
    @Value("${APP_ID}")
    private String appId;
    @Value("${PRIVATE_KEY}")
    private String privateKey;
    @Value("${ALIPAY_GATEWAY}")
    private String alipayGateway;
    @Value("${SIGN_TYPE}")
    private String signType;
    @Value("${notify_url}")
    private String notify_url;
    public String createOrder(String out_trade_no, String amount, String subject, String store_id, String timeout_express) {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayGateway, appId,
                privateKey, "json", "GBK",
                alipayPublicKey, signType);  //获得初始化的AlipayClient
        //创建API对应的request类
        //扫码付款
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        request.setNotifyUrl(notify_url);
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+out_trade_no+"\"," +
                "    \"total_amount\":\""+amount+"\"," +
                "    \"subject\":\""+subject+"\"," +
                "    \"store_id\":\""+store_id+"\"," +
                "    \"timeout_express\":\""+timeout_express+"m\"}");//设置业务参数
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            logger.info("成功调用!");
            String url = responseAnalysis(response);
            return url;
        }else{
            logger.info("失败调用!");
            return "-1";
        }
    }

    /**
     * 创建订单返回数据解析
     * @param response
     */
    public static String responseAnalysis(AlipayTradePrecreateResponse response){
        JSONObject json = JSONObject.fromObject(response.getBody());
        JSONObject a = json.getJSONObject("alipay_trade_precreate_response");
        //返回结果码
        String code = a.getString("code");
        //消息
        String msg = a.getString("msg");
        //订单号
        String trade = a.getString("out_trade_no");
        //订单二维码
        String qrcode = a.getString("qr_code");
        //签名
        String sign = json.getString("sign");
        String url = QRcodeService.getQRCode(qrcode);
        return url;
    }

}