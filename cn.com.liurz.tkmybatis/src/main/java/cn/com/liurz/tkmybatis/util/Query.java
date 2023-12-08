package cn.com.liurz.tkmybatis.util;

public class Query {
    // "查询开始条数，默认为 0"
    private int start_ = 0;
    // 查询数量（每页数量），默认为10
    private int count_ = 10;
    // 查询排序的字段，默认为 空 (不排序)
    private String order_ = null;
    // 查询排序的值：支持 "asc" "desc" 默认为空 （不排序）
    private String by_ = null;
   // 查询时根据该字段进行排序，合并order和by的值
    private String orderByColumn;

    public Query() {
    }

    public String getOrderByColumn() {
        return this.orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public int getStart_() {
        return this.start_;
    }

    public void setStart_(int start_) {
        this.start_ = start_;
    }

    public int getCount_() {
        return this.count_;
    }

    public void setCount_(int count_) {
        this.count_ = count_;
    }

    public String getOrder_() {
        return this.order_;
    }

    public void setOrder_(String order_) {
        this.order_ = order_;
    }

    public String getBy_() {
        return this.by_;
    }

    public void setBy_(String by_) {
        this.by_ = by_;
    }
}
