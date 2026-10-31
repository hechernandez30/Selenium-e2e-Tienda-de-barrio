package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CustomersPage extends BasePage {
    public CustomersPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[normalize-space()='Clientes']"));
    }

    public String title() {
        return visible(By.xpath("//h1[normalize-space()='Clientes']")).getText().trim();
    }

    public WebElement newButton() {
        return visible(By.xpath("//button[contains(.,'Nuevo cliente')]"));
    }

    public void openCreate() {
        click(By.xpath("//button[contains(.,'Nuevo cliente')]"));
        visible(By.xpath("//h2[normalize-space()='Nuevo cliente']"));
    }

    public void submitEmpty() {
        click(By.xpath("//button[contains(.,'Guardar')]"));
    }

    public String nameError() {
        return visible(By.xpath("//p[contains(.,'El nombre completo es obligatorio')]")).getText().trim();
    }

    public void fill(String name, String nit, String phone) {
        type(By.cssSelector("input[placeholder='Nombre del cliente']"), name);
        if (nit != null && !nit.isBlank()) {
            type(By.xpath("//label[contains(.,'NIT')]/following::input[1]"), nit);
        }
        if (phone != null && !phone.isBlank()) {
            type(By.cssSelector("input[placeholder='Opcional (8 dígitos)']"), phone);
        }
    }

    public boolean saveCreated() {
        click(By.xpath("//button[contains(.,'Guardar')]"));
        return snackbarContains("Cliente creado correctamente");
    }

    public void search(String query) {
        type(By.cssSelector("input[placeholder*='Buscar por nombre, NIT']"), query);
        wait.until(d -> tableContains(query));
    }

    public boolean tableContains(String text) {
        return super.tableContains(text);
    }
}
