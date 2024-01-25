package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.ReferenceMapper;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.mapper.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class, RoleMapper.class})
public interface UserMapper {

    User idToEntity(Integer id);

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);



    User registerRequestToEntity(RegisterRequest request);
}
