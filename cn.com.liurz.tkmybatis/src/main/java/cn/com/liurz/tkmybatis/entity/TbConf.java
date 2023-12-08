package cn.com.liurz.tkmybatis.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "tb_conf")
@Data
public class TbConf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String confId;

    @Column(name = "conf_value")
    private String confValue;
}
