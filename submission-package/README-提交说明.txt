项目名称：
住房公积金管理系统——筹集子系统

提交内容：
1. 01-program：项目源码
2. 02-database：数据库脚本
3. 03-report：课程设计报告

技术栈：
Java 17、Maven、Spring MVC、Spring IoC、MyBatis、MySQL、JSP/JSTL、Tomcat 9

运行方式简述：
1. 安装 Java 17、Maven、MySQL、Tomcat 9；
2. 按 02-database/数据库导入说明.txt 导入数据库；
3. 复制 src/main/resources/jdbc.example.properties 为 src/main/resources/jdbc-local.properties；
4. 修改 jdbc-local.properties 中的 MySQL 用户名和密码；
5. 在项目根目录执行 mvn clean package；
6. 将 target/housingfund-collection.war 部署到 Tomcat 9；
7. 访问 http://localhost:8080/housingfund-collection/

注意事项：
1. 本项目是传统 Maven SSM Web 项目，不是 Spring Boot；
2. 建议使用 Tomcat 9，不要使用 Tomcat 10；
3. 提交包中不包含本机数据库密码；
4. jdbc-local.properties 需要运行者按自己电脑环境创建；
5. 数据库名称为 housingfund_collection。
