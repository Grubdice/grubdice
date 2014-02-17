function addNewPlayerRowToGameTable() {
    var table = $("#newGameTable");


    var numberOfRows = $(table).children(".enterPlayerRow").length+1;

    $(table).append('<div class="enterPlayerRow">' +
        '<div class="newPlayerCell">'+numberOfRows+'</div>' +
        '<div class="newPlayerCell newPlayerTextArea"><div><input type="text" placeholder="name" /></div></div>' +
        '<div class="newPlayerCell"><button type="button" class="fa fa-fighter-jet" onclick="addTiePlayer(this)"></button></div>' +
        '</div>')
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
    $(reference).parents('div').siblings('.newPlayerTextArea').first().append('<div><input type="text" placeholder="name" /></div>');
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
    var table = document.getElementById("scoreTableData");
    table.innerHTML = '';

    for (var i = 0; i < values.length; i++) {
        var player = values[i];
        console.log(player);

        var row = table.insertRow(-1);
        row.insertCell(0).innerHTML = player['place'];
        row.insertCell(1).innerHTML = player['name'];
        row.insertCell(2).innerHTML = player['score'];
        row.insertCell(3).innerHTML = player['gamesPlayed'];
        row.insertCell(4).innerHTML = player['averageScore'].toFixed(3);
    }
}

function performPostAndClearTable() {
    var json = { };
    var nodeList = $('#newGameTable').children('.enterPlayerRow');

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
    json['cd-dropdown'] = $('#cd-dropdown').val();
    console.log(json);

    $.ajax({
        type: "POST",
        url: "/api/game",
        processData: false,
        data: JSON.stringify(json),
        success: function () {
            alert("Booyah! Success!");
            refreshScoreBoard();
            updateRecentGames();
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

    $("#newGameTable").html('');

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

function updateRecentGames() {
    $.getJSON("/api/game", function(games) {
        var recentGamesHtml = '';
        $.each(games, function(i, game) {
            recentGamesHtml += '<div class="recentGame"><ul class="recentGameResults">';
            $.each(game.results, function (j, result){
                recentGamesHtml += '<li class="recentGameResult">' + (result.place + 1) + ":&nbsp;&nbsp;&nbsp;" + result.playerName + ' ('+ formatScore(result.score) +')</li>';
            });
            recentGamesHtml += '</ul>';
            recentGamesHtml += '<div class="recentGameTime">'+ moment(game.postingDate).calendar() +'</div>';
            recentGamesHtml += '</div>';
        });

        $('#recentGamesArea').html(recentGamesHtml);
    });

}

function formatScore(score) {
    if (score > 0) {
        return "+" + score;
    } else {
        return score.toString();
    }
}
