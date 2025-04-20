package SurveySystem.Service.Impl;

import SurveySystem.Mapper.CategoryMapper;
import SurveySystem.Model.Category;
import SurveySystem.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategories();
    }

    @Override
    public List<Category> getAllCategoriesById(int userId) {
        return categoryMapper.getAllCategoriesById(userId);
    }

    @Override
    public List<Category> getSubCategories(int parentId) {
        return categoryMapper.getSubCategories(parentId);
    }

    @Override
    public List<Category> getParentCategories() {
        return categoryMapper.getParentCategories();
    }

    @Override
    public void addCategory(Category category) {
        categoryMapper.addCategory(category);
    }

    @Override
    public void deleteCategory(int categoryId) {
        categoryMapper.deleteCategory(categoryId);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void updateCategorySortKey(Category category) {
        categoryMapper.updateCategorySortKey(category);
    }

    @Override
    public Category getCategoryByCategoryId(int categoryId) {
        return categoryMapper.getCategoryByCategoryId(categoryId);
    }

    @Override
    public List<Category> searchCategories(String searchQuery) {
        return categoryMapper.searchCategories(searchQuery);
    }

    @Override
    public List<Category> getCategoriesByPage(int currentPage, int pageSize, String keyword) {
        int offset = (currentPage - 1) * pageSize;
        return categoryMapper.getCategoriesByPage(offset,pageSize,keyword);
    }

    @Override
    public int getCategoryCount(String keyword) {
        return categoryMapper.getCategoryCount(keyword);
    }
}
