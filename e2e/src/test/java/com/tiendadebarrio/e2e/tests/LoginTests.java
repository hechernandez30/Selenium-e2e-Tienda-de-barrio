package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseE2eTest {

    @Test(groups = {"auth", "p0", "global"}, description = "LOG-UI: inspección de elementos, placeholders, tipos y botón")
    public void loginUiInspection() {
        ExcelRow row = loginConfig();
        openLogin(row);

        Assert.assertEquals(loginPage.subtitle(), "Ingresa tus credenciales para continuar");
        Assert.assertEquals(loginPage.usernameLabel(), "Usuario");
        Assert.assertEquals(loginPage.username().getAttribute("type"), "text");
        Assert.assertEquals(loginPage.username().getAttribute("placeholder"), "Ingresa tu usuario");
        Assert.assertEquals(loginPage.username().getAttribute("id"), "username");

        loginPage.typeUsername("Abc123");
        Assert.assertEquals(loginPage.username().getAttribute("value"), "Abc123");

        Assert.assertEquals(loginPage.passwordLabel(), "Contraseña");
        Assert.assertEquals(loginPage.password().getAttribute("placeholder"), "Ingresa tu contraseña");
        Assert.assertEquals(loginPage.passwordType(), "password");

        loginPage.togglePasswordVisibility();
        Assert.assertEquals(loginPage.passwordType(), "text");
                Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-testid='login-toggle-password'], button[aria-label='Ocultar contraseña']")).getAttribute("aria-label"),
                "Ocultar contraseña");
        loginPage.togglePasswordVisibility();
        Assert.assertEquals(loginPage.passwordType(), "password");

        Assert.assertTrue(loginPage.submit().getText().contains("Iniciar sesión"));
        Assert.assertEquals(loginPage.submit().getAttribute("type"), "submit");
        Assert.assertTrue(loginPage.submit().isEnabled(), "El botón debe estar habilitado en reposo");
        Assert.assertTrue(driver.findElement(By.cssSelector("img[alt='Variedades Hernández']")).isDisplayed());
    }

    @Test(groups = {"auth", "p0", "global"}, description = "LOG-VAL-01: clic sin datos muestra ambos mensajes de obligatorio")
    public void loginEmptyFields() {
        openLogin(loginConfig());
        loginPage.submitLogin();
        Assert.assertEquals(loginPage.usernameError(), "El usuario es obligatorio");
        Assert.assertEquals(loginPage.passwordError(), "La contraseña es obligatoria");
    }

    @Test(groups = {"auth", "p0", "global"}, description = "LOG-VAL-02: usuario vacío y clave llena")
    public void loginEmptyUsername() {
        ExcelRow row = loginRow("LOG-VAL-02");
        openLogin(row);
        loginPage.typePassword(row.get("Contrasena"));
        loginPage.submitLogin();
        Assert.assertTrue(loginPage.usernameErrorShown());
        Assert.assertEquals(loginPage.usernameError(), "El usuario es obligatorio");
        Assert.assertFalse(driver.getCurrentUrl().contains("/app/"));
    }

    @Test(groups = {"auth", "p0", "global"}, description = "LOG-VAL-03: usuario lleno y clave vacía")
    public void loginEmptyPassword() {
        ExcelRow row = loginRow("LOG-VAL-03");
        openLogin(row);
        loginPage.typeUsername(row.get("Usuario"));
        loginPage.submitLogin();
        Assert.assertTrue(loginPage.passwordErrorShown());
        Assert.assertEquals(loginPage.passwordError(), "La contraseña es obligatoria");
    }

    @Test(groups = {"auth", "p0", "global"}, description = "LOG-NEG-01: credenciales incorrectas")
    public void loginInvalidCredentials() {
        ExcelRow row = loginRow("LOG-NEG-01");
        openLogin(row);
        loginPage.login(row.get("Usuario"), row.get("Contrasena"));
        Assert.assertEquals(loginPage.backendError(), "Usuario o contraseña incorrectos");
        Assert.assertFalse(driver.getCurrentUrl().contains("/app/"));
    }

    @Test(groups = {"auth", "p0", "global"}, description = "LOG-OK-01: login válido llega al dashboard")
    public void loginSuccessful() {
        ExcelRow row = loginRow("LOG-OK-01");
        openLogin(row);
        loginPage.login(row.get("Usuario"), row.get("Contrasena"));
        dashboardPage.waitLoaded();
        Assert.assertTrue(dashboardPage.welcome().startsWith("Hola"));
        ScreenshotUtil.milestone(driver, "Login exitoso - dashboard");
    }
}
