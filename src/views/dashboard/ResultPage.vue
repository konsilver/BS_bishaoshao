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
              退出登录 
            </button>
          </div>
        </div>
      </div>





    </div>
</template>

<script>
import { useRoute } from "vue-router";
import { ref } from 'vue';
import router from "../../router";
import SEARCHAPI from '../../api/search';
import { ElMessage } from "element-plus";

export default {
  setup() {
    const route = useRoute();
    const searchtext=ref('');
    const keyword=ref('');

    let searchResults = route.query.results;



    const sure = () => {
      keyword.value=searchtext.value;
      search();
    };

    const back =() =>{
        console.log(searchResults); 
        console.log("sorry");
    };

    const search = () => {
      SEARCHAPI.search({"keyword":keyword.value})
      .then(response =>{
        if (response.code === "00000") {
          searchResults.value = response.data.map(item => ({
            id: item.id,
            name: item.name,
            Date: item.Date,
            price: item.price,
            source: item.source,
          }))
          router.push({
            name: 'SearchResults', 
            query: { keyword: searchtext.value },
            state: { results: searchResults.value }
          });
        } else {
          ElMessage.error(response.msg);
          return;
          }
        })
        .catch(error => {
          console.log(error);
        })
    };

    return {
      searchtext,
      sure,
      back,
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
  
}
</style>
