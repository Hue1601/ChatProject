<template>
  <div class="container">
    <comp-header @search="handleSearch" />
    <div class="list">
    <h2 style="text-align: center; margin-top: 3px">Danh sách nhân viên</h2>
    <div class="row-list">
        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="search" 
        @keyup.enter="SearchUser">
        <!-- <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="search" 
        @input="SearchUser"> -->
        <RouterLink to="/user/add" class="btn btn-outline-primary">Add</RouterLink> 
    </div>

    <div class="card-header">
      <table class="table" >
        <thead>
          <tr>
            <th scope="col">STT</th>
            <th scope="col">Username</th>
            <th scope="col">Password</th>
            <th scope="col">SDT</th>
            <th scope="col">Giới tính</th>
            <th scope="col">Địa chỉ</th>
            <th scope="col">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item,index) in users" :key="index">
            <td>{{ index + 1 + ((currentPage - 1) * pageSize) }}</td>
            <td>{{ item.username }}</td>
            <td>{{ item.pass }}</td>
            <td>{{ item.sdt }}</td>
            <td>{{ item.gioitinh }}</td>
            <td>{{ item.diachi }}</td>
            <td>
              <RouterLink :to="`/user/update/${item.id}`" class="btn btn-outline-primary">
              Update</RouterLink>
              <button @click="DeleteUser(item)" class="btn btn-outline-primary" style="margin-left: 3px">
                Delete
              </button>        
            </td>
          </tr>
        </tbody>
      </table>
    </div>
      <nav aria-label="Page navigation example">
        <ul class="pagination">
          <li  class="page-item" :class="{ disabled: currentPage === 1 }" @click="Pagination(currentPage - 1)">
            <a class="page-link" >Previous</a>
          </li>
          <li v-for="page in totalPages"  :key="page"  class="page-item" :class="{ active: page === currentPage }" >
            <a class="page-link"  @click="Pagination(page)" >
              {{ page }}
            </a>
          </li>
          <li  class="page-item" :class="{ disabled: currentPage === totalPages }" 
            @click="Pagination(currentPage + 1)" >
            <a class="page-link" >Next</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from 'axios';
 //const baseUrl = "http://localhost:3000/users";
 const baseUrl = "http://localhost:8080/api";
export default {
  name: "list-user",
  data() {
    return {
      users: [],
      // username:'',
      search:'',
     currentPage: 1,
      totalPages: 1,
      pageSize: 8,
    };
  },
  components: {
    CompHeader,
  },
  methods: {
    // handleSearch(value){
    //   console.log("handling search event" , value);
    //   this.users = value;
    // },
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
    async DeleteUser(itemDelete) {
   if (confirm('Are you sure you want to delete this user?')) {
        try {
          await axios.delete(`${baseUrl}/delete-user/${itemDelete.id}`);
          this.GetListUser(this.currentPage);
          alert('User deleted successfully!');
        } catch (error) {
          console.error(error);
          alert('Error deleting user.');
        }
      }
    },
     Pagination(page) {
      if (page > 0 && page <= this.totalPages) {
        this.GetListUser(page);
      }
    },
  },
  mounted() {
    this.GetListUser();
  }
};
</script>

<style>
.pagination{
   padding-left: 38%; 
}
.btn-outline-primary{
  margin-right: 13px;
}
.list{
  height: 90vh;
  border: 1px solid  #c4c4c4;
}
.row-list {
  margin-top: 5px;
    display: flex;
   padding-left: 60%; 

}
.card-header{
  height: 72vh;
  margin-left: 20px;
  margin-right: 20px;
}
</style>
