import { createApp } from 'vue'
import App from './App.vue'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import router from './router'
import utcToChinaTimeString from "./utils";
//
const app = createApp(App);
// Utils
app.config.globalProperties.$utcToChinaTimeString = utcToChinaTimeString;
//Create and Mount
app.use(router).mount('#app')
