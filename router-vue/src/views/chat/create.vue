<template>
  <div class="container">
    <comp-header />
    <div class="list">
      <div class="border_button">
        <button class="btn btn-outline-primary" @click="setChatType('group')" id="btnGroup">
          Group
        </button>
        <button class="btn btn-outline-primary" @click="setChatType('private')" id="btnPrivate" >
          Private
        </button>
      </div>

      <div class="card-header" id="tableListUser">
        <div class="action_create">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="search" @keyup.enter="SearchUser" />
          <button class="btn btn-outline-primary">Create</button>
        </div>
        <table class="table">
          <tbody>
            <tr v-for="(item, index) in users" :key="index">
              <td>{{ item.username }}</td>
              <td class="checkbox">
                <input type="checkbox" @click="handleCheckbox($event)" id="checkbox" class="checkbox"/>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from "axios";
const baseUrl = "http://localhost:8080/api";
export default {
  name: "create",
  data() {
    return {
      users: [],
      // username:'',
      search: "",
    };
  },
  components: {
    CompHeader,
  },
  methods: {
    setChatType(type) {
      sessionStorage.setItem("chatType", type);
      var btnPrivate = document.getElementById("btnPrivate");
      var btnGroup = document.getElementById("btnGroup");
      var tableListUser = document.getElementById("tableListUser");
       tableListUser.style.display = "block";
      if (type == "group") {
          btnGroup.classList.add('active');
        btnPrivate.classList.remove('active');
      } else {
        btnPrivate.classList.add('active');
         btnGroup.classList.remove('active');
        
      }
    },
  async handleCheckbox(event) {
  const chatType = sessionStorage.getItem("chatType");
  if (chatType === "private") {
    const checkboxes = document.querySelectorAll(".checkbox");

    checkboxes.forEach((cb) => {
      // Nếu checkbox hiện tại không phải checkbox được click, bỏ chọn nó
      if (cb !== event.target) {
        cb.checked = false;
      }
    });
  } 
},

    async SearchUser() {
      try {
        const response = await axios.get(`${baseUrl}/search`, {
          params: { keyword: this.search },
        });
        this.users = response.data;
      } catch (error) {
        console.error("Search error:", error);
      }
    },

    async GetListUser(page = 1) {
      try {
        const response = await axios.get(`${baseUrl}/pagination`, {
          params: { p: page - 1 },
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
  },
};
</script>
<style>

</style>
