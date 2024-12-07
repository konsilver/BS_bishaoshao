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

    <div class="index"><p class="key"><strong>你当前正在搜索：{{keyword}}</strong></p> </div>



      <div class="filter-area">
        <!-- 商品来源筛选 -->
        <div class="filter-source">
          <label>
            <input type="radio" name="source" value="全部" v-model="selectedSource" @change="applyFilters" /> 全部
          </label>
          <label>
            <input type="radio" name="source" value="京东" v-model="selectedSource" @change="applyFilters" /> 只看京东
          </label>
          <label>
            <input type="radio" name="source" value="淘宝" v-model="selectedSource" @change="applyFilters" /> 只看淘宝
          </label>
        </div>


        <!-- 时间筛选部分 -->
        <div class="filter-time">
          <label class="box">
            <input type="checkbox" v-model="recentFirst" @change="applyFilters" /> 看最近商品
          </label>
        </div>

        <!-- 价格筛选部分 -->
        <div class="filter-price">
          <div class="range">商品价格区间</div>
            <div class="price-range">
              <input type="number" placeholder="最低价" v-model.number="minPrice_unsure" />
              <span class="separator">~</span>
              <input type="number" placeholder="最高价" v-model.number="maxPrice_unsure" />
              <button @click="changeprice">确认</button>
            </div>
        </div>


      </div>

      <!-- 搜索结果展示 -->
        <div v-if="paginatedFilteredResults.length" class="search-results">
          <div v-for="result in paginatedFilteredResults" :key="result.id" class="result-item"  @click="setid(result.id)">
            <img v-if="result.image" :src="result.image" alt="商品图片" class="result-image" />
            <div v-else class="no-image">暂无图片</div>
              <h3 class="result-name">{{ result.name }}</h3>
              <p class="result-price">¥{{ result.price }}</p>
              <p class="result-date">{{ result.date }}</p>
              <p class="result-source"><strong>来源:</strong> {{ result.source }}</p>
          </div>
        </div>
        <p v-else class="non-result">{{waitornon}}</p>

         <!-- 分页 -->
        <div v-if="paginatedFilteredResults.length" class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
        <span class="page">第 {{ currentPage }} 页 / 共 {{ totalFilteredPages }} 页</span>
        <button @click="nextPage" :disabled="currentPage === totalFilteredPages">下一页</button>
        </div>

  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { ref,onMounted,computed } from 'vue';
import router from "../../router";
import SEARCHAPI from '../../api/search';
//import { ElMessage } from "element-plus";

export default {
setup() {
  const route = useRoute();
  const searchtext=ref('');
  const searchResults=ref([]);
  const currentPage = ref(1);
  const itemsPerPage = 5; // 每页显示的条目数
  const selectedSource = ref('全部'); // 默认筛选来源
  const recentFirst = ref(false); // 是否按时间排序
  const minPrice = ref(0); // 最低价格
  const maxPrice = ref(9999999); // 最高价格
  const minPrice_unsure=ref(0);
  const maxPrice_unsure=ref(0);
  const selectid = ref(0); //被点击的商品卡片的对应商品id，用于路由跳转
  const waitornon =ref('商品查询中，请稍等')

  let keyword = route.query.results;

  const sure = () => {
    if(searchtext.value!=''){
      keyword=searchtext.value;
      search();
    }
  };

  const back =() =>{
      router.push("/bishaoshao/user/searchboard");
  };

  // 每次刷新页面重新查询商品详情
  onMounted(() => {
    console.log(waitornon);
    SEARCHAPI.search({ "keyword": keyword })
      .then(response => {
        if (response.code === "00000") {
            searchResults.value = response.data.map(item => ({
              id: item.id,
              name: item.name,
              date: new Date(item.date).toLocaleDateString('zh-CN'),
              price: item.price,
              source: item.source,
              image: item.image,
            }));
        }
        else{        
        waitornon.value = '暂无相关商品，试着搜搜别的吧';}
        }
      )
      .catch(error => {

        console.log(error);
      });
  });


  const search = () => {
    router.push({
      name: 'SearchResults',
      query: { results: keyword },
    }).then(() => {
      // 跳转后刷新页面
      window.location.reload();
    });
  };


  const setid = (id) =>{
    selectid.value=id;
    lookin();
  }
  const lookin = () =>{
    router.push({
      name : 'Details',
      query: { id: selectid.value},
    })
  }

  const changeprice = () =>{
    maxPrice.value=maxPrice_unsure.value;
    minPrice.value=minPrice_unsure.value;
    applyFilters();
  }
  const filteredResults = computed(() => {
      let results = [...searchResults.value];

      // 按商品来源筛选
      if (selectedSource.value !== '全部') {
        results = results.filter((item) => item.source === selectedSource.value);
      }

      // 按时间排序
      if (recentFirst.value) {
        results = results.sort((a, b) => new Date(b.date) - new Date(a.date));
      }

      // 按价格区间筛选
      if (minPrice.value !== null) {
        results = results.filter((item) => item.price >= minPrice.value);
      }
      if (maxPrice.value !== null) {
        results = results.filter((item) => item.price <= maxPrice.value);
      }

      return results;
  });

  const applyFilters = () => {
      currentPage.value = 1; // 筛选后重置到第一页
  };

  const paginatedFilteredResults = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return filteredResults.value.slice(start, end);
  });

  const totalFilteredPages = computed(() => {
    return Math.ceil(filteredResults.value.length / itemsPerPage);
  });

  const nextPage = () => {
    if (currentPage.value < totalFilteredPages.value) {
      currentPage.value++;
    }
  };

  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value--;
    }
  };


  return {
    searchtext,
    sure,
    back,
    searchResults,
    currentPage,
    nextPage,
    prevPage,
    keyword,
    selectedSource,
    recentFirst,
    minPrice,
    maxPrice,
    totalFilteredPages,
    applyFilters,
    paginatedFilteredResults,
    setid,
    minPrice_unsure,
    maxPrice_unsure,
    changeprice,
    waitornon,
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
  height: 230vh;
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
 
.index {
  margin-top: 8%;
  font-size: 24px;
  color: #e97a29;
  width: 80%;
  position: absolute; /* 绝对定位 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  left: 17%; /* 距离左侧 20% */
}
.key{
  margin-left: 0%; 
  width: 100%;
}

.search-results {
  margin-top: 15%; 
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 70%; 
  margin-left: 25%; 
  margin-right: 5%; 
}

.result-item {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 100%; /* 确保不会溢出 */
  height: 225px;
  padding: 20px;
  margin: 15px 0;
  border: 1px solid #dddddd;
  border-radius: 8px;
  background-color: #bd1515;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  box-sizing: border-box; /* 包括边框和内边距 */
  background-color: rgb(61, 24, 31);
  cursor: pointer; /* 鼠标悬浮时显示为手形 */
  transition: transform 0.2s; /* 增加过渡效果 */
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
  .index{
    display: none !important;
  }
}



  .result-image {
    width: 18%; /* 放大图片宽度 */
    height: 100%; /* 放大图片高度 */
    object-fit: cover;
    border-radius: 8px;
    border: 1px solid #dddddd2a;
  }

  .result-name {
    margin-bottom: 12%;
    font-size: 22px; /* 放大字体 */
    font-weight: bold;
    color: rgb(235, 218, 218); /* 字体颜色 */
    font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
    width: 60%
  }

  .result-date{
    position: absolute;
    margin-top: 12%;
    margin-left: 15%;
    color: rgb(174, 128, 128);
    margin-right: 40%;
  }

  .result-price{
    position: absolute;
    margin-top: 2%;
    color: rgb(236, 0, 0);
    font-size: 35px;
    margin-left: 55%;
  }

  .result-source{
    position: absolute;
    margin-top: 12%;
    color: rgb(131, 55, 55);
    font-size: 20px;
    margin-left: 50%;
  }

  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 40px; /* 分页部分适当下移 */
    gap: 15px;

    button {
      padding: 15px 30px;
      font-size: 16px;
      background-color: #e97a29;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-weight: bold;

      &:hover {
        background-color: #d66a1f;
      }

      &:disabled {
        background-color: #ccc;
        cursor: not-allowed;
      }
    }

    span {
      font-size: 16px;
    }
  }
.page{
  color: #dddddd;
}


.filter-area {
  position: absolute;
  display: flex;
  flex-direction: column;
  gap: 10px;
  top: 30%;
  left: 5%;
  right: 80%;
  height: 80%;
}

.filter-source {
  margin-top: 25%;
  display: flex;
  flex-direction: column;
  gap: 25px; /* 增加竖向间距 */
  align-items: flex-start; /* 左对齐，确保勾选圆圈对齐 */
  color: #dddddd;
  border: 1px solid #dddddd;
  border-radius: 8px;
  background-color: #b93c3c;
  height: auto; /* 根据内容动态调整高度 */
  font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  font-size: 25px;
  padding: 20px; /* 增加内边距 */
}

/* 通用样式 */


.box {
  font-size: 25px;
  color: #dddddd;
  font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
}

.range {
  font-size: 25px;
  color: #f3ecec;
}

/* 时间筛选专用样式 */
.filter-time {
  background-color: #b93c3c;
  border: 1px solid #dddddd;
  border-radius: 8px;
  gap: 20px;
  padding: 20px;
  width: 15vw; /* 可根据需求调整宽度 */
  height: auto;
  margin-top: 20%; /* 控制整体的垂直间距 */
}

/* 价格筛选专用样式 */
.filter-price {
  background-color: #b93c3c;
  border: 1px solid #dddddd;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  width: 15vw; /* 可根据需求调整宽度 */
  height: auto;
  margin-top: 20%; /* 控制整体的垂直间距 */

  .price-range {
    display: grid;
    grid-template-columns: 1fr 20px 1fr; /* 设置输入框和符号宽度更合适的布局 */
    gap: 8px; /* 控制输入框、分隔符、按钮之间的间距 */
    align-items: center; /* 垂直居中对齐 */
    margin-top: 20%;
  }

  .price-range input {
    width: 80%; /* 输入框宽度填满其所在列 */
    max-width: 150px; /* 限制输入框的最大宽度 */
    padding: 8px; /* 适度增加内边距 */
    height: 70%;
    border-radius: 4px;
    border: 1px solid #dddddd;
    font-size: 16px; /* 增大字体 */
  }

  .separator {
    text-align: center;
    font-size: 30px; /* 调整符号的大小 */
    color: #090101;

  }

  .price-range button {
    grid-column: span 3; /* 按钮占据整行 */
    justify-self: right; /* 按钮居中对齐 */
    padding: 10px 20px;
    background-color: #d66a1f;
    color: #1b0202;
    font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
    font-size: 16px;
    border-radius: 4px;
    border: 1px solid #b93c3c;
    cursor: pointer;
    transition: background-color 0.3s;
    margin-top: 20%;
    &:hover {
      background-color: #b93c3c;
      color: #ffffff;
    }
  }
}

.non-result{
  font-size: 50px; /* 增大字体 */
  color: #e97a29;
  margin-top: 30%;
  margin-left: 20%;

}


@media (max-width: 768px) {
  /* 筛选区域放在搜索结果上方 */
  .filter-area {
    position: relative;
    top: 0;
    left: 0;
    right: 0;
    width: 100%;
    height: auto;
    margin-bottom: 20px; /* 给筛选区添加下边距 */
  }
  .filter-source{
    display: flex;
    flex-direction: row;
    width: 70%;
    height: 4% !important;
    margin-left: 10%;
    font-size: 13px;
    margin-top: 45%;
  }
  .filter-time{
   display: none !important;
  }
  .filter-price{
    display: none !important;
  }

  /* 商品搜索结果区的布局调整 */
  .search-results {
    margin-top: 0;
    margin-left: 5%;
    margin-right: 5%;
    width: 90%; /* 调整搜索结果区域宽度 */
  }

  /* 商品结果项样式 */
  .result-item {
  display: flex; /* 使用 flexbox 布局 */
  flex-direction: row; /* 水平排列 */
  width: 100%;
  height: 25vh;
  padding: 15px;
  margin: 15px 0;
}

.result-image {
  width: 50%; /* 图片占据卡片的左侧部分 */
  height: 80%; /* 高度与卡片一致 */
  object-fit: cover; /* 保持图片比例并裁剪 */
}

.result-name {
  font-size: 12px;
  margin-top: -7vh !important;
  display: -webkit-box; /* 使得元素成为多行布局 */
  -webkit-box-orient: vertical; /* 定义盒子的方向为纵向 */
  -webkit-line-clamp: 2; /* 限制最多显示两行 */
  overflow: hidden; /* 隐藏溢出的内容 */
  text-overflow: ellipsis; /* 使用省略号替代溢出部分 */
}

.result-price {
  font-size: 18px;
  margin-top: 6vh
}

.result-source {
  font-size: 12px;
  margin-top: 15vh
}
.result-date{
  font-size: 12px;
  margin-top: 22vh;
  margin-left: 60vw;
}


  .pagination {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    margin-top: 20px;
  }

  .pagination button {
    padding: 12px 15px;
    font-size: 14px;
    width: 80%; /* 按钮全宽 */
  }

  .pagination span {
    font-size: 14px;
  }

  .non-result {
    font-size: 30px; /* 调整字体大小 */
    margin-top: 30%;
    text-align: center;
  }

  .non-result{
  font-size: 30px; /* 增大字体 */
  color: #e97a29;
  margin-top: 30%;
  margin-left: 10% !important;

}
}

}

@media (max-width: 768px) {
  .login{
    height: 200vh;
  }

  .non-result{
    font-size: 20px !important;
  } 
}


</style>
