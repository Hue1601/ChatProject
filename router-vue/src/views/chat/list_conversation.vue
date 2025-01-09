<template>
  <div class="container">
    <comp-header />
    <div class="list_conversation">
      <div style="display: flex; margin-top: 10px">
        <input class="search_bar" placeholder="Search" v-model="searchQuery" />
        <img
          src="/images/message-add.png"
          alt="Add"
          style="width: 40px; height: 40px"
        />
      </div>
      <div class="body" id="conversationListContainer"></div>
    </div>
  </div>
</template>
<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from "axios";
import { chatState } from "/newwave/ChatProject/router-vue/src/JS/chat.js";
const baseUrl = "http://localhost:8080/api/list-conversation";

export default {
  name: "web-chat",
  components: {
    CompHeader,
  },
  data() {
    return {};
  },
  methods: {
    async getListConversation() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`${baseUrl}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200) {
          this.conversations = response.data;
          this.renderConversations();
        }
      } catch (error) {
        console.log("Error fetching conversations:", error);
      }
    },

    //function to gen list conversation
    renderConversations() {
      const container = document.getElementById("conversationListContainer");
      container.innerHTML = "";

      this.conversations.forEach((conversation) => {
        const chatPeopleDiv = document.createElement("div");
        chatPeopleDiv.className = "chat_people";

        const images = document.createElement("img");
        images.src = "https://ptetutorials.com/images/user-profile.png";
        images.className = "chat_img";

        const chatIbDiv = document.createElement("div");
        chatIbDiv.className = "chat_ib";

        const span = document.createElement("span");
        span.className = "conversation_name";
        span.innerText = conversation.conversationName.replace(/"/g, "");

        const p = document.createElement("p");
        // if it's a new conversation
        if (
          conversation.lastMessage &&
          conversation.lastMessage.trim() !== ""
        ) {
          p.innerText = conversation.lastMessage;
        } else {
          p.style.display = "none";
        
        }

        chatIbDiv.appendChild(span);
        chatIbDiv.appendChild(p);

        const chatTimeDiv = document.createElement("div");
        chatTimeDiv.className = "chat_date";

        // if it's a old conversation
        if (
          !conversation.lastMessage ||
          conversation.lastMessage.trim() === ""
        ) {
          chatTimeDiv.innerText = this.formatTime(conversation.createdAt);
        } else {
          chatTimeDiv.innerText = this.formatTime(conversation.lastMessageTime);
        }

        chatPeopleDiv.appendChild(images);
        chatPeopleDiv.appendChild(chatIbDiv);
        chatPeopleDiv.appendChild(chatTimeDiv);

        chatPeopleDiv.onclick = () => {
          chatState.conversationName = conversation.conversationName;
          chatState.chatType = conversation.type;

          this.loadConversationDetails(conversation.id);
        };

        container.appendChild(chatPeopleDiv);
      });
    },

    loadConversationDetails(conversationId) {
      this.$router.push({
        name: "conversation_detail",
        params: { conversationId },
      });
    },
    // formatTime(time) {
    //   const date = new Date(time)
    //      const hours = date.getHours()
    //   const minutes = date.getMinutes()
    //   return `${hours} : ${minutes}`;
    // }
    formatTime(time) {
      const date = new Date(time);
      const now = new Date();
      const isToday = date.toDateString() === now.toDateString();

      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");

      return isToday
        ? `${hours}:${minutes}`
        : `${date.toLocaleDateString()} ${hours}:${minutes}`;
    },
  },
  mounted() {
    this.getListConversation();
  },
};
</script>
