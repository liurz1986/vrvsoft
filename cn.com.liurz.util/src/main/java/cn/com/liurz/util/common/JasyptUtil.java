package cn.com.liurz.util.common;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * 加解密：
 */
@Component
public class JasyptUtil {
    @Autowired
    private StringEncryptor stringEncryptor;
    /**
     * 加密
     * @param data
     * @return
     */
    public String encrypt(String data){
        return stringEncryptor.encrypt(data);
    }

    /**
     * 解密
     * @param data
     * @return
     */
    public String decrypt(String data){
        return stringEncryptor.decrypt(data);
    }

}
