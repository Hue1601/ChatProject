<template>
  <div class="about">
    <h1>{{header}}</h1>
  
    <input v-model="newItem" type="text" placehoder="add item" />
    <button class="btn btn-primary" v-on:click="items.push(newItem)">Add </button>
    <ul>
    <liv v-for="item in items" :key="item.id"> {{ item }} </liv>
   </ul>
  </div>
</template>

<script>
export default {
  name:'about',
  data(){
    return{
      header:'shopping list app',
      newItem:'',
      items:[
      '10 party hats',
      '2 borad game',
      '20 cups'
    ]
    }
  }
}
</script>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
