<template>
  <div class="container">
    <comp-header />
    <div class="list_conversation">
      <div style="display: flex; margin-top: 10px">
        <input class="search_bar" placeholder="Search" v-model="searchQuery" />
        <img src="/images/message-add.png" alt="Add" style="width: 40px; height:40px" />
      </div>
      <div class="body" ref="conversationListContainer"></div> 
    </div>
  </div>
</template>
<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from "axios";
const baseUrl = "http://localhost:8080/api";

export default {
  name: "web-chat",
  components: {
    CompHeader,
  },
  data() {
    return {
      // loginUsername: this.$store.state.user.username,
      // activeConversationId: null,
      // activeConversationName: null,
      // newMessage: "",
      // conversations: [],
      // messages: [],
      // searchQuery: "",
    };
  },
  methods: {
    async getListConversation() {
      const uId = this.$store.state.user.id;
      try {
        const response = await axios.get(`${baseUrl}/list-conversation?userId=${uId}`);
        if (response.status === 200 && response.data.length > 0) {
          this.conversations = response.data;
          this.renderConversations(); 
        }
      } catch (error) {
        console.log("Error fetching conversations:", error);
      }
    },
    renderConversations() {
      const container = this.$refs.conversationListContainer;
      container.innerHTML = ""; // Clear previous contents

      this.conversations.forEach((conversation) => {
        console.log("Rendering conversation:"+JSON.stringify( conversation));
        const chatPeopleDiv = document.createElement("div");
        chatPeopleDiv.className = "chat_people";

        const images = document.createElement("img");
        images.src = "https://ptetutorials.com/images/user-profile.png";
        images.className = "chat_img";

        const chatIbDiv = document.createElement("div");
        chatIbDiv.className = "chat_ib";

        const span = document.createElement("span");
        span.className = "conversation_name";
        span.innerText = conversation.conversationname;

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

        chatPeopleDiv.onclick = () => this.loadConversationDetails(conversation.id); 

        container.appendChild(chatPeopleDiv);
      });
    },
   
    loadConversationDetails(conversationId) {
      this.$router.push({ name: "conversation_detail", params: { conversationId } });
    },
  },
  mounted() {
    this.getListConversation();
  },
};
</script>

<style>
.container {
  max-width: 100%;
}
.body {
  max-height: 400px;
  overflow-y: auto;
}

.chat_img {
  width: 50px;
  height: 50px;
}
.conversation_name {
  font-size: 14px;
  font-weight: 500;
  width: 97%;
}
.chat_ib {
  float: left;
  padding: 0 0 0 15px;
  width: 90%;
}
.chat_date {
  font-size: 12px;
}
.search_bar {
  width: 95%;
  height: 40px;
}
.nameuser {
  border-bottom: 1px solid #c4c4c4;
  padding: 0 15px;
}
.chat_people {
  display: flex;
  margin-top: 10px;
  height: 60px;
}
</style>
