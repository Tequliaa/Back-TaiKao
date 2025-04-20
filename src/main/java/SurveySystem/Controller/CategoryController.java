package SurveySystem.Controller;

import SurveySystem.Model.Result;
import SurveySystem.Model.Category;
import SurveySystem.Service.CategoryService;
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

    @PutMapping("/update")
    public Result<Void> updateCategory(@RequestBody Category category) {
        category.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        categoryService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteCategory(@RequestParam int categoryId) {
        categoryService.deleteCategory(categoryId);
        return Result.success();
    }

    @GetMapping("/getParentCategories")
    public Result<List<Category>> getParentCategories(@RequestParam int userId) {
        List<Category> categories = categoryService.getParentCategories(userId);
        return Result.success(categories);
    }

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

    @GetMapping("/getAllById")
    public Result<List<Category>> getAllCategoriesById(@RequestParam int userId) {
        System.out.println("getAllCategories");
        List<Category> categories = categoryService.getAllCategoriesById(userId);
        return Result.success(categories);
    }

    @PostMapping("/add")
    public Result<Void> addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success();
    }
}
