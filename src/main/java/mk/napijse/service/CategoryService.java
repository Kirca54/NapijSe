package mk.napijse.service;

import mk.napijse.model.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category create(String name, String description);
}
