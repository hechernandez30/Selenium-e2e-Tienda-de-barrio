package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardTests extends BaseE2eTest {

    @Test(groups = {"dashboard", "p1", "global"}, description = "DAS-UI: bienvenida y tarjetas de acceso rápido")
    public void dashboardCardsVisible() {
        loginWithSessionFrom("11-dashboard.xlsx", "Dashboard");
        Assert.assertTrue(dashboardPage.welcome().startsWith("Hola"));
        Assert.assertTrue(dashboardPage.hasCard("Ventas del día"));
        Assert.assertTrue(dashboardPage.hasCard("Caja actual"));
        Assert.assertTrue(dashboardPage.hasCard("Productos"));
        Assert.assertTrue(dashboardPage.hasCard("Compras"));
        Assert.assertTrue(dashboardPage.hasCard("Reportes"));
        ScreenshotUtil.milestone(driver, "Dashboard con tarjetas");
    }
}
