package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.item.ItemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface InvoiceMapper {
    Invoice idToEntity(Long id);
    InvoiceDTO toDto(Invoice invoice);

    Invoice toEntity(InvoiceDTO invoiceDTO);

    Invoice toEntityWithItems(InvoiceItemsDTO invoiceItemsDTO);
    Invoice updateEntityWithItems(InvoiceItemsDTO invoiceItemsDTO, @MappingTarget Invoice invoice);

    InvoiceItemsDTO toDtoWithItems(Invoice invoice);

    List<InvoiceItemsDTO> toListDtoWithItems(List<Invoice> invoiceList);
}
