document.getElementById("predictButton").addEventListener("click", function() {

    let lot = document.getElementById("lotDropdown").value;
    let time = document.getElementById("timeDropdown").value;

    let url = `http://localhost:8080/predict?lot=${lot}&time=${time}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {

            let chance = data.chance;
            let rangeMessage = "";

            if (chance <= 20) {
                rangeMessage = "Parking is extremely unlikely right now.";
            } else if (chance <= 40) {
                rangeMessage = "Parking will be tough, but not impossible.";
            } else if (chance <= 60) {
                rangeMessage = "You have a fair chance of finding a spot.";
            } else if (chance <= 80) {
                rangeMessage = "You should be able to find parking.";
            } else {
                rangeMessage = "Parking is wide open!";
            }

            document.getElementById("result").innerText =
                `Chance: ${chance}%\n${rangeMessage}`;

            // Update progress bar
            let bar = document.getElementById("progressBar");
            bar.style.width = chance + "%";

            if (chance <= 20) bar.style.backgroundColor = "red";
            else if (chance <= 40) bar.style.backgroundColor = "orange";
            else if (chance <= 60) bar.style.backgroundColor = "yellow";
            else if (chance <= 80) bar.style.backgroundColor = "lightgreen";
            else bar.style.backgroundColor = "green";
        })
        .catch(error => {
            document.getElementById("result").innerText =
                "Error contacting server.";
        });
});
