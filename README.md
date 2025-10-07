# 参考博客

[【SpringBoot】还不会SpringBoot项目模块分层？来这手把手教你 - dreamw - 博客园](https://www.cnblogs.com/wl-blog/p/17260821.html)

相信大家在学习完了Spring Boot之后，在自己写程序时，往往写了一堆，导致代码变得不可维护（相信我！作者初学的时候就是这样！）。一个调理清晰的项目架构可以帮助大家快速的定位到功能模块并编写模块代码，同时也有助于提升大家对Idea编译器、Maven的学习。我们的代码已经开源并可以在这里找到。代码地址[https://github.com/SlideTackle-Tiger/Spring-Boot-scaffold](https://github.com/SlideTackle-Tiger/Spring-Boot-scaffold)

# 项目架构介绍

项目有三个主要模块，将从这里入手来为大家介绍

scaffold

- scaffold-dependencies -- 依赖模块，存放常用依赖

- scaffold-main -- 启动类模块

- scaffold-module  -- 程序主要代码存放

    - scaffold-common  -- 存放复用代码，如Util类。

    - scaffold-controller  -- 存放controller层代码

    - scaffold-dao  -- 数据持久化层，存放Mapper

    - scaffold-dto  -- 前后端数据交互层，主要存放前后端交互模版

    - scaffold-entity  -- 数据库实体类，我们使用了Mybatis-Plus + MySQL持久化数据。

    - scaffold-service  -- 存放服务层代码，业务逻辑写在这里

# 依赖关系介绍

项目中各个模块相互依赖，主要遵循一个原则，用谁引谁。一定要避免A依赖B，B依赖A的情况发生。这里给大家打个比方：

比如我们的controller模块，需要使用到dto(前后端数据交互)、service(service层)、common(ResultUtil返回结果工具类)。那么就需要在controller的pom.xml文件中的project.dependencies.dependency标签中加入引用。

当我们在main模块中执行Maven 的install生命周期时就会递归的将所有模块的依赖都挂载好。

项目依赖介绍

scaffold-main

- scaffold-controller

- scaffold-dto

scaffold-common

- scaffold-dto

scaffold-controller

- scaffold-dto

- scaffold-service

- scaffold-common

scaffold-dao

- scaffold-entity

scaffold-service

- scaffold-dto

- scaffold-dao

# 脚手架示例介绍

我们提供了一个简单的请求实例，通过这个示例可以学习到如何使用这个脚手架，首先在main模块中的application配置文件中配置你自己的环境信息，之后在application-local.yml文件中配置你们数据库配置。执行main模块中sql文件夹下的sql文件，创建数据库与表

由于我们使用的是Mybatis-Plus框架，因此需要创建以下内容（代码中已经包含）

- entity

    - user实体类

- dao

    - userMapper 用于持久化数据

- dto

    - request userRequest 用于接收前端请求参数

    - response userResponse 用于返回前端响应

- service

    - userServer 用户接口

    - userServiceImpl 用户具体业务实现

- controller

    - userController 用户控制类，提供业务代码入口

# 快速开始

首先加载项目根目录中的pom.xml文件到maven，然后刷新maven加载依赖，这里强烈建议国内的读者使用阿里云镜像。

进入到scaffold-main模块的resource包下，修改环境信息，主要修改application.yml application-local.yml文件。

进入到scaffold-main模块的sql包下，执行sql脚本。

然后进入到scaffold-main模块→启动类 启动。

可以调用[http://localhost:8001/scaffold/v1/user?id=1](http://localhost:8001/scaffold/v1/user?id=1) 请求，尝试获取示例的请求信息。









