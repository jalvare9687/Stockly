package org.jalvarez.stockly;

import org.jalvarez.stockly.menu.controller.MenuItemController;
import org.jalvarez.stockly.menu.dto.MenuItemDto;
import org.jalvarez.stockly.menu.dto.RecipeLineDto;
import org.jalvarez.stockly.menu.dto.SingleMenuItemDto;
import org.jalvarez.stockly.menu.service.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
public class MenuItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MenuItemService menuItemService;

    private MenuItemDto activeMenuItemDto;
    private MenuItemDto inactiveMenuItemDto;
    private SingleMenuItemDto singleMenuItemDto;

    @BeforeEach
    void setUp() {
        String unique = UUID.randomUUID().toString().substring(0, 8);

        activeMenuItemDto  = new MenuItemDto(
                1L,
                "Burger",
                new BigDecimal("9.99"),
                true
        );

        inactiveMenuItemDto  = new MenuItemDto(
                2L,
                "Steak",
                new BigDecimal("9.99"),
                false
        );

        List<RecipeLineDto> recipeLineDtos = new ArrayList<>();
        recipeLineDtos.add(new RecipeLineDto(
                1L,
                "Beef patty",
                "Burger",
                new BigDecimal("1.00")
        ));
        recipeLineDtos.add(new RecipeLineDto(
                2L,
                "Lettuce",
                "Burger",
                new BigDecimal("1.00")
        ));

        singleMenuItemDto = new SingleMenuItemDto(
                1L,
                "Burger",
                new BigDecimal("9.99"),
                true,
                recipeLineDtos
        );
    }

    @Test
    void getByActivityTrueShouldReturnActiveItems() throws Exception {
        when(menuItemService.getByActivity(eq(true), any(Pageable.class)))
                .thenReturn(List.of(activeMenuItemDto));

        mvc.perform(
                get("/api/menu-items/by-activity")
                        .param("activity", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getByIdShouldReturnBurger() throws Exception {
        when(menuItemService.getMenuItemById(eq(1L)))
                .thenReturn(singleMenuItemDto);

        mvc.perform(
                get("/api/menu-items/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getByNameShouldReturnBurger() throws Exception {
        when(menuItemService.getMenuItemByName(eq("Burger")))
            .thenReturn(singleMenuItemDto);

        mvc.perform(
                get("/api/menu-items/item-by-name")
                        .param("name", "Burger")
                )
                .andExpect(status().isOk());
    }
}
