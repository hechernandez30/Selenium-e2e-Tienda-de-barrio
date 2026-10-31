package com.tiendadebarrio.e2e.pages;

import com.aventstack.extentreports.Status;
import com.tiendadebarrio.e2e.core.ExtentManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    protected By testId(String id) {
        return By.cssSelector("[data-testid='" + id + "']");
    }

    protected By testId(String id, String fallbackCss) {
        return By.cssSelector("[data-testid='" + id + "'], " + fallbackCss);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        StaleElementReferenceException last = null;
        for (int attempt = 0; attempt < 4; attempt++) {
            try {
                clickable(locator).click();
                step("Clic en " + locator);
                return;
            } catch (StaleElementReferenceException ex) {
                last = ex;
            }
        }
        throw last;
    }

    protected void type(By locator, String value) {
        StaleElementReferenceException last = null;
        for (int attempt = 0; attempt < 4; attempt++) {
            try {
                WebElement element = visible(locator);
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                element.sendKeys(Keys.DELETE);
                if (value != null && !value.isEmpty()) {
                    element.sendKeys(value);
                }
                step("Escribir en " + locator + ": " + (value == null || value.isBlank() ? "(vacío)" : value));
                return;
            } catch (StaleElementReferenceException ex) {
                last = ex;
            }
        }
        throw last;
    }

    protected void clear(By locator) {
        WebElement element = visible(locator);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean shown(By locator) {
        List<WebElement> found = driver.findElements(locator);
        return !found.isEmpty() && found.getFirst().isDisplayed();
    }

    protected void waitUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void step(String message) {
        if (ExtentManager.current() != null) {
            ExtentManager.current().log(Status.INFO, message);
        }
    }

    protected void selectOptionContaining(By locator, String fragment) {
        WebElement select = visible(locator);
        for (WebElement option : select.findElements(By.tagName("option"))) {
            String value = option.getAttribute("value");
            if (value != null && !value.isBlank() && option.getText().contains(fragment)) {
                option.click();
                step("Seleccionar opción que contiene: " + fragment);
                return;
            }
        }
        throw new IllegalStateException("No hay opción que contenga: " + fragment);
    }

    protected boolean tableContains(String text) {
        return !driver.findElements(By.xpath("//table//td[contains(.,'" + text + "')]")).isEmpty();
    }

    protected boolean snackbarContains(String message) {
        return wait.until(d -> {
            try {
                return d.findElements(By.cssSelector("mat-snack-bar-container, .mdc-snackbar, simple-snack-bar"))
                        .stream()
                        .anyMatch(el -> {
                            try {
                                String text = el.getText();
                                return text != null && text.contains(message);
                            } catch (StaleElementReferenceException ex) {
                                return false;
                            }
                        });
            } catch (StaleElementReferenceException ex) {
                return false;
            }
        });
    }
}
