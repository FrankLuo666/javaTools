package AddSheetOfExcel;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 最开始，删除出了sheet1和sheet2之外的所有sheet。
 从第1个sheet的第23行开始，取出这一行几个字段第值。
 每取得1行，就新建一个sheet，以sheet2为模板，将从sheet1取得的值，插入到新sheet的指定cell中。
 最后删除模板sheet2。

 sheet1的几个字段及其值的例子：
 num	functionId	modifier	functionName(logical)	functionName(physical)
 1	001	public	メソード１	function1
 2	002	private	メソード２	function2
 3	003	private	メソード３	function3
 */

public class ReadExcelAndAddSheet {
    public static void main(String[] args) {
        try {
            // 指定Excel文件路径
            String filePath = "/Users/luo/workSpace/java/tools/src/main/java/AddSheetOfExcel/testExcel.xlsx";

            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fis);

            // 获取工作簿中的所有Sheet数量
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历所有Sheet，删除除了Sheet1和Sheet2之外的所有sheet
            for (int i = sheetCount - 1; i >= 0; i--) {
                if (i != 0 && i != 1) {
                    workbook.removeSheetAt(i);
                }
            }

            // 获取第一个Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 从第23行开始读取每一行的前5个字段
            int startRow = 22; // 第23行对应索引为22
            int endRow = sheet.getLastRowNum();

            // 从第2个Sheet开始创建新的Sheet
            for (int i = 1; i <= endRow - startRow + 1; i++) {
                Row row = sheet.getRow(startRow + i - 1);
                if (row == null) {
                    break; // 如果第一个字段为空，中断循环
                }

                String functionNameLogical = getCellValueAsString(row.getCell(3));

                // 复制第二个Sheet作为模板
                Sheet templateSheet = workbook.getSheetAt(1);
                Sheet newSheet = workbook.cloneSheet(1);
                int newSheetIndex = workbook.getSheetIndex(newSheet);
                workbook.setSheetName(newSheetIndex, "functionName_"+functionNameLogical);

                // 复制模板Sheet的内容到新Sheet
                copySheet(templateSheet, newSheet);

                // 在新Sheet的第6行创建单元格，并在6C写入字段值
                Row row6 = newSheet.getRow(5);
                Cell modifierCell = row6.createCell(2);
                // 在新Sheet的第7行创建单元格，并在7C，7F，7M写入字段值
                Row row7 = newSheet.getRow(6);
                Cell functionIdCell = row7.createCell(2);
                Cell functionNameLogicalCell = row7.createCell(5);
                Cell functionNamePhysicalCell = row7.createCell(12);

                // 取得sheet1的值
                Cell functionIdCellSheet1 = sheet.getRow(startRow + i - 1).getCell(1);
                Cell modifierCellSheet1 = sheet.getRow(startRow + i - 1).getCell(2);
                Cell functionNameLogicalCellSheet1 = sheet.getRow(startRow + i - 1).getCell(3);
                Cell functionNamePhysicalCellSheet1 = sheet.getRow(startRow + i - 1).getCell(4);

                // 将sheet1的值设置到新sheet中
                modifierCell.setCellValue(getCellValueAsString(modifierCellSheet1));
                functionIdCell.setCellValue(getCellValueAsString(functionIdCellSheet1));
                functionNamePhysicalCell.setCellValue(getCellValueAsString(functionNamePhysicalCellSheet1));
                functionNameLogicalCell.setCellValue(getCellValueAsString(functionNameLogicalCellSheet1));

                // 输出字段值
                System.out.println("FunctionId: " + getCellValueAsString(functionIdCell));
                System.out.println("Modifier: " + getCellValueAsString(modifierCell));
                System.out.println("FunctionName (Logical): " + getCellValueAsString(functionNameLogicalCell));
                System.out.println("FunctionName (Physical): " + getCellValueAsString(functionNamePhysicalCell));
            }

            // 删除原始的Sheet2
            workbook.removeSheetAt(1);

            // 保存修改后的Excel文件
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copySheet(Sheet sourceSheet, Sheet targetSheet) {
        for (int rowIndex = 0; rowIndex <= sourceSheet.getLastRowNum(); rowIndex++) {
            Row sourceRow = sourceSheet.getRow(rowIndex);
            Row targetRow = targetSheet.createRow(rowIndex);

            if (sourceRow != null) {
                for (int colIndex = 0; colIndex < sourceRow.getLastCellNum(); colIndex++) {
                    Cell sourceCell = sourceRow.getCell(colIndex);
                    Cell targetCell = targetRow.createCell(colIndex);

                    if (sourceCell != null) {
                        targetCell.setCellType(sourceCell.getCellType());

                        switch (sourceCell.getCellType()) {
                            case STRING:
                                targetCell.setCellValue(sourceCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                targetCell.setCellValue(sourceCell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                targetCell.setCellFormula(sourceCell.getCellFormula());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }
}
