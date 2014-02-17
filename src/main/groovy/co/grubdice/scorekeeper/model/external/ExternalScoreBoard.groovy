package co.grubdice.scorekeeper.model.external

import groovy.transform.TupleConstructor

@TupleConstructor
class ExternalScoreBoard {

    def String name
    def int score
    def int place
    def int gamesPlayed
    def double averageScore

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ExternalScoreBoard)) return false

        ExternalScoreBoard that = (ExternalScoreBoard) o

        if (Double.compare(that.averageScore, averageScore) != 0) return false
        if (gamesPlayed != that.gamesPlayed) return false
        if (place != that.place) return false
        if (score != that.score) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        int result
        long temp
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + score
        result = 31 * result + place
        result = 31 * result + gamesPlayed
        temp = averageScore != +0.0d ? Double.doubleToLongBits(averageScore) : 0L
        result = 31 * result + (int) (temp ^ (temp >>> 32))
        return result
    }

    @Override
    public String toString() {
        return "ExternalScoreBoard{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", place=" + place +
                ", gamesPlayed=" + gamesPlayed +
                ", averageScore=" + averageScore +
                '}';
    }
}