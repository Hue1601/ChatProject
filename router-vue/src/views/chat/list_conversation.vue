<template>
  <div class="container">
    <comp-header />
    <div class="list_conversation">
      <div style="display: flex; margin-top: 10px">
        <input class="search_bar" placeholder="Search"  v-model="searchQuery"/>
       <img src="/images/message-add.png" alt="Add" style="width: 40px; height:40px"/>
      </div>

      <div class="body">
          <div class="chat_people"  
         v-for="conversation in filteredConversations" :key="conversation.id"
           @click="loadConversationDetails(conversation.id)" >
             <img src="https://ptetutorials.com/images/user-profile.png" class="chat_img" alt="User"/>
            <div class="chat_ib">
                <span class="conversation_name" >{{ conversation.conversationname }}</span> 
               <p>{{ getLastMessage(conversation.id) }}</p>
            </div>
             <div class="chat_date">11</div>
          </div>
      </div>
    </div>
 
  </div>
</template>

<script>
import CompHeader from "../../components/CompHeader.vue";
import axios from "axios";
const baseUrl = "http://localhost:8080/api";

export default {
  name: "web-chat",
  data() {
    return {
      loginUsername: this.$store.state.user.username,
      activeConversationId: null,
      activeConversationName: null,
      newMessage: "",
      conversations: [],
      messages: [],
      searchQuery: "",
    };
  },
  async created() {
    axios
      .get(`${baseUrl}/conversation`, {
        params: {
          userid: this.$store.state.user.id,
        },
      })
      .then((response) => {
        this.conversations = response.data;
        
      })
      .catch((error) => {
        console.error("Error loading conversations:", error);
      });
  },

  methods: {
       
  
    getLastMessage(conversationId) {
      const conversationMessages = this.messages.filter(
        (message) => message.idconversations.id === conversationId
      );
      return conversationMessages.length
        ? conversationMessages[conversationMessages.length - 1].messagetext
        : "";
    },
    async loadConversationDetails(conversationId) {
  
       this.$router.push({ name: 'conversation_detail', params: { conversationId } });
    }, 
 
 
  },
  computed: {
    filteredConversations() {
      return this.conversations.filter((conversation) =>
        conversation.conversationname
          .toLowerCase()
          .includes(this.searchQuery.toLowerCase())
      );
    },
    filteredMessages() {
      return this.messages.filter(
        (message) => message.idconversations.id === this.activeConversationId
      );
    },
  },
  components: {
    CompHeader,
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

.chat_img{
  width:50px;
   height:50px
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
.chat_date{
  font-size: 12px;
}
.search_bar{
  width: 95%;
   height: 40px
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
