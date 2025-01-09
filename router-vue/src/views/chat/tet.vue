<template>
  <div>
    <div style="background: rgb(236, 236, 236)">
      <img
        src="https://www.svgrepo.com/show/305142/arrow-ios-back.svg"
        class="icon_back"
        @click="back"
      />
      <span style="padding-left: 77%" @click="create">Create</span>
    </div>

    <div class="list" style="padding: 15px">
      <div class="header_setup">
        <div class="choose_image">
          <img
            class="border_img"
            id="previewImage"
            :src="previewSrc || 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Circle-icons-image.svg/768px-Circle-icons-image.svg.png?20160314153703'"
          />
          <div class="border_camera">
            <img
              style="width:35px"
              @click="triggerFileInput"
              src="https://banner2.cleanpng.com/20190512/jev/kisspng-scalable-vector-graphics-computer-icons-camera-enc-monocolor-photo-camera-png-icons-and-graphics-pa-1713893163052.webp"
            />
          </div>
          <input
            type="file"
            id="imageInput"
            accept="image/*"
            style="display:none"
            @change="previewImage"
          />
        </div>
        <input id="title" placeholder="Please enter group name" />
      </div>
      <h6 style="margin-top: 10px">Member</h6>
      <div id="listMemberSelected"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { chatState } from "/newwave/ChatProject/router-vue/src/JS/chat.js";

const baseUrl = "http://localhost:8080/api";

export default {
  name: "setting",
  data() {
    return {
      previewSrc: "", // Holds the preview image source
    };
  },
  methods: {
   async createConversation() {
  const chatType = chatState.chatType;
  const title = chatState.title;
  const userCode = chatState.memberCode;
  const ownerCode = localStorage.getItem("ownerCode");

  try {
    if (chatType === "private") {
      // Gọi API lấy danh sách conversations
      const token = localStorage.getItem("token");
      const response = await axios.get(`${baseUrl}/list-conversation`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        const conversations = response.data;

        // Kiểm tra nếu conversation với user này đã tồn tại
        const existingConversation = conversations.find((conversation) => {
          return (
            conversation.type === "private" &&
            conversation.members.some(
              (member) => member.id === Number(userCode)
            )
          );
        });

        if (existingConversation) {
          // Nếu đã tồn tại, chuyển đến đoạn chat của user đó
          this.$router.push({
            name: "conversation_detail",
            params: { conversationId: existingConversation.id },
          });
          return;
        }
      }

      // Nếu chưa có, tạo conversation mới
      const payload = {
        name: title,
        type: chatType,
        member: [Number(ownerCode), Number(userCode)],
      };

      const createResponse = await axios.post(
        `${baseUrl}/create-conversation`,
        payload,
        {
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        }
      );

      if (createResponse.status === 200) {
        this.$router.push("/list_conversation");
      }
    } else {
      this.$router.push("/setting");
    }
  } catch (error) {
    console.error("Error creating conversation:", error.response || error);
  }
}
  },
  mounted() {
    this.genListMember();
  },
};
</script>
