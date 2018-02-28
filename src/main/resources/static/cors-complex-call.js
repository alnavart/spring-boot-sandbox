// function greeting() {
//     fetch("http://localhost:8080/greeting").then(response => response.json())
//         .then(fullResult => showGreeting(fullResult));
// }
//
// function showGreeting(greeting) {
//     console.log('Greeting:', greeting);
//     let resultContainer = document.getElementById('result');
//     let greetingDiv = document.createElement('div');
//     greetingDiv.innerText = greeting.content;
//     resultContainer.appendChild(greetingDiv);
// }
//
// greeting();
//
// const myHeaders = new Headers();
// myHeaders.append("X-customHeader", "value");
// myHeaders.append("Accept", "application/json");
//
// const fetch = window.fetch.bind(window);
// fetch("http://localhost:8080/greeting", {
//     method: "GET",
//     mode: 'no-cors',
//     headers: myHeaders
// })
//     .then(response => response.json())
//     .then(result => result.content)
//     .then(greeting => document.getElementById('result').innerText = greeting);


fetch("http://localhost:8080/greeting", {
    method: "GET",
    mode: 'cors',
    headers: new Headers({"Content-Type": "text/plain",
        "Accept": "application/json",
        "X-Custom-Header": "ProcessThisImmediately",})
})
    .then(response => response.json())
    .then(greeting => document.getElementById('result').innerText = greeting.id + greeting.content);