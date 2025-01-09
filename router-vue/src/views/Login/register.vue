<template>
    <div class="container mt-5">
        <h2 class="text-center mb-4">Register</h2>
        <div class="mb-3">
            <label class="form-label">Username</label>
            <input class="form-control" id="username" placeholder="Enter your username" v-model="profile.username" >
        </div>
        <div class="mb-3">
            <label class="form-label">Password</label>
            <input type="password" class="form-control" id="pass" placeholder="Enter your password" v-model="profile.pass">
        </div>
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input class="form-control" id="email" placeholder="Enter your email" v-model="profile.email">
        </div>
        <div class="mb-3">
            <label class="form-label">SDT</label>
            <input class="form-control" id="sdt" placeholder="Enter your sdt" v-model="profile.sdt">
        </div>
        <label class="form-label">Gioi tinh</label>
         <div class="mb-3" style="display:flex">
            
            <div>
                <input type="radio" id="Nam" value="Nam" v-model="profile.gioitinh">
                <label for="Nam">Nam</label>
            </div>
            <div style="margin-left: 10px;">
                <input type="radio" id="Nữ" value="Nữ" v-model="profile.gioitinh">
                <label for="Nữ">Nữ</label>
            </div>
            
        </div>
        <button class="btn btn-primary btn-block" @click="register">Submit</button>
        <div id="error-message" class="mt-3 text-danger" style="display: none;"></div>
    </div>
</template>

<script>
import axios from "axios";

const baseUrl = "http://localhost:8080/api/authentication/register";
export default {
    data(){
   return{
    name:"register",
    profile:{
        username:"",
        pass:"",
        gioitinh:"",
        sdt:"",
        email:""
    }
   }
    },
    mounted(){

    },
    methods:{
      async register(){
         const username = document.getElementById('username').value.trim();
        const password = document.getElementById('pass').value.trim();
        const email = document.getElementById('email').value.trim();
          const sdt = document.getElementById('sdt').value.trim();
        const errorMessage = document.getElementById('error-message');

        if (!username || !password || !email) {
            errorMessage.textContent = 'Please fill all the fields';
            errorMessage.style.display = 'block';
            return;
        }
        try {
          const response = await axios.post(baseUrl, this.profile);
          if (response.status === 200) {
             this.$router.push("/")
          }
        } catch (error) {
            errorMessage.textContent =  'An error occurred. Please try again later.' 
        }
      }
    }
}
</script>

<style>

</style>
