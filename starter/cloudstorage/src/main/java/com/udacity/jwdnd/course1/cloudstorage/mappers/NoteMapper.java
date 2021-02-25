package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userId) VALUES(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(NoteData noteData);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<NoteData> getAll(int userId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void remove(int noteId);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid} ")
    void update(NoteData noteData);
}
