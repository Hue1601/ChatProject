<template>
  <div class="container">
    <h2>ĐĂNG NHẬP</h2>
    <form @submit.prevent="login">
      <div class="form-outline mb-4">
        <label class="form-label" for="username">Account</label>
        <input id="username" type="text" class="form-control" v-model="users.username" />
      </div>

      <div class="form-outline mb-4">
        <label class="form-label" for="password">Password</label>
        <input id="password" type="password" class="form-control" v-model="users.pass" />
      </div>

      <div class="row mb-4">
        <div class="col d-flex justify-content-center">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" v-model="rememberMe" />
            <label class="form-check-label">Remember me</label>
          </div>
        </div>

        <div class="col">
          <a href="#!">Forgot password?</a>
        </div>
      </div>

      <button type="submit" class="btn btn-primary btn-block mb-4" :disabled="loading" style="margin-left:33%">
        <span v-if="loading">Logging in...</span>
        <span v-else>ĐĂNG NHẬP</span>
      </button> 

      <div v-if="errorMessage" class="alert alert-danger" role="alert">
        {{ errorMessage }}
      </div>  
    </form>
  </div>
</template>
<script>
import axios from 'axios';
 const baseUrl = "http://localhost:8080/api";
export default {
  name: "login",
  data() {
    return {
      users: {
        username: "",
        pass: ""
      },
      rememberMe: false,
      loading: false,
      errorMessage: ''
    };
  },
 methods: {
  async login() {
     this.loading = true;
    this.errorMessage = '';
    try {
      const response = await axios.post(`${baseUrl}/login`, this.users); 
      if (response.status === 200) {
         localStorage.setItem("username", response.data.username);
          localStorage.setItem("pass", response.data.pass);
          

        const userLogin = {
          id: response.data.id,
          username: response.data.username
        };
        console.log("User: " , userLogin);
        this.$store.commit('setUser', userLogin);

        this.$router.push("/user");
      } else {
        this.errorMessage = "Incorrect account or password"; 
      }
    } catch (err) {
      if (err.response && err.response.status === 401) {
        this.errorMessage = "Incorrect account or password"; 
      } else {
        this.errorMessage = "Error, please try again"; 
      }
    } finally {
      this.loading = false;
    }
  }
 },


  mounted() {
    console.log("mounted() called .......");
  }
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
