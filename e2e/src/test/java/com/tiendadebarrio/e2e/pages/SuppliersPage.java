package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SuppliersPage extends BasePage {
    public SuppliersPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Proveedores']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Proveedores']")).getText().trim();
    }

    public WebElement newButton() {
        return visible(By.xpath("//button[contains(.,'Nuevo proveedor')]"));
    }

    public void openCreate() {
        click(By.xpath("//button[contains(.,'Nuevo proveedor')]"));
        visible(By.xpath("//h2[contains(.,'Nuevo proveedor')]"));
    }

    public void submitEmpty() {
        click(By.xpath("//button[contains(.,'Guardar')]"));
    }

    public String nameError() {
        return visible(By.xpath("//p[contains(.,'El nombre es obligatorio')]")).getText().trim();
    }

    public void fill(String name, String nit, String phone) {
        type(By.cssSelector("input[placeholder='Nombre o razón social']"), name);
        if (nit != null && !nit.isBlank()) {
            type(By.xpath("//label[normalize-space()='NIT']/following::input[1]"), nit);
        }
        if (phone != null && !phone.isBlank()) {
            type(By.xpath("//label[normalize-space()='Teléfono']/following::input[1]"), phone);
        }
    }

    public boolean saveCreated() {
        click(By.xpath("//button[contains(.,'Guardar')]"));
        return snackbarContains("Proveedor creado correctamente");
    }

    public void search(String query) {
        type(By.cssSelector("input[placeholder*='Buscar por nombre, NIT']"), query);
        wait.until(d -> tableContains(query));
    }

    public boolean tableContains(String text) {
        return super.tableContains(text);
    }
}
