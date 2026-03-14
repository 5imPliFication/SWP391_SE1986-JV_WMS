package com.example.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.*;

public class FileUtil {

    private static final String FOLDER_PATH = "static/images/products";

    public static String saveFile(Part filePart, HttpServletRequest request) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

        // Save to /target (load immediately after upload, missing after redeploy)
        // Paths.get auto control "\" or "/" --> Ok with all OS
        Path targetBase = Paths.get(request.getServletContext().getRealPath(""));
        Path targetDir = targetBase.resolve(FOLDER_PATH);

        if (Files.notExists(targetDir)) Files.createDirectories(targetDir);

        Path targetFilePath = targetDir.resolve(fileName);
        filePart.write(targetFilePath.toString());

        // Save to /src (Not load immediately after upload, keeping after redeploy)
        try {
            Path projectRoot = targetBase.getParent().getParent();
            Path sourceDir = projectRoot.resolve(Paths.get("src", "main", "webapp", FOLDER_PATH));

            if (Files.notExists(sourceDir)) Files.createDirectories(sourceDir);

            Path sourceFilePath = sourceDir.resolve(fileName);
            Files.copy(targetFilePath, sourceFilePath, StandardCopyOption.REPLACE_EXISTING); // Can throw IOException

        } catch (Exception e) {
            System.err.println("Fail to save to src: " + e.getMessage());
        }
        // Return for web display (user "/")
        return "/" + FOLDER_PATH + "/" + fileName;
    }

    public static void deleteFile(String imgUrl, HttpServletRequest request) {
        if (imgUrl == null || imgUrl.isEmpty()) return;
        try {
            // If not remove "/" in imgUrl, resolve will ignore targetBase (not + together) and return relativePath as absolute path --> Fail to delete file in target
            String normalizedImgUrl = imgUrl.startsWith("/") ? imgUrl.substring(1) : imgUrl;
            Path relativePath = Paths.get(normalizedImgUrl);

            // Delete in /target
            Path targetBase = Paths.get(request.getServletContext().getRealPath(""));
            // If not remove "/" in imgUrl, resolve will ignore targetBase (not + together) and return relativePath as absolute path --> Fail to delete file in target
            Files.deleteIfExists(targetBase.resolve(relativePath));

            // Xóa ở Source
            try {
                Path projectRoot = targetBase.getParent().getParent();
                Path sourcePath = projectRoot.resolve(Paths.get("src", "main", "webapp")).resolve(relativePath);
                Files.deleteIfExists(sourcePath);
            } catch (Exception e) {
                System.err.println("Fail to delete file in src: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Fail to delete file: " + e.getMessage());
        }
    }

    public static String updateImage(Part newFilePart, String oldImgUrl, HttpServletRequest request) throws IOException {
        if (newFilePart == null || newFilePart.getSize() <= 0) {
            return oldImgUrl;
        }
        if (oldImgUrl != null && !oldImgUrl.isEmpty()) {
            deleteFile(oldImgUrl, request);
        }
        return saveFile(newFilePart, request);
    }
}