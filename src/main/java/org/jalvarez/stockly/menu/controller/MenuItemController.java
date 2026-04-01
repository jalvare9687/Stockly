package org.jalvarez.stockly.menu.controller;

import org.jalvarez.stockly.menu.dto.MenuItemDto;
import org.jalvarez.stockly.menu.dto.SingleMenuItemDto;
import org.jalvarez.stockly.menu.service.MenuItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("by-activity")
    public List<MenuItemDto> getMenuItemsByActivity(@RequestParam Boolean activity, Pageable pageable) {
        return menuItemService.getByActivity(activity, pageable);
    }

    @GetMapping("{id}")
    public SingleMenuItemDto getMenuItemById(@PathVariable Long id) {
        return menuItemService.getMenuItemById(id);
    }

    @GetMapping("item-by-name") //maybe convert path variable to lowercase?
    public SingleMenuItemDto getMenuItemByName(@RequestParam String name) {
        return menuItemService.getMenuItemByName(name);
    }
}
