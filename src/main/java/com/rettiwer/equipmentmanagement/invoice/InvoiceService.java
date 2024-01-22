package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.CycleAvoidingMappingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public List<InvoiceItemsDTO> getAll() {
        return invoiceMapper.toListDtoWithItems(invoiceRepository.findAll(), new CycleAvoidingMappingContext());
    }

    public InvoiceDTO store(InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        return invoiceMapper.toDto(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceItemsDTO createWithItems(InvoiceItemsDTO invoiceItemsDTO) {
        invoiceItemsDTO.getItems().forEach(item -> item.setInvoice(invoiceItemsDTO));

        Invoice invoice = invoiceMapper.toEntityWithItems(invoiceItemsDTO, new CycleAvoidingMappingContext());
        return invoiceMapper.toDtoWithItems(invoiceRepository.save(invoice), new CycleAvoidingMappingContext());
    }
}
