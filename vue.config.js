const { defineConfig } = require('@vue/cli-service');
const path = require('path');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: 'localhost', // 确保开发服务器监听正确的 IP
    port: 5173,
    client: {
      webSocketURL: 'ws://localhost:5173/ws', // 明确指定 WebSocket 地址
    },
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
      },
    },
  },
});
