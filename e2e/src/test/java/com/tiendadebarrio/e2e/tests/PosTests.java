package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.PosPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PosTests extends BaseE2eTest {

    @Test(groups = {"pos", "p0", "global"}, description = "POS-UI: título y estado de caja en el POS")
    public void posUiInspection() {
        loginWithSessionFrom("05-pos.xlsx", "POS");
        dashboardPage.goTo("Ventas / POS");
        PosPage page = new PosPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Punto de Venta");
        ScreenshotUtil.milestone(driver, "Punto de venta visible");
    }

    @Test(groups = {"pos", "p0", "global"}, description = "POS-OK: escanear y registrar venta en efectivo")
    public void posRegisterSale() {
        ExcelRow row = dataRow("05-pos.xlsx", "POS", "POS-OK-01");
        loginAs(row);
        dashboardPage.goTo("Ventas / POS");
        PosPage page = new PosPage(driver);
        page.waitLoaded();
        if (page.cashClosedWarning()) {
            Assert.fail("No hay caja abierta. Ejecuta primero el grupo cash o abre caja manualmente.");
        }
        page.scan(row.get("NombreProducto").isBlank() ? row.getResolved("CodigoBarras") : row.get("NombreProducto"));
        Assert.assertTrue(page.cartHasProduct(row.get("NombreProducto"))
                || page.cartHasProduct(row.getResolved("CodigoBarras")));
        String method = row.get("MetodoPago").isBlank() ? "CASH" : row.get("MetodoPago");
        page.choosePayment(method);
        page.registerSale();
        Assert.assertTrue(page.snackbarContains("Venta registrada correctamente"));
        ScreenshotUtil.milestone(driver, "Venta registrada correctamente");
    }
}
