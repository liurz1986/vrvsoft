package cn.com.liurz.tkmybatis.util;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *使用ScriptRunner对象做sql文件导入
 *
 * 而在解析sql脚本时,ScriptRunner对象进行了一个设置,定义命令间的分隔符为英文的";",所以执行sql语句时,遇到分号就当做一个命令,结果不是一个完整的建表语句,然后就报错了。
 */
@Service
public class WriteDataBySqlFile {
    @Value("${spring.datasource.driver-class-name}")
    private String diverName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pwd;

    /**
     * 读取.sql文件写入数据库中
     * @param fileName
     */
    public void exc(String fileName){
        try {

            Class.forName(diverName);
            Connection conn = DriverManager.getConnection(url, user, pwd);
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setAutoCommit(true);
            File file = new File(fileName);
            try {
                if (file.getName().endsWith(".sql")) {
                    runner.setDelimiter("##");
                    // sql按照那种方式执行：true表示获取整个脚本并执行 false按照自定义的分割符没行执行
                    runner.setSendFullScript(false);
                    // 执行自动提交
                    runner.setAutoCommit(true);
                    runner.setStopOnError(true);
                    // 定义命令间的分割符，  不定义这个多条sql的执行，定义的英文分割符";"会当成一个命令
                    runner.setDelimiter(";");
                    runner.setFullLineDelimiter(false);
                    // 定义是否日志执行的sql，默认是执行，设置为null表示不执行
                    runner.setLogWriter(null);
                    runner.runScript(new InputStreamReader(new FileInputStream(fileName),"GBK"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
    }
}
