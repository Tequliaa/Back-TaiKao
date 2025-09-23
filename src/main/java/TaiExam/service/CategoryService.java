package TaiExam.service;

import TaiExam.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(int userId);
    List<Category> getAllCategoriesById(int userId);
    List<Category> getSubCategories(int parentId,int userId);
    List<Category> getParentCategories(int userId);
    void addCategory(Category category);
    void deleteCategory(int categoryId);
    void updateCategory(Category category);
    void updateCategorySortKey(Category category);
    Category getCategoryByCategoryId(int categoryId);

    List<Category> getCategoriesByPage(int page, int pageSize, String keyword, int userId);

    int getCategoryCount(String keyword,int userId);
}
