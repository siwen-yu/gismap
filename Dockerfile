# 使用官方的 OpenJDK 8 镜像作为基础
FROM openjdk:8-jdk

MAINTAINER yusiwen

# 设置环境变量，以便 Maven 和 Node.js 使用
ENV MAVEN_VERSION=3.8.4
ENV NODE_VERSION=16

# 安装 Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 安装 Node.js
RUN curl -sL https://deb.nodesource.com/setup_${NODE_VERSION}.x | bash -
RUN apt-get install -y nodejs

# 安装 Nginx
RUN apt-get update && \
    apt-get install -y nginx && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 创建工作目录
WORKDIR /app

# 复制并构建后端代码
COPY backend /app/backend
WORKDIR /app/backend
RUN mvn package

# 复制并构建前端代码
WORKDIR /app
COPY frontend /app/frontend
WORKDIR /app/frontend
RUN npm install
RUN npm run build

# 配置 Nginx
RUN rm -rf /etc/nginx/nginx.conf
COPY frontend/deploy/nginx.conf /etc/nginx/nginx.conf

# 暴露端口
EXPOSE 80

# 启动后端服务
CMD ["java", "-jar", "/app/backend/target/backend-0.0.1.jar"]
# 启动 Nginx
#CMD ["nginx", "-g", "daemon off;"]


