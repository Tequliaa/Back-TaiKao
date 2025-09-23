package TaiExam.service.Impl;

import TaiExam.mapper.CategoryMapper;
import TaiExam.model.entity.Category;
import TaiExam.service.CategoryService;
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
    public List<Category> getAllCategories(int userId) {
        return categoryMapper.getAllCategories(userId);
    }

    @Override
    public List<Category> getAllCategoriesById(int userId) {
        return categoryMapper.getAllCategoriesById(userId);
    }

    @Override
    public List<Category> getSubCategories(int parentId,int userId) {
        return categoryMapper.getSubCategories(parentId,userId);
    }

    @Override
    public List<Category> getParentCategories(int userId) {
        return categoryMapper.getParentCategories(userId);
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
    public List<Category> getCategoriesByPage(int currentPage, int pageSize, String keyword,int userId) {
        int offset = (currentPage - 1) * pageSize;
        return categoryMapper.getCategoriesByPage(offset,pageSize,keyword,userId);
    }

    @Override
    public int getCategoryCount(String keyword,int userId) {
        return categoryMapper.getCategoryCount(keyword,userId);
    }
}
