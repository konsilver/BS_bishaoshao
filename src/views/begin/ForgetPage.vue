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
  top: 2%; /* 改为相对视口高度，确保在不同屏幕上有良好的间距 */
  left: 2%; /* 改为相对视口宽度，确保与屏幕边缘有一定间距 */
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1rem; /* 使用 rem 单位，确保布局的相对性 */

  .logo {
    width: 10vw; /* 使用 vw 单位，根据视口宽度调整 logo 的大小 */
    height: auto;
  }

  .title {
    font-size: 1.5rem; /* 使用 rem 单位，使字体大小与根字体大小挂钩 */
    font-weight: bold;
    color: #E97A29;
    margin-top: 2rem; /* 使用 rem 单位，确保标题与上方的间距适中 */
    font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
  }
  }

/* 为手机端添加适配样式 */
@media (max-width: 768px) {
  .login-header {
    top: 4%; /* 缩小顶部间距，使其适配小屏幕 */
    left: 5%; /* 缩小左边距 */
    align-items: center; /* 在手机上居中显示内容 */

    .logo {
      width: 30vw; /* 根据手机屏幕宽度调整 logo 大小 */
    }

    .title {
      font-size: 1rem; /* 在手机上减小标题字体 */
      text-align: center; /* 标题居中 */
      margin-top: 0.5rem; /* 减小标题上方的间距 */
    }
  }
}
.login-content {
    display: flex;
    flex-direction: row;
    background-color: #2C1A2F;
    border-radius: 8px;
    box-shadow: var(--el-box-shadow-light);
    width: 70vw;
    max-width: 100%;  /* 设置最大宽度为 100% */
    min-height: 550px;
    margin-top: 7%;
    overflow: hidden;
    padding: 20px;  /* 添加一些内边距避免边缘过于紧凑 */

    /* PC端 */
    .login-img {
      flex: 4;  /* 让图片部分占据一定宽度 */
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(60deg, #165dff, #6aa1ff);
      min-height: 250px;  /* 为手机端设置最小高度 */
      max-width: 100%;  /* 保证图片不会超过宽度 */
    }

    .login-form {
      flex: 3;  /* 让表单部分占据剩余空间 */
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      padding: 20px;  /* 更改 padding 以适应手机端 */
      max-width: 100%;  /* 保证表单宽度不会超过可视区 */
      background-color: rgba(75, 48, 48, 0.5);  /* 提高表单背景的透明度 */

      .form-title {
        position: relative;
        margin-bottom: 35px;
        text-align: center;
        color: #E97A29;

        h2 {
          font-size: 24px;  /* 减小标题字体大小 */
          font-weight: bold;
        }
      }

      .el-dropdown {
        position: absolute;
        right: 0;
        top: 50%;
        transform: translateY(-50%);
      }

      .input-wrapper {
        display: flex;
        align-items: center;
        position: relative;
        width: 100%;
        margin-bottom: 25px;  /* 减小输入框间距 */

        .el-icon {
          color: #e97a29;
          font-size: 20px;  /* 减小图标大小 */
          margin-right: 10px;
        }

        .el-input {
          flex: 1;
          font-size: 16px;  /* 减小字体大小 */
          height: 50px;  /* 更适合手机端的输入框高度 */
          padding: 0 10px;  /* 增加左右内边距 */
          background-color: #ffffff;
          color: #000000;
          border: 1px solid #ffffff;
          border-radius: 6px;
          line-height: 1.5;

          &::placeholder {
            color: #888888;
          }

          &:focus {
            border-color: #e97a29;
            outline: none;
          }
        }
        .verify-button {
        font-size: 18px;  /* 减小字体大小 */
        padding: 5px 2px;
        margin-top: 5px;
        background-color: #E97A29;  /* 设置按钮背景颜色 */
        color: black;  /* 设置字体颜色为黑色 */
        display: flex;
        justify-content: center;  /* 水平居中 */
        align-items: center;  /* 垂直居中 */
        font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
        width: 100px; /* 设置按钮宽度 */
        height: 40px; /* 设置按钮高度 */
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
          margin-top: 7%;
          &:hover {
            background-color: #ff8c3d;
          }
        }
  
      }
  }

  @media screen and (max-width: 768px) {
  .login-content {
    flex-direction: column !important;  /* 手机端调整为垂直布局 */
    padding: 15px !important;  /* 调整内边距 */
    margin-top: 15% !important;  /* 将 content 部分下移，确保图片在 form 上方 */
    border: none !important;  /* 取消边框 */
    background-color: transparent !important;  /* 背景透明 */
    box-shadow: none !important;  /* 取消阴影 */
    width: 85% !important;  /* 增加宽度，适应更多屏幕 */
    max-width: 450px !important;  /* 设置更大的最大宽度 */
  }

  .login-img {
    display: none !important;
  }

  .login-form {
    padding: 15px !important;  /* 减少表单内边距 */
    width: 90% !important;  /* 设置表单宽度为视口的 90% */
    max-width: 450px !important;  /* 设置表单的最大宽度 */
    height: auto !important;  /* 自适应高度 */
    background-color: rgba(75, 48, 48, 0.5) !important;  /* 增加表单背景透明度 */
    margin: 0 auto !important;  /* 使表单居中 */
    border-radius: 6px !important;  /* 保持圆角 */
  }

  .form-title h2 {
    font-size: 20px !important;  /* 调整标题字体大小 */
    margin-bottom: 50px !important; 
  }

  .input-wrapper {
    margin-bottom: 25px !important;  /* 减小输入框之间的间距 */

    .el-icon {
      font-size: 18px !important;  /* 调整图标大小 */
      margin-right: 8px !important;  /* 调整图标与输入框的间距 */
    }

    .el-input {
      font-size: 16px !important;  /* 调整输入框字体大小 */
      height: 45px !important;  /* 调整输入框高度 */
      padding: 0 10px !important;  /* 调整左右内边距 */
      background-color: #efeded !important;
      border: 1px solid #982e2e !important;
      border-radius: 4px !important;  /* 调整圆角 */
      line-height: 1.5 !important;

      &::placeholder {
        color: #888888 !important;
      }

      &:focus {
        border-color: #e97a29 !important;
        outline: none !important;
      }
    }
  }


  .verify-button {
        font-size: 18px;  /* 减小字体大小 */
        padding: 5px 2px;
        margin-top: 5px;
        background-color: #E97A29;  /* 设置按钮背景颜色 */
        color: black;  /* 设置字体颜色为黑色 */
        display: flex;
        justify-content: center;  /* 水平居中 */
        align-items: center;  /* 垂直居中 */
        font-family: "KaiTi", "楷体", "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
        width: 100px; /* 设置按钮宽度 */
        height: 30px !important;/* 设置按钮高度 */
      }
  .login_button {
    padding: 20px !important;  /* 减少按钮内边距 */
    font-size: 20px !important;  /* 调整按钮字体大小 */
    font-weight: bold !important;
    color: #000 !important;
    background-color: #E97A29 !important;
    border: none !important;
    border-radius: 6px !important;
    cursor: pointer !important;
    width: 100% !important;  /* 确保按钮宽度适应所有屏幕 */
    font-family: "Microsoft YaHei", "微软雅黑", "Heiti SC", "黑体", "Arial", sans-serif;
    margin-top: 15% !important;
    &:hover {
      background-color: #ff8c3d !important;
    }
  }
}

  }
  </style>
  
  <style lang="scss" scoped>
  ::v-deep .el-form-item__error {
    color: #ff4d4f;
    font-size: 12px;
    position: absolute;
    bottom: 2px; /* 距离输入框底部的距离 */
    left: 30px; /* 向右移动的距离 */
    width: calc(100% - 30px); /* 根据右移调整宽度 */
    text-align: left;
    margin-top: -10px;
  }



</style>

  

  
  
  
  
  
  