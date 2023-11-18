package org.groupscope.files.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileManagerImpl implements FileManager {

    private String rootPath;

    private final String defaultPath = File.separator;

    public FileManagerImpl(@Value("${upload.path}") String rootPath) {
        this.rootPath = createRootFolder(rootPath, defaultPath);
    }

    @Override
    public String createFolder(String path) throws IOException {
        if(path != null) {
            path = path.startsWith(rootPath) ? path : rootPath + path;

            File uploadDir = null;
            if(path.startsWith(rootPath)) {
                uploadDir = new File(path);
            } else {
                uploadDir = new File(rootPath + path);
            }

            if (!uploadDir.exists()) {
                boolean isCreate = uploadDir.mkdirs();

                if(isCreate) {
                    log.info("Create directory = " + path );

                    return uploadDir.getAbsolutePath();
                } else
                    throw new IOException("Directory = "+ uploadDir.getAbsolutePath() + " has not created");
            } else {
                return uploadDir.getAbsolutePath();
            }
        } else
            throw new NullPointerException("Path is null");
    }

    @Override
    public MultipartFile uploadFile(String path, MultipartFile file) throws IOException {
        if(path != null && file != null) {
            path = path.startsWith(rootPath) ? path : rootPath + path;
            String createdPath = createFolder(path);

            file.transferTo(new File(createdPath + File.separator + file.getOriginalFilename()));

            return null;
        } else
            throw new NullPointerException("Path or file is null: path = " + path + ", file = " + file);
    }

    @Override
    public File findFile(String path, String name) {
        if(path != null) {
            path = path.startsWith(rootPath) ? path : rootPath + path;
            File dir = new File(path);

            if(!dir.exists()) {
                return null;
            }

            return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    .filter(f -> f.getName().equals(name) && f.isFile())
                    .findFirst()
                    .orElse(null);
        } else
            throw new NullPointerException("Path is null");
    }

    @Override
    public List<File> findFilesByPath(String path) {
        if(path != null) {
            path = path.startsWith(rootPath) ? path : rootPath + path;
            File dir = new File(path);

            if(!dir.exists()) {
                return null;
            }

            return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    .filter(File::isFile)
                    .collect(Collectors.toList());
        } else
            throw new NullPointerException("Path is null");
    }

    @Override
    public List<Resource> downloadFile(List<File> files) throws IOException {
        List<Resource> resources = new ArrayList<>();
        try {
            if(files != null) {
                for (File file : files) {
                    Resource resource = new UrlResource(file.toURI());

                    if (resource.exists() && resource.isReadable()) {
                        resources.add(resource);
                    } else {
                        throw new IOException("Could not read file: path = " + file.getAbsolutePath());
                    }
                }
            } else
                throw new NullPointerException("List of files is null");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return resources;
    }

    @Override
    public void deleteFile(String path, String name) {
        if(path != null && name != null) {
            path = path.startsWith(rootPath) ? path : rootPath + path;

            File file = new File(path + File.separator + name);

            if(file.exists()) {
                log.info("Delete: path = " + file.getAbsolutePath() + " : " + file.delete());
            } else
                throw new IllegalArgumentException("File does not exist: path = " + file.getAbsolutePath());
        } else
            throw new NullPointerException("Path or name is null: path = " + path + ", name = " + name);
    }

    @Override
    public void deleteFolder(String path) throws IOException {
        if(path != null) {
            path = path.startsWith(rootPath) ? path : rootPath + path;
            File dir = new File(path);

            if(dir.exists()) {
                clearDirectory(dir);
                log.info("Delete: path = " + dir.getAbsolutePath() + " : " + dir.delete());
            } else
                throw new IllegalArgumentException("Directory does not exist: path = " + path);
        } else
            throw new NullPointerException("Path is null");
    }

    private String createRootFolder(String rootPath, String defaultPath) {
        if(rootPath != null) {
            File uploadDir = new File(rootPath);

            if (!uploadDir.exists()) {
                boolean isCreate = uploadDir.mkdirs();

                if(isCreate) {
                    log.info("Create directory = " + rootPath);

                    return uploadDir.getAbsolutePath();
                } else {
                    log.warn("Directory = " + rootPath + " has not created");
                    return defaultPath;
                }
            } else {
                return uploadDir.getAbsolutePath();
            }
        } else {
            log.warn("Path is null");
            return defaultPath;
        }
    }

    private void clearDirectory(File directory) throws IOException {
        if (directory == null)
            throw new NullPointerException("Directory is null");
        if (!directory.exists())
            throw new IOException("Directory does not exist");
        if (!directory.isDirectory())
            throw new IllegalArgumentException("File is not directory");

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isHidden() || file.isAbsolute())
                continue;
            else if (file.isDirectory()) {
                clearDirectory(file);
                log.info("Delete: path = " + file.getAbsolutePath() + " : " + file.delete());
            } else {
                log.info("Delete: path = " + file.getAbsolutePath() + " : " + file.delete());
            }
        }
    }
}
