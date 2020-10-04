Spring+Mongodb 快速开发框架


1. 本地安装mongodb  将document下的json文件导入到db中

```
./mongoimport -h localhost -d pipic -c Auth --drop /Users/cuiyongxu/workspace/ijson/in-develop-boot/document/Auth.json
./mongoimport -h localhost -d pipic -c Config --drop /Users/cuiyongxu/workspace/ijson/in-develop-boot/document/Config.json
./mongoimport -h localhost -d pipic -c Role --drop /Users/cuiyongxu/workspace/ijson/in-develop-boot/document/Role.json
./mongoimport -h localhost -d pipic -c User --drop /Users/cuiyongxu/workspace/ijson/in-develop-boot/document/User.json
```


-h:localhost: 具体地址
-d:目标mongdb的库
-p:不填写默认27017
-u:mongodb的用户名,没设置可不填写
-p:mongodb的密码,没设置可不填写
-c:目标Collection,根Json文件名称一致
