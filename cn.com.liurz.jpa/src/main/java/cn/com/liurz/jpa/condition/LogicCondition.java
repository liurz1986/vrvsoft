package cn.com.liurz.jpa.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.com.liurz.jpa.util.CompareExpression;
import org.springframework.data.jpa.domain.Specification;


/**
 * 处理sql中or 、and
 */
public class LogicCondition implements ICondition {
    private List<ICondition> cons;
    private CompareExpression expression;

    public LogicCondition() {
    }

    public static ICondition and(ICondition condition1, ICondition condition2, ICondition... conditions) {
        List<ICondition> list = new ArrayList();
        list.add(condition1);
        list.add(condition2);

        for(int i = 0; i < conditions.length; ++i) {
            list.add(conditions[i]);
        }

        LogicCondition condition = new LogicCondition();
        condition.setCons(list);
        condition.setExpression(CompareExpression.And);
        return condition;
    }

    public static ICondition or(List<ICondition> conditions) {
        LogicCondition condition = new LogicCondition();
        condition.setCons(conditions);
        condition.setExpression(CompareExpression.Or);
        return condition;
    }

    public static ICondition or(ICondition condition1, ICondition condition2, ICondition... conditions) {
        List<ICondition> list = new ArrayList();
        list.add(condition1);
        list.add(condition2);

        for(int i = 0; i < conditions.length; ++i) {
            list.add(conditions[i]);
        }

        LogicCondition condition = new LogicCondition();
        condition.setCons(list);
        condition.setExpression(CompareExpression.Or);
        return condition;
    }

    public static ICondition not(ICondition condition) {
        List<ICondition> list = new ArrayList();
        list.add(condition);
        LogicCondition result = new LogicCondition();
        result.setCons(list);
        result.setExpression(CompareExpression.Not);
        return result;
    }

    public ICondition copy() {
        LogicCondition result = new LogicCondition();
        result.setCons(this.cons);
        result.setExpression(this.expression);
        return result;
    }

    public String toStrCondition() {
        if (this.expression.equals(CompareExpression.Not)) {
            return "(NOT (" + ((ICondition)this.cons.get(0)).toStrCondition() + "))";
        } else {
            String joinChar = " " + this.expression.toString() + " ";
            String collect = (String)this.cons.stream().map((item) -> {
                return item.toStrCondition();
            }).collect(Collectors.joining(joinChar, "(", ")"));
            return collect;
        }
    }

    public Specification<Object> c2s() {
        Specification<Object> result = new Specification<Object>() {
            public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate[] restrictions = new Predicate[LogicCondition.this.cons.size()];

                for(int i = 0; i < LogicCondition.this.cons.size(); ++i) {
                    restrictions[i] = ((ICondition)LogicCondition.this.cons.get(i)).c2s().toPredicate(root, query, cb);
                }

                switch(LogicCondition.this.expression) {
                    case And:
                        Predicate and = cb.and(restrictions);
                        return and;
                    case Or:
                        Predicate or = cb.or(restrictions);
                        return or;
                    case Not:
                        Predicate not = cb.not(restrictions[0]);
                        return not;
                    default:
                        return null;
                }
            }
        };
        return result;
    }

    public List<ICondition> getCons() {
        return this.cons;
    }

    public void setCons(List<ICondition> cons) {
        this.cons = cons;
    }

    public CompareExpression getExpression() {
        return this.expression;
    }

    public void setExpression(CompareExpression expression) {
        this.expression = expression;
    }
}

