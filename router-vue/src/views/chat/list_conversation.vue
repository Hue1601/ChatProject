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
const baseUrl = "http://localhost:8080/api/list-conversation";

export default {
  name: "web-chat",
  components: {
    CompHeader,
  },
  data() {
    return {
      conversationName: "",
      searchQuery: "",
    };
  },
  computed: {
    // Filter conversations based on searchQuery
    filteredConversations() {
      if (!this.searchQuery.trim()) {
        return this.conversations; // Return all conversations if no search query
      }
      return this.conversations.filter((conversation) =>
        conversation.conversationname
          .toLowerCase()
          .includes(this.searchQuery.toLowerCase())
      );
    },
  },
  methods: {
    async getListConversation() {

      try {
        const token = localStorage.getItem("token");
        const response = await axios.get( `${baseUrl}`,{
            headers: {
            Authorization: `Bearer ${token}`,
          },
          }
        );
       
        if (response.status === 200 ) {
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
        span.innerText = conversation.conversationName;

        const p = document.createElement("p");
        p.innerText = "last message";

        chatIbDiv.appendChild(span);
        chatIbDiv.appendChild(p);

        const chatTimeDiv = document.createElement("div");
        chatTimeDiv.className = "chat_date";
        chatTimeDiv.innerText = "11:11";

        chatPeopleDiv.appendChild(images);
        chatPeopleDiv.appendChild(chatIbDiv);
        chatPeopleDiv.appendChild(chatTimeDiv);
        chatPeopleDiv.onclick = () => {
          localStorage.setItem(
            "conversationName",
            conversation.conversationName
          );
          this.loadConversationDetails(
            conversation.id,
            conversation.conversationname
          );
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
  },
  mounted() {
    this.getListConversation();
  },
};
</script>
