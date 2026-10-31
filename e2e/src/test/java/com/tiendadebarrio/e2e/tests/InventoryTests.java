package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InventoryTests extends BaseE2eTest {

    @Test(groups = {"inventory", "p0", "global"}, description = "INV-UI: título y botones de ajuste")
    public void inventoryUiInspection() {
        loginWithSessionFrom("03-inventario.xlsx", "Inventario");
        dashboardPage.goTo("Inventario");
        InventoryPage page = new InventoryPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Inventario");
        Assert.assertTrue(page.incomingButton().getText().contains("Ajuste de entrada"));
        Assert.assertTrue(page.outgoingButton().getText().contains("Ajuste de salida"));
        ScreenshotUtil.milestone(driver, "Módulo Inventario visible");
    }

    @Test(groups = {"inventory", "p0", "global"}, description = "INV-OK: ajuste de entrada con producto del Excel")
    public void inventoryIncomingAdjustment() {
        ExcelRow row = dataRow("03-inventario.xlsx", "Inventario", "INV-OK-01");
        loginAs(row);
        dashboardPage.goTo("Inventario");
        InventoryPage page = new InventoryPage(driver);
        page.waitLoaded();
        page.openIncoming();
        page.searchProduct(row.getResolved("Producto"));
        page.setQuantity(row.get("Cantidad"));
        page.saveAdjustment();
        Assert.assertTrue(page.snackbarContains("Ajuste de entrada registrado"));
        ScreenshotUtil.milestone(driver, "Ajuste de entrada registrado");
    }
}
