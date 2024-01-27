package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.item.ItemMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface InvoiceMapper {
    Invoice idToEntity(Long id);
    InvoiceDTO toDto(Invoice invoice);

    Invoice toEntity(InvoiceDTO invoiceDTO);

    Invoice toEntityWithItems(InvoiceItemsDTO invoiceItemsDTO);

    InvoiceItemsDTO toDtoWithItems(Invoice invoice);

    List<InvoiceItemsDTO> toListDtoWithItems(List<Invoice> invoiceList);
}
