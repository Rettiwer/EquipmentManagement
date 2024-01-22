package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.CycleAvoidingMappingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemRepository itemRepository;
    public final ItemMapper itemMapper;

    public List<ItemInvoiceDTO> getAll() {
        return itemMapper.toListDtoWithInvoice(itemRepository.findAll(), new CycleAvoidingMappingContext());
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow();
    }

    public ItemDTO add(ItemDTO itemDTO) {
        Item item = itemMapper.toEntity(itemDTO);
        return itemMapper
                .toDto(itemRepository.save(item));
    }

    public ItemDTO replace(ItemDTO itemDTO, Long id) {
        Item newItem = itemMapper.toEntity(itemDTO);

        return itemMapper
                .toDto(itemRepository.findById(id)
                        .map(item -> {
                            item.setName(newItem.getName());
                            item.setPrice(newItem.getPrice());
                            item.setComment(newItem.getComment());

                            return itemRepository.save(item);
                        })
                        .orElseGet(() -> {
                            //newItem.setId(id);
                            return itemRepository.save(newItem);
                        }));
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
