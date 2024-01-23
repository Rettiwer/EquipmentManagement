package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class})
public interface UserMapper {

    User idToEntity(Integer id);

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
