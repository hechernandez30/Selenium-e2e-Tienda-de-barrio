package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.DriverFactory;
import com.tiendadebarrio.e2e.core.ExcelReader;
import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ProjectPaths;
import com.tiendadebarrio.e2e.pages.DashboardPage;
import com.tiendadebarrio.e2e.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Path;

public abstract class BaseE2eTest {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        driver = DriverFactory.start();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        DriverFactory.stop();
    }

    protected ExcelRow loginRow(String caseId) {
        return ExcelReader.byCase(ProjectPaths.testdata("01-login.xlsx"), "Login", caseId);
    }

    protected ExcelRow loginConfig() {
        return ExcelReader.first(ProjectPaths.testdata("01-login.xlsx"), "Login");
    }

    protected ExcelRow dataRow(String file, String sheet, String caseId) {
        return ExcelReader.byCase(ProjectPaths.testdata(file), sheet, caseId);
    }

    protected Path testdata(String file) {
        return ProjectPaths.testdata(file);
    }

    protected void openLogin(ExcelRow row) {
        loginPage.open(row.get("BaseUrl"), row.get("Ruta").isBlank() ? "/login" : row.get("Ruta"));
    }

    protected void loginAs(ExcelRow row) {
        openLogin(row);
        loginPage.login(row.get("Usuario"), row.get("Contrasena"));
        dashboardPage.waitLoaded();
    }

    protected void loginWithSessionFrom(String file, String sheet) {
        ExcelRow row = ExcelReader.first(ProjectPaths.testdata(file), sheet);
        loginAs(row);
    }
}
