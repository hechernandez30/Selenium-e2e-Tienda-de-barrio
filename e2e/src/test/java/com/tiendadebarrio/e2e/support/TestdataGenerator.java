package com.tiendadebarrio.e2e.support;

import com.tiendadebarrio.e2e.core.ProjectPaths;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestdataGenerator {
    private TestdataGenerator() {}

    public static void main(String[] args) throws IOException {
        Path testdataDir = ProjectPaths.testdataRoot().resolve("modulos");
        Path globalDir = ProjectPaths.testdataRoot().resolve("global");
        Path catalog = ProjectPaths.catalog();
        Files.createDirectories(testdataDir);
        Files.createDirectories(globalDir);
        Files.createDirectories(catalog.getParent());

        writeCatalog(catalog);

        Path loginDir = moduleFolder(testdataDir, "01-login");
        writeLogin(loginDir.resolve("01-login.xlsx"));
        writeModuleBat(loginDir.resolve("ejecutar-login.bat"), "auth", "Login");

        Path productsDir = moduleFolder(testdataDir, "02-productos");
        writeProducts(productsDir.resolve("02-productos.xlsx"));
        writeModuleBat(productsDir.resolve("ejecutar-productos.bat"), "products", "Productos");

        Path inventoryDir = moduleFolder(testdataDir, "03-inventario");
        writeInventory(inventoryDir.resolve("03-inventario.xlsx"));
        writeModuleBat(inventoryDir.resolve("ejecutar-inventario.bat"), "inventory", "Inventario");

        Path cashDir = moduleFolder(testdataDir, "04-caja");
        writeCash(cashDir.resolve("04-caja.xlsx"));
        writeModuleBat(cashDir.resolve("ejecutar-caja.bat"), "cash", "Caja");

        Path posDir = moduleFolder(testdataDir, "05-pos");
        writePos(posDir.resolve("05-pos.xlsx"));
        writeModuleBat(posDir.resolve("ejecutar-pos.bat"), "pos", "POS");

        Path purchasesDir = moduleFolder(testdataDir, "06-compras");
        writeGeneric(purchasesDir.resolve("06-compras.xlsx"), "Compras",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Proveedor", "Producto", "Cantidad", "Costo", "Notas"},
                new String[][] {
                        {"Sí", "COM-OK-01", "http://localhost", "/app/purchases", "admin", "Admin123", "Proveedor E2E", "Producto E2E", "5", "4.50", "El proveedor y el producto se buscan por texto contenido. Corre proveedores (P1) y productos (P0) antes, o usa datos reales."}
                });
        writeModuleBat(purchasesDir.resolve("ejecutar-compras.bat"), "purchases", "Compras");

        Path customersDir = moduleFolder(testdataDir, "07-clientes");
        writeGeneric(customersDir.resolve("07-clientes.xlsx"), "Clientes",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Nombre", "Nit", "Telefono", "Notas"},
                new String[][] {
                        {"Sí", "CLI-OK-01", "http://localhost", "/app/customers", "admin", "Admin123", "Cliente E2E {timestamp}", "", "55550000", "NIT vacío para no chocar con unicidad. Teléfono de 8 dígitos."}
                });
        writeModuleBat(customersDir.resolve("ejecutar-clientes.bat"), "customers", "Clientes");

        Path suppliersDir = moduleFolder(testdataDir, "08-proveedores");
        writeGeneric(suppliersDir.resolve("08-proveedores.xlsx"), "Proveedores",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Nombre", "Nit", "Telefono", "Notas"},
                new String[][] {
                        {"Sí", "PRO-OK-01", "http://localhost", "/app/suppliers", "admin", "Admin123", "Proveedor E2E {timestamp}", "E2E-{timestamp}", "55551111", "Nombre y NIT únicos. Compras busca el texto Proveedor E2E."}
                });
        writeModuleBat(suppliersDir.resolve("ejecutar-proveedores.bat"), "suppliers", "Proveedores");

        Path usersDir = moduleFolder(testdataDir, "09-usuarios");
        writeGeneric(usersDir.resolve("09-usuarios.xlsx"), "Usuarios",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "NuevoUsuario", "NuevaClave", "Nombre", "Apellido", "Rol", "Notas"},
                new String[][] {
                        {"Sí", "USR-OK-01", "http://localhost", "/app/users", "admin", "Admin123", "e2e.user.{timestamp}", "E2ePass123", "E2E", "Cajero", "CAJERO", "Clave: mínimo 8, mayúscula, minúscula y número. USR-ST reutiliza este usuario."}
                });
        writeModuleBat(usersDir.resolve("ejecutar-usuarios.bat"), "users", "Usuarios");

        Path reportsDir = moduleFolder(testdataDir, "10-reportes");
        writeGeneric(reportsDir.resolve("10-reportes.xlsx"), "Reportes",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "FechaDesde", "FechaHasta", "Notas"},
                new String[][] {
                        {"Sí", "REP-OK-01", "http://localhost", "/app/reports", "admin", "Admin123", "", "", "El test usa el botón Hoy; las fechas del Excel se dejan vacías"}
                });
        writeModuleBat(reportsDir.resolve("ejecutar-reportes.bat"), "reports", "Reportes");

        Path dashboardDir = moduleFolder(testdataDir, "11-dashboard");
        writeGeneric(dashboardDir.resolve("11-dashboard.xlsx"), "Dashboard",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Notas"},
                new String[][] {
                        {"Sí", "DAS-UI-01", "http://localhost", "/app/dashboard", "admin", "Admin123", "Sesion para inspeccionar tarjetas del dashboard"}
                });
        writeModuleBat(dashboardDir.resolve("ejecutar-dashboard.bat"), "dashboard", "Dashboard");

        writeGeneric(globalDir.resolve("regresion.xlsx"), "Regresion",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Notas"},
                new String[][] {
                        {"Sí", "REG-01", "http://localhost", "/login", "admin", "Admin123", "Doble clic en ejecutar-regresion.bat. Reutiliza los Excel de cada modulo."}
                });
        writeBat(globalDir.resolve("ejecutar-regresion.bat"), "..\\..", "global", "Regresion (P0 + P1)");

        System.out.println("Catálogo: " + catalog);
        System.out.println("Datatables: " + testdataDir);
        System.out.println("Global: " + globalDir);
    }

    private static void writeLogin(Path file) throws IOException {
        writeGeneric(file, "Login",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Notas"},
                new String[][] {
                        {"Sí", "LOG-OK-01", "http://localhost", "/login", "admin", "Admin123", "Caso feliz. Docker en puerto 80. Si usas ng serve, cambia a http://localhost."},
                        {"Sí", "LOG-NEG-01", "http://localhost", "/login", "usuario_invalido", "clave_invalida", "Credenciales incorrectas"},
                        {"Sí", "LOG-VAL-02", "http://localhost", "/login", "", "cualquierClave", "Usuario vacío"},
                        {"Sí", "LOG-VAL-03", "http://localhost", "/login", "admin", "", "Contraseña vacía"},
                        {"No", "LOG-ENV-ALT", "http://localhost:4200", "/login", "admin", "Admin123", "Ejemplo ng serve. Poner Ejecutar=Sí si pruebas contra el frontend de desarrollo."}
                });
    }

    private static void writeProducts(Path file) throws IOException {
        writeGeneric(file, "Productos",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "CodigoBarras", "Nombre", "PrecioCompra", "PrecioVenta", "Stock", "Notas"},
                new String[][] {
                        {"Sí", "PRD-UI-01", "http://localhost", "/app/products", "admin", "Admin123", "", "", "", "", "", "Solo sesión para inspección de UI"},
                        {"Sí", "PRD-OK-01", "http://localhost", "/app/products", "admin", "Admin123", "E2E-{timestamp}", "Producto E2E {timestamp}", "5.00", "8.00", "20", "{timestamp} se reemplaza al ejecutar para no chocar códigos"}
                });
    }

    private static void writeInventory(Path file) throws IOException {
        writeGeneric(file, "Inventario",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "Producto", "Cantidad", "Notas"},
                new String[][] {
                        {"Sí", "INV-UI-01", "http://localhost", "/app/inventory", "admin", "Admin123", "", "", "Sesión para inspección"},
                        {"Sí", "INV-OK-01", "http://localhost", "/app/inventory", "admin", "Admin123", "Producto E2E", "2", "Debe existir un producto cuyo nombre o código contenga ese texto. Corre productos antes o usa uno real."}
                });
    }

    private static void writeCash(Path file) throws IOException {
        writeGeneric(file, "Caja",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "MontoApertura", "MontoCierre", "Notas"},
                new String[][] {
                        {"Sí", "CAJ-UI-01", "http://localhost", "/app/cash", "admin", "Admin123", "", "", "Inspección"},
                        {"Sí", "CAJ-OK-01", "http://localhost", "/app/cash", "admin", "Admin123", "100", "100", "Si la caja ya está abierta el test de apertura se marca como éxito con captura"}
                });
    }

    private static void writePos(Path file) throws IOException {
        writeGeneric(file, "POS",
                new String[] {"Ejecutar", "CasoID", "BaseUrl", "Ruta", "Usuario", "Contrasena", "CodigoBarras", "NombreProducto", "MetodoPago", "Notas"},
                new String[][] {
                        {"Sí", "POS-UI-01", "http://localhost", "/app/sales/pos", "admin", "Admin123", "", "", "CASH", "Inspección"},
                        {"Sí", "POS-OK-01", "http://localhost", "/app/sales/pos", "admin", "Admin123", "E2E", "Producto E2E", "CASH", "Requiere caja abierta y un producto con stock. MetodoPago: CASH, TRANSFER o CARD"}
                });
    }

    private static void writeCatalog(Path file) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            CellStyle header = headerStyle(wb);
            writeSheet(wb, header, "Instrucciones",
                    new String[] {"Tema", "Detalle"},
                    new String[][] {
                            {"Qué es este archivo", "Script de escenarios (catálogo). Lo mantiene QA/desarrollo. No se llena en cada corrida."},
                            {"Qué no es", "No es el datatable. Los datos están en e2e/testdata/modulos/<modulo>/*.xlsx y e2e/testdata/global/regresion.xlsx"},
                            {"Cómo ejecutar un módulo", "Doble clic en e2e/testdata/modulos/<modulo>/ejecutar-*.bat  (o desde e2e: mvn test -Dgroups=auth, products, inventory, cash, pos, purchases, customers, suppliers, users, reports, dashboard)"},
                            {"Cómo ejecutar regresión", "Doble clic en e2e/testdata/global/ejecutar-regresion.bat  (grupo global = P0 + P1)"},
                            {"Cómo ejecutar todo P0", "Desde e2e: mvn test -Dgroups=p0"},
                            {"Cómo ejecutar todo P1", "Desde e2e: mvn test -Dgroups=p1. Compras busca un producto cuyo nombre contenga Producto E2E (créalo con P0 o usa uno real)."},
                            {"Informe", "Al terminar se genera e2e/reports/informe-e2e-FECHA.html con capturas de hitos y fallos"},
                            {"Ambiente", "El sistema debe estar levantado. Docker: http://localhost  Dev: http://localhost:4200"},
                            {"Técnicas ISTQB", "Inspección de UI, partición de equivalencia, valores límite, tabla de decisión, transición de estado, error guessing"},
                            {"Diseño", "Selenium valida textos, placeholders, type, habilitado. No compara color ni sombra píxel a píxel"}
                    });
            writeSheet(wb, header, "Login",
                    catalogHeaders(),
                    new String[][] {
                            {"LOG-UI-01", "Login", "Inspección", "UI", "Subtítulo", "Abrir /login", "Texto: Ingresa tus credenciales para continuar", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-02", "Login", "Inspección", "UI", "Etiqueta Usuario", "Buscar label for=username", "Texto Usuario", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-03", "Login", "Inspección", "UI", "Campo usuario", "Inspeccionar input", "type=text, id=username, placeholder Ingresa tu usuario", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-04", "Login", "EP válida", "UI", "Campo usuario", "Escribir Abc123", "El campo acepta letras y números", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-05", "Login", "Inspección", "UI", "Etiqueta y campo contraseña", "Inspeccionar", "Etiqueta Contraseña; type=password; placeholder Ingresa tu contraseña", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-06", "Login", "Función + diseño", "UI", "Mostrar/ocultar clave", "Clic en el icono de visibilidad", "type pasa a text y aria-label a Ocultar contraseña", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-07", "Login", "Inspección", "UI", "Botón Iniciar sesión", "Ver estado en reposo", "Visible, habilitado, type=submit, texto Iniciar sesión", "P0", "loginUiInspection", "Sí"},
                            {"LOG-UI-08", "Login", "Inspección", "UI", "Logo", "Ver imagen", "img alt=Variedades Hernández visible", "P0", "loginUiInspection", "Sí"},
                            {"LOG-VAL-01", "Login", "Tabla de decisión", "Validación", "Formulario vacío", "Clic en Iniciar sesión sin datos", "El usuario es obligatorio y La contraseña es obligatoria", "P0", "loginEmptyFields", "Sí"},
                            {"LOG-VAL-02", "Login", "Tabla de decisión", "Validación", "Usuario vacío", "Clave llena (datatable) y clic", "El usuario es obligatorio; no entra", "P0", "loginEmptyUsername", "Sí"},
                            {"LOG-VAL-03", "Login", "Tabla de decisión", "Validación", "Clave vacía", "Usuario lleno (datatable) y clic", "La contraseña es obligatoria", "P0", "loginEmptyPassword", "Sí"},
                            {"LOG-NEG-01", "Login", "Error guessing", "Negativo", "Credenciales", "Usuario y clave inválidos del Excel", "Usuario o contraseña incorrectos", "P0", "loginInvalidCredentials", "Sí"},
                            {"LOG-OK-01", "Login", "Caso de uso", "Feliz", "Credenciales válidas", "Login con datos del Excel", "Navega a /app/dashboard y captura de éxito", "P0", "loginSuccessful", "Sí"},
                            {"LOG-ST-01", "Login", "Transición de estado", "Sesión", "Ruta privada", "Entrar a /app/dashboard sin sesión", "Redirige a /login", "P1", "", "No"},
                            {"LOG-ST-02", "Login", "Transición de estado", "Sesión", "Salir", "Clic en Salir", "Vuelve a /login", "P1", "", "No"}
                    });
            writeSheet(wb, header, "Productos",
                    catalogHeaders(),
                    new String[][] {
                            {"PRD-UI-01", "Productos", "Inspección", "UI", "Título", "Entrar al módulo", "H1 Productos", "P0", "productsUiInspection", "Sí"},
                            {"PRD-UI-02", "Productos", "Inspección", "UI", "Botón Nuevo producto", "Ver botón", "Visible, habilitado, texto Nuevo producto", "P0", "productsUiInspection", "Sí"},
                            {"PRD-UI-03", "Productos", "Inspección", "UI", "Buscador", "Ver placeholder", "Buscar por nombre, SKU o código de barras", "P1", "", "No"},
                            {"PRD-UI-04", "Productos", "Inspección", "UI", "Diálogo alta", "Abrir Nuevo producto", "Título Nuevo producto; campos código, nombre, precios, stock", "P0", "productsRequiredValidation", "Sí"},
                            {"PRD-VAL-01", "Productos", "EP inválida", "Validación", "Código y nombre", "Guardar vacío", "El código de barras es obligatorio; El nombre es obligatorio", "P0", "productsRequiredValidation", "Sí"},
                            {"PRD-VAL-02", "Productos", "Valores límite", "Validación", "Precio compra", "Ingresar negativo", "No puede ser negativo", "P1", "", "No"},
                            {"PRD-VAL-03", "Productos", "Valores límite", "Validación", "Precio venta", "Ingresar negativo", "No puede ser negativo", "P1", "", "No"},
                            {"PRD-VAL-04", "Productos", "Valores límite", "Validación", "Stock mínimo", "Ingresar negativo", "No puede ser negativo", "P1", "", "No"},
                            {"PRD-OK-01", "Productos", "Caso de uso", "Feliz", "Alta", "Llenar datatable y guardar", "Producto creado correctamente + captura", "P0", "productsCreate", "Sí"},
                            {"PRD-OK-02", "Productos", "EP válida", "Búsqueda", "Listado", "Buscar por código creado", "La tabla muestra el producto", "P0", "productsCreate", "Sí"},
                            {"PRD-OK-03", "Productos", "Caso de uso", "Edición", "Editar nombre", "Cambiar nombre y guardar", "Producto actualizado correctamente", "P1", "", "No"},
                            {"PRD-NEG-01", "Productos", "Error guessing", "Negativo", "Código duplicado", "Crear el mismo código de barras", "Mensaje de error del backend", "P1", "", "No"}
                    });
            writeSheet(wb, header, "Inventario",
                    catalogHeaders(),
                    new String[][] {
                            {"INV-UI-01", "Inventario", "Inspección", "UI", "Título", "Entrar al módulo", "H1 Inventario", "P0", "inventoryUiInspection", "Sí"},
                            {"INV-UI-02", "Inventario", "Inspección", "UI", "Botones", "Ver acciones", "Ajuste de entrada y Ajuste de salida", "P0", "inventoryUiInspection", "Sí"},
                            {"INV-UI-03", "Inventario", "Inspección", "UI", "Tabs", "Ver pestañas", "Movimientos y Stock bajo", "P1", "", "No"},
                            {"INV-VAL-01", "Inventario", "EP inválida", "Validación", "Cantidad", "Guardar sin producto/cantidad", "Debes seleccionar un producto / La cantidad es obligatoria", "P1", "", "No"},
                            {"INV-VAL-02", "Inventario", "Valores límite", "Validación", "Cantidad 0", "Ingresar 0", "La cantidad debe ser mayor que 0", "P1", "", "No"},
                            {"INV-OK-01", "Inventario", "Caso de uso", "Feliz", "Entrada", "Buscar producto del Excel y registrar", "Ajuste de entrada registrado + captura", "P0", "inventoryIncomingAdjustment", "Sí"},
                            {"INV-OK-02", "Inventario", "Caso de uso", "Feliz", "Salida", "Ajuste de salida con stock suficiente", "Ajuste de salida registrado", "P1", "", "No"},
                            {"INV-ST-01", "Inventario", "Transición de estado", "Stock", "Salida excesiva", "Cantidad mayor al stock", "Error de stock insuficiente", "P1", "", "No"}
                    });
            writeSheet(wb, header, "Caja",
                    catalogHeaders(),
                    new String[][] {
                            {"CAJ-UI-01", "Caja", "Inspección", "UI", "Título", "Entrar al módulo", "H1 Caja", "P0", "cashUiInspection", "Sí"},
                            {"CAJ-UI-02", "Caja", "Inspección", "UI", "Estado", "Ver tarjeta Estado", "Abierta o Cerrada", "P0", "cashUiInspection", "Sí"},
                            {"CAJ-UI-03", "Caja", "Transición de estado", "UI", "Botón Abrir caja", "Si está cerrada", "Botón Abrir caja visible", "P0", "cashOpenSession", "Sí"},
                            {"CAJ-VAL-01", "Caja", "EP inválida", "Validación", "Monto inicial", "Abrir sin monto", "El monto inicial es obligatorio", "P1", "", "No"},
                            {"CAJ-VAL-02", "Caja", "Valores límite", "Validación", "Monto negativo", "Ingresar -1", "No puede ser negativo", "P1", "", "No"},
                            {"CAJ-OK-01", "Caja", "Caso de uso", "Feliz", "Apertura", "Monto del Excel", "Caja abierta correctamente + captura (o ya abierta)", "P0", "cashOpenSession", "Sí"},
                            {"CAJ-OK-02", "Caja", "Caso de uso", "Feliz", "Cierre", "Monto contado del Excel", "Caja cerrada correctamente", "P1", "", "No"},
                            {"CAJ-OK-03", "Caja", "Caso de uso", "Feliz", "Movimiento", "Ingreso/egreso manual", "Movimiento registrado correctamente", "P1", "", "No"}
                    });
            writeSheet(wb, header, "POS",
                    catalogHeaders(),
                    new String[][] {
                            {"POS-UI-01", "POS", "Inspección", "UI", "Título", "Entrar a Ventas / POS", "Punto de Venta", "P0", "posUiInspection", "Sí"},
                            {"POS-ST-01", "POS", "Transición de estado", "Caja", "Sin caja", "Entrar con caja cerrada", "Aviso Debes abrir caja y botón Ir a Caja", "P0", "posUiInspection", "Parcial"},
                            {"POS-UI-02", "POS", "Inspección", "UI", "Escáner", "Ver campo", "Placeholder Escanear código o buscar producto...", "P1", "", "No"},
                            {"POS-UI-03", "POS", "Inspección", "UI", "Pagos", "Ver métodos", "Efectivo, Transferencia, Tarjeta", "P1", "", "No"},
                            {"POS-VAL-01", "POS", "EP inválida", "Validación", "Carrito vacío", "Registrar sin productos", "Botón Registrar venta deshabilitado o snackbar", "P1", "", "No"},
                            {"POS-NEG-01", "POS", "Error guessing", "Negativo", "Código inexistente", "Escanear código que no existe", "Producto no encontrado", "P1", "", "No"},
                            {"POS-OK-01", "POS", "Caso de uso", "Feliz", "Venta efectivo", "Escanear código del Excel y registrar", "Venta registrada correctamente + captura", "P0", "posRegisterSale", "Sí"},
                            {"POS-OK-02", "POS", "EP válida", "Feliz", "Transferencia", "Mismo flujo con TRANSFER", "Venta registrada correctamente", "P1", "", "No"},
                            {"POS-OK-03", "POS", "EP válida", "Feliz", "Tarjeta", "Mismo flujo con CARD", "Venta registrada correctamente", "P1", "", "No"}
                    });
            writeSheet(wb, header, "Compras",
                    catalogHeaders(),
                    new String[][] {
                            {"COM-UI-01", "Compras", "Inspección", "UI", "Listado", "Entrar al módulo", "Título Compras y acción de nueva compra", "P1", "purchasesUiInspection", "Sí"},
                            {"COM-VAL-01", "Compras", "EP inválida", "Validación", "Proveedor", "Guardar sin proveedor", "El proveedor es obligatorio", "P1", "purchasesSupplierRequired", "Sí"},
                            {"COM-VAL-02", "Compras", "EP inválida", "Validación", "Líneas", "Guardar sin productos", "Agrega al menos un producto a la compra", "P1", "", "No"},
                            {"COM-OK-01", "Compras", "Caso de uso", "Feliz", "Borrador", "Crear compra", "Compra creada en borrador", "P1", "purchasesCreateAndConfirm", "Sí"},
                            {"COM-OK-02", "Compras", "Transición de estado", "Feliz", "Confirmar", "Confirmar compra", "Compra confirmada. Inventario actualizado.", "P1", "purchasesCreateAndConfirm", "Sí"}
                    });
            writeSheet(wb, header, "Clientes",
                    catalogHeaders(),
                    new String[][] {
                            {"CLI-UI-01", "Clientes", "Inspección", "UI", "Listado", "Entrar", "Título Clientes", "P1", "customersUiInspection", "Sí"},
                            {"CLI-VAL-01", "Clientes", "EP inválida", "Validación", "Nombre", "Guardar vacío", "El nombre completo es obligatorio", "P1", "customersRequiredValidation", "Sí"},
                            {"CLI-OK-01", "Clientes", "Caso de uso", "Feliz", "Alta", "Datos del Excel", "Cliente creado correctamente", "P1", "customersCreate", "Sí"}
                    });
            writeSheet(wb, header, "Proveedores",
                    catalogHeaders(),
                    new String[][] {
                            {"PRO-UI-01", "Proveedores", "Inspección", "UI", "Listado", "Entrar", "Título Proveedores", "P1", "suppliersUiInspection", "Sí"},
                            {"PRO-VAL-01", "Proveedores", "EP inválida", "Validación", "Nombre", "Guardar vacío", "El nombre es obligatorio", "P1", "suppliersRequiredValidation", "Sí"},
                            {"PRO-OK-01", "Proveedores", "Caso de uso", "Feliz", "Alta", "Datos del Excel", "Proveedor creado correctamente", "P1", "suppliersCreate", "Sí"}
                    });
            writeSheet(wb, header, "Usuarios",
                    catalogHeaders(),
                    new String[][] {
                            {"USR-UI-01", "Usuarios", "Inspección", "UI", "Listado", "Entrar", "Usuarios y Roles", "P1", "usersUiInspection", "Sí"},
                            {"USR-VAL-01", "Usuarios", "EP inválida", "Validación", "Alta", "Guardar vacío", "El usuario es obligatorio.", "P1", "usersRequiredValidation", "Sí"},
                            {"USR-OK-01", "Usuarios", "Caso de uso", "Feliz", "Alta cajero", "Rol CAJERO del Excel", "Usuario creado correctamente", "P1", "usersCreateCashier", "Sí"},
                            {"USR-ST-01", "Usuarios", "Transición de estado", "Roles", "Cajero vs Productos", "Login cajero y abrir /app/products", "Permanece en /app/dashboard (roleGuard)", "P1", "usersCashierCannotOpenProducts", "Sí"}
                    });
            writeSheet(wb, header, "Reportes",
                    catalogHeaders(),
                    new String[][] {
                            {"REP-UI-01", "Reportes", "Inspección", "UI", "Filtros", "Entrar", "Fecha desde/hasta y Aplicar filtros", "P1", "reportsUiInspection", "Sí"},
                            {"REP-OK-01", "Reportes", "Caso de uso", "Feliz", "Hoy", "Botón Hoy", "Se muestran totales del día", "P1", "reportsTodayFilter", "Sí"}
                    });
            writeSheet(wb, header, "Dashboard",
                    catalogHeaders(),
                    new String[][] {
                            {"DAS-UI-01", "Dashboard", "Inspección", "UI", "Bienvenida", "Tras login", "Hola, {nombre}", "P0", "dashboardCardsVisible", "Sí"},
                            {"DAS-UI-02", "Dashboard", "Inspección", "UI", "Tarjetas", "Ver accesos", "Ventas del día, Caja actual, Productos, Compras, Reportes", "P1", "dashboardCardsVisible", "Sí"}
                    });
            writeSheet(wb, header, "Regresion",
                    catalogHeaders(),
                    new String[][] {
                            {"REG-01", "Global", "Caso de uso", "E2E", "Login", "LOG-OK-01", "Entra al sistema", "P0", "global", "Sí"},
                            {"REG-02", "Global", "Caso de uso", "E2E", "Producto", "PRD-OK-01", "Queda un producto con stock", "P0", "global", "Sí"},
                            {"REG-03", "Global", "Caso de uso", "E2E", "Inventario", "INV-OK-01", "Stock aumenta", "P0", "global", "Sí"},
                            {"REG-04", "Global", "Caso de uso", "E2E", "Caja", "CAJ-OK-01", "Caja abierta", "P0", "global", "Sí"},
                            {"REG-05", "Global", "Caso de uso", "E2E", "Venta", "POS-OK-01", "Venta cobrada", "P0", "global", "Sí"}
                    });
            try (var out = Files.newOutputStream(file)) {
                wb.write(out);
            }
        }
    }

    private static String[] catalogHeaders() {
        return new String[] {"ID", "Modulo", "Tecnica ISTQB", "Tipo", "Elemento", "Accion", "Resultado esperado", "Prioridad", "Agrupacion", "Automatizado"};
    }

    private static Path moduleFolder(Path testdataDir, String folder) throws IOException {
        Path dir = testdataDir.resolve(folder);
        Files.createDirectories(dir);
        return dir;
    }

    private static void writeModuleBat(Path file, String groups, String title) throws IOException {
        writeBat(file, "..\\..\\..", groups, title);
    }

    private static void writeBat(Path file, String relativeToE2e, String groups, String title) throws IOException {
        String content = String.join("\r\n",
                "@echo off",
                "setlocal",
                "cd /d \"%~dp0" + relativeToE2e + "\"",
                "if not exist \"pom.xml\" (",
                "  echo No se encontro e2e\\pom.xml. Ejecuta este archivo desde el repositorio.",
                "  pause",
                "  exit /b 1",
                ")",
                "where mvn >nul 2>&1",
                "if errorlevel 1 (",
                "  echo Maven no esta en el PATH. Instala Maven y vuelve a intentar.",
                "  pause",
                "  exit /b 1",
                ")",
                "echo.",
                "echo Ejecutando pruebas: " + title + "  (grupo " + groups + ")",
                "echo Datatable: %~dp0",
                "echo Informe HTML: e2e\\reports\\",
                "echo.",
                "call mvn test -Dgroups=" + groups,
                "set EXITCODE=%ERRORLEVEL%",
                "echo.",
                "echo Codigo de salida: %EXITCODE%",
                "pause",
                "exit /b %EXITCODE%",
                ""
        );
        Files.writeString(file, content, StandardCharsets.UTF_8);
    }

    private static void writeGeneric(Path file, String sheetName, String[] headers, String[][] rows) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            writeSheet(wb, headerStyle(wb), sheetName, headers, rows);
            try (var out = Files.newOutputStream(file)) {
                wb.write(out);
            }
        }
    }

    private static void writeSheet(Workbook wb, CellStyle headerStyle, String name, String[] headers, String[][] data) {
        Sheet sheet = wb.createSheet(name);
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            var cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        for (int r = 0; r < data.length; r++) {
            Row row = sheet.createRow(r + 1);
            for (int c = 0; c < data[r].length; c++) {
                row.createCell(c).setCellValue(data[r][c]);
            }
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            int width = sheet.getColumnWidth(i);
            if (width < 3500) {
                sheet.setColumnWidth(i, 3500);
            }
            if (width > 12000) {
                sheet.setColumnWidth(i, 12000);
            }
        }
        sheet.createFreezePane(0, 1);
    }

    private static CellStyle headerStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
