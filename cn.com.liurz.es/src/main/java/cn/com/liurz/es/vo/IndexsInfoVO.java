package cn.com.liurz.es.vo;

import java.util.Arrays;

public class IndexsInfoVO {
    private String[] index;
    private String[] type;

    public IndexsInfoVO() {
    }

    public String[] getIndex() {
        return this.index;
    }

    public String[] getType() {
        return this.type;
    }

    public void setIndex(final String[] index) {
        this.index = index;
    }

    public void setType(final String[] type) {
        this.type = type;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof IndexsInfoVO)) {
            return false;
        } else {
            IndexsInfoVO other = (IndexsInfoVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!Arrays.deepEquals(this.getIndex(), other.getIndex())) {
                return false;
            } else {
                return Arrays.deepEquals(this.getType(), other.getType());
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof IndexsInfoVO;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + Arrays.deepHashCode(this.getIndex());
        result = result * 59 + Arrays.deepHashCode(this.getType());
        return result;
    }

    public String toString() {
        return "IndexsInfoVO(index=" + Arrays.deepToString(this.getIndex()) + ", type=" + Arrays.deepToString(this.getType()) + ")";
    }
}
