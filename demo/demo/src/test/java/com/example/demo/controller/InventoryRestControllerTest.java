package com.example.demo.controller;

import com.example.demo.entity.Inventory;
import com.example.demo.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InventoryRestControllerTest {

    private InventoryService inventoryService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventoryService = mock(InventoryService.class);
        InventoryRestController inventoryController = new InventoryRestController(inventoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        objectMapper = new ObjectMapper();

        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setName("Computador");
    }

    @Test
    void testCreateInventory() throws Exception {
        when(inventoryService.createInventory(any(Inventory.class))).thenReturn(inventory);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateInventory() throws Exception {
        when(inventoryService.updateInventory(eq(1L), any(Inventory.class))).thenReturn(inventory);

        mockMvc.perform(put("/api/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeactivateInventory() throws Exception {
        doNothing().when(inventoryService).deactivateInventory(1L);

        mockMvc.perform(delete("/api/inventory/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllInventory() throws Exception {
        when(inventoryService.getAllInventory()).thenReturn(Collections.singletonList(inventory));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
