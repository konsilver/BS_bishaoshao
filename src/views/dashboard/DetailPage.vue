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
          <img :src="thing_info.image" alt="商品图片" class="product-image" />
          <div class="product-details">
            <h2>{{ thing_info.name }}</h2>
            <p class="source">{{ thing_info.source }}</p>
            <button @click="goToUrl(thing_info.url)" class="direct-link-btn">直达链接</button>
            <p class="price">今日价: ¥{{ thing_info.price }}</p>
          </div>
        </div>

        <!-- 订阅按钮 -->
        <div class="subscribe-button">
          <button @click="toggleSubscription" title="订阅该商品，您可以及时收到相关推送">
            {{ isSubscribed ? '已订阅' : '订阅商品' }}
          </button>
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
          <div class="params-table-title">
            商品规格
          </div>
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
  height: 200vh;
  background: url("@/assets/images/login-background-dark.jpg") no-repeat center center;
  background-size: cover;

  .login-header {
    position: absolute;
    top: 20px;
    left: 20px;
    display: flex;
    flex-direction: row; /* 修改为横向排列 */
    align-items: center;
    gap: 200px; /* logo和搜索栏之间的间距 */
  }

    .logo {
      width: 200px;
    }

    .title {
      font-size: 35px;
      font-weight: bold;
      color: #e97a29;
      margin-top: 20px;
      font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
    }

    .search-bar {
      margin-top: -70px;
      display: flex;
      gap: 0px; /* 增加输入框和按钮之间的间距 */
      margin-left: 800px; 

      input {
        padding: 15px; /* 增加内边距，使输入框更大 */
        font-size: 18px; /* 增加字体大小 */
        width: 300px; /* 增加输入框的宽度 */
        border: 2px solid #e97a29;
        border-radius: 5px;
        width: 600px;
      }

      button {
        padding: 15px 30px; /* 增加内边距，使按钮更大 */
        font-size: 25px; /* 增加按钮的字体大小 */
        background-color: #e97a29;
        color: rgb(115, 32, 204);
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
    .log-out{
      margin-top: -70px;
      display: flex;
      margin-left: 1800px; 
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

.thing-info {
  display: flex;
  flex-direction: column;
  margin-top: 10%;
  color: white;
  background-color: #2c3e50;
  padding: 40px;
  width: 75%;
  height: 80%;
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

.product-image {
  width: 55%;
  height: auto;
  object-fit: cover;
  border-radius: 10px;
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

.direct-link-btn {
  margin-left: 30px;
  font-size: 24px; /* 增大字体大小 */
  background-color: #e97a29;
  color: rgb(7, 1, 15);
  border: none;
  padding: 15px 20px; /* 增大按钮的内边距 */
  border-radius: 10px; /* 更大的圆角，按钮显得更大气 */
  cursor: pointer;
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  font-weight: bold;
  width: auto; /* 确保宽度随内容调整 */
  height: auto; /* 高度根据内容自适应 */
  text-align: center; /* 居中文本 */
}

.direct-link-btn:hover {
  background-color: #d66a21; /* 鼠标悬浮时略微变深的颜色 */
  transform: scale(1.05); /* 增加交互感，悬浮时放大 */
}

.price {
  font-size: 40px;
  color: red;
  margin-top: -10px;
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
  margin-top: 20%;
  margin-bottom: 5%; /* 缩小底部间距，原本是 10%，这里改为 20px */
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
  margin-top: 30%;
  text-align: center

}

.price-history {
  margin-top: 24%;  /* 控制历史价格曲线的位置 */
  width: 50%;        /* 图表宽度为 thing-info 宽度的一半 */
  height: 30%;       /* 图表高度与宽度相似 */
  padding: 20px;     /* 为图表添加内边距 */
  background-color: #34495e; /* 为图表背景添加颜色 */
  border-radius: 10px;  /* 圆角 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  position: absolute;
  left: 45%;         /* 图表的左侧距离 thing-info 左侧 45% */
  right: 5%;         /* 图表的右侧距离 thing-info 右侧 5% */
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

.subscribe-button {
  position: absolute;  /* 使用绝对定位 */
  left: 10%;  /* 距离 .thing-info 左侧 10% */
  margin-top: 50%;  /* 按钮垂直居中，距上方的距离 */
}

.subscribe-button button {
  font-size: 24px; /* 增大字体大小 */
  background-color: #e97a29;
  color: #050000;  /* 字体颜色白色 */
  border: none;
  padding: 15px 30px;  /* 增大按钮的内边距 */
  border-radius: 8px;  /* 圆角增大 */
  cursor: pointer;
  width: 200px;  /* 设置固定宽度 */
  height: 50px;  /* 设置固定高度 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);  /* 增加阴影效果 */
  transition: all 0.3s ease;  /* 添加过渡效果 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
}

.subscribe-button button:hover {
  background-color: #2980b9;
  transform: scale(1.05);  /* 鼠标悬浮时轻微放大 */
}

.subscribe-button button:active {
  background-color: #1c5980;
}




}


</style>
