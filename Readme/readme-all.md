# 后端常用工具
## 安装VMWARE
### 安装软件 
- url : http://www.winwin7.com/soft/17946.html
### 下载ubuntu镜像
- url : https://ubuntu.com/download/server
- 常用账号密码 : chenwh | 123321
#### 设置管理员权限
```shell
sudo vi /etc/sudoers
```
#### 设置代理(可选)
```shell
echo "export http_proxy=http://[ip]:[port]" >> /etc/profile
```
### 安装SecureCrt(可选)
- url : https://www.bing.com/search?q=SecureCRTPortable+%E7%A0%B4%E8%A7%A3%E7%89%88&PC=U316&FORM=CHROMN
## 安装docker 
- url : https://www.runoob.com/docker/docker-tutorial.html
### 可选前置命令
```shell
sudo gpasswd -a $USER docker     #将登陆用户加入到docker用户组中
newgrp docker     #更新用户组
```
### docker 常用命令

```shell
docker images
docker ps [-a]
docker start [containerId]
# 自启动
docker update --restart=always [i=containerId] 
```
## 安装mysql
- url : https://www.runoob.com/docker/docker-install-mysql.html
### 个人步骤
```shell
sudo docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql
```
## 安装redis
- url : https://www.runoob.com/docker/docker-install-redis.html
## 安装nginx
- url : https://www.runoob.com/docker/docker-install-nginx.html


