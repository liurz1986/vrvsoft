package cn.com.liurz.util.email;/*
package com.liurz.util.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailService {
    private Logger log = LoggerFactory.getLogger(MailService.class);
    @Autowired
    private MailDataSource mailDataSource;

    */
/**
     * 发送邮件
     * 在spring中发送邮件
     * @param mailBody 邮件内容
     * @param mailSubject 邮件标题
     * @return
     *//*

    public boolean sendMsg(String mailBody,String mailSubject){
        return sendMsg(mailBody,mailSubject,null,null,null);
    }
    */
/**
     * 发送邮件
     * 在spring中发送邮件
     * @param mailBody 邮件内容
     * @param mailSubject 邮件标题
     * @param form 发件人
     * @param to   收件人
     * @param cc   抄送人
     * @return
     *//*

    public boolean sendMsg(String mailBody,String mailSubject,String form,String[] to,String[] cc) {
        try {
            initMailDataSource(mailBody,mailSubject,form,to,cc);
            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            // 设定邮件服务器
            senderImpl.setHost(mailDataSource.getHostname());
            // 建立邮件消息
            MimeMessage mailMessage = senderImpl.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "GBK");
            // 设置收件人,邮件接收地址
            helper.setTo(mailDataSource.getAccept());
            // 抄送人
            if(!StringUtils.isEmpty(mailDataSource.getAccept())){
                helper.setCc(mailDataSource.getAccept());
            }
            // 发件人，邮件发送地址
            helper.setFrom(mailDataSource.getSend());
            // 标题
            helper.setSubject(mailDataSource.getMailSubject());
            // 邮件主体内容
            helper.setText(mailBody, true);
            // 根据自己的情况,设置username
            senderImpl.setUsername(mailDataSource.getUserName());
            senderImpl.setPassword(mailDataSource.getAuthCode()); // 授权码
            Properties prop = new Properties();
            prop.put(" mail.smtp.auth ", mailDataSource.isNeedAuth()); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put(" mail.smtp.timeout ",mailDataSource.getTimeOut());
            senderImpl.setJavaMailProperties(prop);
            // 发送邮件
            log.info("---正在发送邮件---");
            senderImpl.send(mailMessage);
            log.info("邮件发送成功");
            return true;
        } catch (Exception e) {
            e.getStackTrace();
            log.error("邮件发送失败：" + e.getMessage());
            return true;
        }
    }

    private void initMailDataSource (String mailBody,String mailSubject,String form,String[] to,String[] cc) {
        if(StringUtils.isEmpty(mailDataSource.getHostname())){
            throw new RuntimeException("邮件没有配置hostname");
        }
        if(StringUtils.isEmpty(mailDataSource.getProptocol())){
            throw new RuntimeException("邮件没有配置proptocol");
        }
        if(StringUtils.isEmpty(mailDataSource.getPort())){
            throw new RuntimeException("邮件没有配置port");
        }
        if(StringUtils.isEmpty(mailDataSource.getAuthCode())){
            throw new RuntimeException("邮件没有配置authCode");
        }
        if(StringUtils.isEmpty(mailBody)){
            throw new RuntimeException("邮件内容不能为空");
        }
        mailDataSource.setMailBody(mailBody);
        if(StringUtils.isEmpty(mailDataSource.getTimeOut())){
            mailDataSource.setTimeOut("1000");
        }
        if(StringUtils.isEmpty(mailSubject)){
            throw new RuntimeException("邮件标题不能为空");
        }
        mailDataSource.setMailSubject(mailSubject);
        if(StringUtils.isEmpty(form) && StringUtils.isEmpty(mailDataSource.getSend())){
            throw new RuntimeException("邮件发件人不能为空");
        }
        if(!StringUtils.isEmpty(form)){
            mailDataSource.setSend(form);
        }
        if(StringUtils.isEmpty(to)&& StringUtils.isEmpty(mailDataSource.getAccept())){
            throw new RuntimeException("邮件抄送人不能为空");
        }
        if(!StringUtils.isEmpty(to)){
            mailDataSource.setAccept(to);
        }
        if(!StringUtils.isEmpty(cc)){
            mailDataSource.setCc(cc);
        }
        if(StringUtils.isEmpty(mailDataSource.isNeedAuth())){
            mailDataSource.setNeedAuth(false);
        }
    }
}
*/
