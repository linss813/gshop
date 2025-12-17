# 统一管理项目中所有基础URL

## 一、整合方案

1. **配置环境变量**：在`.env`文件中添加不同环境的baseURL配置
2. **配置axios默认baseURL**：在`main.js`中启用axios的baseURL配置，从环境变量读取
3. **修改组件中的API调用**：移除所有完整URL，只保留相对路径
4. **确保资源引用和跳转链接的统一管理**

## 二、具体修改步骤

### 1. 配置环境变量文件

- 修改 `.env` 文件，添加基础URL配置
- 根据需要创建 `.env.development`、`.env.test`、`.env.production` 等环境配置文件

### 2. 配置axios baseURL

- 修改 `src/main.js` 文件，启用并配置axios的baseURL
- 确保axios实例能够从环境变量中读取正确的baseURL

### 3. 修改组件中的API调用

修改所有使用完整URL的组件，包括但不限于：
- `src/views/Shop/Cart.vue`
- `src/views/Shop/GoodsDetail.vue`
- `src/views/Shop/OrderList.vue`
- `src/views/Shop/Shop.vue`
- `src/views/Shop/UserProfile.vue`
- `src/views/Management/` 下的多个组件
- `src/views/Home/Index.vue`

### 4. 验证修改结果

- 运行项目，确保所有API调用正常工作
- 验证不同环境下的URL切换功能

## 三、配置位置及使用方法

### 配置文件位置

- `.env`：默认环境配置
- `.env.development`：开发环境配置
- `.env.test`：测试环境配置
- `.env.production`：生产环境配置

### 使用方法

1. **修改基础URL**：直接修改对应环境配置文件中的 `VUE_APP_BASE_URL` 变量
2. **组件中使用**：使用相对路径进行API调用，axios会自动拼接baseURL
3. **环境切换**：通过 `npm run serve`（开发）、`npm run build:test`（测试）、`npm run build`（生产）等命令切换环境

## 四、优势

1. **统一管理**：所有URL集中配置，便于维护和修改
2. **环境隔离**：不同环境使用不同的baseURL，避免混淆
3. **减少冗余代码**：组件中只需写相对路径，减少重复代码
4. **便于部署**：部署时只需修改对应环境的配置文件，无需修改代码

## 五、预期效果

- 所有API调用成功，功能正常
- 资源引用和跳转链接正常工作
- 能够通过修改单一配置实现所有URL的一键替换
- 支持开发、测试、生产等多种环境