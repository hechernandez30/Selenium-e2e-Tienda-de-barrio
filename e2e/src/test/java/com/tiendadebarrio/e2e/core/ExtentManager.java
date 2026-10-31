package com.tiendadebarrio.e2e.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();
    private static Path reportFile;

    private ExtentManager() {}

    public static synchronized ExtentReports get() {
        if (extent == null) {
            try {
                Files.createDirectories(ProjectPaths.reports());
            } catch (IOException ex) {
                throw new IllegalStateException("No se pudo crear la carpeta de reportes", ex);
            }
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            reportFile = ProjectPaths.reports().resolve("informe-e2e-" + stamp + ".html");
            ExtentSparkReporter spark = new ExtentSparkReporter(reportFile.toString());
            spark.config().setDocumentTitle("Informe E2E - Tienda de Barrio");
            spark.config().setReportName("Pruebas automatizadas Selenium 4");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setEncoding("UTF-8");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Sistema", "Tienda de Barrio Web");
            extent.setSystemInfo("Navegador", "Google Chrome");
            extent.setSystemInfo("Motor", "Selenium 4 + TestNG");
        }
        return extent;
    }

    public static Path reportFile() {
        get();
        return reportFile;
    }

    public static ExtentTest current() {
        return TEST.get();
    }

    public static void setTest(ExtentTest test) {
        TEST.set(test);
    }

    public static void remove() {
        TEST.remove();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
