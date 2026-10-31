package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ReportsPage extends BasePage {
    public ReportsPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Reportes']"));
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(d ->
                shown(By.xpath("//p[normalize-space()='Ventas totales']"))
                        || shown(By.xpath("//p[contains(.,'error al cargar')]")));
        visible(By.xpath("//p[normalize-space()='Ventas totales']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Reportes']")).getText().trim();
    }

    public WebElement todayButton() {
        return visible(By.xpath("//button[normalize-space()='Hoy']"));
    }

    public WebElement applyButton() {
        return visible(By.xpath("//button[contains(.,'Aplicar filtros')]"));
    }

    public void clickToday() {
        click(By.xpath("//button[normalize-space()='Hoy']"));
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(d ->
                shown(By.xpath("//p[normalize-space()='Ventas totales']"))
                        && !shown(By.xpath("//p[contains(.,'Cargando reportes')]")));
    }

    public boolean hasSalesTotalCard() {
        return shown(By.xpath("//p[normalize-space()='Ventas totales']"));
    }
}
