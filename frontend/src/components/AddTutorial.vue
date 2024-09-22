<template>
  <div class="submit-form">
    <div v-if="!submitted">
      <div class="form-group">
        <label for="title">Title</label>
        <input
          type="text"
          class="form-control"
          id="title"
          required
          v-model="tutorial.title"
          name="title"
        />
      </div>

      <div class="form-group">
        <label for="description">Description</label>
        <input
          class="form-control"
          id="description"
          required
          v-model="tutorial.description"
          name="description"
        />
      </div>

      <button @click="saveTutorial" class="btn btn-success">Submit</button>
    </div>

    <div v-else>
      <h4>You submitted successfully!</h4>
      <button class="btn btn-success" @click="newTutorial">Add</button>
    </div>
    <div v-if="errored" class="alert alert-danger alert-dismissible fade show" role="alert">
    <strong>Error:</strong> {{ errorMessage }}
    <button type="button" class="btn-close" @click="errored = false" aria-label="Close">X</button>
  </div>
  </div>
</template>

<script>
import TutorialDataService from "../services/TutorialDataService";

export default {
  name: "add-tutorial",
  data() {
    return {
      tutorial: {
        id: null,
        title: "",
        description: "",
        published: false
      },
      submitted: false,
      errored: false,
      errorMessage: ''
    };
  },
  methods: {
    saveTutorial() {
      var data = {
        title: this.tutorial.title,
        description: this.tutorial.description
      };

      TutorialDataService.create(data)
        .then(response => {
          console.log(response.data);
          if (response.data.code == 201 ) {
            this.tutorial.id = response.data.data.id;
            this.submitted = true;
            this.errored = false;
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
    },
    
    newTutorial() {
      this.submitted = false;
      this.tutorial = {};
      this.errored = false;
    }
  }
};
</script>

<style>
.submit-form {
  max-width: 300px;
  margin: auto;
}
</style>
