package cn.com.liurz.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试excel导出实例
 */
public class ExportExcelDemo {
    public static void main(String[] args) throws Exception {
        ExportExcelDemo demo = new ExportExcelDemo();
        demo.exportDetail();

    }

    //简单导出：标题及内容
    public void simpleExport() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        ExportExcelUtils excelUtil = new ExportExcelUtils();
        // 构造数据
        String[] headers = {"编号","姓名","描述"};
        List<List<String>> result= getData();
        excelUtil.simpleExportExcel(workbook,"sheet1",headers,result);
        //
        OutputStream out = new FileOutputStream("D:\\opt\\test\\simple.xls");
        workbook.write(out);
        IOUtils.closeQuietly(out);
    }

    // 带下拉框及长度校验的导出
    public void export() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        ExportExcelUtils excelUtil = new ExportExcelUtils();
        // 构造数据
        String[] headers = {"编号","姓名","描述"};
        List<List<String>> result= getData();
        // 构造校验对象:一般是根据表头
        List<ExcelValidationData> vildatas = getVildate(headers);
        excelUtil.exportExcel(workbook,"sheet2",headers,result,vildatas);;

        OutputStream out = new FileOutputStream("D:\\opt\\test\\simple.xls");
        workbook.write(out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 带下拉框及长度校验
     * 帮助文档
     * @throws Exception
     */
    public void exportDetail() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        ExportExcelUtils excelUtil = new ExportExcelUtils();
        // 构造数据
        String[] headers = {"编号","姓名","描述"};
        List<List<String>> result= getData();
        // 构造校验对象:一般是根据表头
        List<ExcelValidationData> vildatas = getVildate(headers);
        // 获取帮忙文档
        List<String> helpDocuments = getHelpDocuments(headers);
        excelUtil.exportExcel(workbook,"sheet2",headers,result,vildatas,helpDocuments);;

        OutputStream out = new FileOutputStream("D:\\opt\\test\\simple.xls");
        workbook.write(out);
        IOUtils.closeQuietly(out);
    }

    private List<String> getHelpDocuments(String[] headers) {
        List<String> documents = new ArrayList<>();
        for(int i= 0 ;i<headers.length;i++){
            String data = setHelpDocument(headers[i]);
            documents.add(data);
        }
        return documents;
    }

    private String setHelpDocument(String header) {
        String helpDocuments = "";
        switch (header){
            case "编号":
                helpDocuments="编号:必填，编号不能重复";
                break;
            case "姓名": // 名称下拉
                helpDocuments="姓名:必填，下拉选择";
                break;
            case "描述":   // 字体长度不能差多50  非必填 ：textMinLenth的值要等于0
                helpDocuments="描述:非必填";
                break;
        }
        return helpDocuments;
    }

    private List<ExcelValidationData> getVildate(String[] headers) {
        List<ExcelValidationData> list = new ArrayList<>();
        for(int i= 0 ;i<headers.length;i++){
            ExcelValidationData data = setValidateData(headers[i]);
            list.add(data);
        }
        return list;
    }

    private ExcelValidationData setValidateData(String header) {
        ExcelValidationData data = null;
        switch (header){
            case "编号": // 必填 ：textMinLenth的值要大于0
                data = new ExcelValidationData(0,1,100,"错误提示","编号必填","必填","编号必填！");
                break;
            case "姓名": // 名称下拉
                String[] names = {"张三","李四","王五"};
                data = new ExcelValidationData(1,names,"必填","请填写描述内容！");
                break;
            case "描述":   // 字体长度不能差多50  非必填 ：textMinLenth的值要等于0
                data = new ExcelValidationData(2,0,50,"错误提示","描述字段过长","提示","请填写描述内容！");
                break;
        }
        return data;
    }

    private static List<List<String>> getData() {
        List<List<String>> data = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for(int i=0 ;i< 10;i++){
            row = new ArrayList<>();
            row.add(0+"");
            row.add("xx"+i);
            row.add("描述："+i);
            data.add(row);
        }
        return data;
    }
}
