package cn.com.liurz.util.email;

/**
 * 邮件对象
 */
public class MailDataSource {

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
    // 收件人
    private String[] accept;
    // 抄送人
    private String[] cc;



    // 邮件标题
    private String mailSubject;
    // 邮件主题
    private String mailBody;
    // 邮件附件路径
    private String fileAffixPath;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getProptocol() {
        return proptocol;
    }

    public void setProptocol(String proptocol) {
        this.proptocol = proptocol;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String[] getAccept() {
        return accept;
    }

    public void setAccept(String[] accept) {
        this.accept = accept;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public String getFileAffixPath() {
        return fileAffixPath;
    }

    public void setFileAffixPath(String fileAffixPath) {
        this.fileAffixPath = fileAffixPath;
    }
}
