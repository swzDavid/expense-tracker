package de.davidschwietz.expensetracker.service;

import de.davidschwietz.expensetracker.model.Category;
import de.davidschwietz.expensetracker.repository.CategoryRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository repository = new CategoryRepository();

    public void addCategory(Category category) {
        repository.save(category);

    }

    public List<Category> findAllCategories() {
        return repository.findAll();

    }

    public Category findCategoryById(long id) {
        return repository.findById(id);

    }

    public void deleteCategory(long id) {
        repository.delete(id);

    }

    public void updateCategory(Category category) {
        repository.update(category);

    }
}
