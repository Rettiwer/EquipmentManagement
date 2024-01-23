package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.CycleAvoidingMappingContext;
import com.rettiwer.equipmentmanagement.ReferenceMapper;
import com.rettiwer.equipmentmanagement.invoice.Invoice;
import com.rettiwer.equipmentmanagement.invoice.InvoiceMapper;
import com.rettiwer.equipmentmanagement.invoice.InvoiceRepository;
import com.rettiwer.equipmentmanagement.invoice.InvoiceService;
import com.rettiwer.equipmentmanagement.user.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InvoiceMapper.class, UserMapper.class})
public interface ItemMapper {

    @Mapping(source = "item.invoice.id", target = "invoiceId")
    @Mapping(source = "item.owner.id", target = "ownerId")
    ItemDTO toDto(Item item);

    @Mapping(source = "invoiceId", target = "invoice")
    @Mapping(source = "ownerId", target = "owner")
    Item toEntity(ItemDTO itemDTO);

    Item updateEntity(ItemDTO itemDTO, @MappingTarget Item item);

    List<ItemDTO> toListDto(List<Item> items);

    //@Mapping(target = "ownerId", source = "owner.id")
    ItemInvoiceDTO toDtoWithInvoice(Item item, @Context CycleAvoidingMappingContext context);

    //@Mapping(target = "owner.id", source = "ownerId")

    Item toEntityWithInvoice(ItemInvoiceDTO itemInvoiceDTO, @Context CycleAvoidingMappingContext context);

    List<ItemInvoiceDTO> toListDtoWithInvoice(List<Item> itemList, @Context CycleAvoidingMappingContext context);
}
