package co.grubdice.scorekeeper.engine

class CommonScoreEngine {

    public static int numberOfPlayersLostTo(int place, List<Integer> numberOfPlayersOutInEachPosition) {
        int lostTo = 0
        for (int i = 0; i < place; i++) {
            lostTo += numberOfPlayersOutInEachPosition[i]
        }
        return lostTo
    }

    public static int numberOfPlayersWonTo(int place, List<Integer> numberOfPlayers) {
        int wonTo = 0
        for (int i = place + 1; i < numberOfPlayers.size(); i++) {
            wonTo += numberOfPlayers[i]
        }
        return wonTo
    }
}
