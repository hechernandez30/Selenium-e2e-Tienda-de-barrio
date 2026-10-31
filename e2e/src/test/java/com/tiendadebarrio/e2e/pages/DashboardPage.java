package com.tiendadebarrio.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void waitLoaded() {
        visible(By.xpath("//h1[contains(.,'Hola')]"));
        waitUrlContains("/app/dashboard");
    }

    public String welcome() {
        return visible(By.xpath("//h1[contains(.,'Hola')]")).getText().trim();
    }

    public void goTo(String label) {
        click(By.xpath("//nav//a[contains(.,'" + label + "')]"));
        step("Navegar a " + label);
    }

    public void logout() {
        click(By.xpath("//button[contains(.,'Salir')]"));
    }

    public boolean hasCard(String title) {
        return shown(By.xpath("//h3[normalize-space()='" + title + "']"));
    }

    public void openUrl(String baseUrl, String route) {
        String root = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String path = route.startsWith("/") ? route : "/" + route;
        driver.get(root + path);
        step("Abrir " + root + path);
    }
}
