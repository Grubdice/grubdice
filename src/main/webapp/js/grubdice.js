function addNewPlayerRowToGameTable() {
    var table = document.getElementById("newGameTable");
    var numberOfRows = document.getElementById('newGameTable').getElementsByTagName('tr').length + 1;
    add3RowToTable(table, '<div>' + numberOfRows + '</div>', '<div><input type="text" placeholder="name" /></div>', '<div><button type="button" class="fa fa-plus-circle" onclick="addTiePlayer(this)"></button></div>')
}

function add3RowToTable(table, cell1Contents, cell2Contents, cell3Contents) {
    var row = table.insertRow(-1);

    // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);

    cell1.innerHTML = cell1Contents;
    cell2.innerHTML = cell2Contents;
    cell3.innerHTML = cell3Contents;
}

function addTiePlayer(reference) {
    $(reference).parents('td').siblings().get(1).innerHTML+='<div><input type="text" placeholder="name" /></div>';
}

function publicRefreshScoreBoard() {
    $.ajax({
        type: "GET",
        url: "/api/public/score",
        processData: false,
        success: updateScoreBoard,
        dataType: 'JSON'
    });
}

function refreshScoreBoard() {
    $.ajax({
        type: "GET",
        url: "/api/score",
        processData: false,
        success: updateScoreBoard,
        dataType: 'JSON'
    });
}

function updateScoreBoard(values) {
    var table = document.getElementById("scoreTable");
    table.innerHTML = '<THEAD><tr><td></td><td><h6> Name </h6></td><td><h6> Score </h6></td></tr></THEAD>';

    for (var i = 0; i < values.length; i++) {
        var player = values[i];
        console.log(player);
        add3RowToTable(table, '<div>' + player['place'] + '</div>', '<div>' + player['name'] + '</div>', "<div>" + player['score'] + "</div>");
    }
}

function preformPostAndClearTable() {
    var json = { };
    var nodeList = document.getElementById('newGameTable').getElementsByTagName('tr');
    var gameResults = new Array()
    for (var i = 0; i < nodeList.length; ++i) {
        var textBoxes = nodeList[i].getElementsByTagName("input");
        var result = {}
        var nameResults = [];

        for (var j=0; j<textBoxes.length; j++) {
            if(textBoxes[j].value != "") {
                nameResults.push(textBoxes[j].value);
            }
        }

        if(nameResults.length != 0){
            result['name'] = nameResults
            gameResults.push(result)
        }
    }

    json['results'] = gameResults;
    json['gameType'] = $('#gameType').val();
    console.log(json);

    $.ajax({
        type: "POST",
        url: "/api/game",
        processData: false,
        data: JSON.stringify(json),
        success: function () {
            alert("This game has been successfully posted")
            refreshScoreBoard();
            clearGameTable();
        },
        error: reportNewGameError,
        contentType: "application/json"
    });
}

function reportNewGameError(jqXHR, textStatus, errorThrown){
    alert(textStatus);
    console.log(jqXHR);
    console.log(errorThrown);
}

function clearGameTable() {
    document.getElementById("newGameTable").innerHTML = '';
    for(var i = 0; i < 4; i++) {
        addNewPlayerRowToGameTable();
    }
}

function createNewPlayer() {
    var playerName = {};
    playerName['name'] = document.getElementById("newPlayer").value;
    console.log(playerName);
    $.ajax({
        type: "POST",
        url: "/api/player",
        processData: false,
        data: JSON.stringify(playerName),
        success: function() {
            alert("Player has been added");
            document.getElementById("newPlayer").value = "";
        },
        contentType: "application/json"
    });
}
