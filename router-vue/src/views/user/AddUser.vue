<template>
  <h2 style="text-align: center; margin-top: 5px">Thêm mới nhân viên</h2>
  <div class="container">
    <label class="form-label">Username</label>
    <input type="text" class="form-control" v-model="users.username" v-bind:class="{'is-invalid':error.username}"/>
    <div class="invalid-feedback">{{error.username}}</div>

    <label class="form-label">Password</label>
    <input type="password" class="form-control" v-model="users.pass"  v-bind:class="{'is-invalid':error.pass}" />
     <div class="invalid-feedback">{{error.pass}}</div>

    <label class="form-label">SDT</label>
    <input type="text" class="form-control" v-model="users.sdt" v-bind:class="{'is-invalid':error.sdt}" />
     <div class="invalid-feedback">{{error.sdt}}</div>

    <label class="form-label">Địa chỉ</label>
    <input type="text" class="form-control" v-model="users.diachi" v-bind:class="{'is-invalid':error.diachi}"/>
    <div class="invalid-feedback">{{error.diachi}}</div>

    <label class="form-label">Giới tính</label>
    <!-- <input type="text" class="form-control" v-model="users.gioitinh" /> -->
    <select class="form-select" v-model="users.gioitinh" aria-label="Default select example" v-bind:class="{'is-invalid':error.gioitinh}">
       <div class="invalid-feedback">{{error.gioitinh}}</div>

   <option disabled value="">Chọn giới tính</option>
   <option value="Nam">Nam</option>
   <option value="Nữ">Nữ</option>
   </select>
    <button type="button" class="btn btn-success" style="margin-top: 5px; margin-left: 45%" @click="AddUser">
      Thêm mới
    </button>
  </div>
</template>

<script>
import axios from "axios";
//const baseUrl = "http://localhost:3000/users";
const baseUrl = "http://localhost:8080/api";
export default {
  name: "add-user",
  data() {
    return {
      error:{
          username: "",
        pass: "",
        sdt: "",
        diachi: "",
        gioitinh: "",
      },
      users: {
        username: "",
        pass: "",
        sdt: "",
        diachi: "",
        gioitinh: "",
      },
    };
  },
  methods: {
    async validate(){
       this.error={
          username: "",
        pass: "",
        sdt: "",
        diachi: "",
        gioitinh: "",
      }
         if(!this.users.username){
          this.error.username="Please fill in here"
         }
         if(!this.users.pass){
          this.error.pass="Please fill in here"
         }
         if(!this.users.sdt){
          this.error.sdt="Please fill in here"
         }
         if(!this.users.diachi){
          this.error.diachi="Please fill in here"
         }
         if(!this.users.gioitinh){
          this.error.gioitinh="Please fill in here"
         }
    },
    async AddUser() {
      try {
        if(this.validate()){
        // if (confirm("Are you sure you want to add this user?")) {
          //await axios.post(baseUrl, this.users);
          await axios.post(`${baseUrl}/add-user`, this.users);
          // alert("User added successfully!");
          this.$router.push("/user");
          this.resetForm();
        // }
        }
        
        
      } catch (error) {
        console.error(error);
      }
    },
    resetForm() {
      this.users = {
        username: "",
        pass: "",
        sdt: "",
        diachi: "",
        gioitinh: "",
      };
    },
  },
};
</script>

<style>
</style>
