<template>
    <div class="login">
      <!-- 登录页头部 -->
      <div class="login-header">
        <div class="left-section">
          <img src="@/assets/images/logo.jpg" alt="Logo" class="logo" />
          <span class="title">————您便捷的商品比价平台</span>
          <!-- 搜索栏 -->
          <div class="search-bar">
            <input type="text" placeholder="请输入您感兴趣的商品" v-model="searchtext" />
            <button @click="sure">搜一搜</button>
          </div>
          <div class="log-out">
            <button @click="back">
              返回首页 
            </button>
          </div>
        </div>
      </div>
      <div class="thing-info">
      <!-- 商品信息展示 -->
      <div class="thing-item">
        <div class="product-image-container">
          <img :src="thing_info.image" alt="商品图片" class="product-image" />
        </div>
        <div class="product-details">
          <h2>{{ thing_info.name }}</h2>
          <p class="source">{{ thing_info.source }}</p>
          <p class="price">今日价: ¥{{ thing_info.price }}</p>

          <!-- 按钮容器 -->
          <div class="button-container">
            <button @click="goToUrl(thing_info.url)" class="direct-link-btn">直达链接</button>
            <button @click="toggleSubscription" class="subscribe-btn" title="订阅该商品，您可以及时收到相关推送">
              {{ isSubscribed ? '已订阅' : '订阅商品' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 历史价格曲线 -->
      <div v-if="historyprice.length > 0" class="price-history">
        <h3>历史价格</h3>
        <line-chart :data="chartData"></line-chart>
      </div>

      <!-- 商品日期 -->
      <p class="product-date">{{ thing_info.date }}</p>

      <!-- 显示 params 字段 -->
      <div v-if="thing_info.params" class="params-table">
        <div class="params-table-title">商品规格</div>
        <table>
          <tbody>
            <tr v-for="(row, index) in limitedParams" :key="index">
              <td v-for="(value, key) in row" :key="key" class="param-cell" :title="`${key}: ${value}`">
                {{ key }}: {{ value }}
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 如果有更多参数，显示提醒 -->
        <p v-if="hasMoreParams" class="more-params">
          更多参数信息请查看商品官网...
        </p>
      </div>

      <!-- 如果 params 为空，显示暂无商品参数信息 -->
      <div v-else class="no-params-info">
        暂无商品参数信息
      </div>
    </div>

    </div>
</template>


<script>
import { useRoute } from "vue-router";
import { ref,onMounted,computed } from 'vue';
import router from "../../router";
import SEARCHAPI from '../../api/search';
import SUBSCRIBEAPI from '../../api/subscribe';
import { ElMessage } from "element-plus";
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale } from 'chart.js';

// 注册所需的 Chart.js 模块
ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale);



export default {

components: {
  LineChart: Line
},
setup() {
  const route = useRoute();
  const searchtext=ref('');
  const thing_info = ref({});
  const historyprice=ref([]);
  const chartData = ref({
      labels: [],
      datasets: []
  });

  let id = route.query.id;
  let keyword="";
  const isSubscribed = ref(false);

  const toggleSubscription = () =>{
    isSubscribed.value=!isSubscribed.value;
    if(isSubscribed.value==true) SUBSCRIBEAPI.add({"thing_id":id});
    else SUBSCRIBEAPI.sub({"thing_id":id});
  }

  const sure = () => {
    if(searchtext.value!=''){
      keyword=searchtext.value;
      search();
    }
  };

  const goToUrl = (url) => {
    window.open(url, '_blank');
  };

  const back =() =>{
      router.push("/bishaoshao/user/searchboard");
  };

  //每次刷新页面进行搜索
  onMounted(() => {
    SEARCHAPI.getdetail({"id":id})
    .then(response =>{
      if (response.code === '00000') {
        // 将 thing_info 直接设置为对象
        thing_info.value = {
          id: response.data.id,
          name: response.data.name,
          date: new Date(response.data.date).toLocaleDateString('zh-CN'), // 转换日期格式
          price: response.data.price,
          source: response.data.source,
          image: response.data.image,
          url: response.data.url,
          params: response.data.params // 处理商品参数信息
        };
        SEARCHAPI.gethistory({"id":id})
        .then(response =>{
          if (response.code === '00000') {
            historyprice.value=response.data;
            updateChartData();
          }else {
            ElMessage.error(response.msg);
            return;
          }
        })
        SUBSCRIBEAPI.isornot({"id":id})
        .then(response =>{
          if (response.code === '00000') {
            isSubscribed.value=response.data;
          }else {
            ElMessage.error(response.msg);
            return;
          }
        })
      } else {
        ElMessage.error(response.msg);
        return;
        }
      })
      .catch(error => {
        console.log(error);
      })
  });

  const search = () => {
    router.push({
      name: 'SearchResults', 
      query: { results: keyword},
    });
  };

  const limitedParams = computed(() => {
    const paramEntries = Object.entries(thing_info.value.params || {});
    const limitedEntries = paramEntries.slice(0, 16); // 截取前16个参数
    const rows = [];
    for (let i = 0; i < limitedEntries.length; i += 2) {
      const row = {};
      if (limitedEntries[i]) row[limitedEntries[i][0]] = limitedEntries[i][1];
      if (limitedEntries[i + 1]) row[limitedEntries[i + 1][0]] = limitedEntries[i + 1][1];
      rows.push(row);
    }
    return rows;
  });

  const hasMoreParams = computed(() => Object.entries(thing_info.value.params || {}).length > 16);

  const updateChartData = () => {
      const labels = historyprice.value.map(item => item.date);
      const data = historyprice.value.map(item => item.price);

      chartData.value = {
        labels: labels,
        datasets: [
          {
            label: '历史价格',
            data: data,
            borderColor: 'rgba(75, 192, 192, 1)',
            fill: false
          }
        ]
      };
  };

  return {
    searchtext,
    sure,
    back,
    keyword,
    thing_info,
    goToUrl,
    limitedParams,
    hasMoreParams,
    historyprice,
    chartData,
    toggleSubscription,
    isSubscribed,
  };
},
};
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 320vh;
  background: url("@/assets/images/login-background-dark.jpg") no-repeat center center;
  background-size: cover;

  .login-header {
    position: absolute;
    top: 2%; /* 改为相对视口高度，确保在不同屏幕上有良好的间距 */
    left: 2%; /* 改为相对视口宽度，确保与屏幕边缘有一定间距 */
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    gap: 1rem; /* 使用 rem 单位，确保布局的相对性 */
    width: 100vw;
    height: 10vh;


    .logo {
      width: 8%; /* 使用 vw 单位，根据视口宽度调整 logo 的大小 */
      height: auto;
    }

    .title {
      font-size: 1.5rem; /* 使用 rem 单位，使字体大小与根字体大小挂钩 */
      font-weight: bold;
      color: #E97A29;
      margin-top: 2rem; /* 使用 rem 单位，确保标题与上方的间距适中 */
      font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
    }
    .search-bar {
      margin-top: -3rem;
      display: flex;
      gap: 1rem; /* 增加输入框和按钮之间的间距 */
      margin-left: 35vw; /* 调整居左的距离 */
      width: 60%; /* 增加搜索栏的宽度，使用 vw 单位，适应屏幕宽度 */
      height: 20%;

      input {
        padding: 10px 15px; /* 减少上下内边距，使输入框更扁长 */
        font-size: 16px; /* 减小字体大小，使其适应扁长布局 */
        width: 50%; /* 增加输入框的宽度，使其占满容器的主要部分 */
        border: 2px solid #e97a29;
        border-radius: 5px;
      }

      button {
        padding: 10px 25px; /* 减少按钮的上下内边距，使其更扁长 */
        font-size: 20px; /* 调整按钮的字体大小 */
        background-color: #e97a29;
        color: rgb(115, 32, 204);
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
        font-weight: bold;
      }

      button:hover {
        background-color: #d66a1f;
      }
    }
    .log-out{
      display: flex;
      margin-left: 85vw; 
      margin-top: -3rem;
      gap: 0px;
      button {
        padding: 15px 30px; /* 增加内边距，使按钮更大 */
        font-size: 20px; /* 增加按钮的字体大小 */
        background-color: #e97a29;
        color: rgb(7, 1, 15);
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
        font-weight: bold; /* 加粗字体 */
      }

      button:hover {
        background-color: #d66a1f;
      }
    }

  }
  @media (max-width: 768px) {
  .login-header {
    top: 7%; /* 缩小顶部间距，使其适配小屏幕 */
    left: 5%; /* 缩小左边距 */
    align-items: center; /* 在手机上居中显示内容 */

    .logo {
      width: 20vw; /* 根据手机屏幕宽度调整 logo 大小 */
    }

    .title {
      font-size: 1.2rem !important; /* 在手机上减小标题字体 */
      text-align: center; /* 标题居中 */
      margin-top: 0.5rem; /* 减小标题上方的间距 */
    }
  }

  .search-bar {
    margin-top: 2rem !important; /* 增加一些间距，避免与其他元素过于紧凑 */
    margin-left: 5vw !important;

    align-items: center; /* 让输入框和按钮居中 */
    width: 100vw !important;
    height: 3vh !important; 

    input {
      padding: 10px 15px; /* 增加内边距，提升触摸体验 */
      font-size: 14px; /* 调整字体大小，适应手机端 */
      width: 50% !important; /* 输入框占据大部分宽度 */
      height: 80% !important; /* 输入框高度占满容器的全部高度 */
      border: 2px solid #e97a29;
      border-radius: 5px;
      margin-right: 0.1rem !important; /* 增加输入框与按钮之间的间距 */
    }

    button {
      padding: 2px 4px !important; /* 增加内边距，适应手机端按键大小 */
      font-size: 16px !important; /* 调整按钮字体大小，确保适配手机端 */
      width: 20% !important; /* 按钮宽度占据容器的 15% */
      height: 100% !important; /* 按钮高度占满容器的全部高度 */
      background-color: #e97a29;
      color: rgb(115, 32, 204);
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
      font-weight: bold;
    }

    button:hover {
      background-color: #d66a1f;
    }
  }

  .log-out {
    display: none !important;
  }
}

.thing-info {
  display: flex;
  flex-direction: column;
  margin-top: 10%;
  color: white;
  background-color: #2c3e50;
  padding: 40px;
  width: 75%;
  height: 85%;
  margin-left: auto;
  margin-right: auto;
  border-radius: 10px;
  position: relative;
}

.product-date {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 14px;
  color: #b0bec5;
  opacity: 0.8;
}

.thing-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 50px;
}

.product-image-container {
  width: 1600px;            /* 设置一个固定宽度 */
  height: 600px;           /* 设置一个固定高度 */
  overflow: hidden;        /* 确保超出部分被裁剪 */
  border-radius: 10px;     /* 圆角效果 */
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
}

.product-image {
  width: 100%;
  height: 100%;            /* 强制图片填满容器 */
  object-fit: cover;       /* 保持图片比例并填充整个容器 */
  border-radius: 10px;     /* 圆角效果 */
}

.product-details {
  flex-grow: 1;
  margin-left: 30px;
}

.product-details h2 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 20px;
}

.source {
  margin-left: 45%;
  font-size: 22px;
  display: inline-block;
  color: rgb(180, 76, 76);
}

.price {
  font-size: 40px;
  color: red;
  margin-top: 20px;
}

.button-container {
  display: flex;
  gap: 20px; /* 按钮之间的间距 */
  margin-top: 15vh;
  margin-left: 10vw;
}

.direct-link-btn,
.subscribe-btn {
  font-size: 24px;
  background-color: #e97a29;
  color: rgb(7, 1, 15);
  border: none;
  padding: 15px 20px;
  border-radius: 10px;
  cursor: pointer;
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  font-weight: bold;
  width: auto;
  height: auto;
  text-align: center;
}

.direct-link-btn:hover,
.subscribe-btn:hover {
  background-color: #d66a21;
  transform: scale(1.05);
}

.subscribe-btn {
  background-color: #2980b9;
}

.subscribe-btn:hover {
  background-color: #3498db;
}

.subscribe-btn:active {
  background-color: #1c5980;
}

.subscribe-button {
  position: absolute;
  left: 10%;
  margin-top: 50%;
}


.params-table table {
  width: 80%; /* 使表格宽度不超过 80% */
  margin: 5% auto; /* 居中显示 */
  border-collapse: collapse;
  table-layout: fixed; /* 强制使用固定布局 */
}



.params-table td {
  padding: 15px; /* 增加单元格的高度 */
  border: 2px solid #4CAF50; /* 增加边框 */
  text-align: center;
  white-space: nowrap; /* 禁止换行 */
  overflow: hidden;  /* 隐藏超出部分 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */
  background-color: #34495e; /* 淡灰色背景，更有对比度 */
  color: white; /* 白色字体 */
  font-size: 16px; /* 适当增大字体 */
  border-radius: 5px; /* 边框圆角 */
  max-width: 150px; /* 设置最大宽度，避免单元格被拉伸 */
  min-width: 100px; /* 设置最小宽度，保持单元格足够宽 */
}



.param-cell {
  cursor: pointer;
}

/* 悬浮效果：显示完整内容 */
.param-cell:hover {
  background-color: #e0e0e0; /* 悬浮时的背景颜色 */
  color: #000; /* 悬浮时字体颜色 */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2); /* 增加阴影效果 */
}

/* 显示完整内容的提示（仅显示完整的参数内容） */
.param-cell[title]:hover::after {
  content: attr(title); /* 显示完整的参数内容 */
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 14px;
  white-space: normal;
  z-index: 10;
}

/* 商品规格标题样式 */
.params-table-title {
  font-size: 40px; /* 增大字体 */
  font-weight: bold; /* 加粗字体 */
  color: white; /* 白色字体 */
  text-align: left;
  margin-top: 60%;
  margin-left: 5%;
}

/* 调整“更多参数信息...”的位置和字体大小 */
.more-params {
  font-size: 18px; /* 增大字体 */
  color: white; /* 白色字体 */
  text-align: right; /* 右对齐 */
  position: relative; /* 使用相对定位 */
  bottom: -10px; /* 向上移动10px，使其更靠近表格 */
  font-weight: bold;
}

.no-params-info{
  font-size: 50px; /* 增大字体 */
  color: #e97a29;
  margin-top: 120vh;
  text-align: center

}

.price-history {
  margin-top: 55%;  /* 控制历史价格曲线的位置 */
  width: 76%;        /* 图表宽度为 thing-info 宽度的一半 */
  height: 26%;       /* 图表高度与宽度相似 */
  padding: 20px;     /* 为图表添加内边距 */
  background-color: #34495e; /* 为图表背景添加颜色 */
  border-radius: 10px;  /* 圆角 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  position: absolute;
  left: 12%;         /* 图表的左侧距离 thing-info 左侧 45% */
  right: 12%;         /* 图表的右侧距离 thing-info 右侧 5% */
}

.price-history h3 {
  font-size: 18px;
  margin-top: 10px;
}

.line-chart {
  width: 100%;       /* 图表占满容器的宽度 */
  height: 100%;      /* 图表高度占满容器的高度 */
  max-width: 100%;   /* 确保图表不超过容器宽度 */
}


@media (max-width: 768px) {

.thing-info {
  width: 85%;
  height: auto;
  padding: 20px;
  margin-top: 50%;
  background-color: #2c3e50;
  border-radius: 10px;
  position: relative;
  margin-left: 7% !important;
  margin-right: 7% !important;
}

.product-date {
  display: none !important;
}

.thing-item {
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.product-image-container {
  width: 100%;
  height: 250px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
}

.product-details {
  flex-grow: 1;
  margin-left: 0;
  text-align: center;
}

.product-details h2 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.source {
  font-size: 18px;
  color: rgb(180, 76, 76);
  margin-bottom: 10px;
  margin-left: 5%;
}

.price {
  font-size: 30px;
  color: red;
  margin-top: 10px;
}

.button-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
}

.direct-link-btn,
.subscribe-btn {
  font-size: 18px;
  padding: 10px 15px;
  width: 70%;
  margin-left: 15%;
}

.params-table {
  margin-top: 20px;
}

.params-table table {
  width: 100%;
  margin-top: 20px;
}

.params-table td {
  padding: 10px;
  border: 2px solid #4CAF50;
  font-size: 14px;
}

.params-table-title {
  font-size: 24px;
  margin-top: 20px;
}

.price-history {
  width: 100%;
  margin-top: 30px;
  padding: 10px;
  background-color: #34495e;
  border-radius: 10px;
  position: relative;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-left: -15%;
}

.line-chart {
  width: 100%;
  height: 200px;
}

.no-params-info {
  font-size: 20px;
  color: #e97a29;
  margin-top: 40px;
  text-align: center;
}

.more-params {
  font-size: 16px;
  text-align: right;
  margin-top: 10px;
}
}




}

@media (max-width: 768px) {
  .login{
    height: 210vh;
  }


}

</style>
