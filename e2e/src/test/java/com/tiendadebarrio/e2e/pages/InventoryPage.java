package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InventoryPage extends BasePage {
    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Inventario']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Inventario']")).getText().trim();
    }

    public void openIncoming() {
        click(By.xpath("//button[contains(.,'Ajuste de entrada')]"));
        visible(By.xpath("//h2[contains(.,'Ajuste de entrada')]"));
    }

    public void searchProduct(String query) {
        type(By.cssSelector("input[placeholder='Buscar por nombre o código de barras']"), query);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul button")));
        click(By.cssSelector("ul button"));
    }

    public void setQuantity(String quantity) {
        type(By.cssSelector("input[formcontrolname='quantity']"), quantity);
    }

    public void saveAdjustment() {
        click(By.xpath("//button[contains(.,'Registrar entrada') or contains(.,'Registrar salida')]"));
    }

    public boolean snackbarContains(String message) {
        return super.snackbarContains(message);
    }

    public WebElement incomingButton() {
        return visible(By.xpath("//button[contains(.,'Ajuste de entrada')]"));
    }

    public WebElement outgoingButton() {
        return visible(By.xpath("//button[contains(.,'Ajuste de salida')]"));
    }

    public void searchMovements(String query) {
        type(By.cssSelector("input[placeholder='Buscar por producto o código de barras']"), query);
        visible(testId("inventory-search")).sendKeys(Keys.TAB);
    }
}
