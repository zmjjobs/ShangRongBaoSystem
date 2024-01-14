# 1、srb后端系统

```bash
D:\ideaWorkSpace\ShangRongBaoS\srb
```

```bash
springboot版本：  2.3.4.RELEASE
```

```bash
 被依赖层次：
 srb-common
 	service-base
 		service-core
```

```sql
#MySQL数据库： 
create schema srb_core collate utf8mb4_general_ci;
```

```bash
git init
git remote add origin https://github.com/zmjjobs/srb.git
git push -u origin master
```

```http
swagger2访问地址:
http://localhost:8110/swagger-ui.html
```



# 2、srb-admin前端系统

```bash
#vscode打开 D:\VSCodeWorkSpace\srb-admin

cd D:\VSCodeWorkSpace\srb-admin
#安装NPM,运行一次就可以
npm install

google浏览器扩展程序，添加 Vue.jsDevtools.zip
```





# 3、启动系统

## 3.1、 启动MySQL、Nginx、Redis

输入 cmd
按快捷键 Ctrl + Shift + Enter 进入管理员模式

```shell
net start MySQL82
D:
cd D:\IdeaSpace\MyFactory\ShangRongBaoSystem\Server
copy /y  nginx-srb.conf D:\MyServer\Nginx-1.14\conf\nginx.conf
cd D:\MyServer\Nginx-1.14
start nginx.exe
cd D:\MyServer\redis
start redis-server.exe
curl http://localhost
mysql -uroot -p123456
```

http://localhost

## 3.2、 启动后端模块

```bash
#service-core模块
service-core/src/main/java/com/zmj/srb/core/ServiceCoreApplication.java
```



## 3.3、 启动前端srb-admin模块

```bash
cd D:\IdeaSpace\MyFactory\ShangRongBaoSystem\Front\srb-admin
# .env.development文件中配置nginx地址
VUE_APP_BASE_API = 'http://localhost'

# mock-server.js修改原来的配置路径
    //url: new RegExp(`${process.env.VUE_APP_BASE_API}${url}`),
    url: new RegExp(`/dev-api${url}`),

#根据package.json里面的"scripts":{"dev": "vue-cli-service serve"...
npm run dev
```



## 3.4、 启动前端srb-admin模块

```bash
cd D:\IdeaSpace\MyFactory\ShangRongBaoSystem\Front\nuxt-app
#根据package.json里面的"scripts":{"dev": "nuxt"...
npm run dev
```



## 3.5、 启动前端srb-site模块

```bash
npm run dev
```

