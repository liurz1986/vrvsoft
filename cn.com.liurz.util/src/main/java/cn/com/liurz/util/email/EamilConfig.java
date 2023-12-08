package cn.com.liurz.util.email;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
// 读取配置文件以spring.email开始的配置信息
@ConfigurationProperties(prefix="spring.eamil")
@Data
public class EamilConfig {

    // 发邮件服务器
    private String hostname;
    // 收邮件服务器、 收邮件协议
    private String proptocol;
    // 邮件服务器端口号
    private String port;
    // smtp是否需要认证
    private boolean needAuth;
    // smtp认证用户名
    private String userName;
    // smtp认证密码(授权码)
    private String authCode;
    //邮件超时时间
    private String timeOut;

    // 发件人
    private String send;
    // 收件人  :多个逗号分割
    private String accept;
    // 抄送人: 多个逗号分割
    private String cc;

    @Bean
    MailDataSource getMailDataSource(){
        MailDataSource dataSource = new MailDataSource();
        dataSource.setHostname(hostname);
        dataSource.setProptocol(proptocol);
        dataSource.setPort(port);
        dataSource.setNeedAuth(needAuth);
        dataSource.setUserName(userName);
        dataSource.setAuthCode(authCode);
        dataSource.setTimeOut(timeOut);
        dataSource.setSend(send);
        if(StringUtils.isNotEmpty(accept)){
            String[] accepts = accept.split(",");
            dataSource.setAccept(accepts);
        }
        if(StringUtils.isNotEmpty(cc)){
            String[] ccs = cc.split(",");
            dataSource.setCc(ccs);
        }
        return dataSource;
    }
}
