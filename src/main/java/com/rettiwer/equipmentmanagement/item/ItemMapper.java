package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.CycleAvoidingMappingContext;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDTO toDto(Item item);

    Item toEntity(ItemDTO itemDTO);

    List<ItemDTO> toListDto(List<Item> items);

    //@Mapping(target = "ownerId", source = "owner.id")
    ItemInvoiceDTO toDtoWithInvoice(Item item, @Context CycleAvoidingMappingContext context);

    //@Mapping(target = "owner.id", source = "ownerId")

    Item toEntityWithInvoice(ItemInvoiceDTO itemInvoiceDTO, @Context CycleAvoidingMappingContext context);

    List<ItemInvoiceDTO> toListDtoWithInvoice(List<Item> itemList, @Context CycleAvoidingMappingContext context);
}
