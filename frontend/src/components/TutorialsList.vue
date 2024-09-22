<template>
  <div class="list row">
    <div class="col-md-8">
      <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Search by title" v-model="title" />
        <div class="input-group-append">
          <button class="btn btn-outline-secondary" type="button" @click="searchTitle">
            Search
          </button>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <h4>Tutorials List</h4>
      <ul class="list-group">
        <li class="list-group-item" :class="{ active: index == currentIndex }" v-for="(tutorial, index) in tutorials"
          :key="index" @click="setActiveTutorial(tutorial, index)">
          {{ tutorial.title }}
        </li>
      </ul>

      <button class="m-3 btn btn-sm btn-danger" @click="retrieveTutorials">
        Retrieve All Tutorials
      </button>
    </div>
    <div class="col-md-6">
      <div v-if="currentTutorial">
        <h4>Tutorial</h4>
        <div>
          <label><strong>ID:</strong></label> {{ currentTutorial.id }}
        </div>
        <div>
          <label><strong>Title:</strong></label> {{ currentTutorial.title }}
        </div>
        <div>
          <label><strong>Description:</strong></label> {{ currentTutorial.description }}
        </div>
        <div>
          <label><strong>Status:</strong></label> {{ currentTutorial.published ? "Published" : "Pending" }}
        </div>
        <div>
          <label><strong>Published at:</strong></label> {{ currentTutorial.published ?
          $utcToChinaTimeString(currentTutorial.publishTime) : "N/A" }}
        </div>
        <div>
          <label><strong>Updated at:</strong></label> {{ $utcToChinaTimeString(currentTutorial.updateTime) }}
        </div>
        <div>
          <label><strong>Created at:</strong></label> {{ $utcToChinaTimeString(currentTutorial.createTime) }}
        </div>
        <router-link :to="{ name: 'tutorial-details', params: { id: currentTutorial.id } }" class="badge badge-warning" >Edit</router-link>        
      </div>
      <div v-else>
        <br />
        <p>Please click on a Tutorial...</p>
      </div>
      <div v-if="errored" class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Error:</strong> {{ errorMessage }}
        <button type="button" class="btn-close" @click="errored = false" aria-label="Close">X</button>
      </div>
    </div>
  </div>
</template>

<script>
import TutorialDataService from "../services/TutorialDataService";
export default {
  name: "tutorials-list",
  data() {
    return {
      tutorials: [],
      currentTutorial: null,
      currentIndex: -1,
      title: "",
      errored: false,
      errorMessage: ''
    };
  },
  methods: {
    retrieveTutorials() {
      TutorialDataService.getAll()
        .then(response => {
          console.log(response.data);
          if (response.data.code == 200 ) {
            this.tutorials = response.data.data;
          } else {
            this.errored = true;
            this.errorMessage = response.data.message + " ! code="+response.data.code;
          }
        })
        .catch(e => {
          console.log(e);
          this.errored = true;
          this.errorMessage = e.message;
        });
    },

    refreshList() {
      this.retrieveTutorials();
      this.currentTutorial = null;
      this.currentIndex = -1;
    },

    setActiveTutorial(tutorial, index) {
      this.currentTutorial = tutorial;
      this.currentIndex = tutorial ? index : -1;
    },

    searchTitle() {
      TutorialDataService.findByTitle(this.title)
        .then(response => {
          this.setActiveTutorial(null);
          console.log(response.data);
          if (response.data.code == 200 ) {
            this.tutorials = response.data.data;
          } else {
            this.errored = true;
            this.errorMessage = response.data.message + "! code="+response.data.code;
          }
        })
        .catch(e => {
          console.log(e);
          this.errored = true;
          this.errorMessage = e.message;
        });

    }
  },
  mounted() {
    this.retrieveTutorials();
  }
};
</script>

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}
</style>
