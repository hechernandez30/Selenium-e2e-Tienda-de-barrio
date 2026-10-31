package com.tiendadebarrio.e2e.tests;

import com.tiendadebarrio.e2e.core.ExcelRow;
import com.tiendadebarrio.e2e.core.ScreenshotUtil;
import com.tiendadebarrio.e2e.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductTests extends BaseE2eTest {

    @Test(groups = {"products", "p0", "global"}, description = "PRD-UI: listado, título y botón Nuevo producto")
    public void productsUiInspection() {
        loginWithSessionFrom("02-productos.xlsx", "Productos");
        dashboardPage.goTo("Productos");
        ProductsPage page = new ProductsPage(driver);
        page.waitLoaded();
        Assert.assertEquals(page.title(), "Productos");
        Assert.assertTrue(page.newButton().getText().contains("Nuevo producto"));
        Assert.assertTrue(page.newButton().isEnabled());
        ScreenshotUtil.milestone(driver, "Módulo Productos visible");
    }

    @Test(groups = {"products", "p0", "global"}, description = "PRD-VAL: guardar vacío muestra obligatorios")
    public void productsRequiredValidation() {
        loginWithSessionFrom("02-productos.xlsx", "Productos");
        dashboardPage.goTo("Productos");
        ProductsPage page = new ProductsPage(driver);
        page.waitLoaded();
        page.openCreate();
        page.submitEmpty();
        Assert.assertEquals(page.barcodeError(), "El código de barras es obligatorio");
        Assert.assertEquals(page.nameError(), "El nombre es obligatorio");
    }

    @Test(groups = {"products", "p0", "global"}, description = "PRD-OK: alta de producto con datos del Excel")
    public void productsCreate() {
        ExcelRow row = dataRow("02-productos.xlsx", "Productos", "PRD-OK-01");
        loginAs(row);
        dashboardPage.goTo("Productos");
        ProductsPage page = new ProductsPage(driver);
        page.waitLoaded();
        page.openCreate();
        String barcode = row.getResolved("CodigoBarras");
        String name = row.getResolved("Nombre");
        page.fillCreate(barcode, name, row.get("PrecioCompra"), row.get("PrecioVenta"), row.get("Stock"));
        Assert.assertTrue(page.saveCreated());
        page.search(barcode);
        Assert.assertTrue(page.tableContains(barcode) || page.tableContains(name));
        ScreenshotUtil.milestone(driver, "Producto creado: " + name);
    }
}
