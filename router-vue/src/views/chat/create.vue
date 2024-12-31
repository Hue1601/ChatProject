<template>
  <div class="container">
    <comp-header />
<div class="list">
    <div class="border_button">
        <button  class="btn btn-outline-primary" @click="setChatType('group')" id="typeIsGroup">Group</button> 
        <button  class="btn btn-outline-primary" @click="setChatType('private')" id="typeIsPrivate">Private</button> 
    </div>

    <div class="card-header" id="tableListUser">
      <div class="action_create">
         <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="search" 
        @keyup.enter="SearchUser">
         <button  class="btn btn-outline-primary">Create</button> 
      </div>
      <table class="table" >
        <tbody>
          <tr v-for="(item,index) in users" :key="index">
            <td>{{ item.username }}</td>
            <td class="checkbox"> <input type="checkbox" @click="handleCheckbox()"></td>
          </tr>
        </tbody>
      </table>
    </div>

    </div>
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from 'axios';
const baseUrl = "http://localhost:8080/api";
export default {
  name: "create",
  data() {
    return {
      users: [],
      // username:'',
      search:'',
  
    };
  },
  components: {
    CompHeader,
  },
  methods: {
    setChatType(type) {
      sessionStorage.setItem("chatType", type);
      var typeIsPrivate = document.getElementById("typeIsPrivate")
      var typeIsGroup = document.getElementById("typeIsGroup")
      var tableListUser = document.getElementById("tableListUser")
      if(typeIsGroup ){
          tableListUser.style.display = "block";
           typeIsPrivate.style.display = "none";
      }else {
         tableListUser.style.display = "block";
           typeIsGroup.style.display = "none";
      }
    },
 async handleCheckbox(){
      if(this.type === 'private'){
        console.log("type: ")
      }else{
        console.log("gr: " )
      }
   },
    async SearchUser() {
      try {
         const response = await axios.get(`${baseUrl}/search`, {
        params: { keyword: this.search }
        });
        this.users = response.data;
      } catch (error) {                                                                                                                                          
        console.error("Search error:", error);
      }
    },
    
      async GetListUser(page = 1) {
      try {
        const response = await axios.get(`${baseUrl}/pagination`, {
          params: { p: page - 1 } 
        });
        this.users = response.data.users;
        this.totalPages = response.data.totalPages;
        this.currentPage = response.data.currentPage + 1; 
      } catch (error) {
        console.error(error);
      }
    },
  
  },
  mounted() {
    this.GetListUser();
  }
};
</script>

