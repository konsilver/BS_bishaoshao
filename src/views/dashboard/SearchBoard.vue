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
        <div class="log-out">
          <button @click="logout">
            退出登录 
          </button>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
     <!-- 分类导航 -->
      <ul>
        <li class="menu-item" >
          <div class="hd"     >
            <Monitor  style="width: 25px; height: 25px;" /> 电脑 / 配件 / 文具
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>笔记本电脑</a></dd>
              <dd><a>平板电脑</a></dd>
              <dd><a>CPU</a></dd>
              <dd><a>显卡</a></dd>
              <dd><a>硬盘</a></dd>
              <dd><a>键盘</a></dd>
              <dd><a>鼠标</a></dd>
              <dd><a>主板</a></dd>
              <dd><a>内存条</a></dd>
              <dd><a>耳机</a></dd>
              <dd><a>U盘</a></dd>
              <dd><a>显示器</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <Cpu  style="width: 25px; height: 25px;" /> 家电 / 手机 / 数码
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>电视</a></dd>
              <dd><a>空调</a></dd>
              <dd><a>冰箱</a></dd>
              <dd><a>洗衣机</a></dd>
              <dd><a>热水器</a></dd>
              <dd><a>电风扇</a></dd>
              <dd><a>手机</a></dd>
              <dd><a>智能手表</a></dd>
              <dd><a>蓝牙耳机</a></dd>
              <dd><a>饮水机</a></dd>
              <dd><a>电风扇</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <House  style="width: 25px; height: 25px;" /> 家具 / 家装 / 厨具
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>油烟机</a></dd>
              <dd><a>消毒柜</a></dd>
              <dd><a>豆浆机</a></dd>
              <dd><a>电饭煲</a></dd>
              <dd><a>电烤箱</a></dd>
              <dd><a>微波炉</a></dd>
              <dd><a>洗碗机</a></dd>
              <dd><a>咖啡机</a></dd>
              <dd><a>面包机</a></dd>
              <dd><a>电水壶</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <User  style="width: 25px; height: 25px;" /> 女装 / 男装 / 内衣
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>连衣裙</a></dd>
              <dd><a>T恤</a></dd>
              <dd><a>衬衫</a></dd>
              <dd><a>羽绒服</a></dd>
              <dd><a>毛衣</a></dd>
              <dd><a>内裤</a></dd>
              <dd><a>卫衣</a></dd>
              <dd><a>西装</a></dd>
              <dd><a>短裤</a></dd>
              <dd><a>牛仔外套</a></dd>
              <dd><a>马甲</a></dd>
              <dd><a>风衣</a></dd>
              <dd><a>休闲裤</a></dd>
              <dd><a>丝袜裤袜</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <ShoppingBag  style="width: 25px; height: 25px;" /> 女鞋 / 男鞋 / 户外
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>高跟鞋</a></dd>
              <dd><a>平底鞋</a></dd>
              <dd><a>跑步鞋</a></dd>
              <dd><a>马丁靴</a></dd>
              <dd><a>休闲皮鞋</a></dd>
              <dd><a>篮球鞋</a></dd>
              <dd><a>商务靴</a></dd>
              <dd><a>正装靴</a></dd>
              <dd><a>泳衣</a></dd>
              <dd><a>户外背包</a></dd>
              <dd><a>钓鱼</a></dd>
              <dd><a>健身车</a></dd>
              <dd><a>球类</a></dd>
              <dd><a>望远镜</a></dd>
              <dd><a>帐篷</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <Coin  style="width: 25px; height: 25px;" /> 珠宝 / 钟表 / 箱包
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>钱包</a></dd>
              <dd><a>书画</a></dd>
              <dd><a>项链/吊坠</a></dd>
              <dd><a>手饰</a></dd>
              <dd><a>手镯</a></dd>
              <dd><a>手提包</a></dd>
              <dd><a>双肩包</a></dd>
              <dd><a>行李箱</a></dd>
              <dd><a>路易威斯</a></dd>
              <dd><a>圣罗兰</a></dd>
              <dd><a>香奈儿</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <Food  style="width: 25px; height: 25px;" /> 食品 / 生鲜 / 酒类
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>牛奶</a></dd>
              <dd><a>茶</a></dd>
              <dd><a>国产水果</a></dd>
              <dd><a>白酒</a></dd>
              <dd><a>葡挞酒</a></dd>
              <dd><a>药品</a></dd>
              <dd><a>葱姜类</a></dd>
              <dd><a>行李箱</a></dd>
              <dd><a>奶粉</a></dd>
              <dd><a>生日蛋糕</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <Present  style="width: 25px; height: 25px;" /> 母婴 / 童装 / 宠物
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>孕妇装</a></dd>
              <dd><a>童衣</a></dd>
              <dd><a>安全座椅</a></dd>
              <dd><a>婴儿推车</a></dd>
              <dd><a>益智积木</a></dd>
              <dd><a>猫粮狗粮</a></dd>
              <dd><a>宠物玩具</a></dd>
              <dd><a>童装</a></dd>
              <dd><a>婴儿礼盒</a></dd>
              <dd><a>妈咪装</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <MagicStick  style="width: 25px; height: 25px;" /> 美妆 / 清洁 / 个护
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>美甲</a></dd>
              <dd><a>香水</a></dd>
              <dd><a>洗发水</a></dd>
              <dd><a>牙膏</a></dd>
              <dd><a>香皂</a></dd>
              <dd><a>沐浴露</a></dd>
              <dd><a>纸巾</a></dd>
              <dd><a>洗洁精</a></dd>
              <dd><a>香薰</a></dd>
              <dd><a>驱蚊器</a></dd>
              <dd><a>洗手液</a></dd>
            </dl>
          </div>
        </li>
        <li class="menu-item" >
          <div class="hd"     >
            <Collection  style="width: 25px; height: 25px;" /> 娱乐 / 图书 / 乐器
            <span class="mmb-iconfont arr" @mouseenter="showSubCategories">></span>
          </div>
          <div class="bd" v-show="showCategories" 
          :style="{ top: currentCategoryPosition.top + 'px', left: currentCategoryPosition.left + 'px' }">
            <dl class="col-three">
              <dd><a>童书绘本</a></dd>
              <dd><a>中小学教辅</a></dd>
              <dd><a>会计</a></dd>
              <dd><a>考研</a></dd>
              <dd><a>钢琴</a></dd>
              <dd><a>吉他</a></dd>
              <dd><a>鲜花绿植</a></dd>
              <dd><a>护肤</a></dd>
              <dd><a>文学作品</a></dd>
              <dd><a>尤克里里</a></dd>
            </dl>
          </div>
        </li>
      </ul>



      <!-- 商品展示 -->
      <div class="product-display carousel-container">
        <!-- 火爆降价商品 -->
        <div class="recommand">
          <h3>这些商品火爆降价中...</h3>
          <transition name="flip" mode="out-in">
            <div class="product-list" :key="currentGroup">
              <div
                class="product-card"
                v-for="(product, index) in groups[currentGroup]"
                :key="'hot-' + index"
              >
                <div class="image-placeholder">
                  <img :src="product.image" :alt="product.name" />
                </div>
                <p class="product-name">{{ product.name }}</p>
                <p class="product-price">{{ product.price }}</p>
              </div>
            </div>
          </transition>
        </div>

        <!-- 左右切换按钮 -->
        <button class="carousel-btn left-btn" @click="prevGroup">‹</button>
        <button class="carousel-btn right-btn" @click="nextGroup">›</button>
      </div>

      <div class="order carousel-container">
        <!-- 火爆降价商品 -->
        <div class="display">
          <h4>您感兴趣的...</h4>
          <transition name="flip" mode="out-in">
            <div class="product-list" :key="currentGroup_guess">
              <div
                class="product-card"
                v-for="(product, index) in groups_guess[currentGroup_guess]"
                :key="'hot-' + index"
              >
                <div class="image-placeholder">
                  <img :src="product.image" :alt="product.name" />
                </div>
                <p class="product-name">{{ product.name }}</p>
                <p class="product-price">{{ product.price }}</p>
              </div>
            </div>
          </transition>
        </div>
                <!-- 左右切换按钮 -->
        <button class="carousel-btn left-btn" @click="prevGroup_guess">‹</button>
        <button class="carousel-btn right-btn" @click="nextGroup_guess">›</button>
      </div>

    </div>
  </div>
</template>


<script>
import { ref } from 'vue';
import { ShoppingCart,Monitor,Cpu,House,User,ShoppingBag,Coin,Food,Present,MagicStick,Collection } from '@element-plus/icons-vue';
import router from "../../router";

export default {
  setup() {
    const showCategories = ref(false);
    const currentCategoryPosition = ref({ top: 0, left: 0 });

    const showSubCategories = (event) => {
      const hdElement = event.target.closest('.hd');
      if (!hdElement) return;
      console.log('show');
      const rect = hdElement.getBoundingClientRect();
      currentCategoryPosition.value = {
        top: rect.top + window.scrollY, // 对齐 hd 的顶部
        left: rect.right + window.scrollX, // 显示在右侧
      };
      showCategories.value = true;
    };

    const hideSubCategories = () => {
      console.log('hide');
      showCategories.value = false;
    };

    // 商品分组索引
    const currentGroup = ref(0);
    const groups = ref([
      [
        { name: '商品 1', price: '¥99.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 2', price: '¥89.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 3', price: '¥79.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 4', price: '¥69.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
      ],
      [
        { name: '商品 5', price: '¥59.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 6', price: '¥49.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 7', price: '¥39.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 8', price: '¥29.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
      ],
    ]);

    const prevGroup = () => {
      currentGroup.value = (currentGroup.value - 1 + groups.value.length) % groups.value.length;
    };

    const nextGroup = () => {
      currentGroup.value = (currentGroup.value + 1) % groups.value.length;
    };

    const currentGroup_guess = ref(0);
    const groups_guess = ref([
      [
        { name: '商品 1', price: '¥99.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 2', price: '¥89.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 3', price: '¥79.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 4', price: '¥69.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 5', price: '¥69.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 6', price: '¥69.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
      ],
      [
        { name: '商品 7', price: '¥59.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 8', price: '¥49.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 9', price: '¥39.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 10', price: '¥29.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 11', price: '¥69.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
        { name: '商品 12', price: '¥69.99', image: 'https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg' },
      ],
    ]);

    const prevGroup_guess = () => {
      currentGroup_guess.value = (currentGroup_guess.value - 1 + groups_guess.value.length) % groups_guess.value.length;
    };

    const nextGroup_guess = () => {
      currentGroup_guess.value = (currentGroup_guess.value + 1) % groups_guess.value.length;
    };


    const logout = () => {
      router.push("/")
    };

    return {
      showCategories,
      currentCategoryPosition,
      showSubCategories,
      hideSubCategories,
      currentGroup,
      groups,
      prevGroup,
      nextGroup,
      currentGroup_guess,
      groups_guess,
      prevGroup_guess,
      nextGroup_guess,
      ShoppingCart,
      Monitor,
      Cpu,
      House,
      User,
      ShoppingBag,
      Coin,
      Food,
      Present,
      MagicStick,
      Collection,
      logout,
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

  

  /* 内容部分 */
.content {
  margin-top: 200px;
  padding: 20px;
  width: 100%; /* 确保内容宽度为页面宽度 */
  box-sizing: border-box; /* 包括内边距 */
}

.hd {
  display: flex;
  align-items: center;
  gap: 20px; /* 增大文字和 ">" 符号的距离 */
  background-color: rgb(61, 24, 31); /* 背景颜色 */
  color: rgb(235, 218, 218); /* 字体颜色 */
  padding: 20px; /* 增大内边距 */
  border-radius: 8px; /* 更明显的圆角效果 */
  width: 350px; /* 增加标签宽度 */
  font-size: 25px; /* 增大文字大小 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  margin-left: 1%;
  position: relative; /* 为子标签的定位提供基础 */
}

.mmb-iconfont.arr {
  font-size: 22px; /* 增大箭头图标的大小 */
  cursor: pointer;
  transition: transform 0.3s ease;
  color: white; /* 保持箭头图标的字体颜色为白色 */
  margin-left: auto; /* 将箭头图标对齐到右侧 */
}

.hd:hover .mmb-iconfont.arr {
  transform: rotate(90deg); /* 鼠标悬浮时箭头旋转 */
}

/* 子分类的样式 */
.bd {
  display: none;
  position: absolute; /* 使子标签悬浮 */
  background-color: rgb(61, 24, 31);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  width: 500px; /* 子标签宽度 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  border-radius: 8px;
  font-size: 22px;
}

.bd a {
  display: block;
  padding: 8px 12px;
  color: rgb(235, 218, 218);
  text-decoration: none;
  transition: background-color 0.3s;
}

.bd a:hover {
  background-color: #c93939;
}

.col-three {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding: 10px;
}

.col-three dd {
  margin: 0;
}

.hd:hover .mmb-iconfont.arr {
  transform: rotate(90deg);
}

/* 主分类标签悬浮效果 */
.hd:hover + .bd, /* 鼠标悬停 .hd 时显示 */
.bd:hover {      /* 鼠标悬停 .bd 时保持显示 */
  display: block;
}

/* 商品展示主样式 */
.product-display {
  background-color: rgb(215, 111, 111);
  margin-left: 28%;
  margin-right: 12%;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  transform: scale(1.1); /* 放大 */
  transition: transform 0.3s ease; /* 平滑动画 */
  margin-top: -700px;
}

/* 推荐区域 */
.recommand {
  text-align: left;
  padding-bottom: 60px;
}

.recommand h3 {
  font-size: 50px;
  margin-bottom: 80px;
  color: #ec0606;
  font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  font-weight: bold; /* 加粗字体 */
}


.order{
  background-color: rgb(215, 111, 111);
  margin-left: 5%;
  margin-right: 5%;
  padding: 30px;
  transition: transform 0.3s ease; /* 平滑动画 */
  margin-top: 100px;
}
.display{
  text-align: left;
  padding-bottom: 85px;
}
.display h4{
  font-size: 45px;
  margin-bottom: 20px;
  color: #ec0606;
  font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  font-weight: bold; /* 加粗字体 */
  position: relative;
  top: -30px;
}

/* 商品列表样式 */
.product-list {
  display: flex;
  justify-content: space-between;

  gap: 20px;
}

/* 单个商品卡片 */
.product-card {
  background-color: #f9f9f9;
  border: 1px solid #c61313;
  border-radius: 5px;
  width: calc(25% - 10px); /* 调整为4列布局 */
  text-align: center;
  padding: 10px;
  height: 400px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.product-card:hover {
  transform: scale(1.05); /* 鼠标悬停放大 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

/* 图片占位符 */
.image-placeholder {
  width: 100%;
  height: 230px;
  background-color: #e0e0e0;
  border-radius: 5px;
  margin-bottom: 40px;
}
/* 图片样式 */
.image-placeholder img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 确保图片填充且不变形 */
  border-radius: 5px;
}


/* 商品名样式 */
.product-name {
  font-size: 20px;
  color: #555;
  margin-bottom: 40px;
}

/* 商品价格样式 */
.product-price {
  font-size: 18px;
  color: #e60012; /* 红色价格 */
  font-weight: bold;
}

/* 按钮基础样式 */
.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 20px;
  z-index: 10;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.3s ease;
}

.carousel-btn:hover {
  background-color: rgba(0, 0, 0, 0.8);
}

.left-btn {
  left: 10px;
}

.right-btn {
  right: 10px;
}
.carousel-container {
  position: relative;
  height: auto; /* 或者固定高度 */
}

.carousel-container:hover .carousel-btn {
  opacity: 1;
  pointer-events: auto;
}

/* 卡片翻转动画 */
.flip-enter-active, .flip-leave-active {
  transition: transform 0.5s ease, opacity 0.5s ease;
  transform-style: preserve-3d; /* 保持3D效果 */
}

.flip-enter-from {
  transform: rotateY(180deg); /* 从180度翻转开始 */
  opacity: 0;
}

.flip-leave-to {
  transform: rotateY(-180deg); /* 翻转离开 */
  opacity: 0;
}

.flip-enter-to, .flip-leave-from {
  transform: rotateY(0); /* 最终恢复正常 */
  opacity: 1;
}


  
}
</style>
