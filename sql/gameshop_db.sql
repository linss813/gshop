/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : gameshop_db

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 05/06/2025 18:32:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后的密码',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `last_login` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用人',
  `type` tinyint(1) NOT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2025011 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (2025001, 'linss', '123456', '2025-02-19 13:33:31', '2025-02-19 13:33:33', '林', 0);
INSERT INTO `admin` VALUES (2025002, 'newadmin', '$2a$10$sxil5oDpjKWuijNfC313leLriV10LKOkjbJhfsJyetcCKnsedpZL2', '2025-03-21 12:04:22', NULL, '123', 1);
INSERT INTO `admin` VALUES (2025006, 'admin', '$2a$10$xaUgikWjAeB81tq.a.2e4.npE1R9uWJJyt0fEiWW0wNMAXckn63xW', '2025-03-21 12:13:33', '2025-05-24 11:27:55', '管理员', 1);
INSERT INTO `admin` VALUES (2025007, 'admin123', '$2a$10$BYxy2rgqOll5A9EgpfkL/.8/t3jfX89.9Rxs3JadfEOBfO/oBvSri', '2025-03-21 13:17:03', '2025-05-24 11:33:21', 'Lin', 0);
INSERT INTO `admin` VALUES (2025010, '123', '$2a$10$8vWGZEsE0xE.6e55CRoQSeFvdQTc0YjMx60HGanpYN7BRmBIxQUL.', '2025-05-21 19:06:57', '2025-05-21 19:07:05', '使用人', 2);

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `bid` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '广告图片URL',
  `jump` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告跳转链接',
  `sort` int NOT NULL COMMENT '排序序号（升序排列）',
  `status` tinyint(1) NOT NULL COMMENT '状态 (1启用, 0禁用)',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`bid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '广告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, 'https://ts1.tc.mm.bing.net/th/id/R-C.71dff9fd8637e7ae6f8fe9ff107b9648?rik=RN%2fEJ62CYbnWmQ&riu=http%3a%2f%2fimg.52desk.com%2ftp%2f000425nsZEO.jpg&ehk=HRY2%2bYwna%2fmOIytG2f8q15VtC%2b%2fjPeCT2tVotvz7%2bzc%3d&risl=&pid=ImgRaw&r=0', 'goods/detail/1', 2, 1, '2025-04-08 16:58:18');
INSERT INTO `banner` VALUES (2, 'https://pic.kts.g.mi.com/0f02a5e0b682aeb28a73c6fee327cc738099726348511355612.jpg', 'shop', 1, 1, '2025-04-08 17:39:19');
INSERT INTO `banner` VALUES (3, 'http://localhost:8088/images/66e7d346-8860-402b-82b2-f798150ef3d4_7cce7acfaf79959fc2ce60c8c7f6eac.jpg', 'shop', 1, 1, '2025-04-09 19:08:48');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cart_id` int NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `user_id` int NOT NULL COMMENT '用户id',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`cart_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1, 10000, '2025-04-21 17:47:39');
INSERT INTO `cart` VALUES (13, 10025, '2025-05-21 18:55:06');

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
  `item_id` int NOT NULL AUTO_INCREMENT COMMENT '购物车条目ID',
  `cart_id` int NULL DEFAULT NULL COMMENT '购物车id',
  `gid` int NULL DEFAULT NULL COMMENT '商品id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NULL DEFAULT NULL COMMENT '商品名',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `quantity` int NULL DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_item
-- ----------------------------
INSERT INTO `cart_item` VALUES (14, 1, 5, '幽灵行者 完整版', 198.00, 1);
INSERT INTO `cart_item` VALUES (15, 1, 9, '荒野大镖客：救赎2', 279.00, 2);
INSERT INTO `cart_item` VALUES (17, 6, 1, '黑神话悟空 - 标准版', 268.00, 1);
INSERT INTO `cart_item` VALUES (18, 7, 2, '黑神话悟空 - 豪华版', 328.00, 1);
INSERT INTO `cart_item` VALUES (26, 1, 6, 'inZOI', 188.00, 1);
INSERT INTO `cart_item` VALUES (31, 1, 11, '席德·梅尔的文明VII', 398.00, 1);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '二级分类id',
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NOT NULL COMMENT '二级分类',
  `tid` int NOT NULL COMMENT '一级分类id',
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `tid`(`tid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '动作', 1);
INSERT INTO `category` VALUES (4, '治愈', 1);
INSERT INTO `category` VALUES (5, '模拟', 1);
INSERT INTO `category` VALUES (6, '休闲', 1);
INSERT INTO `category` VALUES (9, '沙盒', 1);
INSERT INTO `category` VALUES (10, '角色扮演', 1);
INSERT INTO `category` VALUES (11, '开放世界', 1);
INSERT INTO `category` VALUES (12, '礼盒', 2);
INSERT INTO `category` VALUES (13, '回合', 1);
INSERT INTO `category` VALUES (14, '道具兑换码', 3);

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `gid` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tid` int NULL DEFAULT NULL COMMENT '一级分类，goodstype',
  `cid` int NULL DEFAULT NULL COMMENT '二级分类，category',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品标题',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片封面',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `stock` int NOT NULL COMMENT '库存量',
  `is_shelved` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '上架状态（1上架/0下架）',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `rating` float NULL DEFAULT NULL COMMENT '评分（1-5）',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`gid`) USING BTREE,
  INDEX `cid`(`cid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 157 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, 1, 1, '黑神话悟空 - 标准版', 'https://p2.bahamut.com.tw/B/2KU/66/eacb2ab01510db38ee54846fc11o73q5.JPG', 268.00, 24957, 1, '《黑神话：悟空》是一款以中国神话为背景的动作角色扮演游戏。故事取材于中国古典小说“四大名著”之一的《西游记》。你将扮演一位“天命人”，为了探寻昔日传说的真相，踏上一条充满危险与惊奇的西游之路。• 雄奇壮丽，光怪陆离别有世间曾未见，一行一步一花新。欢迎来到亦真亦幻的东方魔幻世界！天命人将前往多个引人入胜又风格迥异的西游故地，再次谱写前所未见的冒险史诗。• 妖魔鬼怪，卷土重来行者名声大，魔王手段强。《西游记》的一大看点，便是各有所长的厉害妖怪。西行的旅途并非只有风光旖旎，天命人还将遭遇许多强大的敌人与可敬的对手，与他们豪快战斗，至死方休。• 山摇地动，各显神通炼就长生多少法，学来变化广无边。五光十色又相生相克的法术、变化与法宝，一直都是中国神话中最为招牌的战斗要素。天命人除了精通不同棍术，更可自由搭配多样化的法术、变化、天赋、武器与披挂，找到最契合自身战斗风格的制胜之道。• 千形万象，荡气回肠六般体相六般兵，六样形骸六样情。敌人的凶恶狡猾只是表象，他们的来历、个性与动机，往往更加耐人寻味。天命人在战斗之外，也会探寻各色人物背后的故事，了解他们的爱与恨，贪与嗔，前世与今生。', 5, '2025-03-07 11:04:42');
INSERT INTO `goods` VALUES (2, 1, 1, '黑神话悟空 - 豪华版', 'https://ts1.tc.mm.bing.net/th/id/R-C.466914c0efce5a8deec3c9446b82da80?rik=lnTRxiXoiilIqQ&riu=http%3a%2f%2fimg.52desk.com%2ftp%2f000738ds60I.jpg&ehk=WGRsKiYysI%2fCEpDd6kzgfAodOMT41FrvfeCjwHPYSfU%3d&risl=&pid=ImgRaw&r=0', 328.00, 24976, 1, '《黑神话：悟空》是一款以中国神话为背景的动作角色扮演游戏。故事取材于中国古典小说“四大名著”之一的《西游记》。你将扮演一位“天命人”，为了探寻昔日传说的真相，踏上一条充满危险与惊奇的西游之路。• 雄奇壮丽，光怪陆离别有世间曾未见，一行一步一花新。欢迎来到亦真亦幻的东方魔幻世界！天命人将前往多个引人入胜又风格迥异的西游故地，再次谱写前所未见的冒险史诗。• 妖魔鬼怪，卷土重来行者名声大，魔王手段强。《西游记》的一大看点，便是各有所长的厉害妖怪。西行的旅途并非只有风光旖旎，天命人还将遭遇许多强大的敌人与可敬的对手，与他们豪快战斗，至死方休。• 山摇地动，各显神通炼就长生多少法，学来变化广无边。五光十色又相生相克的法术、变化与法宝，一直都是中国神话中最为招牌的战斗要素。天命人除了精通不同棍术，更可自由搭配多样化的法术、变化、天赋、武器与披挂，找到最契合自身战斗风格的制胜之道。• 千形万象，荡气回肠六般体相六般兵，六样形骸六样情。敌人的凶恶狡猾只是表象，他们的来历、个性与动机，往往更加耐人寻味。天命人在战斗之外，也会探寻各色人物背后的故事，了解他们的爱与恨，贪与嗔，前世与今生。', 5, '2025-03-07 11:04:42');
INSERT INTO `goods` VALUES (3, 1, 4, '灵魂摆渡人', 'http://localhost:8088/images/c694eef5-d0fa-437e-8430-0d6bc67715e5_1596159448_729923.jpg', 108.00, 4996, 1, '你会如何进行临终道别？《Spiritfarer®》是一款关于死亡的休闲管理游戏。你扮演逝者的船主斯特拉，也是一名灵魂摆渡者。建造一艘船去探索世界，然后帮助和关爱幽灵，最终将他们送到来世。耕种、采矿、捕鱼、收割、烹饪，并用你自己的方法渡过神秘之海。加入小猫达芙迪尔的冒险之旅，开启双人合作模式。与你的幽灵乘客一起度过悠闲的美好时光，创造永恒的回忆，最终学会如何向你珍爱的朋友道别。《Spiritfarer®》是一款屡获殊荣的管理类游戏，其内容围绕死亡这一话题而展开，是一款舒心疗愈的游戏。《Spiritfarer®》Farewell版是其终极版，内含受逾一百万名玩家青睐的《Spiritfarer®》游戏本体和迄今为止发布的所有额外内容包。来看看为何各大评论方称《Spiritfarer》为“2020年度最佳游戏之一”吧。游戏特色：享受美丽的手绘艺术和动画。建造、管理和改进你的渡船。 耕种、采矿、捕鱼、收割、烹饪、编织、制作！各种各样的活动在等着你！结识一群令人难忘的角色，带他们上船，关爱他们，与他们建立良好的关系。你可以采用奔跑、跳跃和滑行的方式一路闯过精心设计的平台关卡。 探索奇幻和充满想象力的世界。寻找和收集各种资源，以升级你的船只并为乘客制作礼物。 体验充满难忘瞬间的感人故事。放松几个小时，进入舒适且令人放松的游戏。花上30个小时即可完成整个游戏，花上几百个小时则能详细感受《Spiritfarer》的所有细节。游戏内针对船只、角色甚至是小猫都有丰富的自定义选项，助你打造独一无二的冒险之旅！在可选的本地合作模式下扮演小猫达芙迪尔。内含疗愈心灵的本体游戏和三个主要内容更新包：探索一个更宽广的世界，结交四个全新的灵魂朋友！ 加入讨论组欢迎加入我们的中文讨论组！《Spiritfarer》官方玩家QQ群：1001220629', 4, '2025-03-07 11:04:42');
INSERT INTO `goods` VALUES (4, 1, 9, 'minecraft', 'https://img.opencritic.com/game/194/o/1kabVU4E.jpg', 89.00, 449, 1, '我的世界Java版+基岩版', 5, '2025-03-07 11:04:42');
INSERT INTO `goods` VALUES (5, 1, 1, '幽灵行者 完整版', 'https://img.3dmgame.com/uploads/images/news/20210901/1630448727_105638.jpg', 198.00, 65, 1, 'Ghostrunner 提供了独特的单玩家游戏体验：快节奏的暴力战斗，以及将科幻故事与世界末日主题相融合的原生环境。它讲述了一个已终结的世界，以及在其', 4, '2025-03-07 11:04:42');
INSERT INTO `goods` VALUES (6, 1, 5, 'inZOI', 'https://img.3dmgame.com/uploads/images/news/20250313/1741832419_151960.png', 188.00, 784, 1, '每个生命都将成为一段独特的故事。通过控制并观察\'Zois\'，你可以创造出一段属于你的，独一无二的人生。 ', 4, '2025-03-07 11:04:42');
INSERT INTO `goods` VALUES (7, 1, 6, '猛兽派对', 'https://ts1.tc.mm.bing.net/th/id/R-C.1e68a8db433c7caf84b1fc860954a791?rik=xGKmKxq%2frwAMcQ&riu=http%3a%2f%2fn.sinaimg.cn%2fsinakd20230921s%2f600%2fw1920h1080%2f20230921%2fee8d-22b5eed718695eced94ede9c895db8bd.jpg&ehk=QMxEDeiPkmyheTl0dwJZAa%2b57hh1f4r8Htz6Hb8Wblk%3d&risl=&pid=ImgRaw&r=0', 98.00, 0, 1, '这是一款基于物理的多人派对游戏。选择你喜欢的小动物角色，和朋友们一起挑战其他玩家，或者展开激烈的对决吧！看到毛绒绒的小动物们你还不心动吗？', 5, '2025-04-07 17:01:33');
INSERT INTO `goods` VALUES (8, 1, 10, '艾尔登法环', 'https://shop.3dmgame.com/upload/ico/2022/0127/1643271486588857.jpg', 298.00, 1217, 1, '艾尔登法环是以正统黑暗奇幻世界为舞台的动作RPG游戏。 走进辽阔的场景与地下迷宫探索未知，挑战困难重重的险境，享受克服困境时的成就感吧。 不仅如', 5, '2025-04-07 17:35:37');
INSERT INTO `goods` VALUES (9, 1, 11, '荒野大镖客：救赎2', 'https://pic1.zhimg.com/v2-df3c954d7668637e9994c009e3ce232f_1440w.jpg?source=172ae18b', 279.00, 550, 1, 'Red Dead Redemption 2 已荣获超过 175 项年度游戏奖项且获得超过 250 个满分评价，游戏中述说亚瑟·摩根和声名狼藉的范德林德帮派的传奇故事，体验', 5, '2025-04-07 17:37:27');
INSERT INTO `goods` VALUES (10, 2, 12, '黑神话悟空实体收藏版', 'https://img.php.cn/upload/article/000/887/227/172411658460849.jpg', 1998.00, 0, 1, '实体收藏版限量10000套，售价1998元，该版本在上述内容基础上，将紧箍替换为了主角1:6可动人偶“直面天命”（40cm），将“乱蟠桃”画卷替换为“受心经”画卷。', 5, '2025-04-07 19:03:25');
INSERT INTO `goods` VALUES (11, 1, 13, '席德·梅尔的文明VII', 'https://img.3dmgame.com/uploads/images/news/20250211/1739276504_110804.jpg', 398.00, 122, 1, '屡获殊荣的策略游戏系列回归，翻开革命性新篇章。 《席德·梅尔的文明®VII》让你能够建立世界上前所未有的伟大帝国！', 5, '2025-04-07 19:06:37');
INSERT INTO `goods` VALUES (152, 3, 14, '充值码', 'http://localhost:8088/images/8737524d-1772-474b-9e00-9206edeb844a_Snipaste_2025-05-19_16-05-48.png', 30.00, 99, 1, 'mt5充值码', 5, '2025-05-19 16:04:46');

-- ----------------------------
-- Table structure for goods_type
-- ----------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type`  (
  `tid` int NOT NULL AUTO_INCREMENT COMMENT '一级分类id',
  `tname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NULL DEFAULT NULL COMMENT '一级分类名',
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_type
-- ----------------------------
INSERT INTO `goods_type` VALUES (1, '游戏');
INSERT INTO `goods_type` VALUES (2, '周边');
INSERT INTO `goods_type` VALUES (3, '虚拟道具');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `item_id` int NOT NULL AUTO_INCREMENT COMMENT 'itemid',
  `order_id` int NULL DEFAULT NULL COMMENT '订单id',
  `gid` int NULL DEFAULT NULL COMMENT '商品id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NULL DEFAULT NULL COMMENT '标题',
  `quantity` int NULL DEFAULT NULL COMMENT '商品数量',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (116, 63, 2, '黑神话悟空 - 豪华版', 1, 328.00);
INSERT INTO `order_item` VALUES (117, 64, 5, '幽灵行者 完整版', 1, 198.00);
INSERT INTO `order_item` VALUES (118, 65, 2, '黑神话悟空 - 豪华版', 2, 328.00);
INSERT INTO `order_item` VALUES (119, 66, 152, '充值码', 1, 30.00);
INSERT INTO `order_item` VALUES (120, 67, 1, '黑神话悟空 - 标准版', 1, 268.00);
INSERT INTO `order_item` VALUES (121, 68, 6, 'inZOI', 1, 188.00);
INSERT INTO `order_item` VALUES (122, 69, 4, 'minecraft', 1, 89.00);
INSERT INTO `order_item` VALUES (123, 70, 11, '席德·梅尔的文明VII', 1, 398.00);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '自增订单ID',
  `user_id` int NOT NULL COMMENT '消费者ID',
  `pay_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式（alipay）',
  `payment_status` int NULL DEFAULT NULL COMMENT '支付状态（0未支付，1已支付，2支付失败）',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `order_status` int NULL DEFAULT NULL COMMENT '订单状态（0取消，1待发货，2已发货，3已签收）',
  `order_time` datetime NULL DEFAULT NULL COMMENT '下单时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (63, 10025, NULL, 328.00, 'alipay', 0, NULL, 0, '2025-05-21 18:56:07', '2025-05-22 18:56:07');
INSERT INTO `orders` VALUES (64, 10025, '78621384-3', 198.00, 'alipay', 1, '2025-05-21 18:56:16', 3, '2025-05-21 18:56:14', '2025-05-22 18:56:14');
INSERT INTO `orders` VALUES (65, 10025, 'd8f21c9b-0', 656.00, 'alipay', 1, '2025-05-21 18:58:14', 1, '2025-05-21 18:58:13', '2025-05-22 18:58:13');
INSERT INTO `orders` VALUES (66, 10000, '0aca26d3-5', 30.00, 'alipay', 1, '2025-05-21 19:04:25', 3, '2025-05-21 19:04:24', '2025-05-22 19:04:24');
INSERT INTO `orders` VALUES (67, 10000, NULL, 268.00, 'alipay', 0, NULL, 0, '2025-05-21 19:26:49', '2025-05-22 19:26:49');
INSERT INTO `orders` VALUES (68, 10000, '534ee4d3-d', 188.00, 'alipay', 1, '2025-05-21 19:26:54', 1, '2025-05-21 19:26:53', '2025-05-22 19:26:53');
INSERT INTO `orders` VALUES (69, 10000, '072ffc6c-2', 89.00, 'alipay', 1, '2025-05-24 10:28:37', 1, '2025-05-24 10:28:36', '2025-05-25 10:28:36');
INSERT INTO `orders` VALUES (70, 10000, NULL, 398.00, 'alipay', 0, NULL, 0, '2025-05-24 11:22:24', '2025-05-25 11:22:24');

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `review_id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `uid` int NOT NULL COMMENT '用户id',
  `gid` int NOT NULL COMMENT '商品id',
  `rating` int NULL DEFAULT NULL COMMENT '评分',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NULL COMMENT '评论内容',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '评论状态',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`review_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (22, 10000, 8, 5, '好玩', 1, '2025-05-07 14:18:22');
INSERT INTO `review` VALUES (23, 10000, 1, 5, '吃俺老孙一棍\n', 1, '2025-05-07 14:18:41');
INSERT INTO `review` VALUES (24, 10000, 1, 5, '很美', 1, '2025-05-07 14:18:49');
INSERT INTO `review` VALUES (27, 10000, 2, 5, '支持', 1, '2025-05-21 17:34:45');
INSERT INTO `review` VALUES (34, 10025, 5, 5, 'ce', 1, '2025-05-21 18:56:21');
INSERT INTO `review` VALUES (35, 10000, 4, 5, '1', NULL, '2025-05-24 10:26:27');
INSERT INTO `review` VALUES (36, 10000, 1, 5, '12', NULL, '2025-05-24 11:22:51');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '自增用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后的密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货地址',
  `last` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `created` datetime NOT NULL COMMENT '创建时间',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10027 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消费者表' ROW_FORMAT = Dynamic STATS_PERSISTENT = 0;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10000, 'Linss', '$2a$10$i333DIviyMCxp7HVNc8Vm.R66beXbI8Iixb7n.Ei0ITp.txllXDQi', 'http://localhost:8088/avatars/535394cc-6305-4e05-81e0-81c2971af337.jpg', 'lin813ss@163.com', '19912345678', '河北省', '2025-05-24 11:26:44', '2025-03-05 16:24:16', 'USER');
INSERT INTO `user` VALUES (10016, 'admin1', '$2a$10$72FmrENBcG4/MPn6HS2Rh.CPw7KzKPno/spZV/yOyrvKymJLHVIXy', '', 'admin1@1.com', '19931912345', NULL, '2025-04-27 18:09:49', '2025-04-27 18:06:53', 'USER');
INSERT INTO `user` VALUES (10025, 'Linss1', '$2a$10$th.fRAbEZ0/uRHX3qZJiVeWWX.EcIFM7C7rPh7mEKIEYbqQMCR5Zq', 'http://localhost:8088/avatars/8f3472c6-1bd4-4f08-8514-da6f87c22cc1.jpg', 'sakura8134@163.com', '19931912345', '黑', '2025-05-21 19:02:10', '2025-05-21 18:55:06', 'USER');

-- ----------------------------
-- Table structure for verification_code
-- ----------------------------
DROP TABLE IF EXISTS `verification_code`;
CREATE TABLE `verification_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NOT NULL COMMENT '接收邮箱',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_danish_ci NOT NULL COMMENT '验证码',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `expires_at` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_danish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of verification_code
-- ----------------------------
INSERT INTO `verification_code` VALUES (1, 'lin813ss@163.com', 'NXPCC07395', '2025-03-05 14:37:05', '2025-03-05 14:47:05');
INSERT INTO `verification_code` VALUES (2, 'lin813ss@163.com', 'PTRDG69509', '2025-03-05 14:38:06', '2025-03-05 14:48:06');
INSERT INTO `verification_code` VALUES (3, 'lin813ss@163.com', 'KNZWR18436', '2025-03-05 14:39:20', '2025-03-05 14:49:20');
INSERT INTO `verification_code` VALUES (4, 'lin813ss@163.com', 'UIGBH77668', '2025-03-05 14:39:36', '2025-03-05 14:49:36');
INSERT INTO `verification_code` VALUES (5, 'lin813ss@163.com', 'FJNDO48192', '2025-03-05 14:40:03', '2025-03-05 14:50:03');
INSERT INTO `verification_code` VALUES (6, 'lin813ss@163.com', 'WYDUL12042', '2025-03-05 14:41:23', '2025-03-05 14:51:23');
INSERT INTO `verification_code` VALUES (7, 'lin813ss@163.com', 'UWIBY31267', '2025-03-05 14:41:58', '2025-03-05 14:51:58');
INSERT INTO `verification_code` VALUES (8, 'lin813ss@163.com', 'GMYZK40460', '2025-03-05 14:43:22', '2025-03-05 14:53:22');
INSERT INTO `verification_code` VALUES (9, 'lin813ss@163.com', 'GYLRP50249', '2025-03-05 15:08:44', '2025-03-05 15:18:44');
INSERT INTO `verification_code` VALUES (10, 'lin813ss@163.com', 'ZOISY53402', '2025-03-05 15:36:04', '2025-03-05 15:46:04');
INSERT INTO `verification_code` VALUES (11, 'lin813ss@163.com', 'WIMUW00877', '2025-03-05 15:43:58', '2025-03-05 15:53:58');
INSERT INTO `verification_code` VALUES (12, 'lin813ss@163.com', 'JBSYE68357', '2025-03-05 15:55:16', '2025-03-05 16:05:16');
INSERT INTO `verification_code` VALUES (13, 'lin813ss@163.com', '5UCFK', '2025-03-05 15:59:58', '2025-03-05 16:09:58');
INSERT INTO `verification_code` VALUES (14, 'lin813ss@163.com', 'F7AGK', '2025-03-05 16:00:59', '2025-03-05 16:10:59');
INSERT INTO `verification_code` VALUES (15, 'lin813ss@163.com', 'ZZH5M', '2025-03-05 16:01:08', '2025-03-05 16:11:08');
INSERT INTO `verification_code` VALUES (16, 'lin813ss@163.com', 'K1Z26', '2025-03-05 16:01:19', '2025-03-05 16:11:19');
INSERT INTO `verification_code` VALUES (20, 'lin813ss@163.com', '96TSW', '2025-03-05 16:37:45', '2025-03-05 16:47:45');
INSERT INTO `verification_code` VALUES (21, '1370225939@qq.com', '64MD3', '2025-03-05 17:09:55', '2025-03-05 17:19:55');
INSERT INTO `verification_code` VALUES (22, 'lin813ss@163.com', '6I7NR', '2025-03-05 17:29:13', '2025-03-05 17:39:13');
INSERT INTO `verification_code` VALUES (23, 'lin813ss@163.com', '37X74', '2025-03-05 17:30:00', '2025-03-05 17:40:00');
INSERT INTO `verification_code` VALUES (24, 'lin813ss@163.com', 'VP6QJ', '2025-03-05 17:33:59', '2025-03-05 17:43:59');
INSERT INTO `verification_code` VALUES (26, 'stray813@163.com', 'I7K7L', '2025-03-05 18:04:59', '2025-03-05 18:14:59');
INSERT INTO `verification_code` VALUES (29, 'lin813ss@163.com', '3B38D', '2025-03-07 10:11:45', '2025-03-07 10:21:45');
INSERT INTO `verification_code` VALUES (30, '1370225939@qq.com', 'HMI8R', '2025-03-07 10:13:18', '2025-03-07 10:23:18');
INSERT INTO `verification_code` VALUES (31, 'sakura813Lyw@gmail.com', '4HB4M', '2025-03-07 10:13:47', '2025-03-07 10:23:47');
INSERT INTO `verification_code` VALUES (35, '{this.form.email}', '9478A', '2025-04-24 12:21:34', '2025-04-24 12:31:34');
INSERT INTO `verification_code` VALUES (36, 'lin813ss@163.com', 'D369P', '2025-04-24 12:36:05', '2025-04-24 12:46:05');
INSERT INTO `verification_code` VALUES (37, 'lin813ss@163.com', '84N2N', '2025-04-24 18:35:33', '2025-04-24 18:45:33');
INSERT INTO `verification_code` VALUES (40, 'stary813@163.com', '0T9A8', '2025-04-27 16:57:07', '2025-04-27 17:07:07');
INSERT INTO `verification_code` VALUES (46, 'lin813ss@163.com', 'LRXP5', '2025-05-04 09:20:00', '2025-05-04 09:30:00');
INSERT INTO `verification_code` VALUES (47, 'lin813ss@163.com', 'KCVYS', '2025-05-04 09:22:58', '2025-05-04 09:32:58');
INSERT INTO `verification_code` VALUES (48, 'lin813ss@163.com', 'QET2D', '2025-05-04 09:28:34', '2025-05-04 09:38:34');
INSERT INTO `verification_code` VALUES (49, 'lin813ss@163.com', 'T01B9', '2025-05-04 09:31:59', '2025-05-04 09:41:59');
INSERT INTO `verification_code` VALUES (50, 'lin813ss@163.com', '812P4', '2025-05-04 09:46:18', '2025-05-04 09:56:18');
INSERT INTO `verification_code` VALUES (51, 'lin813ss@163.com', '9X111', '2025-05-04 09:51:32', '2025-05-04 10:01:32');
INSERT INTO `verification_code` VALUES (53, '1370225939@163.com', '27UK1', '2025-05-07 18:50:36', '2025-05-07 19:00:36');
INSERT INTO `verification_code` VALUES (57, '1370225939@qq.com', 'U3S62', '2025-05-21 17:26:30', '2025-05-21 17:36:30');
INSERT INTO `verification_code` VALUES (59, 'sakura8134@163.com', 'KK545', '2025-05-21 17:36:58', '2025-05-21 17:46:58');
INSERT INTO `verification_code` VALUES (62, 'sakura8134@163.com', '8PGW8', '2025-05-21 18:01:11', '2025-05-21 18:11:11');

SET FOREIGN_KEY_CHECKS = 1;
