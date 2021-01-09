Git Message 编写规范
===
本文档旨在约束大家在进行代码提交时需要使用的规则以及方法。

## 为什么要使用规范？
1. 规范化的提交记录，更易于浏览。
2. 更易于过滤，便于查找消息
3. 可以根据提交日志自动生成CHANGELOG

## Git Message 编写指南
我们参考的是[Angular 规范](https://github.com/angular/angular/blob/22b96b9/CONTRIBUTING.md#-commit-message-guidelines)，同时也参考了[约定式提交](https://www.conventionalcommits.org/zh-hans/v1.0.0-beta.4/).

## 规范说明

规范包括三个部分：Header、Body、Footer

~~~
<type>(<scope>): <subject>
// 空一行
<body>
// 空一行
<footer>
~~~

### Header

Header的部分只有一行,包括三个字段: type(必需), scope(可选), subject(必需)

#### Type 说明

* feat：新功能（feature）
* fix：修补bug
* docs：文档（documentation）
* style： 格式（不影响代码运行的变动,空格,格式化,等等）
* refactor：重构（即不是新增功能，也不是修改bug的代码变动）
* perf: 性能 (提高代码性能的改变)
* test：增加测试或者修改测试
* build: 影响构建系统或外部依赖项的更改(maven,gradle,npm 等等)
* ci: 对CI配置文件和脚本的更改
* chore：对非 src 和 test 目录的修改
* revert: Revert a commit

#### Scope 说明

`scope` 用于说明 commit 影响的范围，比如数据层、控制层、视图层等等，视项目不同而不同。

#### Subject 说明

`subject` 是 commit 目的的简短描述，不超过50个字符

需要注意：

* 以动词开头，使用第一人称现在时，比如change，而不是changed或changes
* 第一个字母小写
* 结尾不加句号（.）

### Body

Body 部分是对本次 commit 的详细描述，可以分成多行

需要注意 
* 使用第一人称现在时，比如使用change而不是changed或changes。
* 应该说明代码变动的动机，以及与以前行为的对比

### Footer

Footer 部分只用于两种情况

1. 不兼容变动：
   如果当前代码与上一个版本不兼容，则 Footer 部分以BREAKING CHANGE开头，后面是对变动的描述、以及变动理由和迁移方法
2. 关闭Issue
   如果当前 commit 针对某个issue，那么可以在 Footer 部分关闭这个 issue 。也可以是多个issue。或者Jira 编号


## Idea工具使用

安装插件： ` Git Commit Template Helper`