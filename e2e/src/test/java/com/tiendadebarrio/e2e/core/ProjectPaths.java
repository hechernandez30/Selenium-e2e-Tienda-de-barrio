package com.tiendadebarrio.e2e.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

public final class ProjectPaths {
    private ProjectPaths() {}

    public static Path moduleRoot() {
        Path userDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        if (userDir.getFileName() != null && "e2e".equals(userDir.getFileName().toString())) {
            return userDir;
        }
        Path nested = userDir.resolve("e2e");
        if (nested.toFile().isDirectory()) {
            return nested;
        }
        return userDir;
    }

    public static Path repoRoot() {
        Path module = moduleRoot();
        return module.getFileName() != null && "e2e".equals(module.getFileName().toString())
                ? module.getParent()
                : module;
    }

    public static Path testdataRoot() {
        return moduleRoot().resolve("testdata");
    }

    /**
     * Resuelve un Excel de datos por nombre de archivo.
     * Orden: carpeta del módulo (testdata/modulos/01-login/01-login.xlsx),
     * testdata/global, y por último cualquier coincidencia bajo testdata/.
     */
    public static Path testdata(String fileName) {
        String name = fileName == null ? "" : fileName.trim();
        if (name.isBlank()) {
            throw new IllegalArgumentException("Nombre de Excel vacío");
        }
        Path testdata = testdataRoot();
        String folder = name.contains(".") ? name.substring(0, name.lastIndexOf('.')) : name;
        Path inModuleFolder = testdata.resolve("modulos").resolve(folder).resolve(name);
        if (Files.isRegularFile(inModuleFolder)) {
            return inModuleFolder;
        }
        Path inGlobal = testdata.resolve("global").resolve(name);
        if (Files.isRegularFile(inGlobal)) {
            return inGlobal;
        }
        Path legacyFlat = testdata.resolve("modulos").resolve(name);
        if (Files.isRegularFile(legacyFlat)) {
            return legacyFlat;
        }
        Path found = findByFileName(testdata, name);
        return found != null ? found : inModuleFolder;
    }

    public static Path catalog() {
        return repoRoot().resolve("docs").resolve("pruebas").resolve("catalogo-casos-de-prueba.xlsx");
    }

    public static Path reports() {
        return moduleRoot().resolve("reports");
    }

    private static Path findByFileName(Path root, String fileName) {
        if (!Files.isDirectory(root)) {
            return null;
        }
        String wanted = fileName.toLowerCase(Locale.ROOT);
        try (Stream<Path> walk = Files.walk(root, 4)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).equals(wanted))
                    .findFirst()
                    .orElse(null);
        } catch (IOException ex) {
            return null;
        }
    }
}
