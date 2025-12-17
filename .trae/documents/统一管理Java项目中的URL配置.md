# 统一管理Java项目中的URL配置

## 1. 检查结果
通过对Java项目的检查，发现以下需要修改的地方：

### 1.1 配置文件中的硬编码URL
- `application.yml` 第15行：`avatar.base.url: http://localhost:8088/avatars/`
- `application.yml` 第28行：`image.base.url: http://localhost:8088/images/`

### 1.2 代码中的硬编码URL
- `GoodsController.java` 第100行：`String imageUrl = "http://localhost:8088/images/" + fileName;`

## 2. 解决方案

### 2.1 配置文件优化
1. **修改主配置文件**：将URL配置提取为可配置的属性
2. **创建环境特定配置文件**：
   - `application-dev.yml`（开发环境）
   - `application-test.yml`（测试环境）
   - `application-prod.yml`（生产环境）
3. **使用Spring Boot的多环境配置机制**：通过`spring.profiles.active`切换环境

### 2.2 代码修改
1. **修改GoodsController.java**：使用`@Value`注解注入配置的URL，替换硬编码URL
2. **确保所有控制器都使用配置的URL**：检查其他控制器是否有类似问题

### 2.3 统一配置管理
- 保持Java项目与Vue项目的URL配置一致性
- 支持通过配置文件灵活切换不同环境的URL
- 确保资源访问和API调用的URL都能正确工作

## 3. 实施步骤

### 3.1 配置文件修改
1. 修改`application.yml`，将硬编码URL替换为配置属性
2. 创建三个环境特定的配置文件，分别配置不同环境的URL
3. 确保配置文件中的URL格式与Vue项目保持一致

### 3.2 代码修改
1. 修改`GoodsController.java`，使用`@Value`注入配置的URL
2. 检查其他可能存在硬编码URL的控制器

### 3.3 测试验证
1. 确保开发环境下URL正确
2. 测试不同环境配置的切换功能
3. 验证资源上传和访问功能正常

## 4. 预期效果
- Java项目中的所有URL都通过配置文件统一管理
- 支持开发、测试、生产环境的灵活切换
- 与Vue项目的URL配置保持一致，便于维护
- 减少硬编码URL，提高代码的可维护性

## 5. 技术要点
- Spring Boot多环境配置机制
- `@Value`注解注入配置属性
- YAML配置文件语法
- 环境变量切换
- 资源访问URL管理