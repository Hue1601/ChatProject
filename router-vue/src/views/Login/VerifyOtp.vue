<template>
  <div class="container">
    <h2>Xác thực OTP</h2>
    <div class="form-outline mb-4">
      <label class="form-label">OTP</label>
      <input class="form-control" v-model="otp.otp" />
    </div>
    <button
      type="submit"
      class="btn btn-primary btn-block mb-4"
      style="margin-left: 33%"
      @click="handleVerify()"
    >
      <span>ĐĂNG NHẬP</span>
    </button>
    <div v-if="errorMessage" class="alert alert-danger" role="alert">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import axios from "axios";
const baseUrl = "http://localhost:8080/api/verify";
export default {
  name: "verify",
  data() {
    return {
      otp: {
        username: sessionStorage.getItem("username"),
        otp: "",
      },
      errorMessage: "",
    };
  },
  methods: {
    async handleVerify() {
      this.errorMessage = "";
      const token = localStorage.getItem("token");
      try {
        const response = await axios.post(baseUrl, this.otp, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.status == 200) {
          this.$router.push("/user");
        }
      } catch (err) {
        this.errorMessage = err.response.data;
      }
    },
  },
};
</script>

<style>
</style>