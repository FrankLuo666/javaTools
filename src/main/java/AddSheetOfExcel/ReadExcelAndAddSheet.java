package AddSheetOfExcel;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 �ʼ��ɾ������sheet1��sheet2֮�������sheet��
 �ӵ�1��sheet�ĵ�23�п�ʼ��ȡ����һ�м����ֶε�ֵ��
 ÿȡ��1�У����½�һ��sheet����sheet2Ϊģ�壬����sheet1ȡ�õ�ֵ�����뵽��sheet��ָ��cell�С�
 ���ɾ��ģ��sheet2��

 sheet1�ļ����ֶμ���ֵ�����ӣ�
 num	functionId	modifier	functionName(logical)	functionName(physical)
 1	001	public	�᥽�`�ɣ�	function1
 2	002	private	�᥽�`�ɣ�	function2
 3	003	private	�᥽�`�ɣ�	function3
 */

public class ReadExcelAndAddSheet {
    public static void main(String[] args) {
        try {
            // ָ��Excel�ļ�·��
            String filePath = "/Users/luo/workSpace/java/tools/src/main/java/AddSheetOfExcel/testExcel.xlsx";

            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fis);

            // ��ȡ�������е�����Sheet����
            int sheetCount = workbook.getNumberOfSheets();
            // ��������Sheet��ɾ������Sheet1��Sheet2֮�������sheet
            for (int i = sheetCount - 1; i >= 0; i--) {
                if (i != 0 && i != 1) {
                    workbook.removeSheetAt(i);
                }
            }

            // ��ȡ��һ��Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // �ӵ�23�п�ʼ��ȡÿһ�е�ǰ5���ֶ�
            int startRow = 22; // ��23�ж�Ӧ����Ϊ22
            int endRow = sheet.getLastRowNum();

            // �ӵ�2��Sheet��ʼ�����µ�Sheet
            for (int i = 1; i <= endRow - startRow + 1; i++) {
                Row row = sheet.getRow(startRow + i - 1);
                if (row == null) {
                    break; // �����һ���ֶ�Ϊ�գ��ж�ѭ��
                }

                String functionNameLogical = getCellValueAsString(row.getCell(3));

                // ���Ƶڶ���Sheet��Ϊģ��
                Sheet templateSheet = workbook.getSheetAt(1);
                Sheet newSheet = workbook.cloneSheet(1);
                int newSheetIndex = workbook.getSheetIndex(newSheet);
                workbook.setSheetName(newSheetIndex, "functionName_"+functionNameLogical);

                // ����ģ��Sheet�����ݵ���Sheet
                copySheet(templateSheet, newSheet);

                // ����Sheet�ĵ�6�д�����Ԫ�񣬲���6Cд���ֶ�ֵ
                Row row6 = newSheet.getRow(5);
                Cell modifierCell = row6.createCell(2);
                // ����Sheet�ĵ�7�д�����Ԫ�񣬲���7C��7F��7Mд���ֶ�ֵ
                Row row7 = newSheet.getRow(6);
                Cell functionIdCell = row7.createCell(2);
                Cell functionNameLogicalCell = row7.createCell(5);
                Cell functionNamePhysicalCell = row7.createCell(12);

                // ȡ��sheet1��ֵ
                Cell functionIdCellSheet1 = sheet.getRow(startRow + i - 1).getCell(1);
                Cell modifierCellSheet1 = sheet.getRow(startRow + i - 1).getCell(2);
                Cell functionNameLogicalCellSheet1 = sheet.getRow(startRow + i - 1).getCell(3);
                Cell functionNamePhysicalCellSheet1 = sheet.getRow(startRow + i - 1).getCell(4);

                // ��sheet1��ֵ���õ���sheet��
                modifierCell.setCellValue(getCellValueAsString(modifierCellSheet1));
                functionIdCell.setCellValue(getCellValueAsString(functionIdCellSheet1));
                functionNamePhysicalCell.setCellValue(getCellValueAsString(functionNamePhysicalCellSheet1));
                functionNameLogicalCell.setCellValue(getCellValueAsString(functionNameLogicalCellSheet1));

                // ����ֶ�ֵ
                System.out.println("FunctionId: " + getCellValueAsString(functionIdCell));
                System.out.println("Modifier: " + getCellValueAsString(modifierCell));
                System.out.println("FunctionName (Logical): " + getCellValueAsString(functionNameLogicalCell));
                System.out.println("FunctionName (Physical): " + getCellValueAsString(functionNamePhysicalCell));
            }

            // ɾ��ԭʼ��Sheet2
            workbook.removeSheetAt(1);

            // �����޸ĺ��Excel�ļ�
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
