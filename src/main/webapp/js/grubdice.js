function addNewRow() {
    var table = document.getElementById("newGameTable");
    add2RowToTable(table, '<div><input type="text" placeholder="name" /></div>', '<div><input type="text" placeholder="points" /></div>')
}

function add2RowToTable(table, cell1Contents, cell2Contents) {
    var row = table.insertRow(-1);

    // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);

    cell1.innerHTML = cell1Contents;
    cell2.innerHTML = cell2Contents;
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

    for (var i = values.length - 1; i >= 0; i--) {
        var player = values[i];
        console.log(player);
        var place = values.length - i + 0.0
        add3RowToTable(table, '<div>' + place + '</div>', '<div>' + player['name'] + '</div>', "<div>" + player['score'] + "</div>");
    }
}

function preformPostAndClearTable() {
    var json = { };
    var nodeList = document.getElementById('newGameTable').getElementsByTagName('tr');
    var gameResults = new Array()
    for (var i = 0; i < nodeList.length; ++i) {
        var textBoxes = nodeList[i].getElementsByTagName("input");
        var result = {}
        result['name'] = textBoxes[0].value
        result['points'] = textBoxes[1].value
        if(!result['name']) {
            alert("Looks like you forgot to enter a players name. Skipping it.")
            continue;
        }
        if (!isNumber(result['points'])) {
            alert("The value for " + textBoxes[0].value + " is not a number!");
            return;
        }
        gameResults.push(result)
    }

    json['results'] = gameResults;
    json['gameType'] = 'LEAGUE';
    console.log(json);

    $.ajax({
        type: "POST",
        url: "/api/game",
        processData: false,
        data: JSON.stringify(json),
        success: function () {
            alert("This game has been successfully posted")
        },
        error: reportNewGameError,
        contentType: "application/json"
    });

    clearGameTable();
    refreshScoreBoard();
}

function reportNewGameError(jqXHR, textStatus, errorThrown){
    alert(textStatus);
    console.log(jqXHR);
    console.log(errorThrown);
}

function clearGameTable() {
    document.getElementById("newGameTable").innerHTML = '<tr><td><div><input type="text" placeholder="name" /></div></td><td><div><input type="text" placeholder="points" /></div></td></tr>'
}
function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}