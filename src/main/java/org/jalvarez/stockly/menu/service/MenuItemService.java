package org.jalvarez.stockly.menu.service;

import org.jalvarez.stockly.menu.dto.MenuItemDto;
import org.jalvarez.stockly.menu.dto.MenuMapper;
import org.jalvarez.stockly.menu.dto.SingleMenuItemDto;
import org.jalvarez.stockly.menu.repository.MenuItemRepository;
import org.jalvarez.stockly.util.exception.MenuItemNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemDto> getByActivity(boolean active, Pageable pageable) {
        return menuItemRepository.findAllByIsActive(active, pageable).stream().map(MenuMapper::toMenuItemDto).toList();
    }

    public SingleMenuItemDto getMenuItemById(Long id) {
        return MenuMapper.toSingularMenuItemDto(menuItemRepository.findById(id).orElseThrow(() -> new MenuItemNotFoundException("Item" + id + " not found."))); //throw error instead
    }

    public SingleMenuItemDto getMenuItemByName(String name) {
        SingleMenuItemDto dto = MenuMapper.toSingularMenuItemDto(menuItemRepository.findByName(name));
        if (dto == null) {
            throw new MenuItemNotFoundException("Item " + name + " not found.");
        }
        return dto;
    }
}
