<template>
  <div>
    <h2 style="text-align:center;margin-top:5px">Update thông tin nhân viên</h2>
    <form @submit.prevent="UpdateUser">
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
        <button type="submit" class="btn btn-success" style="margin-top:5px; margin-left:45%">Update</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
//const baseUrl = "http://localhost:3000/users";
 const baseUrl = "http://localhost:8080/api";
export default {
  name: 'update-user',
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
        id: null,
        username: "",
        pass: "",
        sdt: "",
        gioitinh: "",
        diachi: ""
      }
    };
  },
  mounted() {
    this.getUserData();
  },
  methods: {
  
     async getUserData() {
      try {
        const id = this.$route.params.id;  
        // const response = await axios.get(`${baseUrl}/${id}`);
        const response = await axios.get(`${baseUrl}/push-update-user/${id}`);
        this.users = response.data;
      } catch (error) {
        console.error(error);
      }
    },
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
    async UpdateUser() {
      // if (confirm('Are you sure you want to update this user?')) {
        if (this.validate()) {
        try {
          //await axios.put(`${baseUrl}/${this.users.id}`, this.users);
          await axios.put(`${baseUrl}/update-user/${this.users.id}`, this.users);
          this.$router.push("/user"); 
          this.resetForm(); 
        } catch (error) {
          console.error(error);
         
        }
      }
    },
    resetForm() {
      this.users = {
        username: '',
        pass: '',
        sdt: '',
        diachi: '',
        gioitinh: ''
      };
    }
  }
};
</script>

<style>

</style>
