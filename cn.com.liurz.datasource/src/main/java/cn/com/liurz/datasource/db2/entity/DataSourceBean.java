package cn.com.liurz.datasource.jpa.db2.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="data_source")
@Data
public class DataSourceBean {
    @Id
    @Column(name="id")
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="title")
    private String title;
}
