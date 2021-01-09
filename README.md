# trade-stock-core

库存中心 核心sdk项目

实时库存状态服务，350亿/天 ；实时库存预占服务 9000w/天；实时库存变化服务，3亿/天

## 开发环境
JDK：1.8  
maven:3.6.3  

## 提交规范 请严格按照提交规范进行commit

参考：[GitMessage提交规范](GitMessage提交规范.md)

## 系统文档

参考：[库存架构](https://joyspace.jd.com/page/tufrGCx0Aabbk5wmL9q3)

参考：[库存流程](https://joyspace.jd.com/page/tufrGCx0Aabbk5wmL9q3)

参考：[redis数据规则](https://joyspace.jd.com/page/-bexBoHiWe2P7WU_9MK8)

参考：[库存预占接口文档](https://cf.jd.com/pages/viewpage.action?pageId=300367718)

参考：[库存业务模型](https://joyspace.jd.com/page/6rLk2Y67iGVS_bKmPt8k)

参考：[异步流程timer实现](https://joyspace.jd.com/page/E1564XWNREKvafrpGuWe)

参考：[异步流程timer流程](timer-addTask.puml)

参考：[prd设计](https://joyspace.jd.com/page/Xer-PfwlppXbCpNVzO0P)

## 模块说明
1. trade-stock-parent：该模块为该项目依赖所有jar包的坐标。
2. trade-stock-modules：该模块为业务实现模块，具体实现api和处理服务。

### trade-stock-modules 说明

~~~
├── common       通用模块
├── contract     对外提供api
├── core         核心代码逻辑
├── dao          dao
├── service      
~~~

## 单元测试

本项目必须编写单元测试

## 测试环境

IP：192.168.xx.xx

用户/密码:root/360Cymis%

通过SSH命令行连接: ssh root@192.168.xx.xx -p22

测试环境Tomcat目录: /export/home/tomcat/domains/trade-stock-core/server1

测试环境程序目录: /export/data/tomcatRoot/trade-stock-core/


### 测试环境测试方式
使用[postman](https://www.postman.com)进行测试环境测试

URL请求参数参见 [白皮书 公共参数](doc/京东开放物流接口白皮书.md)

~~~
开发环境：
http://localhost/router/canghai/service?LOP-DN=eclp.api.jdwl.com&method=jingdong.canghai.logisticsproperties.report&app_key=d2575f90b1a54f92a154450cd21cbc18&access_token=2ccc8a4b8917462b96a05e4bf337e8d6&timestamp=2019-01-01 12:00:00v=2.0&customerId=EBU4418046589187&warehouseCode=1100000012&sign=d2575f90b1a54f92a154450cd21cbc18

测试环境
https://test-proxy.jd.com/router/canghai/service?LOP-DN=eclp.api.jdwl.com&method=jingdong.canghai.logisticsproperties.report&app_key=d2575f90b1a54f92a154450cd21cbc18&access_token=2ccc8a4b8917462b96a05e4bf337e8d6&timestamp=2019-01-01 12:00:00v=2.0&customerId=EBU4418046589187&warehouseCode=110000195&sign=d2575f90b1a54f92a154450cd21cbc18
~~~

Postman URL Param 填写示例：

![postman URL Param 示例](http://storage.jd.com/jiuzi.com/jumbotron/20200303214511.png)

Postman Header 填写示例：

![Postman Header 填写示例](http://storage.jd.com/jiuzi.com/jumbotron/20200303214729.png)

Postman body 填写示例：

![Postman body 填写示例](http://storage.jd.com/jiuzi.com/jumbotron/20200303214805.png)


### trade-stock 测试环境
JD侧：http://dev-ops.jd.com

应用名：trade-stock



## 开发注意项
1. UMP 监控
2. 异常的处理
3. inbound、outbound 日志记录查询

## 待跟进

- [ ] JSF 的全局异常处理
- [ ] 研究 [Spring-integration](https://docs.spring.io/spring-integration/docs/5.2.3.RELEASE/reference/html/index.html)
- [ ] 通过物流网关实现调用外部域，如何支持可扩展
- [ ] 单元测试 自动化测试
- [ ] 上线后的沙箱环境