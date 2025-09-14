package SurveySystem.controller;

import SurveySystem.context.BaseContext;
import SurveySystem.entity.*;
import SurveySystem.entity.dto.UserDTO;
import SurveySystem.entity.vo.UserInfoVO;
import SurveySystem.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import SurveySystem.utils.HashUtils;
import SurveySystem.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/user")

public class UserController {
    private final UserService userService;
    public final DepartmentService departmentService;
    private final DepartmentSurveyService departmentSurveyService;
    private final UserSurveyService userSurveyService;
    private final UserRoleService userRoleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${role.default}")
    private String defaultRole;

    // 依赖注入（替代手动 new 和 init()）
    public UserController(UserService userService,DepartmentService departmentService,
                          DepartmentSurveyService departmentSurveyService,
                          UserSurveyService userSurveyService,UserRoleService userRoleService) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.departmentSurveyService = departmentSurveyService;
        this.userSurveyService = userSurveyService;
        this.userRoleService = userRoleService;
    }

    //------------------------ 用户列表查询 ------------------------
    @GetMapping("/list")
    public Result<Map<String, Object>> listUsers(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam int userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int departmentId) {

        List<User> users = userService.getUsersByPage(pageNum, pageSize, keyword, departmentId,userId);
        int totalCount = userService.getUserCount(keyword, departmentId,userId);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("users", users);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    //------------------------ 用户登录 ------------------------
    @PostMapping("/login")
    public Result<String> login(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam(required = false, defaultValue = "false") Boolean rememberMe) {

        User loginUser = userService.getUserByUsername(username);
        if (loginUser == null) {
            return Result.error("用户名不存在");
        }

        if (!HashUtils.verifyPassword(password, loginUser.getPassword(), loginUser.getSalt())) {
            return Result.error("密码错误");
        }

        // 选择过期时间
        long ttlSeconds = Boolean.TRUE.equals(rememberMe) ? jwtUtil.getRememberExpiration() : jwtUtil.getExpiration();

        // 生成 JWT Token（携带自定义过期时间）
        String token = jwtUtil.generateToken((long) loginUser.getId(), ttlSeconds);
        User user = userService.getUserByUserId(loginUser.getId());

        // 转为 Map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userInfo = objectMapper.convertValue(user, Map.class);

        // 存储会话至 Redis（使用同样的 TTL）
        jwtUtil.storeSessionInRedis(token, userInfo, ttlSeconds);

        return Result.success(token);
    }

    //------------------------ 用户注册 ------------------------
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            return Result.error("用户名已被占用");
        }

        // 密码加密
        String salt = HashUtils.getSalt();
        String hashedPassword = HashUtils.hashPassword(user.getPassword(), salt);
        user.setPassword(hashedPassword);
        user.setSalt(salt);
        //新注册用户聚集地
        user.setDepartmentId(1);

        //user.setName("用户");
        String name = "用户";
        user.setName(name+user.getUsername());
        userService.registerUser(user);
        int userId=user.getId();
        List<DepartmentSurvey> departmentSurveys=departmentSurveyService.getDepartmentSurveys(user.getDepartmentId());
        userSurveyService.assignSurveysToUser(userId,departmentSurveys);
        return Result.success();
    }

    /**
     * 用户登出操作
     * @return
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        jwtUtil.inValidate(token);
        return Result.success();
    }


    /**
     * 获取当前用户信息
     * @return
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        int userId =0;
        try {
            userId = BaseContext.getCurrentId();
        }catch (Exception e){
            e.printStackTrace();;
        }finally {
            BaseContext.removeCurrentId();
        }
        log.info("当前用户id：" + userId);
        User user = userService.getUserByUserId(userId);
        UserInfoVO userVo = new UserInfoVO();
        BeanUtils.copyProperties(user,userVo);
        //todo 目前用户和角色是多对一 后续可拓展
        List<Integer> roleIds = userRoleService.getRolesByUserId(userId);
        List<String> codes = new ArrayList<>();
        for(int roleId:roleIds){
            codes = rolePermissionService.getPermissionCodesByRoleId(roleId);
        }
        userVo.setPermissionCodes(codes);
        return Result.success(userVo);
    }


    @RequestMapping("/checkPermission")
    public Result checkPermission(@RequestParam int userId,@RequestParam String permissionCode) {
        String roleName = userService.getUserByUserId(userId).getRoleName();
        int roleId = roleService.getRoleByName(roleName).getId();
        List<String> permissionCodes = rolePermissionService.getPermissionCodesByRoleId(roleId);
        if(permissionCodes.contains(permissionCode)){
            return Result.success("true");
        }else{
            return Result.error("角色无对应权限。");
        }
    }


    //------------------------ 修改密码 ------------------------
    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(
            @RequestParam String oldPwd,
            @RequestParam String newPwd,
            @RequestParam String rePwd,
            @RequestHeader("Authorization") String token) {
        int userId =0;
        try {
            userId = BaseContext.getCurrentId();
        }catch (Exception e){
            e.printStackTrace();;
        }finally {
            BaseContext.removeCurrentId();
        }
        log.info("当前用户id：" + userId);
        User loginUser = userService.getUserByUserId(userId);

        if (oldPwd == null || newPwd == null || rePwd == null) {
            return Result.error("缺少必要的参数");
        } else if (!HashUtils.verifyPassword(oldPwd, loginUser.getPassword(), loginUser.getSalt())) {
            return Result.error("原密码填写不正确");
        } else if (!rePwd.equals(newPwd)) {
            return Result.error("两次填写的新密码不一样");
        }

        // 更新密码
        String salt = HashUtils.getSalt();
        String hashedPassword = HashUtils.hashPassword(newPwd, salt);
        loginUser.setPassword(hashedPassword);
        loginUser.setSalt(salt);
        userService.updatePassword(loginUser);

        // 删除 Redis 中的旧 Token
        jwtUtil.inValidate(token);
        return Result.success();
    }

    //------------------------ 修改用户信息 ------------------------
    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        userService.updateUser(user);

        //调整用户的角色
        UserRole userRole = new UserRole();
        userRole.setRoleId(Integer.parseInt(userDTO.getRole()));
        userRole.setUserId(user.getId());
        userRoleService.insertUserRole(userRole);

        return Result.success();
    }

    //------------------------ 删除用户 ------------------------
    @DeleteMapping("/delete")
    public Result<Void> deleteUser(@RequestParam int id) {
        userService.deleteUserById(id);
        return Result.success();
    }

    //------------------------ 增加用户角色 ------------------------
    //todo 目前为单个增加，后续可拓展为 用户和角色多对多
    @RequestMapping ("/addRoles")
    public Result<Void> addRoles(@RequestBody UserRole userRole) {
        userRoleService.insertUserRole(userRole);
        return Result.success();
    }

    //------------------------ 更新用户角色 ------------------------
    @RequestMapping ("/updateRoles")
    public Result<Void> updateRoles(@RequestBody UserRole userRole) {
        userRoleService.updateUserRole(userRole);
        return Result.success();
    }

    @GetMapping("/export")
    public void exportUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int departmentId,
            @RequestParam int userId,
            HttpServletResponse response) throws IOException {
        String departmentName="所有用户";
        if(departmentId!=0){
            departmentName=departmentService.getDepartmentById(departmentId).getName();
        }
        //
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode("用户列表_" +departmentName+ LocalDate.now() + ".xlsx", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 创建工作簿和工作表
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("用户列表");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"用户ID", "用户名","用户昵称","所属部门", "角色"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 获取数据
            List<User> users = userService.getUsersByPage(1, Integer.MAX_VALUE, keyword, departmentId,userId);

            // 填充数据
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getName());
                row.createCell(3).setCellValue(user.getDepartmentName());
                row.createCell(4).setCellValue(user.getRoleName());
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
        }
    }

    @PostMapping("/import")
    public Result<ImportResult> importUsers(@RequestParam("file") MultipartFile file,
                                            @RequestParam(defaultValue = "") String departmentName) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要导入的文件");
        }

        try {
            // 检查文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return Result.error("请上传Excel文件");
            }

            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<User> userList = new ArrayList<>();
            int totalRows = sheet.getLastRowNum();
            int successCount = 0;
            int skipCount = 0;
            List<String> skipReasons = new ArrayList<>();

            // 跳过标题行
            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    skipCount++;
                    skipReasons.add("第" + (i+1) + "行为空");
                    continue;
                }

                User user = new User();
                user.setUsername(getCellValueAsString(row.getCell(0)).trim()); // 用户名
                user.setName(getCellValueAsString(row.getCell(1)).trim()); // 用户昵称

                String defaultPassword = getCellValueAsString(row.getCell(2)).trim();//默认密码
                String salt = HashUtils.getSalt();
                String hashedPassword = HashUtils.hashPassword(defaultPassword, salt);
                user.setPassword(hashedPassword);
                user.setSalt(salt);

                if(departmentName.isEmpty()||"".equals(departmentName)){
                    departmentName = getCellValueAsString(row.getCell(3)).trim(); // 部门名称
                }

                // 检查用户名是否为空
                if (user.getUsername() == null || user.getUsername().isEmpty()) {
                    skipCount++;
                    skipReasons.add("第" + (i+1) + "行用户名为空");
                    continue;
                }

                // 检查用户名是否已存在
                if (userService.getUserByUsername(user.getUsername()) != null) {
                    skipCount++;
                    skipReasons.add("第" + (i+1) + "行用户名已存在");
                    continue;
                }

                // 根据部门名称查找部门ID
                if (!departmentName.isEmpty()) {
                    Department department = departmentService.getDepartmentByName(departmentName);
                    if (department != null) {
                        user.setDepartmentId(department.getId());
                    }
                }

                userList.add(user);
            }

            // 批量插入
            if (!userList.isEmpty()) {
                successCount = userService.batchInsertUsers(userList);
            }

            workbook.close();

            // 返回导入结果统计
            ImportResult result = new ImportResult();
            result.setTotal(totalRows);
            result.setSuccess(successCount);
            result.setSkip(skipCount);
            result.setSkipReasons(skipReasons);
            System.out.println("totalRows:"+totalRows+"successCount:"+successCount+"skipCount:"+skipCount+"skipReasons:"+skipReasons);

            int departmentId=0;
            //开始对导入的用户分配问卷
            for(User user:userList){
                departmentId=user.getDepartmentId();
                break;
            }
            //获取部门所分发过的问卷
            List<DepartmentSurvey> departmentSurveys=departmentSurveyService.getDepartmentSurveys(departmentId);
            //当用户列表跟部门问卷列表都不为空时再执行批量补发
            if(!userList.isEmpty()&&!departmentSurveys.isEmpty()){
                userSurveyService.assignSurveysToUsers(userList,departmentSurveys);
            }

            //对导入的用户批量分配角色
            Role role = roleService.getRoleByName(defaultRole);
            userRoleService.assignRolesToUsers(userList,role.getId());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    // 导入结果类
    @Data
    public static class ImportResult {
        private int total;      // 总行数
        private int success;    // 成功数量
        private int skip;       // 跳过数量
        private List<String> skipReasons; // 跳过原因
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
