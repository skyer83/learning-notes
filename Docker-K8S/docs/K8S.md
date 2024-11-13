# TODO K8S

《[完整版Kubernetes（K8S）全套入门+微服务实战项目，带你一站式深入掌握K8S核心能力](https://www.bilibili.com/video/BV1MT411x7GH/?spm_id_from=333.999.0.0&vd_source=f050b4d563f8e729f80ae8b3803dfe24)》学习笔记

## 搭建K8S集群

操作系统：CentOS 7

Docker: 20+

K8S: 1.23.6



# Jenkinsfile

## 命令解释

### checkout scm

checkout scm 是指在 Jenkins 中使用 SCM（Source Code Management）插件从代码仓库中检出代码。这个命令会根据 Jenkinsfile 中的配置，从指定的代码仓库中拉取代码，并将其存储在 Jenkins 的工作空间中，以供后续的构建和测试使用。

checkout scm是Jenkins Pipeline中用于从版本控制系统（如Git）检出代码的命令。它是一个特殊的步骤，它会将代码从代码仓库中下载到Jenkins workspace中。

该命令的详细过程如下：

1. Jenkins Pipeline通过scm步骤指定要检出的代码库的URL，以及任何其他必要的参数（例如分支或标记）。
2. Jenkins Pipeline调用checkout scm命令，该命令会启动Git客户端并连接到指定的代码库。
3. Git客户端验证身份并下载代码库中的所有文件。这些文件将被下载到Jenkins workspace的一个子目录中。
4. Jenkins Pipeline将检出的代码与Jenkins workspace中的其他文件合并，以便可以进行构建和测试。
5. 如果存在任何冲突或问题，Jenkins Pipeline会尝试解决它们并继续构建过程。
6. 构建完成后，Jenkins Pipeline会清理Jenkins workspace中的所有文件，包括检出的代码。

总之，checkout scm命令是Jenkins Pipeline中非常重要的一步，它确保了代码库与Jenkins workspace之间的同步，并为后续构建和测试提供了必要的代码基础



注意，区别 Git 的 SCM

> 软件配置管理（Software Configuration Management，SCM）是指在开发过程中各阶段，管理计算机程序演变的学科。SCM提供了结构化、有序化、产品化的软件工程管理方法。软件配置管理的基本单位是软件配置项，由软件开发过程中产生的所有信息构成，包括代码、数据、文档、报告，每一项称为一个配置。Git是当前的主流SCM工具，其官方文档为Pro Git