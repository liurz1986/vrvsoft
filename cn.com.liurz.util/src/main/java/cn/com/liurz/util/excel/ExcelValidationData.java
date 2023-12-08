package cn.com.liurz.util.excel;

import org.apache.poi.hssf.usermodel.DVConstraint;

/**
 * excel格式设置
 * 设置单元格属性：提示、下拉数据、数据有效性校验
 */

public class ExcelValidationData {

	private int firstRow=0;   // 起始行
	private int lastRow =0;   // 终止行
	private int firstCol=0;   // 起始列
	private int lastCol=0;    // 终止列

	private String errorTitle;

	private String errorMsg;

	private String promptTitle;

	private String promptContent;

	private DVConstraint dvConstraint ;

	/**
	 * 设置列的下拉数据
	 *
	 * @param colIndex 列的下标
	 * @param explicitListValues  下拉数据
	 */
	public ExcelValidationData(int colIndex, String[] explicitListValues)
	{
		this.firstRow=1;
		this.lastRow=1024000;
		this.firstCol=colIndex;
		this.lastCol=colIndex;
		this.dvConstraint=DVConstraint.createExplicitListConstraint(explicitListValues);
	}

	/**
	 * 设置列的下拉数据、列的提示
	 * @param colIndex 列的下标
	 * @param explicitListValues 下拉数据
	 * @param promptTitle 提示标题
	 * @param promptContent 提示内容
	 */
	public ExcelValidationData(int colIndex, String[] explicitListValues, String promptTitle, String promptContent)
	{
		this.firstRow=1;
		this.lastRow=1024000;
		this.firstCol=colIndex;
		this.lastCol=colIndex;
		this.dvConstraint=DVConstraint.createExplicitListConstraint(explicitListValues);

		this.promptTitle=promptTitle;
		this.promptContent=promptContent;
	}
	/**
	 * formula
	 * @param colIndex 列的下标
	 * @param formula
	 * @param errorTitle 校验错误提示标题
	 * @param errorMsg 校验错误提示内容
	 * @param promptTitle 提示标题
	 * @param promptContent 提示内容
	 */
	public ExcelValidationData(int colIndex, String formula, String errorTitle, String errorMsg, String promptTitle, String promptContent)
	{
		this.firstRow=1;
		this.lastRow=1024000;
		this.firstCol=colIndex;
		this.lastCol=colIndex;
		this.dvConstraint=DVConstraint.createCustomFormulaConstraint(formula);
		this.errorTitle=errorTitle;
		this.errorMsg=errorMsg;
		this.promptTitle=promptTitle;
		this.promptContent=promptContent;
	}

	/**
	 * 校验单元格字体的长度
	 *
	 * @param colIndex 列的下标 excel是从0开始的，0表示第一列
	 * @param textMinLenth  最小长度
	 * @param textMaxLenth  最大长度
	 * @param errorTitle  校验错误提示标题
	 * @param errorMsg  校验错误提示内容
	 * @param promptTitle  提示标题
	 * @param promptContent 提示内容
	 */
	public ExcelValidationData(int colIndex, int  textMinLenth, int  textMaxLenth, String errorTitle, String errorMsg, String promptTitle, String promptContent)
	{
		this.firstRow=1;
		this.lastRow=1024000;
		this.firstCol=colIndex;
		this.lastCol=colIndex;
		this.dvConstraint= DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.TEXT_LENGTH,
				DVConstraint.OperatorType.BETWEEN,Integer.toString(textMinLenth) , Integer.toString(textMaxLenth));

		this.errorTitle=errorTitle;
		this.errorMsg=errorMsg;
		this.promptTitle=promptTitle;
		this.promptContent=promptContent;
	}
	
	public String getPromptTitle() {
		return promptTitle;
	}
	public void setPromptTitle(String promptTitle) {
		this.promptTitle = promptTitle;
	}
	public String getPromptContent() {
		return promptContent;
	}
	public void setPromptContent(String promptContent) {
		this.promptContent = promptContent;
	}

	public String getErrorTitle() {
		return errorTitle;
	}
	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	public int getFirstRow() {
		return firstRow;
	}
	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}
	public int getLastRow() {
		return lastRow;
	}
	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}
	public int getFirstCol() {
		return firstCol;
	}
	public void setFirstCol(int firstCol) {
		this.firstCol = firstCol;
	}
	public int getLastCol() {
		return lastCol;
	}
	public DVConstraint getDvConstraint() {
		return dvConstraint;
	}
	public void setDvConstraint(DVConstraint dvConstraint) {
		this.dvConstraint = dvConstraint;
	}
	public void setLastCol(int lastCol) {
		this.lastCol = lastCol;
	}

	

}
