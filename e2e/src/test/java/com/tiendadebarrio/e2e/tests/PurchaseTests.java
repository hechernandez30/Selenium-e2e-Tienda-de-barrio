package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.PurchasesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PurchaseTests extends BaseE2eTest {

    @Test(groups = {"purchases", "p1", "global"}, description = "COM-UI: título y botón Nueva compra")
    public void purchasesUiInspection() {
        loginWithSessionFrom("06-compras.xlsx", "Compras");
        dashboardPage.goTo("Compras");
        PurchasesPage page = new PurchasesPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Compras");
        Assert.assertTrue(page.newButton().getText().contains("Nueva compra"));
        ScreenshotUtil.milestone(driver, "Módulo Compras visible");
    }

    @Test(groups = {"purchases", "p1", "global"}, description = "COM-VAL: guardar sin proveedor")
    public void purchasesSupplierRequired() {
        loginWithSessionFrom("06-compras.xlsx", "Compras");
        dashboardPage.goTo("Compras");
        PurchasesPage page = new PurchasesPage(driver);
        page.waitLoaded();
        page.openCreate();
        page.savePurchase();
        Assert.assertEquals(page.supplierError(), "El proveedor es obligatorio");
    }

    @Test(groups = {"purchases", "p1", "global"}, description = "COM-OK: crear compra en borrador y confirmarla")
    public void purchasesCreateAndConfirm() {
        ExcelRow row = dataRow("06-compras.xlsx", "Compras", "COM-OK-01");
        loginAs(row);
        dashboardPage.goTo("Compras");
        PurchasesPage page = new PurchasesPage(driver);
        page.waitLoaded();
        page.openCreate();
        page.selectSupplier(row.get("Proveedor"));
        page.addProduct(row.get("Producto"));
        page.setFirstLine(row.get("Cantidad"), row.get("Costo"));
        Assert.assertTrue(page.saveDraft());
        page.confirmFirstDraft(row.get("Proveedor"));
        Assert.assertTrue(page.snackbar("Compra confirmada. Inventario actualizado."));
        ScreenshotUtil.milestone(driver, "Compra confirmada");
    }
}
