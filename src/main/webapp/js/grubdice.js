function addNewPlayerRowToGameTable() {
    var table = $("#newGameTable");


    var numberOfRows = $(table).children(".enterPlayerRow").length+1;
    var newPlayerRowHtml = '<div class="enterPlayerRow">' +
        '<div class="newPlayerCell">'+numberOfRows+'</div>' +
        '<div class="newPlayerCell newPlayerTextArea"><div><input class="typeahead" type="text" placeholder="name" /></div></div>' +
        '<div class="newPlayerCell"><button type="button" class="fa fa-level-down" onclick="addTiePlayer(this)" tabindex="-1"></button></div>' +
        '</div>';
    var newPlayerRowElement = $(newPlayerRowHtml);
    $(table).append(newPlayerRowElement);

    applyTypeAheadToElement($(newPlayerRowElement).find('.typeahead'));
}

function loadPlayersForTypeAhead() {

    var newGameTable = $('#newGameTable');

    if (!newGameTable.data('players')) {
        var players = new Bloodhound({
            datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.name); },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            prefetch: '/api/public/player'
        });

        players.initialize();

        newGameTable.data('players', players);
    }
}
function applyTypeAheadToElement(elements) {

    loadPlayersForTypeAhead();

    var players = $('#newGameTable').data('players');

    $(elements).typeahead(null, {
        displayKey: 'name',
        source: players.ttAdapter(),
        templates: {
            suggestion: Handlebars.compile([
                '<div style="background-color:#fff;" ><p style="display: inline;"><strong>{{name}}</strong> – {{email}}</p></div>'
            ].join(''))
        }
    });
}

function addTiePlayer(reference) {
    var playerArea = $(reference).parents('div').siblings('.newPlayerTextArea').first();
    var playerInputHtml = '<div><input class="typeahead" type="text" placeholder="name" /></div>';
    var playerInputElement = $(playerInputHtml);

    playerArea.append(playerInputElement);

    applyTypeAheadToElement(playerInputElement.find('.typeahead'));
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
                numberOfPlayers++;
            }
        }

        if(nameResults.length != 0){
            result['name'] = nameResults;
            gameResults.push(result);
        }
    }

    json['results'] = gameResults;
    json['cd-dropdown'] = $('#cd-dropdown').val();
    console.log(json);

    if(numberOfPlayers > 7) {
        alert("This game is invalid, due to there being space for 2 games");
    }  else if(numberOfPlayers >= 4){
        $.ajax({
            type: "POST",
            url: "/api/game",
            processData: false,
            data: JSON.stringify(json),
            success: function () {
                alert("Booyah! Success!");
                publicRefreshScoreBoard();
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
    alert(jQuery.parseJSON(jqXHR.responseText).error);
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
    $.getJSON("/api/public/game", function(games) {
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
