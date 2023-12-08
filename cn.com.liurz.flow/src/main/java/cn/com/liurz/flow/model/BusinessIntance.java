package cn.com.liurz.flow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessIntance {
	@Id
	private String guid;
	private String code;   // 工单编号
	private String name;   // 工单名称
	private String processInstanceId;  // 流程实例id  
	private String processDefGuid; //流程定义guid
	private String processDefName; //流程定义名称
	private String createUserName;//流程实例创建人名称
	private String createUserId;//流程实例创建人guid
	private Date createDate;
	@Column(columnDefinition = "text")
	private String busiArgs;    // 表单的json字符串
	@Column(columnDefinition = "text")
	private String dealPeoples;   // 经手人，每次经手都直接附加，逗号分隔
	@Column(name="finish_date")
	private Date finishDate; //归档时间
	private  Date deadlineDate; //逾期时间
}
