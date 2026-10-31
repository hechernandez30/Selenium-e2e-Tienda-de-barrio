package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UsersPage extends BasePage {
    public UsersPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[contains(.,'Usuarios')]"));
    }

    public String title() {
        return visible(By.xpath("//h1[contains(.,'Usuarios')]")).getText().trim();
    }

    public WebElement newButton() {
        return visible(By.xpath("//button[contains(.,'Nuevo usuario')]"));
    }

    public void openCreate() {
        click(By.xpath("//button[contains(.,'Nuevo usuario')]"));
        visible(By.xpath("//h2[normalize-space()='Nuevo usuario']"));
    }

    public void submitEmpty() {
        click(By.xpath("//button[contains(.,'Crear usuario')]"));
    }

    public String usernameError() {
        return visible(By.xpath("//p[contains(.,'El usuario es obligatorio')]")).getText().trim();
    }

    public void fillCreate(String username, String password, String firstName, String lastName, String role) {
        type(By.cssSelector("input[formcontrolname='username']"), username);
        type(By.cssSelector("input[formcontrolname='password']"), password);
        type(By.cssSelector("input[formcontrolname='confirmPassword']"), password);
        type(By.cssSelector("input[formcontrolname='firstName']"), firstName);
        type(By.cssSelector("input[formcontrolname='lastName']"), lastName);
        wait.until(d -> d.findElements(By.cssSelector("select[formcontrolname='roleId'] option")).stream()
                .anyMatch(option -> option.getText() != null && option.getText().contains(role)));
        selectOptionContaining(By.cssSelector("select[formcontrolname='roleId']"), role);
    }

    public boolean saveCreated() {
        click(By.xpath("//button[contains(.,'Crear usuario')]"));
        return snackbarContains("Usuario creado correctamente");
    }

    public boolean navHas(String label) {
        return shown(By.xpath("//nav//a[contains(.,'" + label + "')]"));
    }
}
