# 问卷调查系统 - 后端项目

## 项目简介

这是一个基于Spring Boot的问卷调查系统后端实现，提供问卷管理、用户管理、数据统计分析等功能的RESTful API接口，以及WebSocket实时通信支持。

## 技术栈

- **框架**: Spring Boot 3.4.4
- **数据库访问**: MyBatis 3.5.13
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **身份认证**: JWT (JSON Web Token)
- **实时通信**: WebSocket
- **API文档**: Knife4j + SpringDoc OpenAPI
- **构建工具**: Maven
- **Java版本**: Java 17
- **工具库**: Hutool、Fastjson、Apache POI
- **AOP**: Spring AOP
- **数据校验**: Spring Validation

## 项目结构

```
src/main/java/TaiExam/
├── TaiExamApplication.java    # 应用入口
├── annotation/                       # 自定义注解
├── aspect/                           # AOP切面
├── config/                           # 配置类
├── context/                          # 上下文
├── controller/                       # 控制器
├── entity/                           # 实体类
│   ├── dto/                          # 数据传输对象
│   └── vo/                           # 视图对象
├── exception/                        # 异常处理
├── mapper/                           # MyBatis映射器
├── service/                          # 服务接口
│   └── Impl/                         # 服务实现
└── utils/                            # 工具类
```

## 主要功能模块

1. **用户管理**
   - 用户注册、登录、信息管理
   - 角色权限管理
   - 部门管理

2. **问卷管理**
   - 问卷创建、编辑、发布
   - 问卷分类管理
   - 题目和选项管理

3. **答题系统**
   - 答卷提交和管理
   - 未完成答卷管理
   - 答卷数据存储

4. **统计分析**
   - 答题数据统计
   - 问卷结果分析

5. **实时通信**
   - WebSocket连接管理
   - 实时数据推送
   - 答题状态更新

## 核心功能说明

### RESTful API设计

系统采用RESTful API设计风格，提供标准化的HTTP接口，支持CRUD操作和资源访问。主要接口包括：

- 用户认证与授权接口
- 问卷管理接口
- 问题选项管理接口
- 答卷提交与查询接口
- 统计分析接口
- 部门角色权限管理接口

### WebSocket实时通信

系统通过WebSocket实现实时通信功能，主要用于：

- 答题列表实时更新
- 未完成列表状态推送
- 管理员操作同步

WebSocket配置位于`config/WebSocketConfig.java`，处理器实现位于`controller/ExamWebSocketHandler.java`。

### 权限管理

系统采用基于JWT的身份认证和RBAC（基于角色的访问控制）权限管理机制：

- 使用JWT进行用户身份验证
- 基于角色和权限的访问控制
- 细粒度的API权限校验
- 自定义权限注解支持

### 数据缓存

系统使用Redis进行数据缓存，提高系统性能：

- 用户会话缓存
- 问卷模板缓存
- 统计数据缓存
- 自定义缓存注解支持

### 数据校验与异常处理

系统实现了完整的数据校验和异常处理机制：

- 请求参数校验
- 业务逻辑校验
- 全局异常捕获
- 统一响应格式

## 安装与运行

### 前提条件

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0
- Redis

### 数据库配置

1. 创建MySQL数据库：`tai_exam`
2. 导入数据库脚本（如有）
3. 配置`application.properties`中的数据库连接信息

### Redis配置

配置`application.properties`中的Redis连接信息：

```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
```

### 构建项目

```bash
mvn clean package
```

### 运行项目

```bash
java -jar target/examPlatform-0.0.1-SNAPSHOT.jar
```

## API文档

项目集成了Knife4j和SpringDoc OpenAPI，提供在线API文档：

- 访问地址：`http://localhost:8082/doc.html`
- 支持API接口浏览、测试和调试

## 数据库设计

系统主要包含以下核心数据表：

- `user`：用户信息表
- `role`：角色表
- `permission`：权限表
- `user_role`：用户角色关联表
- `role_permission`：角色权限关联表
- `department`：部门表
- `exam`：问卷表
- `category`：问卷分类表
- `question`：问题表
- `option`：选项表
- `response`：答卷表
- `user_exam`：用户问卷关联表
- `department_exam`：部门问卷关联表

## 安全措施

- 密码加密存储（MD5）
- JWT令牌验证
- 权限校验拦截器
- 请求参数校验
- 防SQL注入
- XSS防护

## 注意事项

1. 开发环境下，WebSocket端点为`/ws/exam/{examId}`
2. 生产环境需要配置HTTPS和WebSocket的WSS协议
3. 确保Redis服务正常运行，否则缓存功能不可用
4. 系统日志使用SLF4J，配置日志级别可在`application.properties`中设置

## License

MIT