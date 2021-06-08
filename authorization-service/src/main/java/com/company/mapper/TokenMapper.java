package com.company.mapper;

import com.company.entity.TokenEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TokenMapper {

    @Select("SELECT * FROM tokens " +
            "WHERE service_from = #{serviceFrom} AND " +
            "service_to = #{serviceTo} AND " +
            "sending_time = #{sendingTime} AND " +
            "code = #{code}")
    TokenEntity selectTokenIfExist(TokenEntity tokenEntity);

    @Insert("INSERT INTO tokens (service_from, service_to, sending_time, code) " +
            "VALUES (#{serviceFrom}, #{serviceTo}, #{sendingTime}, #{code})")
    void addToken(TokenEntity tokenEntity);

    @Delete("DELETE FROM tokens " +
            "WHERE service_from = #{serviceFrom} AND " +
            "service_to = #{serviceTo} AND " +
            "sending_time = #{sendingTime} AND " +
            "code = #{code}")
    void deleteToken(TokenEntity tokenEntity);

    @Select("SELECT * FROM tokens")
    List<TokenEntity> selectAllTokens();
}
