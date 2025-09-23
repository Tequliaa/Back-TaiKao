package TaiExam.controller;

import TaiExam.model.entity.Result;
import TaiExam.model.entity.Category;
import TaiExam.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分页获取分类列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listCategories(
            @RequestParam int userId,
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword) {
        List<Category> categories = categoryService.getCategoriesByPage(pageNum, pageSize, keyword,userId);
        int totalCount = categoryService.getCategoryCount(keyword,userId);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categories", categories);
        resultMap.put("totalCount", totalCount);

        return Result.success(resultMap);
    }

    /**
     * 更新分类操作
     * @param category
     * @return
     */
    @PutMapping("/update")
    public Result<Void> updateCategory(@RequestBody Category category) {
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        categoryService.updateCategory(category);
        return Result.success();
    }

    /**
     * 删除分类操作
     * @param categoryId
     * @return
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteCategory(@RequestParam int categoryId) {
        categoryService.deleteCategory(categoryId);
        return Result.success();
    }

    /**
     * 获取父分类列表
     * @param userId
     * @return
     */
    @GetMapping("/getParentCategories")
    public Result<List<Category>> getParentCategories(@RequestParam int userId) {
        List<Category> categories = categoryService.getParentCategories(userId);
        return Result.success(categories);
    }

    /**
     * 获取所有分类列表
     * @param userId
     * @return
     */
    @GetMapping("/getAll")
    public Result<List<Category>> getAllCategories(@RequestParam int userId) {
        System.out.println("getAllCategories");
        List<Category> categories = categoryService.getAllCategories(userId);
        //System.out.println("categories: ");
        //for(Category category:categories){
        //    System.out.println(category);
        //}
        return Result.success(categories);
    }

    /**
     * 根据用户授权下的所有分类列表
     * @param userId
     * @return
     */
    @GetMapping("/getAllById")
    public Result<List<Category>> getAllCategoriesById(@RequestParam int userId) {
        System.out.println("getAllCategories");
        List<Category> categories = categoryService.getAllCategoriesById(userId);
        return Result.success(categories);
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping("/add")
    public Result<Void> addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success();
    }
}
