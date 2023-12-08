package cn.com.liurz.flow.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class BusinessTask {
	@Id
	@Column(length = 50)
	private String id;
	private String busiKey;  // 业务关键字，比如ticket
	private String busiId;   // 业务实体id。现在存放的是activiti流程实例id
	private String taskId;   // 任务id
	private String taskDefineKey;  // 任务节点定义的id
	private String taskDefindName; // 任务节点定义的名称
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "busiTaskId")
	private Set<BusinessTaskCandidate> candidates;
	private Date deadDate;   // 截止日期，可以为null，表示没有截止日期。
	private String actions;   // 竖线      |   分隔
	private String executionId;  // 执行流id
	private String taskCode;     // 任务编码。。当任务的创建和销毁不能通过taskId来完成的时候，需要通过该字段来完成
	private String relatedInfos;   // 关联的数据。比如在撤销任务中，通过这个字段存信号量

	@Column(name = "deadline_date")
	private Date deadlineDate; //逾期时间

}
