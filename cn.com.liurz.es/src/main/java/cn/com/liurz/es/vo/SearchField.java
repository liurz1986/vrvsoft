package cn.com.liurz.es.vo;

import cn.com.liurz.es.common.ElasticSearchException;
import cn.com.liurz.es.util.ResultCodeEnum;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class SearchField {
    private String fieldName;
    private FieldType fieldType;
    private String timeFormat;
    private long timeSpan = -1L;
    private DateHistogramInterval timeInterval;
    private SearchField childField;
    private Integer from;
    private Integer size;

    public List<SearchField> getChildrenField() {
        List<SearchField> childrenField = new LinkedList();
        if (this.childField != null) {
            childrenField.add(this.childField);
        }

        return childrenField;
    }

    private void checkChildFieldType(FieldType type, SearchField child) {
        if (child != null && (type == FieldType.NumberAvg || type == FieldType.NumberMax || type == FieldType.NumberMin || type == FieldType.NumberSum || type == FieldType.ObjectDistinctCount) && (child.getFieldType() == FieldType.Date || child.getFieldType() == FieldType.String || child.getFieldType() == FieldType.Object)) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), "String,Object类型Field不能是count相关类型的子类型,请检查");
        }
    }

    public SearchField(String name, FieldType type, SearchField child) {
        this.checkChildFieldType(type, child);
        this.setFieldName(name);
        this.setFieldType(type);
        if (type == FieldType.Date) {
            if (this.timeSpan <= 0L) {
                this.timeSpan = 86400000L;
            }

            if (StringUtils.isEmpty(this.timeFormat)) {
                this.timeFormat = "yyyy-MM-dd HH:mm:ss";
            }
        }

        this.setChildField(child);
    }

    public SearchField(String name, FieldType type, String format, long span, SearchField child) {
        this.checkChildFieldType(type, child);
        this.setFieldName(name);
        this.setFieldType(type);
        this.setTimeFormat(format);
        this.setTimeSpan(span);
        this.setChildField(child);
    }

    public SearchField(String name, FieldType type, String format, long span, SearchField child, Integer from, Integer size) {
        this.checkChildFieldType(type, child);
        this.setFieldName(name);
        this.setFieldType(type);
        this.setTimeFormat(format);
        this.setTimeSpan(span);
        this.setFrom(from);
        this.setSize(size);
        this.setChildField(child);
    }

    public SearchField(String name, FieldType type, String format, DateHistogramInterval timeInterval, SearchField child) {
        this.checkChildFieldType(type, child);
        this.setFieldName(name);
        this.setFieldType(type);
        this.setTimeFormat(format);
        this.setTimeInterval(timeInterval);
        this.setChildField(child);
    }

    public SearchField(String name, FieldType type, String format, DateHistogramInterval timeInterval, SearchField child, Integer from, Integer size) {
        this.checkChildFieldType(type, child);
        this.setFieldName(name);
        this.setFieldType(type);
        this.setTimeFormat(format);
        this.setTimeInterval(timeInterval);
        this.from = from;
        this.size = size;
        this.setChildField(child);
    }

    public SearchField(String name, FieldType type, Integer from, Integer size, SearchField child) {
        this.checkChildFieldType(type, child);
        this.setFieldName(name);
        this.setFieldType(type);
        this.setFrom(from);
        this.setSize(size);
        this.setChildField(child);
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public FieldType getFieldType() {
        return this.fieldType;
    }

    public String getTimeFormat() {
        return this.timeFormat;
    }

    public long getTimeSpan() {
        return this.timeSpan;
    }

    public DateHistogramInterval getTimeInterval() {
        return this.timeInterval;
    }

    public SearchField getChildField() {
        return this.childField;
    }

    public Integer getFrom() {
        return this.from;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldType(final FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public void setTimeFormat(final String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public void setTimeSpan(final long timeSpan) {
        this.timeSpan = timeSpan;
    }

    public void setTimeInterval(final DateHistogramInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setChildField(final SearchField childField) {
        this.childField = childField;
    }

    public void setFrom(final Integer from) {
        this.from = from;
    }

    public void setSize(final Integer size) {
        this.size = size;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SearchField)) {
            return false;
        } else {
            SearchField other = (SearchField)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$fieldName = this.getFieldName();
                Object other$fieldName = other.getFieldName();
                if (this$fieldName == null) {
                    if (other$fieldName != null) {
                        return false;
                    }
                } else if (!this$fieldName.equals(other$fieldName)) {
                    return false;
                }

                Object this$fieldType = this.getFieldType();
                Object other$fieldType = other.getFieldType();
                if (this$fieldType == null) {
                    if (other$fieldType != null) {
                        return false;
                    }
                } else if (!this$fieldType.equals(other$fieldType)) {
                    return false;
                }

                Object this$timeFormat = this.getTimeFormat();
                Object other$timeFormat = other.getTimeFormat();
                if (this$timeFormat == null) {
                    if (other$timeFormat != null) {
                        return false;
                    }
                } else if (!this$timeFormat.equals(other$timeFormat)) {
                    return false;
                }

                if (this.getTimeSpan() != other.getTimeSpan()) {
                    return false;
                } else {
                    label77: {
                        Object this$timeInterval = this.getTimeInterval();
                        Object other$timeInterval = other.getTimeInterval();
                        if (this$timeInterval == null) {
                            if (other$timeInterval == null) {
                                break label77;
                            }
                        } else if (this$timeInterval.equals(other$timeInterval)) {
                            break label77;
                        }

                        return false;
                    }

                    label70: {
                        Object this$childField = this.getChildField();
                        Object other$childField = other.getChildField();
                        if (this$childField == null) {
                            if (other$childField == null) {
                                break label70;
                            }
                        } else if (this$childField.equals(other$childField)) {
                            break label70;
                        }

                        return false;
                    }

                    Object this$from = this.getFrom();
                    Object other$from = other.getFrom();
                    if (this$from == null) {
                        if (other$from != null) {
                            return false;
                        }
                    } else if (!this$from.equals(other$from)) {
                        return false;
                    }

                    Object this$size = this.getSize();
                    Object other$size = other.getSize();
                    if (this$size == null) {
                        if (other$size != null) {
                            return false;
                        }
                    } else if (!this$size.equals(other$size)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SearchField;
    }

    public int hashCode() {
        int result = 1;
        Object $fieldName = this.getFieldName();
        result = result * 59 + ($fieldName == null ? 43 : $fieldName.hashCode());
        Object $fieldType = this.getFieldType();
        result = result * 59 + ($fieldType == null ? 43 : $fieldType.hashCode());
        Object $timeFormat = this.getTimeFormat();
        result = result * 59 + ($timeFormat == null ? 43 : $timeFormat.hashCode());
        long $timeSpan = this.getTimeSpan();
        result = result * 59 + (int)($timeSpan >>> 32 ^ $timeSpan);
        Object $timeInterval = this.getTimeInterval();
        result = result * 59 + ($timeInterval == null ? 43 : $timeInterval.hashCode());
        Object $childField = this.getChildField();
        result = result * 59 + ($childField == null ? 43 : $childField.hashCode());
        Object $from = this.getFrom();
        result = result * 59 + ($from == null ? 43 : $from.hashCode());
        Object $size = this.getSize();
        result = result * 59 + ($size == null ? 43 : $size.hashCode());
        return result;
    }

    public String toString() {
        return "SearchField(fieldName=" + this.getFieldName() + ", fieldType=" + this.getFieldType() + ", timeFormat=" + this.getTimeFormat() + ", timeSpan=" + this.getTimeSpan() + ", timeInterval=" + this.getTimeInterval() + ", childField=" + this.getChildField() + ", from=" + this.getFrom() + ", size=" + this.getSize() + ")";
    }
}
