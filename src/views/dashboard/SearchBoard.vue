<template>
  <div class="login">
    <!-- 登录页头部 -->
    <div class="login-header">
      <div class="left-section">
        <img src="@/assets/images/logo.jpg" alt="Logo" class="logo" />
        <span class="title">————您便捷的商品比价平台</span>
        <!-- 搜索栏 -->
        <div class="search-bar">
          <input type="text" placeholder="请输入您感兴趣的商品" />
          <button>搜一搜</button>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
     <!-- 分类导航 -->
  <ul>
    <li class="">
      <div class="hd" @mouseover="showSubCategories" @mouseleave="hideSubCategories">

        <shopping-cart class="small-icon" style="width: 25px; height: 25px;" /> 大家电、生活电器

        <i class="mmb-iconfont arr">></i>
      </div>
      <div class="bd" v-show="showCategories">
        <dl class="col-three">
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=4"><i class="classiconfont"></i>电视</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=5"><i class="classiconfont"></i>空调</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=6"><i class="classiconfont"></i>冰箱</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=7"><i class="classiconfont"></i>洗衣机</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=8"><i class="classiconfont"></i>热水器</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=26"><i class="classiconfont"></i>取暖器</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=1"><i class="classiconfont"></i>空调扇</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=2"><i class="classiconfont"></i>电风扇</a></dd>
          <dd><a href="https://zhekou.manmanbuy.com/DefaultSharelist.aspx?smallid=3"><i class="classiconfont"></i>饮水机</a></dd>
        </dl>
      </div>
    </li>
  </ul>

      <!-- 商品展示 -->
      <div class="product-display">
        <!-- 火爆降价商品 -->
        <div class="hot-deals">
          <h3>火爆降价商品</h3>
          <div class="product-list">
            <div class="product-card" v-for="i in 5" :key="'hot-' + i">
              <div class="image-placeholder"></div>
              <p class="product-name">商品 {{ i }}</p>
              <p class="product-price">¥99.99</p>
            </div>
          </div>
        </div>
        <!-- 您的订阅 -->
        <div class="subscriptions">
          <h3>您的订阅</h3>
          <div class="product-list">
            <div class="product-card" v-for="i in 5" :key="'sub-' + i">
              <div class="image-placeholder"></div>
              <p class="product-name">商品 {{ i }}</p>
              <p class="product-price">¥99.99</p>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>


<script>
import { ref } from 'vue'; 
import { ShoppingCart } from '@element-plus/icons-vue';


export default {
  setup() {
    
    // 定义变量来控制子分类的显示和隐藏
    const showCategories = ref(false);

    // 鼠标悬浮时显示子分类
    const showSubCategories = () => {
      showCategories.value = true;
    };

    // 鼠标离开时隐藏子分类
    const hideSubCategories = () => {
      showCategories.value = false;
    };

    // 点击主分类标签时触发的自定义脚本
    const handleCategoryClick = () => {
      console.log('主分类点击事件触发');
      // 这里可以执行自定义的脚本
    };

    // 返回响应式数据和方法
    return {
      showCategories,
      showSubCategories,
      hideSubCategories,
      handleCategoryClick,
      ShoppingCart
    };
  }
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
        font-size: 22px; /* 增加按钮的字体大小 */
        background-color: #e97a29;
        color: rgb(115, 32, 204);
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
      }

      button:hover {
        background-color: #d66a1f;
      }
    }

  

  /* 内容部分 */
.content {
  margin-top: 200px;
  padding: 20px;
}

.hd {
  display: flex;
  align-items: center;
  gap: 20px; /* 增大文字和 ">" 符号的距离 */
  background-color: rgb(61, 24, 31); /* 背景颜色 */
  color: rgb(235, 218, 218); /* 字体颜色 */
  padding: 20px; /* 增大内边距 */
  border-radius: 8px; /* 更明显的圆角效果 */
  width: 300px; /* 增加标签宽度 */
  font-size: 25px; /* 增大文字大小 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
}

.mmb-iconfont.arr {
  font-size: 22px; /* 增大箭头图标的大小 */
  cursor: pointer;
  transition: transform 0.3s ease;
  color: white; /* 保持箭头图标的字体颜色为白色 */
  margin-left: auto; /* 将箭头图标对齐到右侧 */
}

.hd.small-icon {
  color: #e97a29;
  font-size: 1px !important; /* 调整图标大小 */
}

.hd:hover .mmb-iconfont.arr {
  transform: rotate(90deg); /* 鼠标悬浮时箭头旋转 */
}

/* 子分类的样式 */
.bd {
  display: none;
  position: absolute;
  top: 40px;
  left: 0;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.bd a {
  display: block;
  padding: 8px 12px;
  color: #333;
  text-decoration: none;
  transition: background-color 0.3s;
}

.bd a:hover {
  background-color: #f0f0f0;
}

.col-three {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 每行三个 */
  gap: 10px;
  padding: 10px;
}

.col-three dd {
  margin: 0;
}

/* 主分类标签的悬浮效果 */
.hd:hover + .bd {
  display: block;
}


    .product-display {
      flex: 3;
      display: flex;
      flex-direction: column;
      gap: 20px;

      .hot-deals,
      .subscriptions {
        background: rgba(255, 255, 255, 0.9);
        padding: 20px;
        border-radius: 5px;

        h3 {
          margin-bottom: 10px;
        }

        .product-list {
          display: flex;
          gap: 10px;

          .product-card {
            flex: 1;
            max-width: 150px;
            text-align: center;

            .image-placeholder {
              width: 150px;
              height: 150px;
              background-color: #ccc;
              border-radius: 5px;
            }

            .product-name {
              font-size: 14px;
              margin: 5px 0;
            }

            .product-price {
              font-size: 14px;
              color: #e97a29;
            }
          }
        }
      }
    }
  
}
</style>
