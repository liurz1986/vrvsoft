package cn.com.liurz.es.common;

public enum CompareExpression {
    Eq,
    NotEq,
    In,
    Between,
    NotNull,
    IsNull,
    Like,
    LikeEnd,
    LikeBegin,
    Gt,
    Ge,
    Le,
    Lt,
    And,
    Or,
    Or1,
    Not;

    private CompareExpression() {
    }
}
