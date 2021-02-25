package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.model.CredData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredMapper {
    @Insert("INSERT INTO CREDENTIALS(url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(CredData credData);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<CredData> getAll(int userId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credId}")
    void remove(int credId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialid} ")
    void update(CredData credData);
}
