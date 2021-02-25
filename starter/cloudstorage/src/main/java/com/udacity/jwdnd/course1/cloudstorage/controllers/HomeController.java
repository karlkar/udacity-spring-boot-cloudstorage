package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.CredData;
import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteData;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredService credService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredService credService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credService = credService;
    }

    @GetMapping("/home")
    public String getHome(
            Authentication authentication,
            Model model
    ) {
        final User user = userService.findUserByName(authentication.getName());
        final int userId = user.getUserId();
        final List<FileData> fileEntries = fileService.getAllFilesOf(userId);
        model.addAttribute("fileEntries", fileEntries);
        final List<NoteData> noteEntries = noteService.getAllNotesOf(userId);
        model.addAttribute("noteEntries", noteEntries);
        final List<CredData> credEntries = credService.getAllCredsOf(userId);
        model.addAttribute("credEntries", credEntries);
        return "home";
    }

    @GetMapping("/home/file/delete/{fileId}")
    public String getResultAfterFileDelete(
            Authentication authentication,
            Model model,
            @PathVariable int fileId
    ) {
        String errorMsg = null;
        final User user = userService.findUserByName(authentication.getName());
        final int userId = user.getUserId();
        final List<FileData> fileEntries = fileService.getAllFilesOf(userId);
        if (fileEntries.stream().anyMatch(fileData -> fileData.getFileId() == fileId)) {
            fileService.removeFile(fileId);
        } else {
            errorMsg = "Trying to remove invalid file";
        }
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @GetMapping("/home/file/view/{fileId}")
    public ResponseEntity<InputStreamResource> getFile(
            Authentication authentication,
            Model model,
            @PathVariable int fileId
    ) {
        String errorMsg = null;
        final User user = userService.findUserByName(authentication.getName());
        final int userId = user.getUserId();
        final List<FileData> fileEntries = fileService.getAllFilesOf(userId);
        FileData file = null;
        if (fileEntries.stream().anyMatch(fileData -> fileData.getFileId() == fileId)) {
            file = fileService.getFile(fileId);
        } else {
            errorMsg = "Trying to remove invalid file";
        }
        if (errorMsg != null) {
            model.addAttribute("errorMsg", errorMsg);
            return ResponseEntity.badRequest().build();
        } else {
            final MediaType mediaType = MediaType.valueOf(file.getContenttype());
            final InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getFiledata()));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFilename())
                    .contentType(mediaType)
                    .contentLength(Long.parseLong(file.getFilesize()))
                    .body(resource);
        }
    }

    @GetMapping("/home/note/delete/{noteId}")
    public String getResultAfterNoteDelete(
            Authentication authentication,
            Model model,
            @PathVariable int noteId
    ) {
        String errorMsg = null;
        final User user = userService.findUserByName(authentication.getName());
        final int userId = user.getUserId();
        final List<NoteData> noteEntries = noteService.getAllNotesOf(userId);
        if (noteEntries.stream().anyMatch(noteData -> noteData.getNoteid() == noteId)) {
            noteService.removeNote(noteId);
        } else {
            errorMsg = "Trying to remove invalid note";
        }
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @PostMapping("/file-upload")
    public String fileUpload(Authentication authentication, @RequestParam MultipartFile fileUpload, Model model) throws IOException {
        String errorMsg = null;
        final int userId = userService.findUserByName(authentication.getName()).getUserId();
        final String filename = fileUpload.getOriginalFilename();
        if (!fileService.isNameAllowed(userId, filename)) {
            errorMsg = "Name already exists!";
        } else {
            final String contentType = fileUpload.getContentType();
            final String size = String.valueOf(fileUpload.getSize());
            final byte[] data = fileUpload.getBytes();
            final int fileId = fileService.insert(new FileData(null, filename, contentType, size, userId, data));
            if (fileId < 0) {
                errorMsg = "Couldn't insert file";
            }
        }
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @PostMapping("/note-upload")
    public String noteUpload(
            Authentication authentication,
            @RequestParam Integer noteId,
            @RequestParam String noteTitle,
            @RequestParam String noteDescription,
            Model model) {
        String errorMsg = null;
        final int userId = userService.findUserByName(authentication.getName()).getUserId();
        if (noteId != null) {
            noteService.updateNote(new NoteData(noteId, noteTitle, noteDescription, userId));
        } else {
            final int createdNoteId = noteService.createNote(new NoteData(null, noteTitle, noteDescription, userId));
            if (createdNoteId < 0) {
                errorMsg = "Couldn't create a note";
            }
        }
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @PostMapping("/cred-upload")
    public String credUpload(
            Authentication authentication,
            @RequestParam Integer credentialId,
            @RequestParam String url,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        String errorMsg = null;
        final int userId = userService.findUserByName(authentication.getName()).getUserId();
        if (credentialId != null) {
            credService.updateCred(new CredData(credentialId, url, username, null, password, userId));
        } else {
            final int createdCredId = credService.createCredential(new CredData(null, url, username, null, password, userId));
            if (createdCredId < 0) {
                errorMsg = "Couldn't create a credential";
            }
        }
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @GetMapping("/home/cred/delete/{credId}")
    public String getResultAfterCredDelete(
            Authentication authentication,
            Model model,
            @PathVariable int credId
    ) {
        String errorMsg = null;
        final User user = userService.findUserByName(authentication.getName());
        final int userId = user.getUserId();
        final List<CredData> noteEntries = credService.getAllCredsOf(userId);
        if (noteEntries.stream().anyMatch(credData -> credData.getCredentialid() == credId)) {
            credService.removeCred(credId);
        } else {
            errorMsg = "Trying to remove invalid credential";
        }
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }
}
