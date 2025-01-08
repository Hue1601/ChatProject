<template>
  <div>
    <div style="background: rgb(236 236 236)">
      <img src="https://www.svgrepo.com/show/305142/arrow-ios-back.svg" class="icon_back" @click="back()"/>
      <span style="padding-left: 77%" @click="create()">Create</span>
    </div>

    <div class="list" style="padding: 15px">
      <div class="header_setup">
        <div class="choose_image">
          <img class="border_img" id="previewImage"
            :src="previewSrc || 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Circle-icons-image.svg/768px-Circle-icons-image.svg.png?20160314153703'"/>

          <div class="border_camera">
            <img style="width: 35px" @click="triggerFileInput"
              src="https://banner2.cleanpng.com/20190512/jev/kisspng-scalable-vector-graphics-computer-icons-camera-enc-monocolor-photo-camera-png-icons-and-graphics-pa-1713893163052.webp"/>
          </div>

          <input type="file" id="imageInput" accept="image/*" style="display: none" @change="previewImage"/>
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
    return {};
  },
  methods: {
    async genListMember() {
      const listMember = document.getElementById("listMemberSelected");
      listMember.innerHTML = "";

      chatState.selectedUsers.forEach((member) => {
        const memberDiv = document.createElement("div");
        memberDiv.className = "member_profile";

        const img = document.createElement("img");
        img.src ="https://cdn.icon-icons.com/icons2/4087/PNG/512/avatar_user_icon_259523.png";
        img.className = "member_avatar";

        const name = document.createElement("p");
        name.innerText = member.username;
        name.className = "member_name";

        memberDiv.appendChild(img);
        memberDiv.appendChild(name);

        listMember.appendChild(memberDiv);
      });
    },
    async create() {
      const chatType = chatState.chatType;
      const userCodes = chatState.memberCode;
      const ownerCode = localStorage.getItem("ownerCode");

      try {
        const groupName = document.getElementById("title").value.trim();
        if (!groupName) {
          alert("Please enter a name for the conversation.");
          return;
        }

        const payload = {
          name: groupName,
          type: chatType,
          member: [Number(ownerCode), ...userCodes],
        };

        const response = await axios.post(`${baseUrl}/create-conversation`,payload);

        if (response.status === 200) {
          this.$router.push("/list_conversation");
        }
      } catch (error) {
        console.error("Error creating conversation:", error.response || error);
      }
    },
    back() {
      this.$router.push("/create");
    },
    
    triggerFileInput() {
      document.getElementById("imageInput").click();
    },
    async previewImage(event) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = function (e) {
        document.getElementById("previewImage").src = e.target.result; 
      };
      reader.readAsDataURL(file);
    }
  },
  mounted() {
    this.genListMember();
  },
};
</script>
