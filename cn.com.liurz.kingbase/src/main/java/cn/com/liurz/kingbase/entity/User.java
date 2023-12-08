package cn.com.liurz.kingbase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据表对应的实体
 * 默认是驼峰格式识别，如果表明和字段名是驼峰格式@TableName、@TableField注解可以不需要
 * @TableId指定主键的
 *
 * 字段level是人大金仓的关键字，sql语句需要增加双引号，mybatis-plus可以在实体类增加注解。
 * \"level\"
 *
 * 字段不属于数据库表的，使用exist=false来忽略
 */
@Data
@Accessors(chain = true)
@TableName("\"user\"") // 表user，user是关键字，sql语句需要增加双引号，mybatis-plus可以在实体类增加注解。
public class User {
        /**
         * 用户ID
         */
        @TableId(value="id",type= IdType.NONE)  //数据库自增:IdType.AUTO, 不自增手填：IdType.NONE
        private int id;
        /**
         * 用户名称
         */
        @TableField("name")
        private String name;
        /**
         * 用户用到登录的帐号名
         * 字段account是人大金仓的关键字，sql语句需要增加双引号，mybatis-plus可以在实体类增加注解。
         */
        @TableField("\"account\"")
        private String account;
        /**
         * 密码
         * 字段password是人大金仓的关键字，sql语句需要增加双引号，mybatis-plus可以在实体类增加注解。
         */
        @TableField("\"password\"")
        private String password;
        @TableField("role_id")
        private String roleId;
        @TableField("status")
        private Integer status;
        @TableField("domain_code")
        private String domainCode;
        @TableField("domain_name")
        private String domainName;
}
