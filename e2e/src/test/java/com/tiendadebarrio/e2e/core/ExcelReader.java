package com.tiendadebarrio.e2e.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ExcelReader {
    private static final DataFormatter FORMATTER = new DataFormatter();

    private ExcelReader() {}

    public static List<ExcelRow> read(Path file, String sheetName) {
        if (!Files.exists(file)) {
            throw new IllegalStateException("No existe el Excel de datos: " + file);
        }
        try (Workbook workbook = WorkbookFactory.create(file.toFile())) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalStateException("La hoja " + sheetName + " no tiene encabezados");
            }
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                headers.add(FORMATTER.formatCellValue(headerRow.getCell(i)).trim());
            }
            List<ExcelRow> rows = new ArrayList<>();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                boolean any = false;
                for (int c = 0; c < headers.size(); c++) {
                    String header = headers.get(c);
                    if (header.isBlank()) {
                        continue;
                    }
                    String value = cell(row.getCell(c));
                    if (!value.isBlank()) {
                        any = true;
                    }
                    map.put(header, value);
                }
                if (!any) {
                    continue;
                }
                ExcelRow excelRow = new ExcelRow(map);
                if (excelRow.execute()) {
                    rows.add(excelRow);
                }
            }
            return rows;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer " + file, ex);
        }
    }

    public static ExcelRow first(Path file, String sheetName) {
        List<ExcelRow> rows = read(file, sheetName);
        if (rows.isEmpty()) {
            throw new IllegalStateException("No hay filas con Ejecutar=Sí en " + file.getFileName() + " / " + sheetName);
        }
        return rows.getFirst();
    }

    public static ExcelRow byCase(Path file, String sheetName, String caseId) {
        String wanted = caseId.trim().toLowerCase(Locale.ROOT);
        return read(file, sheetName).stream()
                .filter(row -> row.get("CasoID").equalsIgnoreCase(wanted) || row.get("CasoId").equalsIgnoreCase(wanted))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay fila Ejecutar=Sí con CasoID=" + caseId + " en " + file.getFileName()));
    }

    private static String cell(Cell cell) {
        return FORMATTER.formatCellValue(cell).trim();
    }
}
