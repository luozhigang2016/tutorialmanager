<template>
  <div v-if="currentTutorial" class="edit-form">
    <h4>Tutorial</h4>
    <form>
      <div class="form-group">
        <label for="title">Title</label>
        <input type="text" class="form-control" id="title" v-model="currentTutorial.title" />
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input type="text" class="form-control" id="description" v-model="currentTutorial.description" />
      </div>

      <div class="form-group">
        <label><strong>Status:</strong></label>
        {{ currentTutorial.published ? "Published" : "Pending" }}
      </div>
    </form>

    <button class="badge badge-primary mr-2" v-if="currentTutorial.published" @click="updatePublished(false)">
      UnPublish
    </button>
    <button v-else class="badge badge-primary mr-2" @click="updatePublished(true)">
      Publish
    </button>

    <button class="badge badge-danger mr-2" @click="deleteTutorial">
      Delete
    </button>

    <button type="submit" class="badge badge-success" @click="updateTutorial">
      Update
    </button>
    <p>{{ message }}</p>
  </div>

  <div v-else>
    <br />
    <p>Please click on a Tutorial...</p>
  </div>

  <div v-if="errored" class="alert alert-danger alert-dismissible fade show" role="alert">
    <strong>Error:</strong> {{ errorMessage }}
    <button type="button" class="btn-close" @click="errored = false" aria-label="Close">X</button>
  </div>

</template>

<script>
import TutorialDataService from "../services/TutorialDataService";

export default {
  // eslint-disable-next-line
  name: "tutorial",
  data() {
    return {
      currentTutorial: null,
      message: '',
      errored: false,
      errorMessage: ''
    };
  },
  methods: {
    getTutorial(id) {
      TutorialDataService.get(id)
        .then(response => {
          console.log(response.data);
          if (response.data.code == 200) {
            this.currentTutorial = response.data.data;
          } else {
            this.errored = true;
            this.errorMessage = response.data.message + "! code=" + response.data.code;
          }
        })
        .catch(e => {
          console.log(e);
          this.errored = true;
          this.errorMessage = e.message;
        });
    },

    updatePublished(status) {
      var data = {
        id: this.currentTutorial.id,
        title: this.currentTutorial.title,
        description: this.currentTutorial.description,
        published: status
      };

      TutorialDataService.update(this.currentTutorial.id, data)
        .then(response => {
          console.log(response.data);
          if (response.data.code == 200) {
            this.currentTutorial.published = status;
            this.message = 'The status was updated successfully!';
          } else {
            this.errored = true;
            this.errorMessage = response.data.message + "! code=" + response.data.code;
          }
        })
        .catch(e => {
          console.log(e);
          this.errored = true;
          this.errorMessage = e.message;
        });
    },

    updateTutorial() {
      TutorialDataService.update(this.currentTutorial.id, this.currentTutorial)
        .then(response => {
          console.log(response.data);
          if (response.data.code == 200) {
            this.message = 'The tutorial was updated successfully!';
          } else {
            this.errored = true;
            this.errorMessage = response.data.message + "! code=" + response.data.code;
          }
        })
        .catch(e => {
          console.log(e);
          this.errored = true;
          this.errorMessage = e.message;
        });
    },

    deleteTutorial() {
      TutorialDataService.delete(this.currentTutorial.id)
        .then(response => {
          console.log(response.data);
          if (response.data.code == 204) {
            this.$router.push({ name: "tutorials" });
          } else {
            this.errored = true;
            this.errorMessage = response.data.message + "! code=" + response.data.code;
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
    this.message = '';
    this.getTutorial(this.$route.params.id);
  }
};
</script>

<style>
.edit-form {
  max-width: 300px;
  margin: auto;
}
</style>
