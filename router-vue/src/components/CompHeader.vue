<template>
  <div class="header">
    <h3 style="width: 30%;">WEB CHAT</h3>
    <div style="width: 65%;">
      <nav class="navbar navbar-expand-lg">
        <ul class="navbar-nav">
          <li class="nav-item">
            <RouterLink to="/user">
              <a class="btn" style="color: white">User</a>
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/list_conversation" style="color: white">
              <a class="btn" style="color: white">Chat</a>
            </RouterLink>
          </li>
           <li class="nav-item">
            <RouterLink to="/create" style="color: white">
              <a class="btn" style="color: white">Choose friend</a>
            </RouterLink>
          </li>
        </ul>
      </nav>
    </div>
   <div class="setting">
      <a class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <img src="https://ptetutorials.com/images/user-profile.png" alt="User" />
      </a>
      <ul class="dropdown-menu">
        <li><a class="dropdown-item">Setting</a></li>
        <li><hr class="dropdown-divider" /></li>
        <li>
          <RouterLink to="/" style="color: white">
            <a class="dropdown-item" href="#">Logout</a>
          </RouterLink>
        </li>
      </ul>
  </div>
</div>
</template>

<script>
import axios from "axios";
const baseUrl = "http://localhost:8080/api";

export default {
  name: "comp-header",

  data() {
    return {
      users: [],
      search: "",
    };
  },
  methods: {},
  mounted() {},
};
</script>

<style>
.dropdown-toggle img {
   width: 45px;
   height: 45px;
}
.header {
  display: flex;
  color: white;
  background: rgb(44, 37, 93);
  align-items: center;
}
.setting{
  width: 20%;
}
.dropdown-toggle {
 text-align: end;
}
.navbar-expand-lg{
  justify-content: center;
}

</style>
