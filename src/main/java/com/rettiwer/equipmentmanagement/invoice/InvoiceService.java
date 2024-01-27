package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;

    public List<InvoiceItemsDTO> getAll() {
        return invoiceMapper.toListDtoWithItems(invoiceRepository.findAll());
    }

    public InvoiceDTO store(InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        return invoiceMapper.toDto(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceItemsDTO createWithItems(InvoiceItemsDTO invoiceItemsDTO) {
        Invoice invoice = invoiceMapper.toEntityWithItems(invoiceItemsDTO);

        invoice.getItems().forEach(item -> item.setInvoice(invoice));

        return invoiceMapper.toDtoWithItems(invoiceRepository.save(invoice));
    }
}
