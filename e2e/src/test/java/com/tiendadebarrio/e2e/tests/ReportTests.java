package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.ReportsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReportTests extends BaseE2eTest {

    @Test(groups = {"reports", "p1", "global"}, description = "REP-UI: título, filtros y tarjeta de ventas")
    public void reportsUiInspection() {
        loginWithSessionFrom("10-reportes.xlsx", "Reportes");
        dashboardPage.goTo("Reportes");
        ReportsPage page = new ReportsPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Reportes");
        Assert.assertTrue(page.todayButton().isEnabled());
        Assert.assertTrue(page.applyButton().isEnabled());
        Assert.assertTrue(page.hasSalesTotalCard());
        ScreenshotUtil.milestone(driver, "Módulo Reportes visible");
    }

    @Test(groups = {"reports", "p1", "global"}, description = "REP-OK: filtro Hoy carga totales")
    public void reportsTodayFilter() {
        loginWithSessionFrom("10-reportes.xlsx", "Reportes");
        dashboardPage.goTo("Reportes");
        ReportsPage page = new ReportsPage(driver);
        page.waitLoaded();
        page.clickToday();
        Assert.assertTrue(page.hasSalesTotalCard());
        ScreenshotUtil.milestone(driver, "Reportes filtro Hoy");
    }
}
