import axios from "axios";
import Cookies from "js-cookie";
const SUBSCRIBE_BASE_URL = "/api/subscribe";

const axiosInstance = axios.create();
  axiosInstance.interceptors.request.use(config => {
    const token = Cookies.get('token');
    if (token) {
      config.headers.Authorization = token;
    }
    return config;
  }, error => {
    return Promise.reject(error);
  });

const SUBSCRIBEAPI = {
    async sub(id) { // 添加 async 修饰
        try {
            const response = await axiosInstance.post(`${SUBSCRIBE_BASE_URL}/sub`, id); 
            return response.data; 
        } catch (error) {
            console.error('Cancel subsription failed:', error.response?.data || error.message);
            throw error;
        }
    },
    async add(id) { // 添加 async 修饰
        try {
            const response = await axiosInstance.post(`${SUBSCRIBE_BASE_URL}/add`,id); 
            return response.data; 
        } catch (error) {
            console.error('Add subsription failed:', error.response?.data || error.message);
            throw error;
        }
    },
    async getany() { // 添加 async 修饰
      try {
          const response = await axiosInstance.get(`${SUBSCRIBE_BASE_URL}/all`); 
          return response.data; 
      } catch (error) {
          console.error('get all failed:', error.response?.data || error.message);
          throw error;
      }
    },
    async getlike() { // 添加 async 修饰
      try {
          const response = await axiosInstance.get(`${SUBSCRIBE_BASE_URL}/like`); 
          return response.data; 
      } catch (error) {
          console.error('get like failed:', error.response?.data || error.message);
          throw error;
      }
    },
    async isornot(id) { // 添加 async 修饰
      try {
          const response = await axiosInstance.post(`${SUBSCRIBE_BASE_URL}/issub`,id); 
          return response.data; 
      } catch (error) {
          console.error('get sure failed:', error.response?.data || error.message);
          throw error;
      }
    },

};

export default SUBSCRIBEAPI;