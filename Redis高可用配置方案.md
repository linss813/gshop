# Redis高可用配置方案

## 1. 主从复制配置

### 1.1 配置说明
主从复制是Redis高可用的基础，用于数据备份和读写分离。

### 1.2 配置步骤

#### 1.2.1 主节点配置（redis-master.conf）
```conf
# 绑定地址，允许所有IP访问
bind 0.0.0.0
# 端口号
port 6379
# 启用保护模式
protected-mode yes
# 设置密码
requirepass your_password
# 后台运行
daemonize yes
# 日志文件
logfile "redis-master.log"
# 持久化文件
dbfilename dump-master.rdb
dir ./data/
```

#### 1.2.2 从节点配置（redis-slave.conf）
```conf
# 绑定地址
bind 0.0.0.0
# 端口号
port 6380
# 启用保护模式
protected-mode yes
# 设置密码
requirepass your_password
# 主节点密码
masterauth your_password
# 后台运行
daemonize yes
# 日志文件
logfile "redis-slave.log"
# 持久化文件
dbfilename dump-slave.rdb
dir ./data/
# 主节点地址和端口
replicaof 127.0.0.1 6379
```

### 1.3 验证方法
```bash
# 登录主节点
redis-cli -p 6379 -a your_password
# 查看主从状态
info replication

# 登录从节点
redis-cli -p 6380 -a your_password
# 查看从节点状态
info replication
```

## 2. 哨兵机制配置

### 2.1 配置说明
哨兵机制用于自动监控主从节点状态，当主节点故障时自动进行故障转移。

### 2.2 配置步骤

#### 2.2.1 哨兵配置文件（sentinel.conf）
```conf
# 绑定地址
bind 0.0.0.0
# 端口号
port 26379
# 后台运行
daemonize yes
# 日志文件
logfile "sentinel.log"
# 监控主节点，名称为mymaster，投票数为1
# sentinel monitor <master-name> <ip> <redis-port> <quorum>
sentinel monitor mymaster 127.0.0.1 6379 1
# 主节点密码
sentinel auth-pass mymaster your_password
# 主节点超时时间（毫秒）
sentinel down-after-milliseconds mymaster 30000
# 故障转移超时时间（毫秒）
sentinel failover-timeout mymaster 180000
# 并行同步从节点数量
sentinel parallel-syncs mymaster 1
```

### 2.3 验证方法
```bash
# 启动哨兵
redis-sentinel sentinel.conf

# 查看哨兵状态
redis-cli -p 26379
sentinel master mymaster
sentinel slaves mymaster
```

## 3. Redis集群配置

### 3.1 配置说明
Redis集群用于实现数据分片和高可用，支持自动故障转移。

### 3.2 配置步骤

#### 3.2.1 集群节点配置（redis-cluster-7000.conf）
```conf
# 绑定地址
bind 0.0.0.0
# 端口号
port 7000
# 启用集群模式
cluster-enabled yes
# 集群配置文件
cluster-config-file nodes-7000.conf
# 集群节点超时时间
cluster-node-timeout 15000
# 持久化文件
dbfilename dump-7000.rdb
dir ./data/
# 后台运行
daemonize yes
# 日志文件
logfile "redis-cluster-7000.log"
```

#### 3.2.2 创建集群
```bash
# 启动所有节点后，创建集群
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1
```

### 3.3 验证方法
```bash
# 登录集群节点
redis-cli -c -p 7000
# 查看集群信息
cluster info
# 查看节点信息
cluster nodes
```

## 4. Spring Boot集成Redis哨兵/集群

### 4.1 哨兵模式集成
```yaml
spring:
  redis:
    sentinel:
      master: mymaster
      nodes: 127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381
    password: your_password
```

### 4.2 集群模式集成
```yaml
spring:
  redis:
    cluster:
      nodes: 127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005
      max-redirects: 3
    password: your_password
```

## 5. 高可用验证与测试

### 5.1 主从复制测试
```bash
# 在主节点写入数据
redis-cli -p 6379 -a your_password set test_key test_value
# 在从节点读取数据
redis-cli -p 6380 -a your_password get test_key
```

### 5.2 故障转移测试
```bash
# 查看主节点进程ID
redis-cli -p 6379 -a your_password info server | grep process_id
# 杀死主节点进程
kill -9 <process_id>
# 等待30秒后，查看哨兵日志或从节点状态，确认故障转移完成
```

## 6. 最佳实践

1. **主从节点分布**：将主从节点部署在不同的物理机器或虚拟机上，避免单点故障
2. **密码设置**：所有节点必须设置强密码，并确保主从节点密码一致
3. **持久化策略**：主节点建议使用AOF持久化，从节点可使用RDB持久化
4. **监控告警**：配置Redis监控，实时监控主从状态、内存使用、命令执行等指标
5. **定期备份**：定期备份Redis数据，确保数据安全性

## 7. 性能优化建议

1. **读写分离**：将读请求分发到从节点，减轻主节点压力
2. **合理设置复制积压缓冲区**：根据业务量调整`repl-backlog-size`参数
3. **优化网络传输**：主从节点之间使用高速网络连接
4. **避免大key**：大key会影响复制性能，建议拆分大key
5. **定期清理过期数据**：使用`maxmemory-policy allkeys-lru`策略，定期清理过期数据

# 预期效果

1. **数据安全性提升**：通过主从复制和持久化，确保数据不丢失
2. **系统可用性提高**：哨兵机制和集群模式实现自动故障转移，减少 downtime
3. **性能提升**：读写分离和数据分片提高系统吞吐量
4. **扩展性增强**：支持水平扩展，满足业务增长需求
