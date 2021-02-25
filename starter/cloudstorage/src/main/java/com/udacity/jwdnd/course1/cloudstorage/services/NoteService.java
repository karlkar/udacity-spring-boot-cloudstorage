package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(NoteData noteData) {
        return noteMapper.insert(noteData);
    }

    public List<NoteData> getAllNotesOf(int userId) {
        return noteMapper.getAll(userId);
    }

    public void removeNote(int noteId) {
        noteMapper.remove(noteId);
    }

    public void updateNote(NoteData noteData) {
        noteMapper.update(noteData);
    }
}
