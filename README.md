# DragonAuthMe插件使用帮助v1.1.0

DragonAuthMe插件是一个支持邮箱注册和修改密码的插件。它可以帮助你简易化玩家的注册难度。

## 功能特点

- 邮箱验证：插件可以通过邮箱验证注册账号，可以通过邮箱验证找回密码。
- 修改密码：插件支持修改密码，验证正确密码之后，修改新的密码（手残党的福音）。
- 安全保护：插件在玩家登录之前提供安全保护，确保只有经过身份验证的玩家才能进入游戏。
- 错误弹窗：如果玩家密码输入出现错误，会有错误弹窗。

## 使用方法

1. 将压缩包下的DragonAuthMe.jar插件并将其放入你的服务器的插件目录中。
2. 将压缩包目录下 /plugins/DragonCore/Gui/ 的龙核配置放到你服务器的对应位置（一定要下载DragonCore插件）。
3. 启动服务器，插件将自动加载，在插件目录下生成config.yml文件。
4. 配置config.yml文件，具体配置方法写在了配置文件当中。
5. 获取QQ邮箱的“授权码”,生成授权码位置 <QQ邮箱-设置-账户>。
![img_1.png](img_1.png)
6. 如果你不需要邮箱你可以在配置文件config.yml中关闭这个功能。
7. 配置好之后重启服务器即可。
8. 你可以通过命令/DragonAuthMeAdmin SendTest <邮箱地址> 快速测试邮箱是否配置成功

## 配置文件

    #插件版本
    version: 1.1.0
    #只支持QQ邮箱
    Email:
      #是否开启邮箱功能
      enable: true
      Address:
        IP: "smtp.qq.com"
        #端口
        Port: 465
        #使用SSL
        UseSSL: true
      Auth:
        #登录账户(请与发件人邮箱一致)
        Account: "123456@qq.com"
        #邮箱授权码(不是QQ密码,如果不知道请查看插件教程)
        Password: "123456"
      Content:  #发件设置
        #发件人邮箱(请与登录账户一致)
        SendEmailAddress: "123456@qq.com"
        #邮件标题
        SendEmailTitle: "DragonAuthMe验证码"
        #邮件内容(%Email_Code%是验证码变量)
        SendEmailText: "你的验证码为: %Email_Code%"
    Storge:
      #数据储存方式 "Yaml" “MYSQL” 建议使用Yaml,MYSQL可能有些问题
      Type: "Yaml"
      MySQLIP: "localhost:3306"
      MySQLUserName: "root"
      MySQLPassWord: "123456"
      MySQLDataBase: "root"
      MySQLTableName: "DragonAuthMe"
    Setting:
      #是否启用AuthMe的修改密码指令 true/为开启 false/为关闭       (等待更新)
      AuthMePassword: true
      #是否启用AuthMe的注册功能 true/为开启  false/为关闭
      #关闭是为了开启邮箱注册功能的时候避免玩家通过AuthMe插件进行注册(主要是防有些卡的龙之登录的界面都打不开的玩家，直接绕过了)
      AuthMeRegister: false
      #验证码长度
      CodeLength: 6
      #玩家登录之后强制绑定(等待更新)
      ForceBind: false
      #邮箱正则的正则表达式 (没有特殊需求 请不要修改)
      Regx: "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"





## 命令和权限

**权限节点：DragonAuthMe.Player**

* /DragonAuthMe View  ---   查看自己邮箱的地址

**权限节点：DragonAuthMe.Admin**
* /DragonAuthMeAdmin Add <玩家名称> <邮箱地址>  --- 为一个玩家添加邮箱地址 
* /DragonAuthMeAdmin Edit <玩家名称> <邮箱地址>  --- 修改一个玩家的邮箱地址
* /DragonAuthMeAdmin Delete <玩家名称> <邮箱地址>  --- 删除一个玩家的邮箱地址
* /DragonAuthMeAdmin View <玩家名称> <邮箱地址>  --- 查看某个玩家的邮箱地址
* /DragonAuthMeAdmin SendTest <邮箱地址> --- 发送一封测试邮件


## 注意事项！！！(必读)

- 确保你的服务器已经安装了AuthMe和DragonCore插件。
- 有关DragonAuthMe插件的更多信息和帮助，请前往交流群590612103。


## 插件效果图
<img src="https://bbs.mc9y.net/attachments/4115/" width="800px" height="560px" alt="效果图1">
<img src="https://bbs.mc9y.net/attachments/4116/" width="800px" height="560px" alt="效果图2">
<img src="https://bbs.mc9y.net/attachments/4117/" width="800px" height="560px" alt="效果图3">
<img src="https://bbs.mc9y.net/attachments/4118/" width="800px" height="560px" alt="效果图4">



