package cn.com.liurz.datasource.jpa.db1.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="asset")
@Data
public class AssetBean {
    @Id
    @Column(name="guid")
    private String Guid;

    @Column(name="name")
    private String name;

}
