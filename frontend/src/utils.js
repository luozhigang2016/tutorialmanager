export default function utcToChinaTimeString(utcTimeString) {
    //const utcTimeString = "2024-08-31T07:56:01.869+00:00";
    const chinaTime = new Date(utcTimeString);
    const year = chinaTime.getFullYear();
    const month = chinaTime.getMonth() + 1; 
    const day = chinaTime.getDate();
    const hours = chinaTime.getHours();
    const minutes = chinaTime.getMinutes();
    const seconds = chinaTime.getSeconds();
    const pad = (number) => (number < 10 ? '0' + number : number);
    const chinaTimeString = `${year}-${pad(month)}-${pad(day)} ${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
    //console.log(chinaTimeString);
    return chinaTimeString;
}