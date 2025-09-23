package TaiExam.mapper;

import TaiExam.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CategoryMapper {
    List<Category> getAllCategories(int userId);
    List<Category> getAllCategoriesById(int userId);
    List<Category> getSubCategories(int parentId,int userId);
    List<Category> getParentCategories(int userId);
    void addCategory(Category category);
    void deleteCategory(int categoryId);
    void updateCategory(Category category);
    void updateCategorySortKey(Category category);
    Category getCategoryByCategoryId(int categoryId);
    // 添加分类搜索功能
    List<Category> getCategoriesByPage(int offset, int pageSize,String keyword,int userId);

    int getCategoryCount(String keyword,int userId);
}
