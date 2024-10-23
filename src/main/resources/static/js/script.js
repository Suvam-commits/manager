console.log("script loading")
document.body.style.zoom="80%"
let currentTheme = getTheme();

document.addEventListener('DOMContentLoaded',()=>{
    changeTheme();
})


function changeTheme(){
    console.log(currentTheme);

    //set to web page
    document.querySelector("html").classList.add(currentTheme);

    //set the listner to change the theme_button
    const changeThemeButton = document.querySelector("#theme_change")

    //changing the text of the theme button
    changeThemeButton.querySelector("span").textContent = currentTheme == "light"?"Dark":"Light";

    changeThemeButton.addEventListener("click",()=>{
        console.log("theme button clicked");
        const oldTheme = currentTheme;

        if(currentTheme == "dark"){
            currentTheme="light";
        }else{
            currentTheme="dark";
        }

        //set theme to Local storage
        setTheme(currentTheme);

        //remove and add theme in all html

        document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(currentTheme);

        //changing the text of the theme button
    changeThemeButton.querySelector("span").textContent = currentTheme == "light"?"Dark":"Light";
    })
}

//set theme to local storage

function setTheme(theme){
    localStorage.setItem("theme",theme);
}


//get theme from local storage

function getTheme(){

    let theme = localStorage.getItem("theme")
    return theme ? theme :"light";
}