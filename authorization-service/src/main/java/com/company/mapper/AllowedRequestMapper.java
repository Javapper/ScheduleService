package com.company.mapper;

import com.company.entity.AllowedRequestEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface AllowedRequestMapper {

    @Select("SELECT * FROM allowed_requests " +
            "WHERE service_from = #{serviceFrom} AND " +
            "service_to = #{serviceTo}")
    AllowedRequestEntity selectAllowedRequestIfExist(AllowedRequestEntity allowedRequestEntity);

    @Insert("INSERT INTO allowed_requests (service_from, service_to) " +
            "VALUES (#{serviceFrom}, #{serviceTo})")
    void addAllowedRequest(AllowedRequestEntity allowedRequestEntity);

    @Delete("DELETE FROM allowed_requests " +
            "WHERE service_from = #{serviceFrom} AND " +
            "service_to = #{serviceTo}")
    void deleteAllowedRequest(AllowedRequestEntity allowedRequestEntity);
}
