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
        <p v-else class="non-result">{{waitornon.value}}</p>

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

  //每次刷新页面重新查询商品详情
  onMounted(() => {
    SEARCHAPI.search({"keyword":keyword})
    .then(response =>{
      if (response.code === "00000") {
        searchResults.value = response.data.map(item => ({
          id: item.id,
          name: item.name,
          date: new Date(item.date).toLocaleDateString('zh-CN'), 
          price: item.price,
          source: item.source,
          image: item.image,
        }))
      }
      else {
        waitornon.value='暂无相关商品，试着搜搜别的吧';
      }
      })
      .catch(error => {
        console.log(error);
      })
    
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
    waitornon
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
  height: 170vh;
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
.index {
  margin-top: 10%;
  font-size: 30px;
  color: #e97a29;
  width: 80%;
  position: absolute; /* 绝对定位 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  left: 20%; /* 距离左侧 20% */
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
  width: 60%; 
  margin-left: 25%; 
  margin-right: 15%; 
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
    margin-top: 9%;
    margin-left: 12%;
    color: rgb(174, 128, 128);
    margin-right: 40%;
  }

  .result-price{
    position: absolute;
    margin-top: 2%;
    color: rgb(236, 0, 0);
    font-size: 35px;
    margin-left: 52%;
  }

  .result-source{
    position: absolute;
    margin-top: 9%;
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
  height: 100%;
}

.filter-source {
  margin-top: 15%;
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
  font-size: 30px;
  padding: 20px; /* 增加内边距 */
}

/* 通用样式 */
.filter-section {
  background-color: #b93c3c;
  border: 1px solid #dddddd;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  width: 300px; /* 可根据需求调整宽度 */
  margin-top: 20%; /* 控制整体的垂直间距 */
}

.box {
  font-size: 30px;
  color: #dddddd;
  font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
}

.range {
  font-size: 24px;
  color: #090101;
}

/* 时间筛选专用样式 */
.filter-time {
  @extend .filter-section;
  /* 特殊样式在此添加 */
}

/* 价格筛选专用样式 */
.filter-price {
  @extend .filter-section;

  .price-range {
    display: grid;
    grid-template-columns: 1fr 20px 1fr; /* 设置输入框和符号宽度更合适的布局 */
    gap: 10px; /* 控制输入框、分隔符、按钮之间的间距 */
    align-items: center; /* 垂直居中对齐 */
    margin-top: 20%;
  }

  .price-range input {
    width: 80%; /* 输入框宽度填满其所在列 */
    max-width: 150px; /* 限制输入框的最大宽度 */
    padding: 8px; /* 适度增加内边距 */
    height: 80%;
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

}


</style>
