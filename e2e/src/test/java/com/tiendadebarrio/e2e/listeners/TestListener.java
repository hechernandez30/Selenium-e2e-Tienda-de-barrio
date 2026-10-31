package com.tiendadebarrio.e2e.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tiendadebarrio.e2e.core.DriverFactory;
import com.tiendadebarrio.e2e.core.ExtentManager;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        ExtentManager.get();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.get().createTest(
                displayName(result),
                "Método: " + result.getMethod().getMethodName());
        String[] groups = result.getMethod().getGroups();
        if (groups != null) {
            for (String group : groups) {
                test.assignCategory(group);
            }
        }
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentManager.current() != null) {
            ExtentManager.current().log(Status.PASS, "Escenario superado");
        }
        ExtentManager.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ScreenshotUtil.failure(DriverFactory.get(), "Fallo: " + displayName(result));
        if (ExtentManager.current() != null && result.getThrowable() != null) {
            ExtentManager.current().fail(result.getThrowable());
        }
        ExtentManager.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.current() != null) {
            ExtentManager.current().log(Status.SKIP, "Escenario omitido");
        }
        ExtentManager.remove();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
        System.out.println("Informe HTML: " + ExtentManager.reportFile());
    }

    private static String displayName(ITestResult result) {
        String description = result.getMethod().getDescription();
        if (description != null && !description.isBlank()) {
            return description.trim();
        }
        return result.getMethod().getMethodName();
    }
}
