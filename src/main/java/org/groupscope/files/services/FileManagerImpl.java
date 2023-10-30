package org.groupscope.files.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileManagerImpl implements FileManager {

    private final GoogleDriveService googleDriveService;

    @Autowired
    public FileManagerImpl(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @Transactional
    @Override
    public String createFolder(String path, String name) {
        return null;
    }

    @Transactional
    @Override
    public MultipartFile save(String path, MultipartFile file) {
        return null;
    }

    @Transactional
    @Override
    public MultipartFile findFile(String path, String name) {
        return null;
    }

    @Transactional
    @Override
    public void deleteFile(String path, String name) {

    }

    @Transactional
    @Override
    public void deleteFolder(String path, String name) {

    }
}
