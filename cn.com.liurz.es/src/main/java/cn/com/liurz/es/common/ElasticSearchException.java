package cn.com.liurz.es.common;

public class ElasticSearchException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Integer resultCode;

    public ElasticSearchException() {
    }

    public ElasticSearchException(Integer code, String message) {
        super(message);
        this.resultCode = code;
    }

    public ElasticSearchException(Integer code, Exception ex) {
        super(ex);
        this.resultCode = code;
    }

    public Integer getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
