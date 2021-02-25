package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredData;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredService {

    private final CredMapper credMapper;
    private final EncryptionService encryptionService;

    public CredService(CredMapper credMapper, EncryptionService encryptionService) {
        this.credMapper = credMapper;
        this.encryptionService = encryptionService;
    }

    public int createCredential(CredData credData) {
        final String key = createEncryptionKey();
        final String encryptedPassword = encryptionService.encryptValue(credData.getPassword(), key);
        credData.setKey(key);
        credData.setPassword(encryptedPassword);
        return credMapper.insert(credData);
    }

    public List<CredData> getAllCredsOf(int userId) {
        return credMapper.getAll(userId).stream()
                .peek(credData -> credData.setDecryptedPassword(encryptionService.decryptValue(credData.getPassword(), credData.getKey())))
                .collect(Collectors.toList());
    }

    public void removeCred(int credId) {
        credMapper.remove(credId);
    }

    public void updateCred(CredData credData) {
        final String key = createEncryptionKey();
        final String encryptedPassword = encryptionService.encryptValue(credData.getPassword(), key);
        credData.setKey(key);
        credData.setPassword(encryptedPassword);
        credMapper.update(credData);
    }

    private String createEncryptionKey() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
