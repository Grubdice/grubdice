package co.grubdice.scorekeeper.model.external

import groovy.transform.TupleConstructor
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ReflectionToStringBuilder

@TupleConstructor
class ExternalScoreBoard {

    def String name
    def int score
    def int place
    def int gamesPlayed
    def double averageScore

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object)
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this)
    }
}