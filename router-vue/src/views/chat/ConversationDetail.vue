<template>
  <div class="container_detail" style="height: 100vh">
    <div class="header_detail">
        <img  class="icon_back"
        @click="this.$router.push({ name: 'list_conversation' })"
        src="/images/undo.png" alt="" />
      
      <div class="chat_title">
        <img
          src="/images/Avt.png"
          class="avatar_header"
        />
        <span class="last_message">{{ activeConversationName }}</span>
      </div>
      <img
        src="/images/setting.png"
        style="width: 30px"
        @click="setting()"
      />
    </div>

    <div class="body_chat_detail">
      <div class="list_message" id="listMessage"></div>
      <div class="input_bar">
        <!-- <input
          v-model="newMessage"
          class="input_message"
          placeholder="Type a message"
        /> -->
        <textarea
          id="messageInput"
          v-model="newMessage"
          class="input_message"
          placeholder="Type a message"
          @input="adjustTextareaHeight"
        >
        </textarea>
        <button class="send_btn" @click="sendMessage">
          <i class="bi bi-send-fill"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { chatState } from "../../JS/chat.js";
import { chatTypeEnum } from "../../JS/Common.js";
import { Stomp } from "@stomp/stompjs";
import SockJS from "socket.io-client";

const baseUrl = " http://localhost:8080/api/chat";

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
      stompClient: null,
    };
  },

  methods: {
    // Set height base on content for message input
    adjustTextareaHeight(event) {
      const textarea = event.target;
      textarea.style.height = "auto";
      textarea.style.height = Math.min(textarea.scrollHeight, 150) + "px"; // Giới hạn chiều cao tối đa là 150px
    },

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
      const type = chatState.chatType === chatTypeEnum.GROUP;

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
        borderName.className = type ? "cover_name_message" : "cover_message";

        // If the message is received (not sent by the logged-in user), show the avatar
        if (!isSendMessage) {
          const img = document.createElement("img");
          img.className = "avatar_member";
          img.src = "/images/Avt.png";

          if (type) {
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

    connectWebSocket() {
      const socket = new SockJS(`ws://localhost:8080/ws?token=${this.token}`);
      this.stompClient = Stomp.client(socket);

      this.stompClient.debug = (str) => console.log(str);
      this.stompClient.connect(
        {
          Authorization: `Bearer ${this.token}`,
        },
        () => {
          console.log("Websocket connected");
          this.stompClient.subscribe(`/topic/response`, (message) => {
            const messageData = JSON.parse(message.body);
            this.messages.push(messageData);
            this.renderSingleMessage(messageData);
          });
        }
      );
      this.stompClient.activate();
    },
    disconnectWebSocket() {
      if (this.stompClient) {
        this.stompClient.deactivate();
        console.log("Disconnected from WebSocket");
      }
    },
    // async sendMessage() {
    //   if (!this.newMessage.trim() || !this.activeConversationId) return;

    //   // Ensure stompClient is connected before publishing a message
    //   if (!this.stompClient || !this.stompClient.connected) {
    //     console.error("WebSocket not connected!");
    //     alert("Failed to send the message. WebSocket is not connected.");
    //     return;
    //   }

    //   const payload = {
    //     messageText: this.newMessage.trim(),
    //     messageType: "text",
    //     user: this.userId,
    //     conversation: this.activeConversationId,
    //   };

    //   try {
    //     // Send the message over WebSocket
    //     this.stompClient.publish({
    //       destination: "/app/chat.sendMessage",
    //       body: JSON.stringify(payload),
    //     });

    //     // Optionally: Push the message locally for instant feedback
    //     const newMessage = {
    //       ...payload,
    //       memberCode: Number(this.userId),
    //       createAt: new Date().toISOString(),
    //     };

    //     this.messages.push(newMessage);
    //     this.renderSingleMessage(newMessage);
    //     this.newMessage = "";
    //     this.scrollToBottom();
    //   } catch (error) {
    //     console.error("Error sending message:", error);
    //     alert("Failed to send the message. Please try again.");
    //   }
    // },

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
    setting() {
      this.$router.push("/setting-group");
    },
  },

  mounted() {
    this.getConversationDetail();
    //  this.connectWebSocket();
  },
  beforeDestroy() {
    this.disconnectWebSocket();
  },
};
</script>

 