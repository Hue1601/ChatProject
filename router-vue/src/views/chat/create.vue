<template>
  <div class="container">
    <comp-header />
    <div class="list">
      <div class="border_button">
        <button
          class="btn btn-outline-primary"
          @click="setChatType('group')"
          id="btnGroup"
        >
          Group
        </button>
        <button
          class="btn btn-outline-primary"
          @click="setChatType('private')"
          id="btnPrivate"
        >
          Private
        </button>
      </div>

      <div class="card-header" id="tableListUser">
        <div class="action_create">
          <input
            class="form-control me-2"
            type="search"
            placeholder="Search"
            aria-label="Search"
            v-model="search"
            @keyup.enter="SearchUser"
          />
          <button class="btn btn-outline-primary" @click="createConversation()">
            Create
          </button>
        </div>
        <table class="table">
          <tbody id="listUser"></tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from "axios";
import { chatState } from "/newwave/ChatProject/router-vue/src/JS/chat.js";
const baseUrl = "http://localhost:8080/api";
export default {
  name: "create",
  data() {
    return {
      users: [],
      search: "",
      
    };
  },
  components: {
    CompHeader,
  },
  methods: {
    setChatType(type) {
      chatState.chatType = type;
      var btnPrivate = document.getElementById("btnPrivate");
      var btnGroup = document.getElementById("btnGroup");
      var tableListUser = document.getElementById("tableListUser");
      tableListUser.style.display = "block";
      if (type == "group") {
        btnGroup.classList.add("active");
        btnPrivate.classList.remove("active");
      } else {
        btnPrivate.classList.add("active");
        btnGroup.classList.remove("active");
      }
       const checkboxes = document.querySelectorAll(".checkboxs");
    checkboxes.forEach((checkbox) => {
      checkbox.checked = false;
    });
    chatState.selectedUsers = [];
    },

    async GetListUser() {
      try {
        const ownerCode = localStorage.getItem("ownerCode");
        const body = document.getElementById("listUser");
          body.innerHTML = "";
        const response = await axios.get(`${baseUrl}/list`);

        if (response.status == 200) {
          this.users = response.data;

          this.users.forEach((user) => {
            const tr = document.createElement("tr");

            const username = document.createElement("td");
            username.innerText = user.username;

            const borderCheckbox = document.createElement("td");
            borderCheckbox.className = "checkbox";

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.className = "checkboxs";
            checkbox.setAttribute("userCode", user.id);

            checkbox.addEventListener("click", (event) => {
              this.handleCheckbox(event, user);
            });

            borderCheckbox.appendChild(checkbox);
            if (user.id === Number(ownerCode)) {
              username.className = "disable";
              checkbox.classList.add("disable");
            }

            tr.appendChild(username);
            tr.appendChild(borderCheckbox);

            body.appendChild(tr);
          });
        }
      } catch (error) {
        console.error(error);
      }
    },
    async handleCheckbox(event, user) {
      const chatType = chatState.chatType;
  
      if (chatType === "private") {
        const checkboxes = document.querySelectorAll(".checkboxs");

        // Uncheck all checkboxes except the one being clicked
        checkboxes.forEach((cb) => {
          if (cb !== event.target) {
            cb.checked = false;
          }
        });
        chatState.selectedUsers = event.target.checked ? [user] : [];
        chatState.title = JSON.stringify(chatState.selectedUsers[0].username);
        chatState.memberCode = JSON.stringify(chatState.selectedUsers[0].id);
      }else if (chatType === "group") {
     
      if (event.target.checked) {
        chatState.selectedUsers.push(user); // Add user to the list
       
      } else {
        chatState.selectedUsers = chatState.selectedUsers.filter(
          (selectedUser) => selectedUser.id !== user.id
        ); // Remove user from the list
      }
       chatState.memberCode = chatState.selectedUsers.map((selectedUser) => selectedUser.id);
      }
      console.log("select " + JSON.stringify(chatState.selectedUsers));
    },

    async createConversation() {
      const chatType = chatState.chatType;
      const title = chatState.title;
      const userCode = chatState.memberCode;
          console.log("userCode " + userCode);
      const ownerCode = localStorage.getItem("ownerCode");

      try {
        if (chatType === "private") {
          const payload = {
            name: title,
            type: chatType,
            member: [Number(ownerCode), Number(userCode)],
          };

          const response = await axios.post(
            `${baseUrl}/create-conversation`,
            payload
          );

          if (response.status === 200) {
            this.$router.push("/list_conversation");
          }
        } else {
          this.$router.push("/setting");
        }
      } catch (error) {
        console.error("Error creating conversation:", error.response || error);
      }
    },
  },
  mounted() {
    this.GetListUser();
  },
};
</script>

