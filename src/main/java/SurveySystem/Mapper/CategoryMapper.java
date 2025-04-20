package SurveySystem.Mapper;

import SurveySystem.Model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CategoryMapper {
    List<Category> getAllCategories();
    List<Category> getAllCategoriesById(int userId);
    List<Category> getSubCategories(int parentId);
    List<Category> getParentCategories();
    void addCategory(Category category);
    void deleteCategory(int categoryId);
    void updateCategory(Category category);
    void updateCategorySortKey(Category category);
    Category getCategoryByCategoryId(int categoryId);
    // 添加分类搜索功能
    List<Category> searchCategories(String searchQuery);

    List<Category> getCategoriesByPage(int offset, int pageSize,String keyword);

    int getCategoryCount(String keyword);
}
