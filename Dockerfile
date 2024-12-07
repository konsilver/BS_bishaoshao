# 使用官方 Node.js 镜像作为基础镜像
FROM node:18

# 设置工作目录
WORKDIR /app

# 将 package.json 和 package-lock.json 复制到容器中
COPY package*.json ./

# 安装依赖
RUN npm install

# 复制前端源代码到容器中
COPY . .

# 构建前端应用
RUN npm run build

# 安装并启动 Vue 项目开发服务器
CMD ["npm", "run", "serve"]

# 映射容器端口到宿主机端口
EXPOSE 5173
