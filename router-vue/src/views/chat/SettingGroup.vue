<template>
  <div>
    <div style="background: rgb(236 236 236); display: flex">
      <img
        src="/images/undo.png"
        class="icon_back"
        @click="back()"
      />
      <div class="name_conversation">
        <span style="align-content: center">{{ activeConversationName }}</span>
      </div>
    </div>
    <div class="action_group">
      <div class="action" id="addMember" @click="addMemberToGroup('group')">
        <img
          src="/images/add-member.png"
          class="action_icon"
        />
        <span>Thêm thành viên </span>
      </div>
      <div
        class="action"
        id="listMember"
        @click="$router.push('/member-in-group')"
      >
        <img
          src="/images/list-member.png"
          class="action_icon"
        />
        <span>Danh sách thành viên</span>
      </div>
      <div class="action" id="deleteMessage" @click="deleteMessage()">
        <img
          src="/images/trash.png"
          class="action_icon"
        />
        <span>Xóa tin nhắn</span>
      </div>
      <div class="action" id="leaveGroup" @click="leaveGroup()">
        <img
          src="/images/leave.png"
          class="action_icon"
        />
        <span>Rời nhóm</span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { chatState } from "../../JS/chat.js";
import { chatTypeEnum } from "../../JS/Common.js";

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
      token: localStorage.getItem("token"),
      listMemberInGroup: [],
    };
  },
  mounted() {
    this.openIcon();
  },
  methods: {
    openIcon() {
      if (this.chatType === chatTypeEnum.PRIVATE) {
        document.getElementById("deleteMessage").style.display = "flex";
      } else {
        document.getElementById("addMember").style.display = "flex";
        document.getElementById("listMember").style.display = "flex";
        document.getElementById("deleteMessage").style.display = "flex";
        document.getElementById("leaveGroup").style.display = "flex";
      }
    },
    async deleteMessage() {
      try {
        const response = await axios.delete(
          `${baseUrl}/delete-conversation/${this.activeConversationId}`,
          {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          }
        );
        if (response.status === 200) {
          this.$router.push("/list_conversation");
        }
      } catch (e) {
        alert("Fail to delete message: " + e.message);
      }
    },
    async leaveGroup() {
      try {
        const response = await axios.delete(
          `${baseUrl}/leave-group/${this.activeConversationId}?userId=${this.ownerCode}`,
          {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          }
        );
        if (response.status === 200) {
          this.$router.push("/list_conversation");
        }
      } catch (e) {
        alert("Fail to leave grouup " + e);
      }
    },
    async addMemberToGroup(type) {
      try {
        const response = await axios.get(
          `${baseUrl}/member-in-group/${this.activeConversationId}`,
          {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          }
        );

        if (response.status === 200) {
          this.listMemberInGroup = response.data;
          chatState.memberInGroup = this.listMemberInGroup.map(
            (member) => member.id
          );
        }

        chatState.isAddMember = true;
        chatState.chatType = type;

        console.log("isAddMember" +  chatState.isAddMember)
        this.$router.push("/create");
      } catch (e) {
        alert("Failed to fetch members in group:")
      }
    },
    back() {
      this.$router.push(`/conversation_detail/${this.activeConversationId}`);
    },
  },
};
</script>

