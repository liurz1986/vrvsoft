package cn.com.liurz.flow.vo;

public class FlowConstant {
	public static final String BUSI_ARG = "busiArg";  // 业务对象
	public static final String ARG = "Arg";   // 额外的参数对象
	public static final String ACTION = "action"; // 操作，默认一个任务都是同一个操作，直接往下，但有时可以有多个选项的操作
	public static final String ADVICE = "advice"; //建议。每个流程过程中，都可以输入的一段文字
    public static final String USERID = "userId"; //执行用户ID
	public static final String INSTANCE = "instance";
	public static final String PARAMS = "PARAMS";  // 业务对象


	public static final String PROCESSRESULT = "process_result";   // 用来存放流程是执行成功还是失败
}
