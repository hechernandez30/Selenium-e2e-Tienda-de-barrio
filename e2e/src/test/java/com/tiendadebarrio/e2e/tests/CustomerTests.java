package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.CustomersPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CustomerTests extends BaseE2eTest {

    @Test(groups = {"customers", "p1", "global"}, description = "CLI-UI: título y botón Nuevo cliente")
    public void customersUiInspection() {
        loginWithSessionFrom("07-clientes.xlsx", "Clientes");
        dashboardPage.goTo("Clientes");
        CustomersPage page = new CustomersPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Clientes");
        Assert.assertTrue(page.newButton().getText().contains("Nuevo cliente"));
        Assert.assertTrue(page.newButton().isEnabled());
        ScreenshotUtil.milestone(driver, "Módulo Clientes visible");
    }

    @Test(groups = {"customers", "p1", "global"}, description = "CLI-VAL: guardar vacío muestra nombre obligatorio")
    public void customersRequiredValidation() {
        loginWithSessionFrom("07-clientes.xlsx", "Clientes");
        dashboardPage.goTo("Clientes");
        CustomersPage page = new CustomersPage(driver);
        page.waitLoaded();
        page.openCreate();
        page.submitEmpty();
        Assert.assertEquals(page.nameError(), "El nombre completo es obligatorio");
    }

    @Test(groups = {"customers", "p1", "global"}, description = "CLI-OK: alta de cliente con datos del Excel")
    public void customersCreate() {
        ExcelRow row = dataRow("07-clientes.xlsx", "Clientes", "CLI-OK-01");
        loginAs(row);
        dashboardPage.goTo("Clientes");
        CustomersPage page = new CustomersPage(driver);
        page.waitLoaded();
        page.openCreate();
        String name = row.getResolved("Nombre");
        page.fill(name, row.get("Nit"), row.get("Telefono"));
        Assert.assertTrue(page.saveCreated());
        page.search(name);
        Assert.assertTrue(page.tableContains(name));
        ScreenshotUtil.milestone(driver, "Cliente creado: " + name);
    }
}
