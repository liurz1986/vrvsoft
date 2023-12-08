package cn.com.liurz.jpa.condition;


import org.springframework.data.jpa.domain.Specification;

public interface ICondition {
    String toStrCondition();

    <T> Specification<T> c2s();

    ICondition copy();

    default String getValueStr(Object value) {
        if (value instanceof Number) {
            return "" + value + "";
        } else {
            return "";
        }
    }
}
