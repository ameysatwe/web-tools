package com.example.assignment2;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DateUtil;


@WebServlet(name="XlsServlet",value = "/excel.xls")
public class XlsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
//        Row row;
        try{
            PrintWriter out = resp.getWriter();
            File excelFile = new File((getServletContext().getRealPath("/")  + "WEB-INF/" + "store.xls"));
            if (!excelFile.exists()) {
                throw new FileNotFoundException("Excel file not found at: " + excelFile);
            }
            FileInputStream fis = new FileInputStream(excelFile);

            Workbook workbook = new HSSFWorkbook(fis);
            Sheet spreadsheet = workbook.getSheetAt(0);
            Iterator < Row >  rowIterator = spreadsheet.iterator();
            out.println("<html><body><table style='border: 1px solid black;'>");
            if (rowIterator.hasNext()) {
                // Print the table headers (first row of the Excel sheet)
                Row headerRow = rowIterator.next();
                out.println("<thead><tr style='background-color: #f2f2f2;border: 1px solid black;'>");
                for (Cell cell : headerRow) {
                    out.println("<th>" + cell.getStringCellValue() + "</th>");
                }
                out.println("</tr></thead>");
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                out.println("<tr>");

                // Loop through each cell in the row
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    // Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case STRING:
                            out.println("<td>" + cell.getStringCellValue() + "</td>");
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                out.println("<td>" + cell.getDateCellValue() + "</td>");
                            } else {
                                out.println("<td>" + cell.getNumericCellValue() + "</td>");
                            }
                            break;
                        case BOOLEAN:
                            out.println("<td>" + cell.getBooleanCellValue() + "</td>");
                            break;
                        case FORMULA:
                            out.println("<td>" + cell.getCellFormula() + "</td>");
                            break;
                        default:
                            out.println("<td></td>"); // Empty cell
                            break;
                    }
                }
                out.println("</tr>");
            }
            out.println("</table></body> </html>");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
