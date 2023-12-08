package cn.com.liurz.jpa.util;

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
    Not;

    private CompareExpression() {
    }
}
