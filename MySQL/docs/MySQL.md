# 初始数据库

安装 MySQL 8.0 并初始化数据库之后，默认会创建以下系统数据库：

- mysql，存储了 MySQL 服务器正常运行所需的各种信息。

- information_schema，提供了访问数据库元数据的各种视图，包括数据库、表、字段类型以及访问权限等。
- performance_schema，为 MySQL 服务器的运行时状态提供了一个底层的监控功能。
- sys，包含了一系列方便 DBA 和开发人员利用 performance_schema 性能数据库进行性能调优和诊断的视图。



# 备份数据库

## Window 命令行

```bat
:: Change to Unicode(UTF-8)
chcp 65001
:: 备份数据库到当前命令行所在目录
:: 备份 dbDemo 数据库
SET dbName=dbDemo

:: --databases，导出的 sql 文件中会附带创建对应数据库的脚本
:: 密码单独输入
mysqldump -uroot -p --databases %dbName% > %dbName%.sql
:: 或直接写入密码
mysqldump -uroot -proot --databases %dbName% > %dbName%.sql

:: 导出所有数据库（所有数据库、表结构、数据），全部备份（除了MySQL自带的4个数据库）：
mysqldump -u root -proot --all-databases > all.sql
```

```bat
:: 备份远程数据库
mysqldump -h127.0.0.1 -P3306 -utest -p123456 student > I:\sql\2021-07-27_14-19-14\student.sql
```



# 还原数据库

## Window 命令行

```bat
:: Change to Unicode(UTF-8)
chcp 65001
:: 在当前命令行所在目录存在 dbDemo.sql 文件

:: 未创建对应数据库的情况下
:: 配套备份脚本：mysqldump -uroot -proot --databases %dbName% > %dbName%.sql
mysql -uroot -proot < dbDemo.sql

:: 已创建对应数据库的情况下
:: 配套备份脚本：mysqldump -uroot -proot %dbName% > %dbName%.sql
mysql -uroot -proot %dbName%  < dbDemo.sql
```

# 问题/解决

## MySQL排序分页查询数据顺序错乱

参见 [MySQL排序分页查询数据顺序错乱的原因和解决办法](https://blog.csdn.net/weixin_44299027/article/details/121627609)

### 问题说明

当 order by field_xxx 的 field_xxx 字段不是索引，也不是主键，且值又一样，在进行分页查询时，可能会出现第一页查询到的数据又继续在第二页出现

> mysql对无索引字段进行排序后limit ，当被排序字段有相同值时并且在limit范围内，取的值并不是正常排序后的值，有可能第一页查询的记录，重复出现在第二页的查询记录中，而且第二页的查询结果乱序，导致分页结果查询错乱问题。

### 解决方案

给 field_xxx 创建索引，或增加 索引列或主键列 排序

## [MySQL5.7 实现类似 MySQL8.0 中 row_number() over(partition by ... order by ...) 函数的分组排序编号效果](https://blog.csdn.net/weixin_49523761/article/details/129214488)

> 建议嵌套一层，排序效果才会达到预期

### 未嵌套

> 未嵌套，acct_no 关联其他标过滤 [ a.acct_no in (select b.acct_no from temp_acct_no b, crm_trade_account c where b.acct_no = c.acct_no and c.trade_group = '1') ]，查询出的结果没达到预期

```SQL
select a.parent_no, a.close_time, @num := if(@field_1 <=> a.parent_no, @num + 1, 1) row_no, @field_1 := a.parent_no field_1 from crm_trade_order a, (select @num := 0, @field_1 := null) bb where a.acct_no in (select b.acct_no from temp_acct_no b, crm_trade_account c where b.acct_no = c.acct_no and c.trade_group = '1') and a.order_status = '1' order by a.parent_no asc, a.close_time desc;
```

![image-20241220172026360](./MySQL.assets/image-20241220172026360.png)

> 未嵌套，acct_no 直接过滤[ a.acct_no in ('142241205123','142241205124','142241205125') ]，查询出的结果达到预期

```SQL
select a.parent_no, a.close_time, @num := if(@field_1 <=> a.parent_no, @num + 1, 1) row_no, @field_1 := a.parent_no field_1 from crm_trade_order a, (select @num := 0, @field_1 := null) bb where a.acct_no in ('142241205123','142241205124','142241205125') and a.order_status = '1' order by a.parent_no asc, a.close_time desc;
```

![image-20241220172150529](./MySQL.assets/image-20241220172150529.png)

### 有嵌套

> 未嵌套，acct_no 关联其他标过滤 [ a.acct_no in (select b.acct_no from temp_acct_no b, crm_trade_account c where b.acct_no = c.acct_no and c.trade_group = '1') ]，查询出的结果达到预期

```SQL
select aa.parent_no, aa.close_time, @num := if(@field_1 <=> aa.parent_no, @num + 1, 1) row_no, @field_1 := aa.parent_no field_1 from (select a.parent_no, a.close_time from crm_trade_order a where a.acct_no in (select b.acct_no from temp_acct_no b, crm_trade_account c where b.acct_no = c.acct_no and c.trade_group = '1') and a.order_status = '1') aa, (select @num := 0, @field_1 := null) bb order by aa.parent_no asc, aa.close_time desc;
```

![image-20241220172403151](./MySQL.assets/image-20241220172403151.png)
