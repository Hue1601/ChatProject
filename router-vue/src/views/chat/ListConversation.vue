<template>
  <div class="container">
    <comp-header />
    <div class="list_conversation">
      <div style="display: flex; margin-top: 10px">
        <input
          class="search_bar"
          placeholder="Search"
          v-model="searchQuery"
          @input="filterConversations"
        />
    
      </div>
      <div class="body" id="conversationListContainer"></div>
    </div>
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
 import axios from "axios";

import { chatState } from "../../JS/chat.js";


const baseUrl = "http://localhost:8080/api/chat/list-conversation";

export default {
  name: "web-chat",
  components: {
    CompHeader,
  },
  data() {
    return {
      searchQuery: "", 
      conversations: [], 
      filteredConversations: [],
    };
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
          this.filterConversations();
        }
      } catch (error) {
        console.log("Error fetching conversations:", error);
      }
    },

    filterConversations() {
      const search = this.searchQuery.toLowerCase();
      this.filteredConversations = this.conversations.filter((conversation) =>
        conversation.conversationName.toLowerCase().includes(search)
      );
      this.renderConversations();
    },

    renderConversations() {
      const container = document.getElementById("conversationListContainer");
      container.innerHTML = "";

      this.filteredConversations.forEach((conversation) => {
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

        const lastMessage = document.createElement("p");

        const chatTimeDiv = document.createElement("div");
        chatTimeDiv.className = "chat_date";
        const p = document.createElement("p")
        p.className="chat_time";

        // if (conversation.lastMessage && conversation.lastMessage.trim() !== "") {
          if (conversation.lastMessageTime) {
          lastMessage.innerText = conversation.lastMessage;
          p.innerText = this.formatTime(conversation.lastMessageTime);
        } 
       else {
         p.innerText = this.formatTime(conversation.createdAt);
       }
         chatTimeDiv.appendChild(p)
        chatIbDiv.appendChild(span);
        chatIbDiv.appendChild(lastMessage);

        chatPeopleDiv.appendChild(images);
        chatPeopleDiv.appendChild(chatIbDiv);
        chatPeopleDiv.appendChild(chatTimeDiv);

        chatPeopleDiv.onclick = () => {
          chatState.conversationName = conversation.conversationName;
          chatState.chatType = conversation.type;
          chatState.conversationId = conversation.id;
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

    formatTime(time) {
      const date = new Date(time);
      const now = new Date();
      const isToday = date.toDateString() === now.toDateString();

      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");

      return isToday
        ? `${hours}:${minutes}`
        : `${date.toLocaleDateString()} `;
    },
  },
  mounted() {
    this.getListConversation();
  },
};
</script>
