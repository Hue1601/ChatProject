<template>
  <div class="container_detail" style="height: 100vh">
    <div class="header">
      <div style="width: 15%; margin-left:17px" @click="this.$router.push({ name: 'list_conversation' })">
        <img src="/images/back.png" alt="" />
      </div>
      <div class="chat_title">
          <img src="https://ptetutorials.com/images/user-profile.png" class="avatar_header" />
        <span>Conversation: {{ activeConversationName }}</span>
      </div>
    </div>
    <div class="body_chat_detail">
      <div class="list_message">
        <div v-for="message in messages" :key="message.id" :class=" message.iduser.username === loginUsername? 'message_send' : 'message_receive' " >
          <img src="https://ptetutorials.com/images/user-profile.png" class="avatar_member"/>
          <div v-if="message.iduser.username !== loginUsername" class="message_content">
            <div class="member_name">{{ message.iduser.username }}</div>

            <div class="received_bubble">
              <p>{{ message.messagetext }}</p>
            </div>
            <span class="time_date"> {{ formatDate(message.timestamp) }}</span>
          </div>
          <div v-else class="message_send">
            <div class="send_bubble">
              <p>{{ message.messagetext }}</p>
            </div>
             <span class="time_date">{{ formatDate(message.timestamp) }}</span>
          </div>
        </div>
      </div>
      <div class="input_bar">
        <input v-model="newMessage" class="input_message" placeholder="Type a message" />
        <button class="send_btn" @click="sendMessage">
          <i class="bi bi-send-fill"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
const baseUrl = "http://localhost:8080/api";

export default {
  name: "ConversationDetail",
  props: ["conversationId"],
  data() {
    return {
      messages: [],
      newMessage: "",
      loginUsername: this.$store.state.user.username,
      activeConversationName: "",
      activeConversationId: this.conversationId, // You can initialize here
    };
  },
  created() {
    this.loadConversationDetails(this.conversationId);
  },
  methods: {
    async loadConversationDetails(conversationId) {
      try {
        const response = await axios.get(`${baseUrl}/messages`, {
          params: { conversationId }, // Add conversationId to the request
        });
        this.messages = response.data; // Set the messages directly
        const selectedConversation = this.$store.state.conversations.find(
          (conv) => conv.id === conversationId
        );
        this.activeConversationName = selectedConversation.conversationname;
      } catch (error) {
        console.error("Error loading messages:", error);
      }
    },
    async sendMessage() {
      if (!this.newMessage.trim() || !this.activeConversationId) return;

      try {
        const newMessageData = {
          messagetext: this.newMessage,
          iduser: {
            id: this.$store.state.user.id,
            username: this.loginUsername,
          },
          idconversations: { id: this.activeConversationId },
          timestamp: new Date().toISOString(),
        };
        const response = await axios.post(
          `${baseUrl}/messages`,
          newMessageData
        );
        this.messages.push(response.data);
        this.newMessage = ""; // Clear the input field after sending the message
      } catch (error) {
        console.error("Error sending message:", error);
      }
    },
    formatDate(timestamp) {
      const date = new Date(timestamp);
      return `${date.toLocaleTimeString()} | ${date.toLocaleDateString()}`;
    },
  },
};
</script>

  <style >
.header{
    height: 56px;
    width: 100%;
    display: flex;
    align-items: center;
}
.avatar_header{
  width: 35px;
  height: 35px;
}
.chat_title {
  display: flex;
  align-items: center;
}
.body_chat_detail {
  background: #f7f8fa;
  overflow-y: hidden;
}
.list_message {
  height: 85vh;
  padding: 10px;
  overflow-y: scroll;
}
.input_bar {
  display: flex;
  border-top: 1px solid #c4c4c4;
  background: white;
  min-height: 48px;
  align-items: center;
}
.input_message {
  font-size: 15px;
  min-height: 40px;
  width: 100%;
  border: none;
  padding-right: 50px;
}
.send_btn {
  background: #05728f;
  border: none;
  border-radius: 50%;
  color: #fff;

  font-size: 17px;
  height: 33px;
  position: absolute;
  right: 0;
  width: 33px;
}
.avatar_member {
  width: 30px;
  height: 30px;
}
.member_name {
  color: #747474;
  display: block;
  font-size: 12px;
}
.message_receive {
  display: flex;
  max-width: 90%;
}
.send_bubble,
.received_bubble {
  width: fit-content;
  max-width: 80%;
  min-height:fit-content ;
  max-height: 60px;
  background: #ffffff;
  padding: 10px;
  border-radius: 5%;
}
.received_bubble p{
margin-bottom: 0;
margin-top: 0;
}
.time_date {
  color: #747474;
  display: block;
  font-size: 12px;
  margin: 8px 0 0;
}
.message_content {
  margin-left: 10px;
  width: 70%;
}
</style>

