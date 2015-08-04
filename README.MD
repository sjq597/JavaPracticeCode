### Java学习代码仓库

#### 项目初始化
新建一个maven工程，使用了公司的仓库模板，过程大概如下：
* 1.new->project->maven->create from archetype->maven-archetype-quickstart
* 2.GroupId:com.note.practice | ArtiFactId:code
* 3.Project name:JavaPracticeCode
**NOTE:**注意，我使用的模板`maven-archetype-quickstart`是公司仓库里的，正常情况下没有连接到公司的仓库应该没有，可以
选择不使用模板，即create from archetype不勾选，或者直接新建一个空的工程，把我的工程目录导入。
等待maven工程导入，工程目录结构大概如下所示：
```
├── .gitignore
├── JavaPracticeCode.iml
├── pom.xml
├── README.MD
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── learn
    │               └── note
    │                   └── practice
    │                       └── App.java
    └── test
        └── java
            └── com
                └── learn
                    └── note
                        └── practice
                            └── AppTest.java

```
**NOTE1:**关于IntelliJ IDEA中如何使用github的帖子很多，不清楚的可以先找相关帖子，配置好之后，
在工具栏的VCS->Import into Version Control->Create Git Repository即可把当前的项目创建Git
仓库，然后add之后，commit，然后再push。
**NOTE2:**注意直接使用IDEA这样push不行，因为没有远端的仓库地址，你可以先在github网站上手动创建一个远端仓库，但是这样很麻烦，这里介绍一个命令行创建的方法。
所有的操作均在本地仓库的路径下，首先cd到你的本地仓库的目录下，我的是～/JavaPracticeCode
> 1.首先安装curl
>  sudo apt-get install curl
> 2.模拟提交一个post请求到github
> curl -u username https://api.github.com/user/repos -d '{"name":"reposName"}'
> 3.将本地master分支提交到远端
> git push origin master

**NOTE:**userName是你在github上的名字，reposName是你想创建的仓库的名字，创建过程中还需要输入用户名和密码，创建成功会返回各种信息。为了忽略一定的信息，可以新建.gitignore文件，
把需要忽略的文件填在里面。.gitignore文件内容如下
```
# 忽略.idea文件夹下的项目信息文件
.idea
```
**NOTE:**github每次上传都需要输入用户名和密码，在idea中无法push，原因是采用https加密方式传输，改为SSH方式
```shell
~/JavaPracticeCode git remote -v
origin	https://github.com/sjq597/JavaPracticeCode.git (fetch)
origin	https://github.com/sjq597/JavaPracticeCode.git (push)
~/JavaPracticeCode git remote rm origin
~/JavaPracticeCode git remote add origin git@github.com:sjq597/JavaPracticeCode.git
~/JavaPracticeCode git push origin
fatal: The current branch master has no upstream branch.
To push the current branch and set the remote as upstream, use

    git push --set-upstream origin master

~/JavaPracticeCode git push origin master
Everything up-to-date
```
