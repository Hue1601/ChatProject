<template>
  <div class="container">
    <h2>ĐĂNG NHẬP</h2>

    <div class="form-outline mb-4">
      <label class="form-label" >Account</label>
      <input id="username" type="text" class="form-control" v-model="users.username" />
    </div>

    <div class="form-outline mb-4">
      <label class="form-label">Password</label>
      <input id="password" type="password" class="form-control" v-model="users.password" />
    </div>

    <button type="submit" class="btn btn-primary btn-block mb-4" style="margin-left: 33%" @click="login()"  >
      <span>ĐĂNG NHẬP</span>
    </button>

    <div v-if="errorMessage" class="alert alert-danger" role="alert">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { chatState } from '/newwave/ChatProject/router-vue/src/JS/chat.js';
const baseUrl = "http://localhost:8080/api/login";

export default {
  name: "login",
  data() {
    return {
      users: {
        username: "",
        password: "",
      },
      rememberMe: false,
      errorMessage: "",
    };
  },
  methods: {
  async login() {
      this.errorMessage = "";
      const username = document.getElementById("username").value.trim();
      const password = document.getElementById("password").value.trim();
      if (!username || !password) {
        this.errorMessage = "Please fill this form";
        return;
      }
      try {
        let response = await axios.post(baseUrl, this.users);

        if (response.status === 200) {
          localStorage.setItem("token", response.data.token)
          chatState.ownerCode = response.data.id;
          this.$router.push("/verify");
        }
      } catch (err) {
        this.errorMessage = err.message;
      }
    },
  },
};
</script>

<style scoped>

</style>
