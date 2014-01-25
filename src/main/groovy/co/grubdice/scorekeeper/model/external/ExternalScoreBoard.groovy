package co.grubdice.scorekeeper.model.external

class ExternalScoreBoard {

    def int place
    def String name
    def int score

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ExternalScoreBoard)) return false

        ExternalScoreBoard that = (ExternalScoreBoard) o

        if (place != that.place) return false
        if (score != that.score) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        int result
        result = place
        result = 31 * result + (name != null ? name.hashCode() : 0)
        result = 31 * result + score
        return result
    }


    @Override
    public String toString() {
        return "SearchResults{" +
                "place=" + place +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}