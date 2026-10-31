package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.SuppliersPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SupplierTests extends BaseE2eTest {

    @Test(groups = {"suppliers", "p1", "global"}, description = "PRO-UI: título y botón Nuevo proveedor")
    public void suppliersUiInspection() {
        loginWithSessionFrom("08-proveedores.xlsx", "Proveedores");
        dashboardPage.goTo("Proveedores");
        SuppliersPage page = new SuppliersPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Proveedores");
        Assert.assertTrue(page.newButton().getText().contains("Nuevo proveedor"));
        ScreenshotUtil.milestone(driver, "Módulo Proveedores visible");
    }

    @Test(groups = {"suppliers", "p1", "global"}, description = "PRO-VAL: guardar vacío muestra nombre obligatorio")
    public void suppliersRequiredValidation() {
        loginWithSessionFrom("08-proveedores.xlsx", "Proveedores");
        dashboardPage.goTo("Proveedores");
        SuppliersPage page = new SuppliersPage(driver);
        page.waitLoaded();
        page.openCreate();
        page.submitEmpty();
        Assert.assertEquals(page.nameError(), "El nombre es obligatorio");
    }

    @Test(groups = {"suppliers", "p1", "global"}, description = "PRO-OK: alta de proveedor con datos del Excel")
    public void suppliersCreate() {
        ExcelRow row = dataRow("08-proveedores.xlsx", "Proveedores", "PRO-OK-01");
        loginAs(row);
        dashboardPage.goTo("Proveedores");
        SuppliersPage page = new SuppliersPage(driver);
        page.waitLoaded();
        page.openCreate();
        String name = row.getResolved("Nombre");
        page.fill(name, row.getResolved("Nit"), row.get("Telefono"));
        Assert.assertTrue(page.saveCreated());
        page.search(name);
        Assert.assertTrue(page.tableContains(name));
        ScreenshotUtil.milestone(driver, "Proveedor creado: " + name);
    }
}
