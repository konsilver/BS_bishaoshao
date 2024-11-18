import { defineStore } from "pinia";

export const useGlobalStore = defineStore("global", {
  state: () => ({
    search_key: "",
    user_id:"",
  }),
  actions: {
    setsearch_key(value) {
      this.search_key = value;
    },
  },
});
