package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CashPage extends BasePage {
    public CashPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Caja']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Caja']")).getText().trim();
    }

    public boolean isOpen() {
        return shown(By.xpath("//button[contains(.,'Cerrar caja')]"))
                || shown(By.xpath("//button[contains(.,'Nuevo movimiento')]"));
    }

    public boolean isClosed() {
        return shown(By.xpath("//button[contains(.,'Abrir caja')]"));
    }

    public void openCash(String amount) {
        click(By.xpath("//button[contains(.,'Abrir caja')]"));
        type(By.cssSelector("input[formcontrolname='openingAmount']"), amount);
        click(By.xpath("//form//button[@type='submit' and contains(.,'Abrir caja')]"));
    }

    public void closeCash(String counted) {
        click(By.xpath("//button[contains(.,'Cerrar caja')]"));
        type(By.cssSelector("input[formcontrolname='countedAmount']"), counted);
        click(By.xpath("//form//button[@type='submit' and contains(.,'Cerrar caja')]"));
    }

    public boolean snackbarContains(String message) {
        return super.snackbarContains(message);
    }

    public WebElement statusCard() {
        return visible(By.xpath("//p[normalize-space()='Estado']/following-sibling::p"));
    }
}
