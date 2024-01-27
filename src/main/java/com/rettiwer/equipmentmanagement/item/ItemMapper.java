package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.user.UserMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface ItemMapper {

    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "owner.id", target = "ownerId")
    ItemDTO toDto(Item item);

    @Mapping(source = "ownerId", target = "owner")
    Item toEntity(ItemDTO itemDTO);

    Item updateEntity(ItemDTO itemDTO, @MappingTarget Item item);

    List<ItemDTO> toListDto(List<Item> items);
}
