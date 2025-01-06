<template>
  <div class="container_detail" style="height: 100vh">
    <div class="header_detail">
      <div
        style="width: 15%; margin-left: 17px"
        @click="this.$router.push({ name: 'list_conversation' })"
      >
        <img src="/images/back.png" alt="" />
      </div>
      <div class="chat_title">
        <img
          src="https://ptetutorials.com/images/user-profile.png"
          class="avatar_header"
        />
        <span>{{ activeConversationName }}</span>
      </div>
    </div>

    <div class="body_chat_detail">
      <div class="list_message" id="listMessage"></div>
      <div class="input_bar">
        <input
          v-model="newMessage"
          class="input_message"
          placeholder="Type a message"
        />
        <button class="send_btn" @click="sendMessage">
          <i class="bi bi-send-fill"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
const baseUrl = " http://localhost:8080/api/detail-conversation";

export default {
  name: "ConversationDetail",
  props: ["conversationId"],
  data() {
    return {
      messages: [],
      newMessage: "",
      loginUsername:sessionStorage.getItem("username"),
      activeConversationName: localStorage.getItem("conversationName").replace(/"/g,""),

      activeConversationId: this.conversationId,
    };
  },

  methods: {
    async getConversationDetail() {

      try {
         const token = localStorage.getItem("token");
        const response = await axios.get(`${baseUrl}/${this.activeConversationId}`,{
            headers:{
              Authorization :`Bearer ${token}`,
            }
          }
        );

        if (response.status === 200 ) {
          this.messages = response.data;
          this.renderConversationDetail();
        }
      } catch (error) {
        console.log("Error fetching conversations:", error);
      }
    },

    renderConversationDetail() {
      const listMessage = document.getElementById("listMessage");
      listMessage.innerHTML = ""; 

      this.messages.forEach((message) => {
        // Check if the message is sent by the logged-in user
        const isSendMessage = message.username === this.loginUsername;

        const div = document.createElement("div");
        div.className = isSendMessage ? "message_send" : "message_receive";

        const borderName = document.createElement("div");
        // If the message is received (not sent by the logged-in user), show the avatar
        if (!isSendMessage) {
          const img = document.createElement("img");
          img.className = "avatar_member";
          img.src = "https://ptetutorials.com/images/user-profile.png";

          const username = document.createElement("span");
          username.className = "member_name";
          username.innerHTML = message.username;

          div.appendChild(img);
          borderName.appendChild(username);
        }

        const receivedBubble = document.createElement("div");
        receivedBubble.className = isSendMessage
          ? "send_bubble"
          : "received_bubble";

        borderName.appendChild(receivedBubble);

        const text = document.createElement("p");
        text.innerText = message.messageText;

        const messageTime = document.createElement("span");
        messageTime.className = "time_date";
        messageTime.innerText = this.formatTime(message.createAt);

        if (isSendMessage) {
          div.appendChild(messageTime);

          div.appendChild(borderName);
        } else {
          div.appendChild(borderName);
          div.appendChild(messageTime);
        }

        receivedBubble.appendChild(text);

        listMessage.appendChild(div);
        this.scrollToBottom();
      });
    },
    formatTime(localTime) {
      const date = new Date(localTime);
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes =String(date.getMinutes()).padStart(2, '0');
      return `${hours}:${minutes}`;
    },

    async sendMessage() {
      if (!this.newMessage.trim() || !this.activeConversationId) return;

      try {
        const newMessageData = {
          messagetext: this.newMessage,
          messagetype: "text",
          timestamp: new Date().toISOString(),
          iduser: {
            id: this.$store.state.user.id,
            username: this.loginUsername,
          },
          conversation: {
            id: this.activeConversationId,
            conversationname: this.activeConversationName,
          }
        };
        const response = await axios.post(
          `${baseUrl}/send-message`,
          newMessageData
        );

        if (response.status === 200 && response.data) {
          this.messages.push(response.data); // Thêm tin nhắn vào mảng messages
          this.newMessage = "";

          this.renderConversationDetail(); // Cập nhật giao diện
          this.scrollToBottom(); // Cuộn xuống cuối danh sách tin nhắn
        }
      } catch (error) {
        console.error("Error sending message:", error);
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const listMessage = document.getElementById("listMessage");
        listMessage.scrollTop = listMessage.scrollHeight;
      });
    }
  },
  mounted(){
this.getConversationDetail();
  }
  
};
</script>

