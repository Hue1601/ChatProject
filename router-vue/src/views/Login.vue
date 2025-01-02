<template>
  <div class="container">
    <h2>ĐĂNG NHẬP</h2>

    <div class="form-outline mb-4">
      <label class="form-label" for="username">Account</label>
      <input id="username" type="text" class="form-control" v-model="users.username"/>
    </div>

    <div class="form-outline mb-4">
      <label class="form-label" for="password">Password</label>
      <input id="password" type="password" class="form-control" v-model="users.password"/>
    </div>

    <button type="submit" class="btn btn-primary btn-block mb-4" style="margin-left: 33%"  @click="login()">
      <span>ĐĂNG NHẬP</span>
    </button>

    <div v-if="errorMessage" class="alert alert-danger" role="alert">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import axios from "axios";

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
      const username = document.getElementById('username').value.trim();
       const password = document.getElementById('password').value.trim();
     if(!username || !password){
             this.errorMessage = "Please fill this form";
             return;
        }
  try {
     
        // Gửi yêu cầu API với thông tin username và password
        const response = await axios.post(baseUrl, this.users);
          // const tokennnnn = response.data.token; 
          // console.log("tokennnnn:", tokennnnn);
        if (response.ok) {
          const token = response.data.token; 
          console.log("Token:", token);

          // Lưu token vào localStorage 
            localStorage.setItem("token", token);

          // Chuyển hướng sang trang người dùng
          // this.$router.push("/user");
        }
        else{
           this.errorMessage = response.data.messagemessage;
           console.log("messagemessage:", response.data.messagemessage);
        }
      } catch (err) {
        console.error("Error:", err);
        this.errorMessage = "loi";
      }
    },
  },
};
</script>

<style scoped>
.container {
  width: 25%;
  margin-top: 10px;
}
h2 {
  text-align: center;
}
</style>
