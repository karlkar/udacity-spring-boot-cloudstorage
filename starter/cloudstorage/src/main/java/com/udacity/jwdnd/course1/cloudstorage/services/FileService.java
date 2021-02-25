package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isNameAllowed(int userId, String filename) {
        return fileMapper.getFileByName(userId, filename) == null;
    }

    public int insert(FileData fileData) {
        return fileMapper.insert(fileData);
    }

    public List<FileData> getAllFilesOf(int userId) {
        return fileMapper.getAllFilesOf(userId);
    }

    public void removeFile(int fileId) {
        fileMapper.removeFile(fileId);
    }

    public FileData getFile(int fileId) {
        return fileMapper.getFileById(fileId);
    }
}
