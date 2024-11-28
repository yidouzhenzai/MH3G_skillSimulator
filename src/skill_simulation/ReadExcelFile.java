package skill_simulation;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



public class ReadExcelFile {
    private Object[][] data;

    public ReadExcelFile(String filePath, String sheetName) throws IOException {
        // Excelファイルを読み込む
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

        // Object型の配列を作成
        data = new Object[rowCount][colCount];

        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            data[i][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i][j] = (int) cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            data[i][j] = cell.getBooleanCellValue();
                            break;
                        default:
                            data[i][j] = null; // 空セルはnull
                    }
                }
            }
        }
        workbook.close();
        fis.close();
    }

    public Object[][] getData() {
        return data;
    }
}
