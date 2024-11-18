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
          </div>

        <!-- 邮箱 -->
        <el-form-item prop="mail" class="kuang">
            <div class="input-wrapper">
              <el-icon class="mx-2">
                  <User />
              </el-icon>
              <el-input
                v-model="loginData.mail"
                size="large"
                class="input_mail"
                :placeholder="'邮箱'"
              />
            </div>
        </el-form-item>
        <!-- 邮箱验证码 -->
        <el-form-item prop="checkcode" class="kuang">
            <div class="input-wrapper">
              <el-icon class="mx-2">
                  <Key />
              </el-icon>
              <el-input
                v-model="loginData.checkcode"
                auto-complete="off"
                size="large"
                class="input_checkcode"
                :placeholder="'验证码'"
              >
              <template #append>
                <el-button
                    :disabled="emailCountdown > 0"
                    @click="sendVerificationCode"
                    :title="'向您的邮箱发送验证码'"
                    class="verify-button"
                >
                {{ emailCountdown > 0 ? `${emailCountdown}s后重新发送` : "发送验证码" }}
                  </el-button>
              </template>
            </el-input>
            </div>
        </el-form-item>


          <!-- 密码 -->
            <el-form-item prop="password" class="kuang">
              <div class="input-wrapper">
                <el-icon class="mx-2">
                  <Lock />
                </el-icon>
                <el-input
                  v-model="loginData.password"
                  :placeholder="'请输入您的新密码'"
                  type="password"
                  name="password"
                  size="large"
                  class="input_password"
                  show-password
                />
              </div>
            </el-form-item>


          <!-- 重置密码按钮 -->
          <el-button

            type="primary"
            size="large"
            class="login_button"
            @click.prevent="handleForgetSubmit"
          >
            确认重置密码
          </el-button>
        </el-form>
      </div>
    </div>
    </div>
  </template>
  
  <script>
  import { ref, computed } from 'vue';
  import router from "../../router";
  import LOGINAPI from '../../api/login';
  //import { ElForm, ElFormItem } from 'element-plus';
  import { ElMessage } from "element-plus";
  import CryptoJS from 'crypto-js';
  
  
  // 配置图片路径和表单数据
  export default {
    setup() {
      // eslint-disable-next-line
      const loginFormRef = ref(null);
  
      const loginImage = ref("@/assets/images/login-background.jpg");

      const emailCountdown = ref(0);
  
      const loginData = ref({
        password: "",
        checkcode: "",
        mail: "",
        answer: ""
      });
  
      // 修改后的规则
      const loginRules = computed(() => ({
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
        checkcode: [
          {
            required: true,
            trigger: "blur",
            message: "验证码不能为空",
          },
        ],
        mail: [
            {
            validator: (rule, value, callback) => {
              const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
              if (value) {
                if (!emailPattern.test(value)) {
                  callback(new Error("邮箱格式不正确"));
                } else {
                  callback();
                }
              } else {
                callback(new Error("邮箱不能为空"));
              }
            },
            trigger: "blur",
          },
        ],
      }));

      // 发送验证码
    const sendVerificationCode = async () => {
        if (!loginData.value.mail) {
            ElMessage.error("请输入邮箱");
            return;
        }
        const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if(!emailPattern.test(loginData.value.mail)){
            ElMessage.error("邮箱格式不正确");
            return;
        }
        try {
            console.log('验证码发送前');
            LOGINAPI.mail({"mail":loginData.value.mail})
            .then(response => {
                loginData.value.answer = response.data;
                ElMessage.success("已向您的邮箱发送验证码");
            })
            .catch(error => {
                if (error.response) {
                console.error("请求失败，响应状态：", error.response.status);
                console.error("错误信息：", error.response.data);
                ElMessage.error(`发送验证码失败: ${error.response.data.message || error.response.statusText}`);
                } else {
                ElMessage.error("发送验证码失败");
                }
            });
        }catch (error) {
            ElMessage.error("发送验证码失败");
            console.error(error);
        }

        emailCountdown.value = 60;
        const timer = setInterval(() => {
            emailCountdown.value -= 1;
            if (emailCountdown.value <= 0) clearInterval(timer);
        }, 1000);
    };

      //注册
      const handleForgetSubmit = async () => {
      if (loginFormRef.value) {
        loginFormRef.value.validate((valid, fields) => {
          if(loginData.value.answer!=loginData.value.checkcode) ElMessage.error("验证码错误");
          else if (valid) {
            const encrypted = CryptoJS.SHA256(loginData.value.password).toString();
            LOGINAPI.forget({
              "password": encrypted,
              "mail":loginData.value.mail,
            })
              .then(response =>{
                if (response.code === "00000") {
                  ElMessage.success("密码重置成功，请重新登录");
                  router.push("/");
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
  
      // 返回需要在模板中使用的变量和函数
      return {
        loginFormRef,
        loginImage,
        loginData,
        loginRules,
        handleForgetSubmit,
        sendVerificationCode,
        emailCountdown,
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
        .verify-button {
        font-size: 24px;  /* 减小字体大小 */
        padding: 5px 2px;
        margin-top: 10px;
        background-color: #E97A29;  /* 设置按钮背景颜色 */
        color: black;  /* 设置字体颜色为黑色 */
        display: flex;
        justify-content: center;  /* 水平居中 */
        align-items: center;  /* 垂直居中 */
        font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
        width: 150px; /* 设置按钮宽度 */
        height: 50px; /* 设置按钮高度 */
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
          width: 80%;
          height: 70px;
          margin: 50px 0 0 60px;
  
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
  

  
  
  
  
  
  