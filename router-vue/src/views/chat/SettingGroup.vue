<template>
  <div>
    <div style="background: rgb(236 236 236); display: flex">
      <img
        src="https://www.svgrepo.com/show/305142/arrow-ios-back.svg"
        class="icon_back"
        @click="back()"
      />
      <div class="name_conversation">
        <span style="align-content: center">{{ activeConversationName }}</span>
      </div>
    </div>
    <div class="action_group">
        <div class="action" id="addMember">
            <img src="https://cdn0.iconfinder.com/data/icons/ui-22/24/177-512.png" class="action_icon" />
            <span>Thêm thành viên </span>
        </div>
         <div class="action" id="listMember"  @click="$router.push('/member-in-group')">
            <img src="https://cdn1.iconfinder.com/data/icons/jumpicon-basic-ui-line-1/32/-_Hamburger-Menu-More-Navigation-List-512.png" class="action_icon"  />
            <span>Danh sách thành viên</span>
        </div>
         <div class="action" id="deleteMessage" @click="deleteMessage()">
            <img src="https://cdn3.iconfinder.com/data/icons/font-awesome-regular-1/512/trash-can-512.png" class="action_icon"  />
            <span>Xóa tin nhắn</span>
        </div>
         <div class="action" id="leaveGroup" @click="leaveGroup()">
            <img src="https://cdn0.iconfinder.com/data/icons/arrows-pack-30/16/log_out_arrow_exit_ui_check_out_go_out_leave_output_square-512.png" class="action_icon"  />
            <span>Rời nhóm</span>
        </div>
     
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { chatState } from "../../JS/chat.js";
import {chatTypeEnum } from "../../JS/Common.js";

const baseUrl = "http://localhost:8080/api/chat";
export default {
  name: "setting-group",
  props: ["conversationId"],
  data() {
    return {
      activeConversationName: chatState.conversationName.replace(/"/g, ""),
      activeConversationId: chatState.conversationId,
      chatType: chatState.chatType,
      ownerCode: localStorage.getItem("ownerCode"),
      token:localStorage.getItem("token")
    };
  },
  mounted() {
   this.openIcon()
  },
  methods: {
    openIcon(){
        console.log("type " + this.chatType)
        //  if(this.chatType === "private"){
         if(this.chatType === chatTypeEnum.PRIVATE){
          document.getElementById("addMember").style.display='flex'
           document.getElementById("deleteMessage").style.display='flex'
        }else{
         document.getElementById("addMember").style.display='flex'
           document.getElementById("listMember").style.display='flex'
           document.getElementById("deleteMessage").style.display='flex'
           document.getElementById("leaveGroup").style.display='flex'
        }
    },
    async deleteMessage(){
      
          const response = await axios.delete(`${baseUrl}/delete-conversation/${this.activeConversationId}`, {
        headers: {
          Authorization: `Bearer ${this.token}`,
        },
      });
      if(response.status === 200 ){
        this.$router.push("/list_conversation")
      }
    },
async leaveGroup(){
  console.log("ownerCode " + this.ownerCode)
          const response = await axios.delete(`${baseUrl}/leave-group/${this.activeConversationId}?userId=${this.ownerCode}`, {
        headers: {
          Authorization: `Bearer ${this.token}`,
        },
      });
      if(response.status === 200 ){
        this.$router.push("/list_conversation")
      }
    },
    back() {
      this.$router.push(`/conversation_detail/${this.activeConversationId}`);
    },
  },
};
</script>

