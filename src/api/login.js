import axios from 'axios';

const LOGIN_BASE_URL = "/api/login";

const LOGINAPI = {
  /** 获取验证码 */
  getCaptcha() {
    return axios
      .get(`${LOGIN_BASE_URL}/captcha`)
      .then((response) => {
        console.log("success");
        return response.data;
      })
      .catch((error) => {
        console.error("获取验证码失败:", error);
        throw error;
      });
  },

  /** 用户登录 */
  login(data) {
    return axios
      .post(`${LOGIN_BASE_URL}/in`, data)
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        console.error("登录请求失败:", error);
        throw error;
      });
  },

    /** 用户注册 */
    register(data) {
      return axios
        .post(`${LOGIN_BASE_URL}/register`, data)
        .then((response) => {
          return response.data;
        })
        .catch((error) => {
          console.error("注册请求失败:", error);
          throw error;
        });
    },

    /** 邮箱验证码发送 */
    mail(data) {
      console.log(data);
      return axios
        .post(`${LOGIN_BASE_URL}/mail`, data)
        .then((response) => {
          return response.data;
        })
        .catch((error) => {
          console.error("验证码发送失败:", error);
          throw error;
        });
    },

        /** 忘记密码 */
        forget(data) {
          return axios
            .post(`${LOGIN_BASE_URL}/forget`, data)
            .then((response) => {
              return response.data;
            })
            .catch((error) => {
              console.error("注册请求失败:", error);
              throw error;
            });
        },
};

export default LOGINAPI;

/**
 * 登录请求参数
 * @typedef {Object} LoginData
 * @property {string} username - 用户名
 * @property {string} password - 密码
 * @property {string} captchaKey - 验证码缓存key
 * @property {string} captchaCode - 验证码
 */

/**
 * 验证码响应
 * @typedef {Object} CaptchaResult
 * @property {string} captchaKey - 验证码缓存key
 * @property {string} captchaBase64 - 验证码图片Base64字符串
 */
