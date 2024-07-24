# 使用 Node.js 16 作为基础镜像
FROM node:16 AS built

# 创建工作目录
WORKDIR /app

# 将当前目录复制到工作目录
COPY . .

# 安装依赖
RUN npm install --registry https://registry.npmmirror.com

# 构建前端应用
RUN npm run build

# 新建一个基于 Nginx 的镜像
FROM nginx:stable-alpine

# 将构建好的前端文件复制到 Nginx 的默认静态文件目录
COPY --from=built /app/dist /etc/nginx/html
# 更新配置文件
COPY nginx.conf /etc/nginx/nginx.conf

# 暴露 Nginx 默认端口（如果需要）
EXPOSE 80

# Nginx 已经在镜像启动时自动运行，不需要额外的 CMD 或 ENTRYPOINT
CMD ["nginx", "-g", "daemon off;"]