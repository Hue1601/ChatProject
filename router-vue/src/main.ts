import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createStore } from 'vuex'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.bundle.js';
import 'bootstrap-icons/font/bootstrap-icons.css';

const store = createStore({
    state () {
      return {
        count: 0,
        user: {
            id:'',
            username:'',
            sdt:'',
            diachi:'',
            gioitinh:''
        }
      }
    },
    mutations: {
      increment (state: any) {
        state.count++
      },
      setUser(state: any, user: any) {

        state.user = user;
        console.log("commit successfully");
      },
      updateUser(state: any, userData: any) {
        Object.assign(state.user, userData);
      }
    }
  })


const app = createApp(App)

app.use(router)

app.use(store)

app.mount('#app')
