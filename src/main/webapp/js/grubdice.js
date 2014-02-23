function addNewPlayerRowToGameTable() {
    var table = $("#newGameTable");


    var numberOfRows = $(table).children(".enterPlayerRow").length+1;

    $(table).append('<div class="enterPlayerRow">' +
        '<div class="newPlayerCell">'+numberOfRows+'</div>' +
        '<div class="newPlayerCell newPlayerTextArea"><div><input class="typeahead" type="text" placeholder="name" /></div></div>' +
        '<div class="newPlayerCell"><button type="button" class="fa fa-level-down" onclick="addTiePlayer(this)"></button></div>' +
        '</div>')
}

function addTiePlayer(reference) {
    $(reference).parents('div').siblings('.newPlayerTextArea').first().append('<div><input class="typeahead" type="text" placeholder="name" /></div>');
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

    var gameResults = new Array();
    var numberOfPlayers = 0;
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
            result['name'] = nameResults;
            gameResults.push(result);
            numberOfPlayers++;
        }
    }

    json['results'] = gameResults;
    json['cd-dropdown'] = $('#cd-dropdown').val();
    console.log(json);

    if(numberOfPlayers >= 4){
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
    } else {
        alert("You need at least 4 players");
    }
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
    playerName['emailAddress'] = document.getElementById("newPlayerEmail").value;
    console.log(JSON.stringify(playerName));
    $.ajax({
        type: "POST",
        url: "/api/player",
        processData: false,
        data: JSON.stringify(playerName),
        success: function() {
            alert("Player has been added");
            document.getElementById("newPlayer").value = "";
        },
        error: function(xhr) {
            alert("The player was not added because: " + jQuery.parseJSON(xhr.responseText).badValue)
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
