package cn.com.liurz.util.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * excel导出工具类poi
 *
 *  poi版本 ：3.9
 *
 *  2022-10-14
 */
public class ExportExcelUtils {
 
	private CellStyle defaultStyle=null;

	private CellStyle headStyle=null;

	private CellStyle mergedtStyle=null;


	/**
	 * 简单导出
	 * 一般用于数据导出
	 * @param workbook
	 * @param sheetTitle （sheet的名称）
	 * @param headers （表格的标题）
	 * @param result （表格的数据）
	 * @throws Exception
	 */
	public  void simpleExportExcel(Workbook workbook, String sheetTitle, String[] headers, List<List<String>> result) {
		exportExcel(workbook,sheetTitle,headers,result,null,null);
    }

    /**
	 * 导出excel
	 * @param workbook
	 * @param sheetTitle （sheet的名称）
	 * @param headers （表格的标题）
	 * @param result （表格的数据）
	 * @param validationDatas 单元格格式设置
	 * @throws Exception
	 */
    public  void  exportExcel(Workbook workbook, String sheetTitle, String[] headers, List<List<String>> result,List<ExcelValidationData> validationDatas )   {
		exportExcel(workbook,sheetTitle,headers,result,validationDatas,null);
        
    }

	/**
	 * 带格式和帮忙文档的导出
	 *
	 * 一般作为模板导出，
	 *
	 * @param workbook
	 * @param sheetTitle （sheet的名称）
	 * @param headers （表格的标题）
	 * @param result （表格的数据）
	 * @param validationDatas 单元格格式设置
	 * @param helpDocuments 帮助文档设置
	 * @throws Exception
	 */
    
    public  void  exportExcel(Workbook workbook, String sheetTitle, String[] headers, List<List<String>> result,List<ExcelValidationData> validationDatas,List<String> helpDocuments ) {
        // 生成一个表格  
        Sheet sheet = workbook.createSheet(sheetTitle);
		//  顶部插入帮助文档
        setHelpDocument(workbook, headers, helpDocuments, sheet);
        // 构造excel数据
        exportExcel(workbook, headers, result, sheet);
        // 设置单元格属性：提示、下拉数据等
        setSheetDataValidation(workbook, validationDatas, sheet);
    }



	/**
	 * 在excel 顶部插入帮助文档
	 *
	 * @param workbook
	 * @param headers
	 * @param helpDocuments
	 * @param sheet
	 */
	private void setHelpDocument(Workbook workbook, String[] headers, List<String> helpDocuments, Sheet sheet) {
		if(CollectionUtils.isEmpty(helpDocuments)) {
			return ;
		}
		//在excel 顶部插入帮助文档
		  // 生成一个样式  
		 CellStyle style = this.getMergedtStyle(workbook);

        Row helpRow = sheet.createRow(0);
        short height = helpRow.getHeight();
        if(height==0)
        {
        	height=20;
        }
        height=(short)(height*1.05*(helpDocuments.size()+1));
        helpRow.setHeight(height);
        Cell cell = helpRow.createCell(0);
        
        for(int i=0;i<headers.length;i++)
        {
        	Cell cell2 = helpRow.getCell(i);
        	if(cell2==null)
        	{
        		cell2=helpRow.createCell(i);
        	}
        	cell2.setCellStyle(style);
        }
        cell.setCellStyle(style);  
        cell.setCellValue(StringUtils.join(helpDocuments,"\r\n"));
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));
	}


    /**
     * 设置单元格属性：提示、下拉数据、数据有效性校验等
	 * @param workbook
     * @param validationDatas
     * @param sheet
     */
	private void setSheetDataValidation(Workbook workbook, List<ExcelValidationData> validationDatas, Sheet sheet) {
		if(CollectionUtils.isEmpty(validationDatas)) {
			return ;
		}
		int index = 0;
		while(isMergedRegion(sheet,index,0))
		{
			index++;
			if(index>10)
			{
				break;
			}
		}
		for (ExcelValidationData validationData : validationDatas) {
			int firstRow = validationData.getFirstRow();
			int lastRow = validationData.getLastRow();
			int firstCol = validationData.getFirstCol();
			int lastCol = validationData.getLastCol();

			if (firstRow < 0 || lastRow < 0 || firstCol < 0 || lastCol < 0 || lastRow < firstRow || lastCol < firstCol) {
				throw new IllegalArgumentException("Wrong Row or Column index : " + firstRow + ":" + lastRow + ":" + firstCol + ":" + lastCol);
			}

			if (firstRow < index+1) {
				firstRow = index+1;
			}
			// 设置作用范围：起始行、终止行、起始列、终止列
			CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
			if (validationData.getDvConstraint() != null) {
				DVConstraint dvConstraint = validationData.getDvConstraint();
				// 对于列有下拉数据的处理
				if (validationData.getDvConstraint().getExplicitListValues() != null) {
					String[] explicitListValues = validationData.getDvConstraint().getExplicitListValues();
					if (explicitListValues.length > 20||StringUtils.join(explicitListValues,",").length()>128) {
						String name = "hidden_"+sheet.getSheetName()+"_" + validationData.getFirstCol() + "_" + validationData.getLastCol();
						Sheet hidden = workbook.getSheet(name);
						if (hidden == null) {
							String[] dataArray = validationData.getDvConstraint().getExplicitListValues();
							hidden = workbook.createSheet(name);
							Cell cell = null;
							for (int i = 0, length = dataArray.length; i < length; i++) {
								String data = dataArray[i];
								Row row = hidden.createRow(i);
								cell = row.createCell(0);
								cell.setCellValue(data);
							}
							Name namedCell = workbook.createName();
							namedCell.setNameName(name);
							namedCell.setRefersToFormula(name + "!$A$1:$A$" + dataArray.length);
							workbook.setSheetHidden(workbook.getSheetIndex(name), true);
						}
						// 加载数据,将名称为hidden的
						dvConstraint = DVConstraint.createFormulaListConstraint(name);
					}
				}
				// 数据有效校验对象
				DataValidation validation = new HSSFDataValidation(addressList, dvConstraint);
				// 校验后错误提示设置
				if (StringUtils.isNotEmpty(validationData.getErrorTitle()) && StringUtils.isNotEmpty(validationData.getErrorMsg())) {
					validation.setShowErrorBox(true);
					validation.createErrorBox(validationData.getErrorTitle(), validationData.getErrorMsg());
				}
				// 列的提示设置
				if (StringUtils.isNotEmpty(validationData.getPromptTitle()) && StringUtils.isNotEmpty(validationData.getPromptContent())) {
					validation.setShowPromptBox(true);
					validation.createPromptBox(validationData.getPromptTitle(), validationData.getPromptContent());
				}
				sheet.addValidationData(validation);
			}
		}
	}

	private static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 构造excel数据
	 *
	 * @param workbook
	 * @param headers 表头
	 * @param result  具体数据
	 * @param sheet
	 */
	private void exportExcel(Workbook workbook, String[] headers, List<List<String>> result, Sheet sheet) {
		//workbook.setSheetName(sheetNum, sheetTitle);  
        // 设置表格默认列宽度为20个字节  
        sheet.setDefaultColumnWidth(20);  
        // 生成一个样式
        CellStyle headstyle =this.getHeadStyle(workbook);
        CellStyle defaultstyle = this.getDefaultStyle(workbook);

        int index = 0;
       while(isMergedRegion(sheet,index,0))
        {
        	index++;	        	
        	if(index>10)
        	{
        		break;
        	}
        }
        
        
        // 产生表格标题行
		Row row = sheet.createRow(index);
        for (int i = 0; i < headers.length; i++) {  
        	sheet.setDefaultColumnStyle(i, defaultstyle);//设置列默认属性
        	sheet.setColumnWidth(i, 20*256);
            Cell cell = row.createCell((short) i);
            cell.setCellStyle(headstyle);  
            cell.setCellValue(headers[i]);  

        }  

        index += 1;
      
//      sheet.createFreezePane( a,b,c,d);  
//      四个参数的含义：
//	        ａ表示要冻结的列数；
//	        ｂ表示要冻结的行数；
//	        ｃ表示右边区域[可见]的首列序号；
//	   ｄ表示下边区域[可见]的首行序号；
        sheet.createFreezePane(0, index+1, 0, index+1);
        // 遍历集合数据，产生数据行  
        if (result != null) {  
       
            for (List<String> m : result) {  
                row = sheet.createRow(index);  
                int cellIndex = 0;  
                for (String str : m) { 
                	if(str==null) {
                		str = "";
                	}
                    Cell cell = row.createCell((short) cellIndex);
                    cell.setCellValue(str.toString());  
                    cellIndex++;  
                }  
                index++;  
            }  
        }
	}

	// 和并单元格格式样式设置
	public CellStyle getMergedtStyle(Workbook workbook)  {
		if(mergedtStyle==null)
		{
			mergedtStyle = workbook.createCellStyle();
			// 设置背景色
			mergedtStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());  // 设置单元格背景填充色
			mergedtStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);// 设置填充方式
			// 设置边框
			mergedtStyle.setBorderBottom(CellStyle.BORDER_THIN); //下边框
			mergedtStyle.setBorderLeft(CellStyle.BORDER_THIN);   //左边框
			mergedtStyle.setBorderRight(CellStyle.BORDER_THIN);   //上边框
			mergedtStyle.setBorderTop(CellStyle.BORDER_THIN);  //右边框
			// 设置居中
			mergedtStyle.setAlignment(CellStyle.ALIGN_GENERAL);  //设置水平方向字体向左齐
			mergedtStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 设置垂直方向字体居中
			// 设置字体样式
			Font font = this.getDefaultFont(workbook);
			mergedtStyle.setFont(font);   // 设置单元格字体颜色
			// 指定当单元格内容显示不下时自动换行
			mergedtStyle.setWrapText(true);
		}
		return mergedtStyle;
	}

	// 正常单元格格式样式设置
	public CellStyle getDefaultStyle(Workbook workbook) {
		if(defaultStyle==null)
		{
			defaultStyle = workbook.createCellStyle();
			// 设置背景色
			defaultStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());  // 设置单元格背景填充色
			defaultStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);// 设置填充方式
			// 设置边框
			defaultStyle.setBorderBottom(CellStyle.BORDER_THIN); //下边框
			defaultStyle.setBorderLeft(CellStyle.BORDER_THIN);   //左边框
			defaultStyle.setBorderRight(CellStyle.BORDER_THIN);   //上边框
			defaultStyle.setBorderTop(CellStyle.BORDER_THIN);  //右边框
			// 设置居中
			defaultStyle.setAlignment(CellStyle.ALIGN_CENTER);  //设置水平方向字体居中
			defaultStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 设置垂直方向字体居中

			DataFormat format = workbook.createDataFormat();
			defaultStyle.setDataFormat(format.getFormat("@"));

			//设置字体样式
			Font font = this.getDefaultFont(workbook);
			defaultStyle.setFont(font);   // 设置单元格字体颜色
			// 指定当单元格内容显示不下时自动换行
			defaultStyle.setWrapText(true);
		}
		return defaultStyle;
	}


	/**
	 * 表头单元格格式样式设置
	 * @param workbook
	 * @return
	 */
	public CellStyle getHeadStyle(Workbook workbook) {

		if (headStyle == null) {
			headStyle = workbook.createCellStyle();
			// 设置背景色
			headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);// 设置填充方式
			// 设置边框
			headStyle.setBorderBottom(CellStyle.BORDER_THIN); //下边框
			headStyle.setBorderLeft(CellStyle.BORDER_THIN);   //左边框
			headStyle.setBorderRight(CellStyle.BORDER_THIN);   //上边框
			headStyle.setBorderTop(CellStyle.BORDER_THIN);  //右边框
			// 设置居中
			headStyle.setAlignment(CellStyle.ALIGN_CENTER);  //设置水平方向字体居中
			headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 设置垂直方向字体居中

			DataFormat format = workbook.createDataFormat();
			headStyle.setDataFormat(format.getFormat("@"));
			// 设置字体样式
			Font font = this.getDefaultBoldFont(workbook);
			headStyle.setFont(font);
			// 指定当单元格内容显示不下时自动换行
			headStyle.setWrapText(false);
		}
		return headStyle;
	}

	// 字体设置
	private Font _defaultFont=null;
	public Font getDefaultFont(Workbook workbook) {
		if (_defaultFont == null) {
			_defaultFont = workbook.createFont();
			_defaultFont.setFontHeightInPoints((short) 10); //设置excel数据字体大小
			_defaultFont.setBoldweight(Font.BOLDWEIGHT_NORMAL); // 正常显示
		}
		return _defaultFont;
	}

	// 表头字体设置
	private Font _defaultBoldFont=null;
	public Font getDefaultBoldFont(Workbook workbook) {
		if (_defaultBoldFont == null) {
			_defaultBoldFont = workbook.createFont();
			_defaultBoldFont.setFontHeightInPoints((short) 12); //设置excel数据字体大小
			_defaultBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD); //粗体显示
		}
		return _defaultBoldFont;
	}




}
