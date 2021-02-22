package mk.napijse.service.impl;

import mk.napijse.model.Category;
import mk.napijse.repository.CategoryRepository;
import mk.napijse.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category create(String name, String description) {
        Category category = new Category(name, description);
        return this.categoryRepository.save(category);
    }
}
