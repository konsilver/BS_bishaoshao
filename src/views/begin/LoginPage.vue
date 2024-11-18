<template>
  <div class="login">
    <!-- 登录页头部 -->
    <div class="login-header">
        <div class="left-section">
          <img src="@/assets/images/logo.jpg" alt="Logo" class="logo" />
          <span class="title">————您便捷的商品比价平台</span>
        </div>
    </div>

    <!-- 登录页内容 -->
    <div class="login-content">
      <div class="login-img">
        <img :src="require('@/assets/images/login-background.jpg')" style="width: 100%; height: 100%; object-fit: cover;" />
      </div>
      <div class="login-form">
        <el-form ref="loginFormRef" :model="loginData" :rules="loginRules">
          <div class="form-title">
            <h2>比少少平台欢迎您</h2>
            <el-dropdown style="position: absolute; right: 0; margin-left: 50px;">
              <div class="cursor-pointer">
                <el-icon>
                  <arrow-down />
                </el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    @click="setLoginCredentials('bishaoshao_tester', '123456abc')"
                  >
                    测试用户：bishaoshao_tester
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
          </el-dropdown>
          </div>

          <!-- 用户名 -->
          <el-form-item prop="username" class="kuang">
            <div class="input-wrapper">
              <el-icon class="mx-2">
                <User />
              </el-icon>
              <el-input
                ref="username"
                v-model="loginData.username"
                :placeholder="'用户名/邮箱'"
                name="username"
                size="large"
                class="input_username"
              />
            </div>
          </el-form-item >

          <!-- 密码 -->
            <el-form-item prop="password" class="kuang">
              <div class="input-wrapper">
                <el-icon class="mx-2">
                  <Lock />
                </el-icon>
                <el-input
                  v-model="loginData.password"
                  :placeholder="'密码'"
                  type="password"
                  name="password"
                  size="large"
                  class="input_password"
                  show-password
                  @keyup.enter="handleLoginSubmit"
                />
              </div>
            </el-form-item>

          <!-- 验证码 -->
          <el-form-item prop="captchaCode" class="kuang">
            <div class="input-wrapper">
              <el-icon class="mx-2">
                  <key />
              </el-icon>
              <el-input
                v-model="loginData.captchaCode"
                auto-complete="off"
                size="large"
                class="input_checkcode"
                :placeholder="'验证码'"
                @keyup.enter="handleLoginSubmit"
              />

              <img
                :src="captchaBase64" 
                class="captcha-img"
                @click="getCaptcha"
              />
            </div>
          </el-form-item>

          <div class="flex-x-between w-full py-1">
            <el-checkbox>
              记住我
            </el-checkbox>
          </div>

          <div class="flex-x-between w-full py-2">
            <el-link type="primary" @click="navigateToForget">忘记密码</el-link>
            <!-- 注册链接 -->
            <el-link type="primary" @click="navigateToRegister">还没账号？创建一个</el-link>
          </div>

          <!-- 登录按钮 -->
          <el-button

            type="primary"
            size="large"
            class="login_button"
            @click.prevent="handleLoginSubmit"
          >
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref, computed } from 'vue';
import router from "../../router";
import LOGINAPI from '../../api/login';
//import { ElForm, ElFormItem } from 'element-plus';
import { ElMessage } from "element-plus";
import CryptoJS from 'crypto-js';
import Cookies from "js-cookie";

// 配置图片路径和表单数据
export default {
  setup() {
    // eslint-disable-next-line
    const loginFormRef = ref(null);
    const captchaBase64 = ref(); // 验证码图片Base64字符串

    const loginImage = ref("@/assets/images/login-background.jpg");

    const loginData = ref({
      username: "",
      password: "",
      captchaKey: "",
      captchaCode: "",
    });

    // 修改后的规则
    const loginRules = computed(() => ({
      username: [
        {
          required: true,
          trigger: "blur",
          message: "用户名/邮箱不能为空",
        },
        {
          validator: (rule, value, callback) => {
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (value) {
              if (emailPattern.test(value) && value.length < 8) {
                callback(new Error("用户名长度少于6或邮箱格式不正确"));
              } else {
                callback();
              }
            } else {
              callback(new Error("用户名/邮箱不能为空"));
            }
          },
          trigger: "blur",
        },
      ],
      password: [
        {
          required: true,
          trigger: "blur",
          message: "密码不能为空",
        },
        {
          min: 8,
          trigger: "blur",
          message: "密码不能少于8位",
        },
        {
          pattern: /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, // 密码必须包含字母和数字
          trigger: "blur",
          message: "密码必须包含数字或英文字母",
        },
      ],
      captchaCode: [
        {
          required: true,
          trigger: "blur",
          message: "验证码不能为空",
        },
        {
          validator: (rule, value, callback) => {
            // 验证 captchaCode 是否等于 captchaKey
            if (value !== loginData.value.captchaKey) {
              callback(new Error("验证码不匹配"));
            } else {
              callback();
            }
          },
          trigger: "blur",
        },
      ],
    }));

    // 获取验证码
    const getCaptcha = () => {
      LOGINAPI.getCaptcha().then((data) => {
        const encrypted = CryptoJS.SHA256("123456abc").toString();
            console.log(encrypted);
        loginData.value.captchaKey = data.data.captchaKey;
        captchaBase64.value = data.data.captchaBase64;
      }).catch((error) => {
        ElMessage.error("获取验证码失败", error);
      });
    };

    // 页面加载时调用
    onMounted(() => {
      getCaptcha();
    });

    // 跳转到注册页面
    const navigateToRegister = () => {
      router.push("/bishaoshao/user/register")
        .then(() => console.log("Navigation to register succeeded"))
        .catch((error) => console.log("Navigation failed", error));
    };

    const navigateToForget = () => {
      router.push("/bishaoshao/user/forget")
        .then(() => console.log("Navigation to forget succeeded"))
        .catch((error) => console.log("Navigation forget", error));
    };

    // 处理登录表单提交
    const handleLoginSubmit = async () => {
      if (loginFormRef.value) {
        loginFormRef.value.validate((valid, fields) => {
          if (valid) {
            // 登录验证逻辑
            const encrypted = CryptoJS.SHA256(loginData.value.password).toString();
            LOGINAPI.login({
              "username": loginData.value.username,
              "password": encrypted
            })
              .then(response =>{
                if (response.code === "00000") {
                  ElMessage.success("登录成功");
                  Cookies.set('token', response.data.token);
                  router.push("/bishaoshao/user/searchboard");
                } else {
                  ElMessage.error(response.msg);
                  return;
                }
              })
              .catch(error => {
                console.log(error);
              })
          } else {
            if (Object.keys(fields).length > 0) {
              // 显示具体的错误信息
              const firstError = Object.values(fields)[0][0]?.message || "请填写完整信息";
              ElMessage.error(firstError);
            } else {
              // 未填写完整信息
              ElMessage.error("请填写完整信息");
            }
          }
        });
      }
    };

    // 设置登录凭证
    const setLoginCredentials = (username, password) => {
      loginData.value.username = username;
      loginData.value.password = password;
    };

    // 返回需要在模板中使用的变量和函数
    return {
      loginFormRef,
      captchaBase64,
      loginImage,
      loginData,
      loginRules,
      getCaptcha,
      navigateToRegister,
      handleLoginSubmit,
      setLoginCredentials,
      navigateToForget,
    };
  }
};
</script>




<style lang="scss" scoped>
.login {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh; /* 让高度充满视口 */
  background: url("@/assets/images/login-background-dark.jpg") no-repeat center center;
  background-size: cover; /* 背景图片完全平铺 */

  .login-header {
    position: absolute;
    top: 20px;
    left: 20px; /* 将 header 固定在左上角 */
    display: flex;
    flex-direction: column; /* 垂直布局 */
    align-items: flex-start; /* 左对齐 */
    gap: 15px;

    .logo {
      width: 200px; /* 放大 logo 大小 */
      height: auto;
    }

    .title {
      font-size: 40px; /* 调整标题字体稍大 */
      font-weight: bold;
      color: #E97A29;
      margin-top: 100px; /* 将标题稍微向下移动 */
      font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
    }
  }

  .login-content {
  display: flex;
  flex-direction: row;
  background-color: #2C1A2F; /* 设置背景颜色 */
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light);
  width: 70vw; /* 放大 content 的宽度 */
  max-width: 3000px;
  min-height: 650px; /* 增加 content 的高度 */
  margin-top: 50px; /* 确保 content 与 header 保持足够距离 */
  overflow: hidden;

  .login-img {
    flex: 4; /* 设置为 4:3 比例 */
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(60deg, #165dff, #6aa1ff);
  }

  .login-form {
    flex: 3; /* 设置为 4:3 比例 */
    display: flex;
    flex-direction: column;
    justify-content: flex-start; /* 标题向上对齐 */
    padding: 40px;

    .form-title {
      position: relative; /* 添加相对定位 */
      margin-bottom: 30px;
      text-align: center;
      color: #E97A29;

      h2 {
        font-size: 32px;
        font-weight: bold;
      }
    }

    .el-dropdown {
      position: absolute;
      right: 0;
      top: 50%; /* 调整垂直居中 */
      transform: translateY(-50%);
    }

    .input-wrapper {
      display: flex;
      align-items: center;
      position: relative; /* 添加相对定位以便子元素的定位 */
      width: 100%;
      margin-bottom: 20px; /* 增大输入框之间的间距 */

      .el-icon {
        color: #e97a29; /* 图标颜色 */
        font-size: 24px; /* 图标大小 */
        margin-right: 10px;
      }

      .el-input {
        flex: 1;
        font-size: 18px; /* 字体大小 */
        height: 70px; /* 增加输入框高度 */
        padding: 0 0px; /* 左右内边距 */
        background-color: #ffffff; /* 输入框背景改为白色 */
        color: #000000; /* 输入框文字改为黑色 */
        border: 1px solid #ffffff; /* 边框颜色改为白色 */
        border-radius: 6px;
        line-height: 1.5; /* 调整行高使内容居中 */

        &::placeholder {
          color: #888888; /* placeholder 字体颜色为浅灰色 */
        }

        &:focus {
          border-color: #e97a29; /* 聚焦时边框颜色改为主题色 */
          outline: none;
        }
      }
      .el-form-item.is-error .el-input {
          border-color: #ff4d4f; /* 错误时边框变红 */
        }
        .el-form-item.is-error .el-input__inner {
          border-color: #ff4d4f; /* 错误时输入框内边框变红 */
        }
      .captcha-img {
          position: absolute;
          right: 10px; /* 验证码图片与输入框右侧留空 */
          top: 50%; /* 垂直居中 */
          transform: translateY(-50%); /* 精确居中 */
          width: 120px;
          height: 50px;
          cursor: pointer;
        }

      }
      .flex-x-between {
        display: flex;
        justify-content: space-between; /* 分别居左和居右 */
        align-items: center;
        margin-top: 15px; /* 增加与按钮的间距 */
        margin-bottom: 30px;

        el-link {
          font-size: 14px;
          color: #888;
          cursor: pointer;

          &:hover {
            color: #E97A29;
          }
        }
      }

      .login_button {
        padding: 15px;
        font-size: 20px; /* 增大按钮大小 */
        font-weight: bold;
        color: #000;
        background-color: #E97A29;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        width: 100%;

        &:hover {
          background-color: #ff8c3d;
        }
      }

    }
}
}
</style>

<style lang="scss" scoped>
  ::v-deep .el-form-item__error {
    color: #ff4d4f;
    font-size: 16px;
    position: absolute;
    bottom: 20px; /* 距离输入框底部的距离 */
    left: 30px; /* 向右移动的距离 */
    width: calc(100% - 30px); /* 根据右移调整宽度 */
    text-align: left;
    margin-top: -18px;
  }
</style>





