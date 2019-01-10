# Valet详细设计规约

1652772 肖   睿

1652712 赵欣宇

 

# 〇 修订历史

 

| **编写日期** | **SEPG** | **版本** | **说明** | **作者**    | **评审时间** | **评审参与人员** | **评审后修改批准日期** | **确认签字人员** |
| ------------ | -------- | -------- | -------- | ----------- | ------------ | ---------------- | ---------------------- | :--------------: |
| 2018.10.14   | VA       | 0.1      | 初稿     | 赵欣宇      | 2018.10.8    | 赵欣宇 肖睿      | 2018.10.15             |                  |
| 2018.11.29   | VA       | 1        | 定稿     | 赵欣宇 肖睿 | 2018.11.23   | 赵欣宇 肖睿      | 2018.11.30             |                  |
| 2018.12.9    | VA       | 1.1      | 修订稿   | 赵欣宇 肖睿 | 2018.12.3    | 赵欣宇 肖睿      | 2018.12.30             |                  |

 

## 一 引言

### 1编写目的

### 2背景与依据

本系统blabla简单描述一下

本文档内容依据《概要设计规约》，在其基础上进行撰写和校订。

### 3参考资料

[1]《Valet 需求规约》

[2]《Valet 需求分析规约》

[3]《Valet 概要设计规约》

## 二 系统软件结构

以下内容同《Valet 概要设计规约》

### 2.1 系统包图

![系统包图](Diagrams/ArchitectureDiagrams/%E7%B3%BB%E7%BB%9F%E5%8C%85%E5%9B%BE.png)



#### 2.1.1 Data Access Package

该包负责数据的持久化存储，包括 MySQL 子系统和服务器运行平台的文件系统两部分。其中文件系统主要负责存储系统中所有多媒体内容，包括但不限于用户上传的衣物图片、穿搭图片等，以及对图片进行压缩时的本地缓存。MySQL 数据库负责对其余系统相关数据进行存储。

#### 2.1.2 ORM Package

ORM 包主要负责建立持久化存储数据和软件运行时数据对象间的映射，封装 MySQL 数据库相关操作并提供一体化高灵活度的数据操作方式。

#### 2.1.3 Model Package

该包包含一系列实体类以及各功能模块的控制类，实现了系统的核心业务逻辑，其接收来自 Controller 包中控制器的业务请求，使用 ORM 包封装的数据库操作接口处理数据并将处理结果返回给 Controller 包中的控制器。

#### 2.1.4 Controller Package

该包由各模块控制器组成，负责向 Http Service 包提供接口服务。其根据收到的服务请求创建对应的Model并将其操作结果返回给 Http Service 包。在整个业务流程中 Controller 包中的控制器只负责提供各功能模块的请求接口，并创建对应的Model然后将处理结果信息返回。

#### 2.1.5 Http Response Package

该包由一系列实体类组成，这些实体类定义了前端和后端发送信息的数据格式。

#### 2.1.6 Http Service Package

该包由一系列接口类组成，这些接口类定义了前端和后端之间的数据接口的参数和返回值类型格式，并通过Retrofit框架实现Http请求的发送和返回值解析。

#### 2.1.7 View Package

该包负责整个系统的页面展示，包含一系列安卓系统 Activity，Fragment 以及 Adapter，为数据的呈现和用户交互提供了支持，并且对用户输入数据进行了一些简单的合法性验证。

### 2.2 类调用关系图

#### 2.2.1 用户模块

![ArchitectureDesign__用户模块设计类图_33](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__%E7%94%A8%E6%88%B7%E6%A8%A1%E5%9D%97%E8%AE%BE%E8%AE%A1%E7%B1%BB%E5%9B%BE_33.png)

[用户模块-设计类图]

用户部分的类主要实现的系统功能为新用户的注册，用户信息验证以及获取用户推荐宝贝信息。

该部分包括两个界面类 Login Activity 和 Register Activity 负责接收用户的注册和登录输入数据信息，并进行一些简单的合法性验证，以及登录和注册的结果显示和界面跳转。

User，Taobao Item 以及 User Manager 负责实现该部分系统功能业务逻辑，由 User Controller 负责界面类与实体类之间的信息沟通，接收来自界面类的请求，创建 User Manager 和 User ，并将处理结果返回。

#### 2.2.2 衣橱管理模块

![ArchitectureDesign__衣橱管理模块设计类图_35](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__%E8%A1%A3%E6%A9%B1%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97%E8%AE%BE%E8%AE%A1%E7%B1%BB%E5%9B%BE_35.png)

[衣橱管理模块-设计类图]

衣橱管理部分主要实现了新建衣橱，删除衣橱功能。

该部分包括两个界面类 Manage Wardrobe Activity 和 Wardrobe Recycler View Adapter 负责处理用户交互，其中Wardrobe Recycler View Adapter 主要为 Manage Wardrobe Activity 所对应的衣橱管理界面的衣橱列表的用户交互行为进行支持。

Wardrobe 和 Wardrobe Manager 实现了衣橱相关系统功能的业务逻辑，由 Wardrobe Controller 负责 边界类和实体类之间的信息沟通。

#### 2.2.3 衣物管理模块

![ArchitectureDesign__衣物管理模块设计类图_34](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__%E8%A1%A3%E7%89%A9%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97%E8%AE%BE%E8%AE%A1%E7%B1%BB%E5%9B%BE_34.png)

[衣物管理模块-设计类图]

衣物管理部分主要实现了新建衣物，删除衣物和修改衣物功能。

该部分包括四个界面类 Manage Clothes Activity ， Edit Clothes Activity ， Add Clothes Activity 以及 Clothes Recycler View Adapter，其中 Clothes Recycler View Adapter 主要为 Manage Clothes Activity 所对应的衣物管理界面的衣物列表的用户交互行为进行支持。

Clothes Type 为枚举类，记录和定义了系统所支持的衣物类型。Clothes 和 Clothes Manager 类实现了该部分系统功能的业务逻辑，由 Clothes Controller 负责边界类和实体类之间的信息沟通。

#### 2.2.4 穿搭管理模块

![ArchitectureDesign__穿搭管理模块设计类图_36](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__%E7%A9%BF%E6%90%AD%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97%E8%AE%BE%E8%AE%A1%E7%B1%BB%E5%9B%BE_36.png)

[穿搭管理模块 - 设计类图]

穿搭管理部分主要实现了推荐今日穿搭，选择今日穿搭，新建穿搭以及删除穿搭功能。

该部分包括六个界面类 Main Activity， Add New Suit Activity ， Wear Suit Activity ， Best Suit Fragment ， Suit Clothes List Adapter ， Wear Suit Recycler View Adapter，其中Best Suit Fragment 负责 Main Activity 所对应主界面中的今日最适穿搭卡片的用户交互，Wear Suit Recycler View Adapter 负责了主界面中的穿搭列表的用户交互，Suit Clothes List 负责对 Wear Suit Activity 所对应的查看穿搭衣物详情页面的衣物列表的用户交互行为提供支持。

Suit ，Clothes_Suit ，Suit Manager 实现了该部分系统功能的业务逻辑，其中 Clothes_Suit 负责处理由Clothes 到 Suit 之间的对应关系。其中获取天气信息相关功能可能会用到 Utils 包中的 Weather Api 工具类。Suit Controller 负责该部分边界类和实体类之间的信息沟通。

#### 2.2.5 Http 接口请求类

![ArchitectureDesign__Http接口请求类图_39](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__Http%E6%8E%A5%E5%8F%A3%E8%AF%B7%E6%B1%82%E7%B1%BB%E5%9B%BE_39.png)

[Http接口请求 - 设计类图]

该部分包括对 Retrofit 框架进行封装的 Retrofit Client 类以及各模块 Http 接口定义类。

该部分为界面类定义了后端 Http 服务的接口规范，界面类通过调用该部分接口实现和控制类的信息沟通。

#### 2.2.6 Http 接口响应类

![ArchitectureDesign__Http接口响应类图_40](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__Http%E6%8E%A5%E5%8F%A3%E5%93%8D%E5%BA%94%E7%B1%BB%E5%9B%BE_40.png)

[Http接口响应 - 设计类图]

该部分包括各模块 Http 接口响应格式，为界面类和控制类定义了接口的响应格式规范，控制类通过该部分类生成接口返回值，界面类通过该部分类解析来自控制类的返回值信息，从而规范化了前后端的信息沟通。

#### 2.2.7 工具类和项目配置类

![ArchitectureDesign__工具类项目配置类设计类图_37](Diagrams/ArchitectureDiagrams/DesignClassDiagrams/ArchitectureDesign__%E5%B7%A5%E5%85%B7%E7%B1%BB%E9%A1%B9%E7%9B%AE%E9%85%8D%E7%BD%AE%E7%B1%BB%E8%AE%BE%E8%AE%A1%E7%B1%BB%E5%9B%BE_37.png)

[工具类项目配置类 - 设计类图]

该部分包含了实现系统功能业务逻辑所需的工具类，以及设置系统配置信息的配置类。

其中 Http Util 类为各模块封装了 Http Get 请求，Image Util 类封装了图片压缩功能，Taobao Api 类封装了从淘宝识图API获取淘宝宝贝信息的接口，Weather Api 类封装了天气信息获取功能。

Config 类集中存储了系统运行的配置信息，包括数据库连接字符串、天气信息获取路径、淘宝商品搜索路径等。

## 三 类设计

3.1

### 3.1类描述

### 3.2功能

### 3.3性能（可能不考虑）

### 3.4输入项（输入接口）

### 3.5输出项（输出接口）

### 3.6属性详细描述

### 3.7方法详细描述及实现流程

### 3.8类内、类外方法调用状态图

### 3.9限制条件及出错处理