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
import { chatState } from "/newwave/ChatProject/router-vue/src/JS/chat.js";

const baseUrl = " http://localhost:8080/api";

export default {
  name: "ConversationDetail",
  props: ["conversationId"],
  data() {
    return {
      messages: [],
      newMessage: "",
      userId: localStorage.getItem("ownerCode"),
      activeConversationName: chatState.conversationName.replace(/"/g, ""),
      activeConversationId: this.conversationId,
      token: localStorage.getItem("token"),
    };
  },

  methods: {
    async getConversationDetail() {
      try {
        const response = await axios.get(
          `${baseUrl}/detail-conversation/${this.activeConversationId}`,
          {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          }
        );

        if (response.status === 200) {
          this.messages = response.data;
          this.renderConversationDetail();
        }
      } catch (error) {
        console.log("Error fetching conversations:", error);
      }
    },
    renderConversationDetail() {
      const listMessage = document.getElementById("listMessage");
      const type = chatState.chatType;

      listMessage.innerHTML = "";

      let previousDate = null;

      this.messages.forEach((message) => {
        const messageDate = new Date(message.createAt);
        const formattedDate = this.formatDateHeader(messageDate);

        // Check if the date has changed
        if (!previousDate || previousDate !== formattedDate) {
          previousDate = formattedDate;

          const dateHeader = document.createElement("div");
          dateHeader.className = "date_header";
          dateHeader.innerText = formattedDate;
          listMessage.appendChild(dateHeader);
        }

        // Check if the message is sent by the logged-in user
        const isSendMessage = message.memberCode === Number(this.userId);

        const div = document.createElement("div");
        div.className = isSendMessage ? "message_send" : "message_receive";

        const borderName = document.createElement("div");
        // If the message is received (not sent by the logged-in user), show the avatar
        if (!isSendMessage) {
          const img = document.createElement("img");
          img.className = "avatar_member";
          img.src = "https://ptetutorials.com/images/user-profile.png";

          if (type === "group") {
            const username = document.createElement("span");
            username.className = "member_name";
            username.innerHTML = message.username;
            borderName.appendChild(username);
          }
          div.appendChild(img);
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
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      return `${hours}:${minutes}`;
    },
    formatDateHeader(date) {
      const options = {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        weekday: "short",
      };
      return date.toLocaleDateString(options);
    },

    async sendMessage() {
      if (!this.newMessage.trim() || !this.activeConversationId) return;

      const payload = {
        messageText: this.newMessage.trim(),
        messageType: "text",
        user: this.userId,
        conversation: this.activeConversationId,
      };

      try {
        const response = await axios.post(`${baseUrl}/send-message`, payload, {
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        });

        if (response.status === 200) {
          //push new message to list message
          const newMessage = {
            ...response.data,
            memberCode: Number(this.userId),
            createAt: new Date().toISOString(),
          };

          this.messages.push(newMessage);
          this.renderSingleMessage(newMessage);
          this.newMessage = "";
          this.scrollToBottom();
        }
      } catch (error) {
        console.error("Error sending message:", error);
        alert("Failed to send the message. Please try again.");
      }
    },

    //to gen new messagemessage
    renderSingleMessage(message) {
      const listMessage = document.getElementById("listMessage");
      const isSendMessage = message.memberCode === Number(this.userId);

      const div = document.createElement("div");
      div.className = isSendMessage ? "message_send" : "message_receive";

      const borderName = document.createElement("div");
      if (!isSendMessage) {
        const img = document.createElement("img");
        img.className = "avatar_member";
        img.src = "https://ptetutorials.com/images/user-profile.png";

        if (chatState.chatType === "group") {
          const username = document.createElement("span");
          username.className = "member_name";
          username.innerHTML = message.username;
          borderName.appendChild(username);
        }
        div.appendChild(img);
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
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const listMessage = document.getElementById("listMessage");
        listMessage.scrollTop = listMessage.scrollHeight;
      });
    },
  },
  mounted() {
    this.getConversationDetail();
  },
};
</script>

