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
            @input="filterUsers"
          />
          <button class="btn btn-outline-primary" @click="createConversation()">
            Create
          </button>
        </div>
        <div id="listUser"></div>
      </div>
    </div>
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from "axios";
import { chatState } from "../../JS/chat.js";
import { chatTypeEnum, CommonEnum } from "../../JS/Common.js";

const baseUrl = "http://localhost:8080/api";

export default {
  name: "create",
  data() {
    return {
      users: [],
      filteredUsers: [],
      search: "",
      token: localStorage.getItem("token"),
    };
  },
  components: {
    CompHeader,
  },
  mounted() {
    this.GetListUser();
  },
  methods: {
    //set chat type for btn group/private
    setChatType(type) {
      chatState.chatType = type;
      const btnPrivate = document.getElementById("btnPrivate");
      const btnGroup = document.getElementById("btnGroup");
      const tableListUser = document.getElementById("tableListUser");
      tableListUser.style.display = CommonEnum.BLOCK;

      if (type === chatTypeEnum.GROUP) {
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
        const response = await axios.get(`${baseUrl}/user/list`, {
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        });

        if (response.status === 200) {
          this.users = response.data;
          this.filteredUsers = this.users;

          const listUser = document.getElementById("listUser");
          this.users.forEach((user) => {
            const div = document.createElement("div");
            div.className = "user_infor";

            const username = document.createElement("span");
            username.innerText = user.username;

            const cb = document.createElement("input");
            cb.type = "checkbox";
            cb.onclick = (event) => this.handleCheckbox(event, user);

            div.appendChild(username);
            div.appendChild(cb);

            listUser.appendChild(div);
          });

          if (chatState.chatType === chatTypeEnum.GROUP) {
            const memberAlredayInGroup = chatState.memberInGroup.some(
              (member) => member.id === user.id
            );
            if (memberAlredayInGroup) {
            }
          }
        }
      } catch (error) {
        alert("Error fetching users:", error);
      }
    },

    filterUsers() {
      const searchLower = this.search.toLowerCase();
      this.filteredUsers = this.users.filter((user) =>
        user.username.toLowerCase().includes(searchLower)
      );
    },

    isOwner(userId) {
      const ownerCode = Number(localStorage.getItem("ownerCode"));
      return userId === ownerCode;
    },

    async handleCheckbox(event, user) {
      const chatType = chatState.chatType;

      if (chatType === chatTypeEnum.PRIVATE) {
        const checkboxes = document.querySelectorAll(".checkboxs");

        checkboxes.forEach((cb) => {
          if (cb !== event.target) {
            cb.checked = false;
          }
        });
        chatState.selectedUsers = event.target.checked ? [user] : [];

        return;
      }

      // handle checkbox with chat type is group
      if (event.target.checked) {
        chatState.selectedUsers.push(user);
      } else {
        chatState.selectedUsers = chatState.selectedUsers.filter(
          (selectedUser) => selectedUser.id !== user.id
        );
      }
      chatState.memberCode = chatState.selectedUsers.map(
        (selectedUser) => selectedUser.id
      );
    },

    async createConversation() {
      const chatType = chatState.chatType;
      const title = JSON.stringify(chatState.selectedUsers[0].username);
      const userCode = JSON.stringify(chatState.selectedUsers[0].id);
      const ownerCode = localStorage.getItem("ownerCode");

      try {
        if (chatType === chatTypeEnum.PRIVATE) {
          //Filter all users before creating new chats
          const isUserExist = await this.filterExistUser(title);

          if (isUserExist) {
            return;
          }

          await this.createPrivateChat(title, chatType, ownerCode, userCode);
        }

        //if chat type is group
        if (chatType !== chatTypeEnum.PRIVATE) {
          this.$router.push("/setting");
        }
      } catch (error) {
        alert("Error creating conversation:", error.response || error);
      }
    },

    async filterExistUser(title) {
      const response = await axios.get(`${baseUrl}/chat/list-conversation`, {
        headers: {
          Authorization: `Bearer ${this.token}`,
        },
      });

      if (response.status === 200) {
        const existingConversation = response.data.find(
          (conversation) =>
            conversation.type === "private" &&
            conversation.conversationName.includes(title)
        );

        if (existingConversation) {
          alert("A private chat with this user already exists.");
          return true;
        }
      }
      return false;
    },
    async createPrivateChat(title, chatType, ownerCode, userCode) {
      const payload = {
        name: title,
        type: chatType,
        member: [Number(ownerCode), Number(userCode)],
      };
      try {
        const createResponse = await axios.post(
          `${baseUrl}/chat/create-conversation`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          }
        );

        if (createResponse.status === 200) {
          //clear chat state after create
          chatState.chatType = null;
          chatState.selectedUsers = null;

          this.$router.push("/list_conversation");
        }
      } catch {
        alert("Error create private conversation");
      }
    },
  },
};
</script>
