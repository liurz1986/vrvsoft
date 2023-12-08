package cn.com.liurz.es.vo;



public class PageReq {
    private String order_;
    private String by_;
    private Integer start_;
    private Integer count_;
    
    public PageReq() {
    }

    public String getOrder_() {
        return this.order_;
    }

    public String getBy_() {
        return this.by_;
    }

    public Integer getStart_() {
        return this.start_;
    }

    public Integer getCount_() {
        return this.count_;
    }

    public void setOrder_(final String order_) {
        this.order_ = order_;
    }

    public void setBy_(final String by_) {
        this.by_ = by_;
    }

    public void setStart_(final Integer start_) {
        this.start_ = start_;
    }

    public void setCount_(final Integer count_) {
        this.count_ = count_;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageReq)) {
            return false;
        } else {
            PageReq other = (PageReq)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$order_ = this.getOrder_();
                    Object other$order_ = other.getOrder_();
                    if (this$order_ == null) {
                        if (other$order_ == null) {
                            break label59;
                        }
                    } else if (this$order_.equals(other$order_)) {
                        break label59;
                    }

                    return false;
                }

                Object this$by_ = this.getBy_();
                Object other$by_ = other.getBy_();
                if (this$by_ == null) {
                    if (other$by_ != null) {
                        return false;
                    }
                } else if (!this$by_.equals(other$by_)) {
                    return false;
                }

                Object this$start_ = this.getStart_();
                Object other$start_ = other.getStart_();
                if (this$start_ == null) {
                    if (other$start_ != null) {
                        return false;
                    }
                } else if (!this$start_.equals(other$start_)) {
                    return false;
                }

                Object this$count_ = this.getCount_();
                Object other$count_ = other.getCount_();
                if (this$count_ == null) {
                    if (other$count_ != null) {
                        return false;
                    }
                } else if (!this$count_.equals(other$count_)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageReq;
    }

    public int hashCode() {
        int result = 1;
        Object $order_ = this.getOrder_();
        result = result * 59 + ($order_ == null ? 43 : $order_.hashCode());
        Object $by_ = this.getBy_();
        result = result * 59 + ($by_ == null ? 43 : $by_.hashCode());
        Object $start_ = this.getStart_();
        result = result * 59 + ($start_ == null ? 43 : $start_.hashCode());
        Object $count_ = this.getCount_();
        result = result * 59 + ($count_ == null ? 43 : $count_.hashCode());
        return result;
    }

    public String toString() {
        return "PageReq(order_=" + this.getOrder_() + ", by_=" + this.getBy_() + ", start_=" + this.getStart_() + ", count_=" + this.getCount_() + ")";
    }
}
