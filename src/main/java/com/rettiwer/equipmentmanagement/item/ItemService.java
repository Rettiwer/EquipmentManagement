package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.CycleAvoidingMappingContext;
import com.rettiwer.equipmentmanagement.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemRepository itemRepository;
    public final InvoiceRepository invoiceRepository;
    public final ItemMapper itemMapper;

    public List<ItemInvoiceDTO> getAll() {
        return itemMapper.toListDtoWithInvoice(itemRepository.findAll(), new CycleAvoidingMappingContext());
    }

    public ItemDTO getItemById(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow());
    }

    public ItemDTO insert(ItemDTO itemDTO) {
        Item item = itemMapper.toEntity(itemDTO);
        return itemMapper.toDto(itemRepository.save(item));
    }

    public ItemDTO replaceOrInsert(ItemDTO itemDTO, @Nullable Item item) {
        if (item != null)
            itemDTO.setId(null);

       return itemMapper.toDto(
                itemRepository.save(itemMapper.toEntity(itemDTO)));
    }

    public HttpStatus delete(Item item) {
        if (item != null) {
            itemRepository.delete(item);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }
}
