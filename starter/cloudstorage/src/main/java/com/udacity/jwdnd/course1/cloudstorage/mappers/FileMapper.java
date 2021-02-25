package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contenttype}, #{filesize}, #{userId}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(FileData fileData);

    @Select("SELECT * FROM FILES WHERE userId = #{userId} filename = #{filename}")
    FileData getFileByName(int userId, String filename);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<FileData> getAllFilesOf(int userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void removeFile(int fileId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    FileData getFileById(int fileId);
}
