package com.tiendadebarrio.e2e.core;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {
    private ScreenshotUtil() {}

    public static Path capture(WebDriver driver, String name) {
        if (driver == null) {
            return null;
        }
        try {
            Path dir = ProjectPaths.reports().resolve("screenshots");
            Files.createDirectories(dir);
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss-SSS"));
            Path file = dir.resolve(sanitize(name) + "-" + stamp + ".png");
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, bytes);
            return file;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar la captura", ex);
        }
    }

    public static void attach(WebDriver driver, Status status, String title) {
        if (ExtentManager.current() == null || driver == null) {
            return;
        }
        Path file = capture(driver, title);
        try {
            ExtentManager.current().log(
                    status,
                    title,
                    MediaEntityBuilder.createScreenCaptureFromPath(file.toAbsolutePath().toString()).build());
        } catch (Exception ex) {
            ExtentManager.current().log(status, title + " (no se pudo incrustar la captura)");
        }
    }

    public static void milestone(WebDriver driver, String title) {
        attach(driver, Status.PASS, title);
    }

    public static void failure(WebDriver driver, String title) {
        attach(driver, Status.FAIL, title);
    }

    private static String sanitize(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9-_]+", "-");
    }
}
