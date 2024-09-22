import axios from "axios";

const apiBaseUrl = process.env.VUE_APP_API_BASE_URL || "/api";
console.log("API_BASE_URL="+ apiBaseUrl);

export default axios.create({
  baseURL: apiBaseUrl,
  headers: {
    "Content-type": "application/json"
  }
});
