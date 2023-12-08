package cn.com.liurz.flow.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 任务处理者guid
 * @author wd-pc
 *
 */
@Entity
@Data
public class BusinessTaskCandidate{
	@Id
	@Column(length = 50)
	private String id; //主键guid
	private String busiTaskId; //businessTask的guid
	private String taskId; //任务taskId
	private String candidate; //任务处理者guid
	private String candidateName; //候选人人名
	private Date createDate;  // 候选人任务时间
	private Integer assignType; //1-业务触发,2-处理者触发
}
