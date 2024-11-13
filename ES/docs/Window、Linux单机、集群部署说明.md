# Window 单机部署

软件下载地址：https://www.elastic.co/cn/downloads/past-releases/elasticsearch-7-8-0

配置好 JAVA 环境（版本 1.8），解压 elasticsearch-7.8.0-windows-x86_64.zip ，直接运行解压后的 elasticsearch-7.8.0\bin\elasticsearch.bat 文件

![image-20241008122111058](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008122111058.png)

![image-20241008122245555](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008122245555.png)

访问 http://localhost:9200/ 返回

![image-20241008184836199](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008184836199.png)

# Window 集群部署

### 解压、复制ES

解压 elasticsearch-7.8.0-windows-x86_64.zip 到 es-cluster 目录，并复制 2 份，分别命名为：es-node-1001、es-node-1002、es-node-1003

![image-20241008122807244](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008122807244.png)

![image-20241008123035231](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008123035231.png)

![image-20241008160455984](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008160455984.png)

### 修改配置

修改集群文件目录中每个节点的  config/elasticsearch.yml配置文件，节点规划说明：

集群名：es-cluster-test

| 节点名称 | IP地址 | http 访问端口 | 数据传输端口 |
| :------: | :----: | :-----------: | :------------: |
| es-7.8.0-node-01 | localhost | 9201 | 9301 |
| es-7.8.0-node-02 | localhost | 9202 | 9302 |
| es-7.8.0-node-03 | localhost | 9203 | 9303 |

因为是在同一台机子测试部署集群，所以 IP 地址一样，端口配置不一样，若部署在不同服务器，则 IP 地址不一样，端口可以一样

#### es-7.8.0-node-01 配置

```yml
# 集群名称，节点之间要保持一致
cluster.name: es-cluster-test

# 节点名称，集群内要唯一
node.name: es-7.8.0-node-01
# 具有主节点选举资格
node.master: true
# 节点可存储数据
node.data: true

# ip 地址
network.host: localhost

# http 访问端口
http.port: 9201
# tcp 监听端口，数据传输接口
transport.tcp.port: 9301

# 用于启动当前节点时，发现其他节点的初始列表，此设置通常应包含群集中所有符合主机条件的节点的地址，是主机地址数组或逗号分隔的字符串。 
discovery.seed_hosts: ["localhost:9301", "localhost:9302", "localhost:9303"]
discovery.zen.fd.ping_timeout: 1m
discovery.zen.fd.ping_retries: 5

# 首次启动 Elasticsearch 集群时，群集引导步骤将确定在第一次选举中计算其投票的主合格节点集。
# 在开发模式下，如果未配置发现设置，此步骤将由节点自己自动执行。
# 由于自动引导原生不安全，因此在生产模式下启动新集群时，必须明确列出在第一次选举中应计算其投票的主合格节点。
# 可以使用 cluster.initial_master_nodes 设置此列表。
# 注意：
#    集群首次成功形成后，删除每个节点配置中的 cluster.initial_master_nodes 设置。重新启动集群或向现有集群添加新节点时，不要使用此设置。
#    新节点可通过配置 discovery.seed_hosts 发现现有集群中的节点，然后自动加入集群。
#
# 补充说明：
#    cluster.initial_master_nodes 该配置项并不是需要每个节点设置保持一致，设置需谨慎，如果其中的主节点关闭了，可能会导致其他主节点也会关闭。
#    因为一旦节点初始启动时设置了这个参数，它下次启动时还是会尝试和当初指定的主节点链接，当链接失败时，自己也会关闭！
#    例如：配置了 cluster.initial_master_nodes: ["es-7.8.0-node-01", "es-7.8.0-node-02"]，当 "es-7.8.0-node-02" 节点关闭时，
#    会导致自身节点 "es-7.8.0-node-01" 也无法正常使用
#    
#    因此，为了保证可用性，预备做主节点的节点不用每个上面都配置该配置项！保证有的主节点上就不设置该配置项，这样当有主节点故障时，
#    还有可用的主节点不会一定要去寻找初始节点中的主节点。
cluster.initial_master_nodes: ["es-7.8.0-node-01"]

# 跨域配置
http.cors.enabled: true
http.cors.allow-origin: "*"
```

#### es-7.8.0-node-02 配置

```yml
# 集群名称，节点之间要保持一致
cluster.name: es-cluster-test

# 节点名称，集群内要唯一
node.name: es-7.8.0-node-02
# 具有主节点选举资格
node.master: true
# 节点可存储数据
node.data: true

# ip 地址
network.host: localhost

# http 访问端口
http.port: 9202
# tcp 监听端口，数据传输接口
transport.tcp.port: 9302

# 候选主节点的地址，在开启服务后可以被选为主节点  
discovery.seed_hosts: ["localhost:9301", "localhost:9302", "localhost:9303"]
discovery.zen.fd.ping_timeout: 1m
discovery.zen.fd.ping_retries: 5

# 集群内可以被选为主节点的节点列表 
#cluster.initial_master_nodes: []

# 跨域配置
http.cors.enabled: true
http.cors.allow-origin: "*"
```

#### es-7.8.0-node-03 配置

```yml
# 集群名称，节点之间要保持一致
cluster.name: es-cluster-test

# 节点名称，集群内要唯一
node.name: es-7.8.0-node-03
# 具有主节点选举资格
node.master: true
# 节点可存储数据
node.data: true

# ip 地址
network.host: localhost

# http 访问端口
http.port: 9201
# tcp 监听端口，数据传输接口
transport.tcp.port: 9301

# 候选主节点的地址，在开启服务后可以被选为主节点
discovery.seed_hosts: ["localhost:9301", "localhost:9302", "localhost:9303"]
discovery.zen.fd.ping_timeout: 1m
discovery.zen.fd.ping_retries: 5

# 集群内可以被选为主节点的节点列表 
#cluster.initial_master_nodes: []

# 跨域配置
http.cors.enabled: true
http.cors.allow-origin: "*"
```

### 启动 ES

直接运行对应节点下的 bin\elasticsearch.bat 文件

### 集群状态查询

![image-20241008181149877](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008181149877.png)

### 集群节点查询

![image-20241008181515794](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008181515794.png)



# Linux 单机部署

## 安装配置 Java 

### 安装 Java

```shell
# 创建路径
[root@localhost home]# mkdir -p /home/tanji/packages/java
```

上传 jdk-8u152-linux-x64.rpm 安装包

![image-20241008194029365](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008194029365.png)

到对应目录下执行安装命令

```shell
[root@localhost home]# cd /home/tanji/packages/java
[root@localhost java]# rpm -Uvh *.rpm --nodeps --force

# 查看安装路径
[root@localhost java]# whereis java
java: /usr/bin/java /usr/share/man/man1/java.1
[root@localhost java]# ll /usr/bin/java
lrwxrwxrwx. 1 root root 22 10月  8 19:42 /usr/bin/java -> /etc/alternatives/java
[root@localhost java]# ll /etc/alternatives/java
lrwxrwxrwx. 1 root root 35 10月  8 19:42 /etc/alternatives/java -> /usr/java/jdk1.8.0_152/jre/bin/java

# 获知安装路径：/usr/java/jdk1.8.0_152
```

![image-20241008194235143](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008194235143.png)

![image-20241008194609013](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008194609013.png)

### 配置环境

#### 编辑 /etc/profile 文件

```shell
[root@localhost java]# vi /etc/profile
```

在 profile 文件最后补充如下代码，JAVA_HOME 配置为 jdk 的安装或解压的对应版本（jdk1.8.0_261）的路径

```shell
# java environment
export JAVA_HOME=/usr/java/jdk1.8.0_152
export CLASSPATH=.:${JAVA_HOME}/jre/lib/rt.jar:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar
export PATH=$PATH:${JAVA_HOME}/bin
```



![image-20241008223842326](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008223842326.png)



```shell
# 重新加载，使修改生效
[root@localhost java]# source /etc/profile

# 检查配置是否生效
[root@localhost java]# echo $JAVA_HOME
/usr/java/jdk1.8.0_152
```

![image-20241008224551392](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008224551392.png)

#### 编辑 /etc/rc.d/rc.local 文件

由于 rc.local 文件是在所有服务之前运行，所以 /etc/profiles 或 bashrc 里的环境变量这个时候根本没有执行，此时环境变量自然无法生效，因此在此文件设置开机启动命令时，如需要 java 环境变量 $JAVA_HOME ，则会因为加载不到环境变量导致服务启动报错，因此，也需在 rc.local 中加入环境变量

```shell
[root@localhost java]# vi /etc/rc.local
```

```shell
export JAVA_HOME=/usr/java/jdk1.8.0_152
export CLASSPATH=.:${JAVA_HOME}/jre/lib/rt.jar:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar
export PATH=$PATH:${JAVA_HOME}/bin
```

![image-20241008225410048](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008225410048.png)

## 安装配置 ES

### 解压软件

```shell
# 创建路径
[root@localhost home]# mkdir -p /home/tanji/packages/elasticsearch
```

上传 elasticsearch-7.8.0-linux-x86_64.tar.gz 压缩包

![image-20241008230233092](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008230233092.png)

```shell
[root@localhost home]# cd /home/tanji/packages/elasticsearch
# 解压缩
[root@localhost elasticsearch]# mkdir -p /opt/tanji
[root@localhost elasticsearch]# tar -zxvf elasticsearch-7.8.0-linux-x86_64.tar.gz -C /opt/tanji
# 改名 
[root@localhost elasticsearch]# cd /opt/tanji
[root@localhost tanji]# mv elasticsearch-7.8.0 es-7.8.0
```

![image-20241008231424893](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008231424893.png)

> 启动 es 后，会动态生成 data 目录，若要将 es-7.8.0 做为模板拷贝到别的服务器，则需删除 data 目录及 logs 下的日志文件（保留 logs 目录）

### 创建用户

因为安全问题，Elasticsearch 不允许 root 用户直接运行，所以要创建新用户，在 root 用户中创建新用户

```shell
# 新增 es 用户，创建用户的时候会默认创建一个和用户名相同的用户组，若有需要可单独指定
[root@localhost tanji]# useradd es
# 为 es 用户设置密码为：es
[root@localhost tanji]# passwd es
# 将对应的文件夹权限赋给该用户及用户组
[root@localhost tanji]# chown -R es:es /opt/tanji/es-7.8.0

# 如果添加错了，可以先删除再添加
# -r：删除用户账户及其用户主目录。使用此选项将同时删除用户主目录，确保用户的所有数据都被清除，
# 使用 -r选项时要小心，该选项会连同用户主目录一并删除，可能导致用户的数据丢失，请谨慎操作
[root@localhost tanji]# userdel -r es
```

![image-20241009000714005](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009000714005.png)

### 修改配置文件

#### 修改 es 配置文件

```yml
# 修改/opt/tanji/es-7.8.0/config/elasticsearch.yml 文件

cluster.name: elasticsearch
node.name: node-1
# 设置 host 为 0.0.0.0 ，即可启用该物理机器所有网卡网络访问
network.host: 0.0.0.0
http.port: 9200
cluster.initial_master_nodes: ["node-1"]
```

#### 修改最大允许打开文件数

因为 es 打开文件数比较多，而 linux 系统默认允许最大打开数为 1024，

![image-20241009093341182](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009093341182.png)

为了避免出现 [too many open files]，导致 es 无法正常使用，调整最大允许打开文件数。

> 系统（其实是pam_limits这个模块）会先读入 /etc/security/limits.conf，然后读入 /etc/security/limits.d/ 下面的文件，所以相同配置信息时，是 /etc/security/limits.d/20-nproc.conf 文件设定的值生效。
>
> 另外 /etc/profile 环境变量里的参数配置的优先级最高，它会覆盖 limits.conf 里的参数配置，因此如果有配置 ulimit -n xxxxx （x 表示数字），则会覆盖  limits.conf 配置

##### 修改 /etc/security/limits.conf

```shell
# 临时修改，只对当前进程生效，退出 shell 进程，再进入查看最大文件数还是变成原来的值1024
[root@localhost ~]# ulimit -n 65535

[root@localhost ~]# cat /etc/security/limits.conf
# 永久修改，修改/etc/security/limits.conf 文件
# 在文件末尾中增加下面内容，指定 es 用户（若是用 * 则表示所有用户）每个进程可以打开的文件数的限制
[root@localhost ~]# echo "es soft nofile 65535" >> /etc/security/limits.conf
[root@localhost ~]# echo "es hard nofile 65535" >> /etc/security/limits.conf
```

![image-20241009095219915](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009095219915.png)

![image-20241009103732047](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009103732047.png)

> [*] 代表针对所有用户，nproc 是代表最大进程数，nofile 是代表最大文件打开数
>
> hard和soft的区别：
>
> > -H  ：hard limit ，严格的设定，必定不能超过这个设定的数值
> >
> > > [root@dbserver ~]# ulimit -Hn  # 查看硬限制
> >
> > -S  ：soft limit ，警告的设定，可以超过这个设定值，但是若超过则有警告信息
> >
> > > [root@dbserver ~]# ulimit -Sn
> >
> > 在设定上，通常 soft 会比 hard 小，如：soft可以设置为80，而hard设定为100，那么可以使用到90（没有超过100），但介于80~100之间时，系统会有警告信息通知
>
> 总结：
>
> > a.    所有进程打开的文件描述符数不能超过 /proc/sys/fs/file-max
> >
> > b.    单个进程打开的文件描述符数不能超过 user limit 中 nofile 的 soft limit
> >
> > c.    nofile 的 soft limit 不能超过其 hard limit
> >
> > d.    nofile 的 hard limit 不能超过 /proc/sys/fs/nr_open
> >

##### 修改 /etc/security/limits.d/20-nproc.conf

```shell
[root@localhost ~]# cat /etc/security/limits.d/20-nproc.conf
# 指定 es 用户（若是用 * 则表示所有用户）每个进程可以打开的文件数的限制
[root@localhost ~]# echo "es soft nofile 65535" >> /etc/security/limits.d/20-nproc.conf
[root@localhost ~]# echo "es hard nofile 65535" >> /etc/security/limits.d/20-nproc.conf
# 操作系统级别对每个用户创建的进程数的限制
[root@localhost ~]# echo "* hard nproc 4096" >> /etc/security/limits.d/20-nproc.conf
```

![image-20241009103648893](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009103648893.png)

##### 修改/etc/sysctl.conf

```shell
[root@localhost ~]# cat /etc/sysctl.conf

# 查看 vm.max_map_count
[root@localhost ~]# sysctl vm.max_map_count
# 修改一个进程可以拥有的 VMA(虚拟内存区域)的数量，默认值为 65536
[root@localhost ~]# echo "vm.max_map_count=655350" >> /etc/sysctl.conf

# 查看整个系统最大允许打开文件数
[root@localhost ~]# cat /proc/sys/fs/file-max
# 临时修改，系统最大允许打开文件数
[root@localhost ~]# echo 6553500 > /proc/sys/fs/file-max
# 永久修改，系统最大允许打开文件数
[root@localhost ~]# echo "fs.file-max=6553500" >> /etc/sysctl.conf

# 重新加载，修改立即生效
[root@localhost ~]# sysctl -p
```

> `vm.max_map_count` 是 Linux 系统内核中的一个参数，它定义了一个进程可以拥有的最大内存映射区域数量。该参数主要影响大规模应用程序（如 Elasticsearch）和数据库的运行，因为这些应用程序可能会创建大量的内存映射文件。
>
> `max-file` 表示系统级别的能够打开的文件句柄的数量，是对整个系统的限制，并不是针对用户的。

![image-20241009110716302](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009110716302.png)

![image-20241009111115666](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009111115666.png)

## 启动 ES

>启动时，会动态生成文件，如果文件所属用户不匹配，会发生错误，因此需要用 es 用户启动，若出现该情况，则用 root 用户重新执行命令：
>
>> chown -R es:es /opt/tanji/es-7.8.0
>
>将动态生成的文件及目录重新赋权限给 es 用户及用户组

```shell
# 使用 root 用户
# 关闭防火墙 
systemctl stop firewalld
 
# 开机启用防火墙，永久性生效，重启后不会复原
systemctl enable firewalld.service
# 开机禁用防火墙，永久性生效，重启后不会复原
systemctl disable firewalld.service
```

```shell
# 使用ES用户启动
[root@localhost ~]# su es

[es@localhost root]$ cd /opt/tanji/es-7.8.0
# 启动 
[es@localhost es-7.8.0]$ bin/elasticsearch
# 后台启动 
[es@localhost es-7.8.0]$ bin/elasticsearch -d
```

浏览器访问地址：http://192.168.56.120:9200 查看 ES 信息

![image-20241009122218826](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009122218826.png)

## 域名访问

修改要访问 es 服务的服务器（注意：非部署 es 的服务器）的 hosts 文件，添加：192.168.56.120 es-single-point

> Linux系统：/etc/hosts
>
> Window系统：C:\Windows\System32\drivers\etc\hosts

![image-20241009144016663](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009144016663.png)

>若是 Linux 系统，则修改保存后，需再执行：
>
>> /etc/init.d/network restart 
>
>重启 network ，使 hosts 生效

添加完成后，Window 系统就可以通过浏览器访问地址：http://es-single-point:9200 查看部署在 192.168.56.120 Linux系统上的 es 信息

![image-20241009144123919](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009144123919.png)



# Linux 集群部署

基本可以参考 `Linux 单机部署`，但 es集群配置有所差异

> 如果系统是从别的已经部署了 es 的服务器克隆过来的，记得先删除 `data` 目录和 `logs` 目录下的日志
>
> ![image-20241009164027801](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009164027801.png)

## 修改 es-node-1 配置

修改 `/opt/tanji/es-7.8.0/config/elasticsearch.yml` 文件，在文件最后添加以下内容

```shell
# 集群名称
cluster.name: es-cluster

# 节点名称，每个节点的名称不能重复
node.name: node-1
# 具有主节点选举资格
node.master: true
# 节点可存储数据
node.data: true

# ip 地址，每个节点的地址不能重复，此时不能设置为：0.0.0.0，否则无法组成集群
network.host: 192.168.56.121
# 启用或禁用TCP保持活动。默认值为true
network.tcp.keep_alive: true
# 启用或禁用TCP无延迟设置。默认值为true。
network.tcp.no_delay: true

# http 访问端口，默认：9200
http.port: 9200

# tcp 监听端口，数据传输接口，默认：9300
transport.tcp.port: 9300
# 设置是否压缩tcp传输时的数据，默认为false，不压缩。
transport.tcp.compress: true

# head 插件需要这打开这两个配置，支持跨域访问
http.cors.allow-origin: "*"
http.cors.enabled: true
http.max_content_length: 200mb

# es7.x 之后新增的配置，节点发现
discovery.seed_hosts: ["192.168.56.121:9300", "192.168.56.122:9300", "192.168.56.123:9300"]

# 预期的节点数。加入集群的节点数（数据节点或具备 Master 资格的节点）达到这个数量即开始 gateway 恢复。默认为 0
gateway.expected_nodes: 3
# 如果没有达到预期节点数量，恢复程将等待配置的时间，再尝恢复。默认为 5min
gateway.recover_after_time: 5m
# 只要配置数量的节点（数据节点或具备 Master 资格的节点）加入群就可以开始恢复
gateway.recover_after_nodes: 2
# 以上配置表示，启动时节点达 3 个则即进入 recover，如果一直没达到 3 个，5min 超时后如果节点达到 2 个也进入 recovery

# es7.x 之后新增的配置，初始化一个新的集群时需要此配置来选举 master
cluster.initial_master_nodes: ["node-1"]

# 集群内同时启动的数据任务个数，默认是 2 个
# 控制同时在节点移动的分片数量，指定整个集群中同时可以在节点间移动的分片数量。如果集群由很多节点组成，可以提高这个值。默认值2。
cluster.routing.allocation.cluster_concurrent_rebalance: 2
# 添加或删除节点及负载均衡时并发恢复的线程个数，默认 2 个
# 控制单个节点上同时初始化的分片数量，设置Elasticsearch在单个节点上一次可以初始化多少分片。分片还原过程是非常耗I/O的，默认是2。
cluster.routing.allocation.node_concurrent_recoveries: 2
# 初始化数据恢复时，并发恢复线程的个数，默认 4 个
# 控制单个节点上同时初始化的主分片数量
cluster.routing.allocation.node_initial_primaries_recoveries: 4
```

## 修改 es-node-2 配置

基本与 `修改 es-node-1 配置` 一样，不同点修改如下：

```shell
# 节点名称，每个节点的名称不能重复
node.name: node-2

# ip 地址，每个节点的地址不能重复，此时不能设置为：0.0.0.0，否则无法组成集群
network.host: 192.168.56.122

# es7.x 之后新增的配置，初始化一个新的集群时需要此配置来选举 master
cluster.initial_master_nodes: []
```

## 修改 es-node-3 配置

基本与 `修改 es-node-1 配置` 一样，不同点修改如下：

```shell
# 节点名称，每个节点的名称不能重复
node.name: node-3

# ip 地址，每个节点的地址不能重复，此时不能设置为：0.0.0.0，否则无法组成集群
network.host: 192.168.56.123

# es7.x 之后新增的配置，初始化一个新的集群时需要此配置来选举 master
cluster.initial_master_nodes: []
```

## 启动 ES

可禁用防火墙，或开发 ES 涉及端口：9200、9300

```shell
# 使用 root 用户
# 关闭防火墙 
systemctl stop firewalld
 
# 开机启用防火墙，永久性生效，重启后不会复原
systemctl enable firewalld.service
# 开机禁用防火墙，永久性生效，重启后不会复原
systemctl disable firewalld.service
```

```shell
# 使用ES用户启动
[root@localhost ~]# su es

[es@localhost root]$ cd /opt/tanji/es-7.8.0
# 启动 
[es@localhost es-7.8.0]$ bin/elasticsearch
# 后台启动 
[es@localhost es-7.8.0]$ bin/elasticsearch -d
```

浏览器访问地址：http://192.168.56.121:9200/_cat/nodes 查看节点信息

![image-20241009164647685](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009164647685.png)

# 可视化工具

参见 [简单好用的ElasticSearch可视化工具](https://developer.aliyun.com/article/1297184) 介绍

## elasticsearch-head

谷歌浏览器插件：elasticsearch-head（[下载](https://github.com/skyer83/learning-es/blob/main/plugin/elasticsearch-head-chrome-plugin.rar))

![image-20241009175028748](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009175028748.png)

![image-20241009175354881](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241009175354881.png)





## es-client

插件下载地址：https://es-client.esion.xyz/download/，推荐 [安装包下载](https://static.esion.xyz/#/%E6%8F%92%E4%BB%B6/es-client)

![image-20241008183526661](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241008183526661.png)

##  Kibana

下载地址：https://artifacts.elastic.co/downloads/kibana/kibana-7.8.0-windows-x86_64.zip

1、解压缩下载的 zip 文件

![image-20241010214000252](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241010214000252.png)

2、修改 config/kibana.yml 文件 

```yml
# 默认端口
server.port: 5601
# ES 服务器的地址，集群时，用逗号分隔开，如：["http://localhost:9200","http://localhost:9201","http://localhost:9202"]
elasticsearch.hosts: ["http://localhost:9200"]
# 索引名
kibana.index: ".kibana"
# 支持中文
i18n.locale: "zh-CN"
```

3、Windows环境下执行 bin/kibana.bat 文件

![image-20241010214618412](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241010214618412.png)

![image-20241010214743301](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241010214743301.png)

4、通过浏览器访问：http://localhost:5601

![image-20241010215047571](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241010215047571.png)

![image-20241010215116872](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/image-20241010215116872.png)

# 问题

## master not discovered or elected yet

参见：https://blog.csdn.net/wang_peng/article/details/117294875

一个单机三个节点的集群, 如果一次性全部停掉,再启动master节点时,就会启动不成功，报：

> master not discovered or elected yet, an election requires at least X nodes with ids from [XXXXX]

主要有两点原因,

1) 如果ES集群是第一次启动时,已经加入集群的几点信息保存在data目录下，以供下次启动使用，这样也就是说cluster.initial_master_nodes就不在起作用了

2) 每个ES集群都维护一个选举配置集合(Voting Configurations),这个选举集合由可以选举为主节点的master类型节点组成.它除了提供选举功能，还决定者集群的状态，当选举配置集合中超过一半的节点存活时，集群才提供服务（也就是过半原则，通常集群解决脑裂问题都是采用这种方式）.也就是说3个节点,挺掉一个,还有两个,属于过半了,不会有什么问题.但如果一下3个全停了,那就完犊子了.三个服务都彻底不能用了

但如果不愿意启动多个节点，也就是要将多节点集群降级，也就是如何减少集群中的节点数呢？发现很多推荐做法是清空data目录，确实这样相当于重新启动而创建一个全新的集群，可以解决问题，但是结果是导致所有的数据丢失.所以只能一个一个的停,每停一个节点,向主节点的voting_config_exclusions中添加一下要删除节点的ID或者名称

具体操作:

1) 添加排除，也就是从配置集合中删除,可以使用节点Id(node_ids)或者节点名称(node_names)来排除,如果执行失败.加上参数 wait_for_removal=false 试试

用PostMan向主节点Post http://localhost:9201/_cluster/voting_config_exclusions?node_names=node-1002,node-1003

![img](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/51bce3cdd8c19cec6b495d7d614cf74f.png)

2) 查看排除列表

用PostMan向主节点Get http://localhost:9201/_cluster/state?filter_path=metadata.cluster_coordination.voting_config_exclusions&pretty

![img](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/7a64006034780af7232682dc2f0d591b.png)

3) 然后就可以停掉节点node-1002,node1003

4) 清空列表 用PostMan向主节点 Delete http://localhost:9201/_cluster/voting_config_exclusions

 ![img](Window%E3%80%81Linux%E5%8D%95%E6%9C%BA%E3%80%81%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%AF%B4%E6%98%8E.assets/ed33df7c0cb94c0228ac267c4fbd88c9.png)
