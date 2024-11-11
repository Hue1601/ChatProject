<template>
  <div class="row">
    <div class="col-2" style="text-align:center">
      <h3>WEB CHAT</h3>
    </div>
    <div class="col-6">
         <div class="input-group rounded mt-2" >
        <div class="d-flex" role="search">
        <!-- <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="search"> -->
        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="search" v-on:keyup.enter="SearchUser">
        <button class="btn btn-outline-light" @click="SearchUser">Search</button>
      </div>
      </div>    
    </div>
    <div class="col-4">
      <nav class="navbar navbar-expand-lg">
        <ul class="navbar-nav">
          <li class="nav-item">
            <RouterLink to="/user">
              <a class="btn" style="color: white">User</a>
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/list_conversation" style="color: white">
              <a class="btn" style="color: white">Chat</a>
            </RouterLink>
          </li>
        </ul>
        <a class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="https://ptetutorials.com/images/user-profile.png" alt="User">
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" >Setting</a></li>
            <li><hr class="dropdown-divider"></li>
            <li>
              <RouterLink to="/" style="color: white">
              <a class="dropdown-item" href="#">Checkout</a>
              </RouterLink>
              </li>
          </ul>
      </nav>
    </div>
  </div>
 <ul class="list-group" v-for="(item,index) in users" :key="index">
          <li  class="list-group-item">{{ item.username }}</li>
        </ul>
</template>

<script>
import axios from 'axios';
const baseUrl = "http://localhost:8080/api";

export default {
  name: "comp-header",
  
  data() {
    return {
      users: [],
      search: ''
    };
  },
  methods: {
    async SearchUser() {
      console.log('search');
      try {
        const response = await axios.get(`${baseUrl}/search`, {
          params: { keyword: this.search }
        });
        this.users = response.data;
      } catch (error) {
        console.error("Search error:", error);
      }
     this.$emit("search", this.users);
    },
    async GetListUser() {
      try {
        const response = await axios.get(`${baseUrl}/list`);
        this.users = response.data;
      } catch (error) {
        console.error(error);
      }
    }
  },
  mounted() {
    // if (this.search === '') {
    //   this.GetListUser();
    // } else {
    //   this.SearchUser();
    // }
  }
};
</script>

<style>
.dropdown-toggle img{ max-width:45%;}
.row {
  color: white;
  background: rgb(44, 37, 93);
}
.dropdown-toggle{
  margin-left:45%
}
.dropdown-menu{
   margin-left:45%
}
.col-2 {
  padding-top: 10px;
}
.navbar-nav {
  padding-left: 1%;
}
</style>
