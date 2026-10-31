package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.UsersPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserTests extends BaseE2eTest {

    private static String createdUsername;
    private static String createdPassword;
    private static String baseUrl;

    @Test(groups = {"users", "p1", "global"}, description = "USR-UI: título y botón Nuevo usuario")
    public void usersUiInspection() {
        loginWithSessionFrom("09-usuarios.xlsx", "Usuarios");
        dashboardPage.goTo("Usuarios y Roles");
        UsersPage page = new UsersPage(driver);
        page.waitLoaded();
        Assert.assertTrue(page.title().contains("Usuarios"));
        Assert.assertTrue(page.newButton().getText().contains("Nuevo usuario"));
        ScreenshotUtil.milestone(driver, "Módulo Usuarios visible");
    }

    @Test(groups = {"users", "p1", "global"}, description = "USR-VAL: crear vacío muestra usuario obligatorio")
    public void usersRequiredValidation() {
        loginWithSessionFrom("09-usuarios.xlsx", "Usuarios");
        dashboardPage.goTo("Usuarios y Roles");
        UsersPage page = new UsersPage(driver);
        page.waitLoaded();
        page.openCreate();
        page.submitEmpty();
        Assert.assertEquals(page.usernameError(), "El usuario es obligatorio.");
    }

    @Test(groups = {"users", "p1", "global"}, description = "USR-OK: alta de cajero con datos del Excel")
    public void usersCreateCashier() {
        ExcelRow row = dataRow("09-usuarios.xlsx", "Usuarios", "USR-OK-01");
        loginAs(row);
        dashboardPage.goTo("Usuarios y Roles");
        UsersPage page = new UsersPage(driver);
        page.waitLoaded();
        page.openCreate();
        createdUsername = row.getResolved("NuevoUsuario");
        createdPassword = row.get("NuevaClave");
        baseUrl = row.get("BaseUrl");
        String first = row.get("Nombre").isBlank() ? "E2E" : row.get("Nombre");
        String last = row.get("Apellido").isBlank() ? "Cajero" : row.get("Apellido");
        page.fillCreate(createdUsername, createdPassword, first, last, row.get("Rol"));
        Assert.assertTrue(page.saveCreated());
        ScreenshotUtil.milestone(driver, "Usuario cajero creado: " + createdUsername);
    }

    @Test(groups = {"users", "p1", "global"}, dependsOnMethods = "usersCreateCashier",
            description = "USR-ST: cajero no entra a Productos (roleGuard)")
    public void usersCashierCannotOpenProducts() {
        Assert.assertNotNull(createdUsername, "Debe existir el cajero creado en USR-OK");
        loginPage.open(baseUrl, "/login");
        loginPage.login(createdUsername, createdPassword);
        dashboardPage.waitLoaded();
        UsersPage page = new UsersPage(driver);
        Assert.assertFalse(page.navHas("Productos"), "El cajero no debe ver Productos en el menú");
        dashboardPage.openUrl(baseUrl, "/app/products");
        dashboardPage.waitLoaded();
        Assert.assertTrue(driver.getCurrentUrl().contains("/app/dashboard"));
        Assert.assertFalse(driver.getCurrentUrl().contains("/products"));
        ScreenshotUtil.milestone(driver, "Cajero redirigido al dashboard");
    }
}
