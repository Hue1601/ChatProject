<template>
<div>
 <div style="background: rgb(236 236 236)">
      <img src="/images/undo.png" class="icon_back" @click="$router.push('/setting-group')"/>
    </div>
  <div style="padding:20px">
    <h5>Thành viên trong nhóm</h5>
    <div id="listMember"></div>
  </div>
</div>
</template>

<script>
import axios from "axios";
import { chatState } from "../../JS/chat.js";
const baseUrl = "http://localhost:8080/api/chat/member-in-group";
export default {
  name: "member-in-group",
  data() {
    return {
      listMember: [],
      id: chatState.conversationId,
    };
  },
  methods: {
    async getListMember() {
      const token = localStorage.getItem("token");

      const reponse = await axios.get(`${baseUrl}/${this.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (reponse.status == 200) {
        this.listMember = reponse.data;

        this.renderListMember();
      }
    },
    renderListMember() {
      const listMemberContainer = document.getElementById("listMember");
      this.listMember.forEach((member) => {
        const listMember = document.createElement("div");
        listMember.className = "member_infor";

        const images = document.createElement("img");
        images.src = "https://ptetutorials.com/images/user-profile.png";
        images.className = "chat_img";

        const span = document.createElement("span");
        span.className ="username"
        span.innerText = member.username;


        listMember.appendChild(images);
        listMember.appendChild(span);

        listMemberContainer.appendChild(listMember);
      });
    },
  },
  mounted() {
    this.getListMember();
  },
};
</script>

<style>
</style>