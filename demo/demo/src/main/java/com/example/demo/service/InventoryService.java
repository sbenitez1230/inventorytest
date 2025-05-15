package com.example.demo.service;

import com.example.demo.entity.Inventory;
import com.example.demo.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public Inventory createInventory(Inventory inventory) {
        return repository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory updateInventory) {
        Inventory existing = repository.findById(id).orElseThrow();
        existing.setName(updateInventory.getName());
        existing.setDescription(updateInventory.getDescription());
        existing.setPurchaseDate(updateInventory.getPurchaseDate());
        existing.setPurchaseValue(updateInventory.getPurchaseValue());
        existing.setAnnualExpRate(updateInventory.getAnnualExpRate());
        return repository.save(existing);
    }

    public void deactivateInventory(Long id) {
        Inventory inventory = repository.findById(id).orElseThrow();
        inventory.setActive(false);
        repository.save(inventory);
    }

    public List<Inventory> getAllInventory() {
        return repository.findAll();
    }

    public BigDecimal calculateDepreciatedValue(Inventory asset) {
        long years = Period.between(asset.getPurchaseDate(), LocalDate.now()).getYears();
        BigDecimal rate = BigDecimal.valueOf(1 - asset.getAnnualExpRate() / 100.0);
        return asset.getPurchaseValue().multiply(rate.pow((int) years));
    }

}
