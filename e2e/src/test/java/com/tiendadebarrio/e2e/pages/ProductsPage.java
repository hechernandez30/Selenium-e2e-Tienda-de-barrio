package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductsPage extends BasePage {
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Productos']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Productos']")).getText().trim();
    }

    public void openCreate() {
        click(By.xpath("//button[contains(.,'Nuevo producto')]"));
        visible(By.xpath("//h2[normalize-space()='Nuevo producto']"));
    }

    public void fillCreate(String barcode, String name, String purchase, String sale, String stock) {
        type(By.cssSelector("input[placeholder='Ej: 7501234567890']"), barcode);
        type(By.cssSelector("input[placeholder='Nombre del producto']"), name);
        type(By.xpath("//label[contains(.,'Precio de compra')]/following::input[1]"), purchase);
        type(By.xpath("//label[contains(.,'Precio de venta')]/following::input[1]"), sale);
        if (!driver.findElements(By.xpath("//label[contains(.,'Stock inicial')]/following::input[1]")).isEmpty()) {
            type(By.xpath("//label[contains(.,'Stock inicial')]/following::input[1]"), stock);
        }
    }

    public boolean saveCreated() {
        click(By.xpath("//button[contains(.,'Guardar')]"));
        return snackbarContains("Producto creado correctamente");
    }

    public void submitEmpty() {
        click(By.xpath("//button[contains(.,'Guardar')]"));
    }

    public String barcodeError() {
        return visible(By.xpath("//p[contains(.,'El código de barras es obligatorio')]")).getText().trim();
    }

    public String nameError() {
        return visible(By.xpath("//p[contains(.,'El nombre es obligatorio')]")).getText().trim();
    }

    public void search(String query) {
        type(By.cssSelector("input[placeholder*='Buscar por nombre']"), query);
        step("Buscar producto: " + query);
    }

    public boolean tableContains(String text) {
        return driver.findElements(By.xpath("//table//td[contains(.,'" + text + "')]")).size() > 0;
    }

    public boolean snackbarContains(String message) {
        return super.snackbarContains(message);
    }

    public WebElement newButton() {
        return visible(By.xpath("//button[contains(.,'Nuevo producto')]"));
    }
}
