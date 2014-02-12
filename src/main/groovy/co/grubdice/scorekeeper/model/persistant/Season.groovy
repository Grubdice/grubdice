package co.grubdice.scorekeeper.model.persistant

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.TupleConstructor
import org.hibernate.annotations.Columns
import org.hibernate.annotations.Type
import org.joda.time.DateTime

import javax.persistence.*

@Entity
@Table(name = "seasons")
@TupleConstructor(excludes = 'id')
class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Integer id

    @Columns(columns=[ @Column(name="start_date")])
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    DateTime startDate

    @Columns(columns=[ @Column(name="end_date")])
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    DateTime endDate

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "season")
    List<Game> games = []

}
