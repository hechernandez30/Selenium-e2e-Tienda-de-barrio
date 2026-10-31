package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.CashPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CashTests extends BaseE2eTest {

    @Test(groups = {"cash", "p0", "global"}, description = "CAJ-UI: título y estado de caja")
    public void cashUiInspection() {
        loginWithSessionFrom("04-caja.xlsx", "Caja");
        dashboardPage.goTo("Caja");
        CashPage page = new CashPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Caja");
        Assert.assertTrue(page.isOpen() || page.isClosed());
        ScreenshotUtil.milestone(driver, "Módulo Caja visible");
    }

    @Test(groups = {"cash", "p0", "global"}, description = "CAJ-OK: abrir caja si está cerrada")
    public void cashOpenSession() {
        ExcelRow row = dataRow("04-caja.xlsx", "Caja", "CAJ-OK-01");
        loginAs(row);
        dashboardPage.goTo("Caja");
        CashPage page = new CashPage(driver);
        page.waitLoaded();
        if (page.isOpen()) {
            ScreenshotUtil.milestone(driver, "Caja ya estaba abierta");
            return;
        }
        page.openCash(row.get("MontoApertura"));
        Assert.assertTrue(page.snackbarContains("Caja abierta correctamente"));
        ScreenshotUtil.milestone(driver, "Caja abierta correctamente");
    }
}
