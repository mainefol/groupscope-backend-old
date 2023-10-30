package org.groupscope.files.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {

    String createFolder(String path, String name);

    MultipartFile save(String path, MultipartFile file);

    MultipartFile findFile(String path, String name);

    void deleteFile(String path, String name);

    void deleteFolder(String path, String name);
}
