import axios from "axios";
import Cookies from "js-cookie";
const SEARCH_BASE_URL = "/api/search";

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

const SEARCHAPI = {
    async logout() { // 添加 async 修饰
        try {
            const response = await axiosInstance.delete('/api/login/out'); 
            console.log('Logout successful:', response.data);
            return response.data; 
        } catch (error) {
            console.error('Logout failed:', error.response?.data || error.message);
            throw error;
        }
    },

    async search(data) { // 添加 async 修饰
        try {
            const response = await axiosInstance.post(`${SEARCH_BASE_URL}/lookfor`,data); 
            return response.data; 
        } catch (error) {
            console.error('Search failed:', error.response?.data || error.message);
            throw error;
        }
    },

    async getdetail(id) { // 添加 async 修饰
      try {
          const response = await axiosInstance.post(`${SEARCH_BASE_URL}/lookin`,id); 
          return response.data; 
      } catch (error) {
          console.error('GetDetails failed:', error.response?.data || error.message);
          throw error;
      }
    },

    async gethistory(id) { // 添加 async 修饰
      try {
          const response = await axiosInstance.post(`${SEARCH_BASE_URL}/lookback`,id); 
          return response.data; 
      } catch (error) {
          console.error('GetDetails failed:', error.response?.data || error.message);
          throw error;
      }
    }
};

export default SEARCHAPI;