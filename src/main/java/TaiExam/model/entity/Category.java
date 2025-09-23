package TaiExam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;          // 分类ID
    private String categoryName;
    private Integer parentCategoryId; // 上一级分类ID
    private String parentCategoryName;// 分类名称
    private String description;       // 分类描述
    private int categoryLevel;        // 分类层级
    private Timestamp createdAt; // 创建时间
    private Timestamp updatedAt; // 更新时间
    private int createdBy;
    private String sortKey;
}
