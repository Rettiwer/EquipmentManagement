package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.UserMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface ItemMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(target = "invoiceId", source = "invoice.id")
    ItemDTO toDto(Item item);

    @Mapping(source = "ownerId", target = "owner")
    Item toEntity(ItemDTO itemDTO);

    @Mapping(source = "owner.id", target = "ownerId")
    List<ItemDTO> toListDto(List<Item> items);


    /*

        User items mapping

     */

    @Mapping(target = "supervisorId", source = "supervisor.id")
    UserItemsDTO toUserItemsDto(User user);

    List<UserItemsDTO> toUserItemsDtoList(List<User> users);
}
