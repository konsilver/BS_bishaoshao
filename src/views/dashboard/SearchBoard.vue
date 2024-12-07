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
              <dd><a @click="setKeyword('笔记本电脑')">笔记本电脑</a></dd>
              <dd><a @click="setKeyword('平板电脑')">平板电脑</a></dd>
              <dd><a @click="setKeyword('CPU')">CPU</a></dd>
              <dd><a @click="setKeyword('显卡')">显卡</a></dd>
              <dd><a @click="setKeyword('硬盘')">硬盘</a></dd>
              <dd><a @click="setKeyword('键盘')">键盘</a></dd>
              <dd><a @click="setKeyword('鼠标')">鼠标</a></dd>
              <dd><a @click="setKeyword('主板')">主板</a></dd>
              <dd><a @click="setKeyword('内存条')">内存条</a></dd>
              <dd><a @click="setKeyword('耳机')">耳机</a></dd>
              <dd><a @click="setKeyword('U盘')">U盘</a></dd>
              <dd><a @click="setKeyword('显示器')">显示器</a></dd>
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
              <dd><a @click="setKeyword('电视')">电视</a></dd>
              <dd><a @click="setKeyword('空调')">空调</a></dd>
              <dd><a @click="setKeyword('冰箱')">冰箱</a></dd>
              <dd><a @click="setKeyword('洗衣机')">洗衣机</a></dd>
              <dd><a @click="setKeyword('热水器')">热水器</a></dd>
              <dd><a @click="setKeyword('电风扇')">电风扇</a></dd>
              <dd><a @click="setKeyword('手机')">手机</a></dd>
              <dd><a @click="setKeyword('智能手表')">智能手表</a></dd>
              <dd><a @click="setKeyword('蓝牙耳机')">蓝牙耳机</a></dd>
              <dd><a @click="setKeyword('饮水机')">饮水机</a></dd>
              <dd><a @click="setKeyword('电风扇')">电风扇</a></dd>
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
              <dd><a @click="setKeyword('油烟机')">油烟机</a></dd>
              <dd><a @click="setKeyword('消毒柜')">消毒柜</a></dd>
              <dd><a @click="setKeyword('豆浆机')">豆浆机</a></dd>
              <dd><a @click="setKeyword('电饭煲')">电饭煲</a></dd>
              <dd><a @click="setKeyword('电烤箱')">电烤箱</a></dd>
              <dd><a @click="setKeyword('微波炉')">微波炉</a></dd>
              <dd><a @click="setKeyword('洗碗机')">洗碗机</a></dd>
              <dd><a @click="setKeyword('咖啡机')">咖啡机</a></dd>
              <dd><a @click="setKeyword('面包机')">面包机</a></dd>
              <dd><a @click="setKeyword('电水壶')">电水壶</a></dd>
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
              <dd><a @click="setKeyword('连衣裙')">连衣裙</a></dd>
              <dd><a @click="setKeyword('T恤')">T恤</a></dd>
              <dd><a @click="setKeyword('衬衫')">衬衫</a></dd>
              <dd><a @click="setKeyword('羽绒服')">羽绒服</a></dd>
              <dd><a @click="setKeyword('毛衣')">毛衣</a></dd>
              <dd><a @click="setKeyword('内裤')">内裤</a></dd>
              <dd><a @click="setKeyword('卫衣')">卫衣</a></dd>
              <dd><a @click="setKeyword('西装')">西装</a></dd>
              <dd><a @click="setKeyword('短裤')">短裤</a></dd>
              <dd><a @click="setKeyword('牛仔外套')">牛仔外套</a></dd>
              <dd><a @click="setKeyword('马甲')">马甲</a></dd>
              <dd><a @click="setKeyword('风衣')">风衣</a></dd>
              <dd><a @click="setKeyword('休闲裤')">休闲裤</a></dd>
              <dd><a @click="setKeyword('丝袜裤袜')">丝袜裤袜</a></dd>
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
              <dd><a @click="setKeyword('高跟鞋')">高跟鞋</a></dd>
              <dd><a @click="setKeyword('平底鞋')">平底鞋</a></dd>
              <dd><a @click="setKeyword('跑步鞋')">跑步鞋</a></dd>
              <dd><a @click="setKeyword('马丁靴<')">马丁靴</a></dd>
              <dd><a @click="setKeyword('休闲皮鞋')">休闲皮鞋</a></dd>
              <dd><a @click="setKeyword('篮球鞋')">篮球鞋</a></dd>
              <dd><a @click="setKeyword('商务靴')">商务靴</a></dd>
              <dd><a @click="setKeyword('正装靴')">正装靴</a></dd>
              <dd><a @click="setKeyword('泳衣')">泳衣</a></dd>
              <dd><a @click="setKeyword('户外背包')">户外背包</a></dd>
              <dd><a @click="setKeyword('钓鱼')">钓鱼</a></dd>
              <dd><a @click="setKeyword('健身车')">健身车</a></dd>
              <dd><a @click="setKeyword('球类')">球类</a></dd>
              <dd><a @click="setKeyword('望远镜')">望远镜</a></dd>
              <dd><a @click="setKeyword('帐篷')">帐篷</a></dd>
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
              <dd><a @click="setKeyword('钱包')">钱包</a></dd>
              <dd><a @click="setKeyword('书画')">书画</a></dd>
              <dd><a @click="setKeyword('项链/吊坠')">项链/吊坠</a></dd>
              <dd><a @click="setKeyword('手饰')">手饰</a></dd>
              <dd><a @click="setKeyword('手镯')">手镯</a></dd>
              <dd><a @click="setKeyword('手提包')">手提包</a></dd>
              <dd><a @click="setKeyword('双肩包')">双肩包</a></dd>
              <dd><a @click="setKeyword('行李箱')">行李箱</a></dd>
              <dd><a @click="setKeyword('路易威斯')">路易威斯</a></dd>
              <dd><a @click="setKeyword('圣罗兰')">圣罗兰</a></dd>
              <dd><a @click="setKeyword('香奈儿')">香奈儿</a></dd>
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
              <dd><a @click="setKeyword('牛奶')">牛奶</a></dd>
              <dd><a @click="setKeyword('茶')">茶</a></dd>
              <dd><a @click="setKeyword('国产水果')">国产水果</a></dd>
              <dd><a @click="setKeyword('白酒')">白酒</a></dd>
              <dd><a @click="setKeyword('葡挞酒')">葡挞酒</a></dd>
              <dd><a @click="setKeyword('药品')">药品</a></dd>
              <dd><a @click="setKeyword('葱姜类')">葱姜类</a></dd>
              <dd><a @click="setKeyword('行李箱')">行李箱</a></dd>
              <dd><a @click="setKeyword('奶粉')">奶粉</a></dd>
              <dd><a @click="setKeyword('生日蛋糕')">生日蛋糕</a></dd>
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
              <dd><a @click="setKeyword('孕妇装')">孕妇装</a></dd>
              <dd><a @click="setKeyword('安全座椅')">安全座椅</a></dd>
              <dd><a @click="setKeyword('婴儿推车')">婴儿推车</a></dd>
              <dd><a @click="setKeyword('益智积木')">益智积木</a></dd>
              <dd><a @click="setKeyword('猫粮狗粮')">猫粮狗粮</a></dd>
              <dd><a @click="setKeyword('宠物玩具')">宠物玩具</a></dd>
              <dd><a @click="setKeyword('童装')">童装</a></dd>
              <dd><a @click="setKeyword('婴儿礼盒')">婴儿礼盒</a></dd>
              <dd><a @click="setKeyword('妈咪装')">妈咪装</a></dd>
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
              <dd><a @click="setKeyword('美甲')">美甲</a></dd>
              <dd><a @click="setKeyword('香水')">香水</a></dd>
              <dd><a @click="setKeyword('洗发水')">洗发水</a></dd>
              <dd><a @click="setKeyword('牙膏')">牙膏</a></dd>
              <dd><a @click="setKeyword('香皂')">香皂</a></dd>
              <dd><a @click="setKeyword('沐浴露')">沐浴露</a></dd>
              <dd><a @click="setKeyword('纸巾')">纸巾</a></dd>
              <dd><a @click="setKeyword('洗洁精')">洗洁精</a></dd>
              <dd><a @click="setKeyword('香薰')">香薰</a></dd>
              <dd><a @click="setKeyword('驱蚊器')">驱蚊器</a></dd>
              <dd><a @click="setKeyword('洗手液')">洗手液</a></dd>
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
              <dd><a @click="setKeyword('童书绘本')">童书绘本</a></dd>
              <dd><a @click="setKeyword('中小学教辅')">中小学教辅</a></dd>
              <dd><a @click="setKeyword('会计')">会计</a></dd>
              <dd><a @click="setKeyword('考研')">考研</a></dd>
              <dd><a @click="setKeyword('钢琴')">钢琴</a></dd>
              <dd><a @click="setKeyword('吉他')">吉他</a></dd>
              <dd><a @click="setKeyword('鲜花绿植')">鲜花绿植</a></dd>
              <dd><a @click="setKeyword('护肤')">护肤</a></dd>
              <dd><a @click="setKeyword('文学作品')">文学作品</a></dd>
              <dd><a @click="setKeyword('尤克里里')">尤克里里</a></dd>
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
                @click="setid(product.id)"
              >
                <div class="image-placeholder">
                  <img :src="product.image" />
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

    </div>
  </div>
</template>


<script>
import { ref ,onMounted} from 'vue';
import { ShoppingCart,Monitor,Cpu,House,User,ShoppingBag,Coin,Food,Present,MagicStick,Collection } from '@element-plus/icons-vue';
import {useRouter} from "vue-router";
import SUBSCRIBEAPI from '../../api/subscribe';
import SEARCHAPI from '../../api/search';
import { ElMessage } from "element-plus";


export default {
  setup() {
    const router = useRouter();
    const showCategories = ref(false);
    const currentCategoryPosition = ref({ top: 0, left: 0 });
    const keyword=ref('');
    const searchtext=ref('');
    const groups = ref([[], [], []]); // 用于存放商品分组，每组最多4个商品
    const selectid = ref(0); //被点击的商品卡片的对应商品id，用于路由跳转

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
      showCategories.value = false;
    };

    const search = () => {
      router.push({
        name: 'SearchResults', 
        query: { results: keyword.value },
      });
    };

    const setKeyword = (word) =>{
      keyword.value = word; // 将点击的词条赋值给变量 keyword
      search();
    };
    
    const sure = () => {
      if(searchtext.value!=''){
        keyword.value=searchtext.value;
        search();
      }
    };

    // 商品分组索引
    const currentGroup = ref(0);


    const waitornon = ref('加载中...');  // 初始提示

    onMounted(() => {
      SUBSCRIBEAPI.getany()
      .then(response => {
        if (response.code === "00000") {
          const items = response.data;  // 假设返回的数据是 items 数组

          // 清空现有的分组，确保每次数据刷新时分组是干净的
          groups.value = [[], [], []];

          // 将商品数据分配到4个组中，每组最多4个商品
          let groupIndex = 0;
          items.forEach((item, index) => {
            groups.value[groupIndex].push({
              id: item.id,
              name: item.name,
              price: `¥${item.price.toFixed(2)}`, // 格式化价格
              image: item.image,
            });

            // 每4个商品换到下一组
            if ((index + 1) % 4 === 0 && groupIndex < 2) {  // 确保不超出3组
              groupIndex++;
            }
          });
        } else {
          waitornon.value = '暂无相关商品，试着搜搜别的吧';
        }
      })
      .catch(error => {
        console.log(error);
        waitornon.value = '加载失败，请稍后再试';
      });
    });

    const prevGroup = () => {
      currentGroup.value = (currentGroup.value - 1 + groups.value.length) % groups.value.length;
    };

    const nextGroup = () => {
      currentGroup.value = (currentGroup.value + 1) % groups.value.length;
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


    const logout = () => {
      SEARCHAPI.logout()
      .then(response =>{
          if (response.code === "00000") {
              ElMessage.success("您已退出登录");
              router.push("/");
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
      showCategories,
      currentCategoryPosition,
      showSubCategories,
      hideSubCategories,
      currentGroup,
      groups,
      prevGroup,
      nextGroup,
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
      setKeyword,
      searchtext,
      sure,
      lookin,
      setid,
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
  height: 140vh;
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






  /* 内容部分 */
.content {
  margin-top: 12%;
  padding: 20px;
  width: 100vw; /* 确保内容宽度为页面宽度 */
  box-sizing: border-box; /* 包括内边距 */
}

.hd {
  display: flex;
  align-items: center;
  gap: 12px; /* 增大文字和 ">" 符号的距离 */
  background-color: rgb(61, 24, 31); /* 背景颜色 */
  color: rgb(235, 218, 218); /* 字体颜色 */
  padding: 20px; /* 增大内边距 */
  border-radius: 8px; /* 更明显的圆角效果 */
  width: 18%; /* 增加标签宽度 */
  font-size: 20px; /* 增大文字大小 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  margin-left: 1%;
  position: relative; /* 为子标签的定位提供基础 */
}

.mmb-iconfont.arr {
  font-size: 18px; /* 增大箭头图标的大小 */
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
  width: 30vw; /* 子标签宽度 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  border-radius: 8px;
  font-size: 18px;
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
  background-color: rgb(132, 55, 55);
  margin-left: 30%;
  margin-right: 10%;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  transform: scale(1.1); /* 放大 */
  transition: transform 0.3s ease; /* 平滑动画 */
  margin-top: -45%;
}

/* 推荐区域 */
.recommand {
  text-align: left;
  padding-bottom: 60px;
}

.recommand h3 {
  font-size: 45px;
  margin-bottom: 80px;
  color: #d7c6c6;
  font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  font-weight: bold; /* 加粗字体 */
}

/* 商品列表样式 */
.product-list {
  display: flex;
  justify-content: space-between;

  gap: 20px;
}

/* 单个商品卡片 */
.product-card {
  background-color: #b35a5a;
  border: 1px solid #c61313;
  border-radius: 5px;
  width: calc(25% - 10px); /* 调整为4列布局 */
  text-align: center;
  padding: 10px;
  height: 45vh;
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
  height: 50%;
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
  font-size: 15px;
  color: #140202;
  margin-bottom: 10px; /* 调整底部间距，避免价格和标题重叠 */
  
  display: -webkit-box; /* 使得元素成为多行布局 */
  -webkit-box-orient: vertical; /* 定义盒子的方向为纵向 */
  -webkit-line-clamp: 2; /* 限制最多显示两行 */
  overflow: hidden; /* 隐藏溢出的内容 */
  text-overflow: ellipsis; /* 使用省略号替代溢出部分 */
  font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
}

/* 商品价格样式 */
.product-price {
  font-size: 23px;
  color: #e60012; /* 红色价格 */
  font-weight: bold;
  margin-bottom: 30px;
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

@media (max-width: 768px) {
  .content {
    margin-top: 40vh;
    flex-direction: column; /* 修改为上下排列 */
    align-items: center; /* 居中对齐 */
    margin-left: 1vw !important;
  }

  .menu-item {
    display: none !important;
  }

  .product-display {
    margin-left: 5%;
    margin-right: 5%;
    padding: 10px;
    transform: scale(1);
  }

  .recommand h3 {
    font-size: 20px;
    margin-bottom: 4% !important;
  }

  .product-list {
  display: flex;
  flex-wrap: wrap; /* 允许换行 */
  margin-top: 10% !important;
  gap: 0px !important; /* 调整间距，减少每个商品之间的空隙 */
}

.product-card {
  width: calc(50% - 30px); /* 每行显示2个商品卡片，减小间隔空间 */
  height: auto; /* 高度自动调整 */
  margin-bottom: 20px; /* 为每个商品卡片添加间距 */
  max-height: 250px; /* 限制每个商品卡片的最大高度 */
}


  /* 图片占位符 */
  .image-placeholder {
    width: 100%;
    height: 60%;
    background-color: #e0e0e0;
    border-radius: 5px;
    margin-bottom: 20px; /* 调整为更小的间距 */
  }

  /* 图片样式 */
  .image-placeholder img {
    width: 100%;
    height: 100%;
    object-fit: cover; /* 确保图片填充且不变形 */
    border-radius: 5px;
  }
  .product-price{
    font-size: 20px !important;
  }

  .carousel-btn {
    position: absolute;
    margin-top: 10%;
    bottom: 10px;
    z-index: 10;
    font-size: 12px !important;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    border: none;
    padding: 8px 16px;
    cursor: pointer;
  }

  .left-btn {
    left: 5px !important;
  }

  .right-btn {
    right: 5px !important;
  }
}

.carousel-container:hover .carousel-btn {
  opacity: 1;
  pointer-events: auto;
}

.flip-enter-active, .flip-leave-active {
  transition: transform 0.5s ease, opacity 0.5s ease;
  transform-style: preserve-3d;
}

.flip-enter-from {
  transform: rotateY(180deg);
  opacity: 0;
}

.flip-leave-to {
  transform: rotateY(-180deg);
  opacity: 0;
}

.flip-enter-to, .flip-leave-from {
  transform: rotateY(0);
  opacity: 1;
}

  
}
</style>
