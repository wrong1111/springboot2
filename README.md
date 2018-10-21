# springboot2.0.6_mybatis_shiro后台项目

#### 项目介绍
一款小巧，方便，快捷开发后台管理系统使用。
包含有shiro权限管理--菜单管理，角色管理，用户管理
同时也包括了activemq的使用方法。

不管是个人还是企业对于快速开发后台提供了最基本的组件。

#### 软件架构
 所有开发软件基于最近流行的开发框架。
 springboot 2.0.6 
 mybatis
 shiro 1.3.1
 activemq 


#### 安装教程

 

#### 使用说明
 1. 使用过程中，使用mybatis自动生成的代码创建mapper.xml 没有primarykey,只是因为数据库是5.5,数据库驱动未使用最新6.0;
在升级6.0驱动过程中，出现了一些问题，由于没有时间处理，所以未处理。
 2，代码全部未按流行的组织拆分，但是把模块独立出来了。
  com.yuce.admin - 
                 - common 包括了所有的配置初始化程序。
                 - manage 包括了系统配置菜单程序文件。
                 - task 包括了定时任务的程序文件。
                 - buiness 包括了简单的业务使用程序文件。 

所有文件，都可以以本地jar包方式运行。
java -jar xxx.jar -Xmx256m 
  
 

#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


 