package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PosPage extends BasePage {
    public PosPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Punto de Venta']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Punto de Venta']")).getText().trim();
    }

    public boolean cashClosedWarning() {
        return shown(By.xpath("//a[contains(.,'Ir a Caja')]"))
                || shown(By.xpath("//p[contains(.,'Debes abrir caja')]"));
    }

    public void scan(String barcode) {
        type(By.cssSelector("input[placeholder*='Escanear código']"), barcode);
        visible(By.cssSelector("input[placeholder*='Escanear código']")).sendKeys(Keys.ENTER);
        wait.until(d ->
                cartHasProduct(barcode)
                        || !d.findElements(By.xpath("//p[contains(.,'coincidencia')]")).isEmpty()
                        || shown(By.xpath("//p[contains(.,'Producto no encontrado')]")));
        if (!cartHasProduct(barcode)) {
            var results = driver.findElements(By.xpath("//p[contains(@class,'text-sm') and contains(@class,'font-medium')]/ancestor::button"));
            if (!results.isEmpty()) {
                results.getFirst().click();
            }
        }
    }

    public void choosePayment(String method) {
        String label = switch (method) {
            case "TRANSFER" -> "Transferencia";
            case "CARD" -> "Tarjeta";
            default -> "Efectivo";
        };
        click(By.xpath("//h2[contains(.,'Método de pago')]/following-sibling::div//button[contains(.,'" + label + "')]"));
    }

    public void registerSale() {
        click(By.xpath("//button[contains(.,'Registrar venta')]"));
    }

    public boolean snackbarContains(String message) {
        return super.snackbarContains(message);
    }

    public boolean cartHasProduct(String nameOrBarcode) {
        String xpath = "//h2[contains(.,'Carrito de venta')]/ancestor::div[contains(@class,'rounded-2xl')][1]//td[contains(.,'"
                + nameOrBarcode + "')]";
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    public WebElement registerButton() {
        return visible(By.xpath("//button[contains(.,'Registrar venta')]"));
    }
}
