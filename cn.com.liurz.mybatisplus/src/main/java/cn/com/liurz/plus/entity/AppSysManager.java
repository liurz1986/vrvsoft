package cn.com.liurz.plus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 数据表对应的实体
 * 默认是驼峰格式识别，如果表明和字段名是驼峰格式@TableName、@TableField注解可以不需要
 * @TableId指定主键的
 */
@Data
@TableName("app_sys_manager")
public class AppSysManager {
    @TableId
    @TableField( "id")
    private Integer id;


    @TableField( "app_no")
    private String appNo;


    @TableField( "app_name")
    private String appName;


    @TableField( "department_name")
    private String departmentName;

    @TableField( "department_guid")
    private String departmentGuid;


    @TableField( "domain_name")
    private String domainName;


    @TableField( "secret_level")
    private String secretLevel;


    @TableField( "secret_company")
    private String secretCompany;


    @TableField( "service_id")
    private String serviceId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField( "create_time")
    private Date createTime;

    @TableField("data_source_type")
    private int dataSourceType;

    @TableField("sync_source")
    private String syncSource;

    @TableField("sync_uid")
    private String syncUid;
}
