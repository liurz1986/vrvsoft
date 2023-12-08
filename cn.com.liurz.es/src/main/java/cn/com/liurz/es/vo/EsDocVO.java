package cn.com.liurz.es.vo;

import java.util.Map;

public class EsDocVO {
    private String idValue;
    private Map<String, Object> map;

    public EsDocVO() {
    }

    public String getIdValue() {
        return this.idValue;
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public void setIdValue(final String idValue) {
        this.idValue = idValue;
    }

    public void setMap(final Map<String, Object> map) {
        this.map = map;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof EsDocVO)) {
            return false;
        } else {
            EsDocVO other = (EsDocVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$idValue = this.getIdValue();
                Object other$idValue = other.getIdValue();
                if (this$idValue == null) {
                    if (other$idValue != null) {
                        return false;
                    }
                } else if (!this$idValue.equals(other$idValue)) {
                    return false;
                }

                Object this$map = this.getMap();
                Object other$map = other.getMap();
                if (this$map == null) {
                    if (other$map != null) {
                        return false;
                    }
                } else if (!this$map.equals(other$map)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EsDocVO;
    }

    public int hashCode() {
        int result = 1;
        Object $idValue = this.getIdValue();
        result = result * 59 + ($idValue == null ? 43 : $idValue.hashCode());
        Object $map = this.getMap();
        result = result * 59 + ($map == null ? 43 : $map.hashCode());
        return result;
    }

    public String toString() {
        return "EsDocVO(idValue=" + this.getIdValue() + ", map=" + this.getMap() + ")";
    }
}
