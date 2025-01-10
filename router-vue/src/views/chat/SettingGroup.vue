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
        <div class="action" @click="$router.push('/member-in-group')">
            <img src="https://cdn0.iconfinder.com/data/icons/ui-22/24/177-512.png" class="action_icon" />
            <span>Them thanh vien</span>
        </div>
         <div class="action">
            <img src="https://cdn1.iconfinder.com/data/icons/jumpicon-basic-ui-line-1/32/-_Hamburger-Menu-More-Navigation-List-512.png" class="action_icon"  />
            <span>Danh sach thanh vien</span>
        </div>
         <div class="action">
            <img src="https://cdn3.iconfinder.com/data/icons/font-awesome-regular-1/512/trash-can-512.png" class="action_icon"  />
            <span>Xoa tin nhan</span>
        </div>
         <div class="action">
            <img src="https://cdn0.iconfinder.com/data/icons/arrows-pack-30/16/log_out_arrow_exit_ui_check_out_go_out_leave_output_square-512.png" class="action_icon"  />
            <span>Roiwf nhosm</span>
        </div>
     
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { chatState } from "../../JS/chat.js";
const baseUrl = "http://localhost:8080/api/chat/member-in-group";
export default {
  name: "setting-group",
  props: ["conversationId"],
  data() {
    return {
      activeConversationName: chatState.conversationName.replace(/"/g, ""),
      activeConversationId: chatState.conversationId,
      
    };
  },
  mounted() {},
  methods: {
   
    back() {
      this.$router.push(`/conversation_detail/${this.activeConversationId}`);
    },
  },
};
</script>

