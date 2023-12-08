package cn.com.liurz.es.vo;

import java.util.List;

public class ScrollVO<T> {
    private List<T> list;
    private String scrollId;
    private long total;

    public ScrollVO() {
    }

    public List<T> getList() {
        return this.list;
    }

    public String getScrollId() {
        return this.scrollId;
    }

    public long getTotal() {
        return this.total;
    }

    public void setList(final List<T> list) {
        this.list = list;
    }

    public void setScrollId(final String scrollId) {
        this.scrollId = scrollId;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ScrollVO)) {
            return false;
        } else {
            ScrollVO<?> other = (ScrollVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label39: {
                    Object this$list = this.getList();
                    Object other$list = other.getList();
                    if (this$list == null) {
                        if (other$list == null) {
                            break label39;
                        }
                    } else if (this$list.equals(other$list)) {
                        break label39;
                    }

                    return false;
                }

                Object this$scrollId = this.getScrollId();
                Object other$scrollId = other.getScrollId();
                if (this$scrollId == null) {
                    if (other$scrollId != null) {
                        return false;
                    }
                } else if (!this$scrollId.equals(other$scrollId)) {
                    return false;
                }

                if (this.getTotal() != other.getTotal()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ScrollVO;
    }

    public int hashCode() {
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + ($list == null ? 43 : $list.hashCode());
        Object $scrollId = this.getScrollId();
        result = result * 59 + ($scrollId == null ? 43 : $scrollId.hashCode());
        long $total = this.getTotal();
        result = result * 59 + (int)($total >>> 32 ^ $total);
        return result;
    }

    public String toString() {
        return "ScrollVO(list=" + this.getList() + ", scrollId=" + this.getScrollId() + ", total=" + this.getTotal() + ")";
    }
}

