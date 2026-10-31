package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PurchasesPage extends BasePage {
    public PurchasesPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Compras']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Compras']")).getText().trim();
    }

    public WebElement newButton() {
        return visible(By.xpath("//button[contains(.,'Nueva compra')]"));
    }

    public void openCreate() {
        click(By.xpath("//button[contains(.,'Nueva compra')]"));
        visible(By.xpath("//h2[normalize-space()='Nueva compra']"));
    }

    public void savePurchase() {
        click(By.xpath("//button[contains(.,'Guardar compra')]"));
    }

    public String supplierError() {
        return visible(By.xpath("//p[contains(.,'El proveedor es obligatorio')]")).getText().trim();
    }

    public void selectSupplier(String fragment) {
        selectOptionContaining(By.cssSelector("select[formcontrolname='supplierId']"), fragment);
    }

    public void addProduct(String query) {
        type(By.cssSelector("input[placeholder*='Buscar producto']"), query);
        By result = By.xpath("//ul//button[contains(.,'" + query + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(result));
        click(result);
    }

    public void setFirstLine(String quantity, String unitCost) {
        if (quantity != null && !quantity.isBlank()) {
            type(By.xpath("(//table//tbody//input[@type='number'])[1]"), quantity);
        }
        if (unitCost != null && !unitCost.isBlank()) {
            type(By.xpath("(//table//tbody//input[@type='number'])[2]"), unitCost);
        }
    }

    public boolean saveDraft() {
        savePurchase();
        return snackbarContains("Compra creada en borrador");
    }

    public void confirmFirstDraft(String supplierFragment) {
        By confirmInRow = By.xpath(
                "//tr[.//td[contains(.,'" + supplierFragment + "')] and .//button[@title='Confirmar']]//button[@title='Confirmar']");
        wait.until(ExpectedConditions.elementToBeClickable(confirmInRow));
        click(confirmInRow);
        visible(By.xpath("//h2[normalize-space()='Confirmar compra']"));
        click(By.xpath("//h2[normalize-space()='Confirmar compra']/following::button[normalize-space()='Confirmar'][1]"));
    }

    public boolean snackbar(String message) {
        return snackbarContains(message);
    }
}
