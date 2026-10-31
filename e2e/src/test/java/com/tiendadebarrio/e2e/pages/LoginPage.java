package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUrl, String route) {
        String url = trimSlash(baseUrl) + (route.startsWith("/") ? route : "/" + route);
        driver.get(url);
        visible(By.xpath("//p[contains(.,'Ingresa tus credenciales para continuar')]"));
        step("Abrir " + url);
    }

    public String subtitle() {
        return visible(By.xpath("//p[contains(.,'Ingresa tus credenciales para continuar')]")).getText().trim();
    }

    public WebElement username() {
        return visible(testId("login-username", "#username"));
    }

    public WebElement password() {
        return visible(testId("login-password", "#password"));
    }

    public WebElement submit() {
        return visible(testId("login-submit", "form button[type='submit']"));
    }

    public String usernameLabel() {
        return visible(By.cssSelector("label[for='username']")).getText().trim();
    }

    public String passwordLabel() {
        return visible(By.cssSelector("label[for='password']")).getText().trim();
    }

    public void typeUsername(String value) {
        type(testId("login-username", "#username"), value);
    }

    public void typePassword(String value) {
        type(testId("login-password", "#password"), value);
    }

    public void submitLogin() {
        click(testId("login-submit", "form button[type='submit']"));
    }

    public void login(String username, String password) {
        typeUsername(username);
        typePassword(password);
        submitLogin();
    }

    public void togglePasswordVisibility() {
        click(testId("login-toggle-password", "button[aria-label='Mostrar contraseña'], button[aria-label='Ocultar contraseña']"));
    }

    public String passwordType() {
        return password().getAttribute("type");
    }

    public String usernameError() {
        return visible(By.xpath("//p[contains(.,'El usuario es obligatorio')]")).getText().trim();
    }

    public String passwordError() {
        return visible(By.xpath("//p[contains(.,'La contraseña es obligatoria')]")).getText().trim();
    }

    public boolean usernameErrorShown() {
        return shown(By.xpath("//p[contains(.,'El usuario es obligatorio')]"));
    }

    public boolean passwordErrorShown() {
        return shown(By.xpath("//p[contains(.,'La contraseña es obligatoria')]"));
    }

    public String backendError() {
        return visible(By.xpath("//span[contains(normalize-space(.),'Usuario o contraseña incorrectos')]")).getText().trim();
    }

    public boolean backendErrorShown() {
        return shown(By.xpath("//*[contains(.,'Usuario o contraseña incorrectos')]"));
    }

    private static String trimSlash(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }
}
