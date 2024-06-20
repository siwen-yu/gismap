# 使用 Maven 和 Java 8 作为基础镜像
FROM maven:3.8.4-openjdk-8 AS builder

MAINTAINER yusiwen

# 创建工作目录
WORKDIR /app

# 复制整个项目到工作目录
COPY . .

# 编译和打包 Spring Boot 应用程序
RUN mvn package -DskipTests

# 使用 OpenJDK 8 作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将构建好的 Spring Boot JAR 文件复制到工作目录
COPY --from=builder /app/target/*.jar .

# 定义启动命令
ENTRYPOINT ["java", "-jar", "backend-0.0.1.jar"]


