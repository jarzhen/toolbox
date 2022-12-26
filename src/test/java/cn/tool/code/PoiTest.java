package cn.tool.code;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 利用Apache Poi实现动态公式计算
 *
 * @author BBF
 */
public class PoiTest {

  private static final CellValue DEFAULT_VALUE = new CellValue("");

  /**
   * 获取单元格
   * <p>如果单元格没有值，需要构建单元格</p>
   *
   * @param row 行对象
   * @param idx 单元格的列索引号，从0开始
   * @return 单元格对象
   */
  private static Cell getCell(Row row, int idx) {
    Cell cell = row.getCell(idx);
    if (cell == null) {
      cell = row.createCell(idx);
    }
    return cell;
  }

  /**
   * 重新计算单元格的值
   * <p style="color:red">特别注意：当动态改变excel的值的时候，必须用本方法重新用公式计算单元格的值</p>
   *
   * @param cell 单元格
   * @return {@link org.apache.poi.ss.usermodel.CellValue}
   */
  private static CellValue calcCellValue(Cell cell) {
    Workbook workbook = cell.getSheet().getWorkbook();
    FormulaEvaluator formulaEvaluator = null;
    if (cell instanceof HSSFCell) {
      // Excel 2003
      formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
    } else if (cell instanceof XSSFCell) {
      // Excel 2007+
      formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
    }
    if (formulaEvaluator != null) {
      // 进行计算并拿到值
      return formulaEvaluator.evaluate(cell);
    }
    return DEFAULT_VALUE;
  }
  
  /**
   * 重新设置公式和值，并读取公式计算后的值
   *
   * @param args Main函数默认入参
   */
  public static void main(String[] args) {
    // 创建空对象（xssf - excel2007+；hssf - excel2003）
    Workbook hw = new XSSFWorkbook();
    // 创建sheet
    Sheet sheet = hw.createSheet();
    // 定位excel的行
    Row r1 = sheet.createRow(0);
    // 调用公式取值
    Cell c1 = getCell(r1, 0);
    // 重新设置单元格的公式，不允许等号开头！！此公式直接从excel文件中复制出来。
    c1.setCellFormula("SQRT(POWER(B1+C1,2)/D1)");
    // 传入值，依次是B1、C1、D1
    getCell(r1, 1).setCellValue(9);
    getCell(r1, 2).setCellValue(8);
    getCell(r1, 3).setCellValue(4);
    // 获取c1的值，也就是公式计算后的值
    CellValue v1 = calcCellValue(c1);
    System.out.println("单元格的值：" + v1.formatAsString());
    c1.setCellFormula("FACT(B1)");
    System.out.println("阶乘值： " + calcCellValue(c1).getNumberValue());
  }
}