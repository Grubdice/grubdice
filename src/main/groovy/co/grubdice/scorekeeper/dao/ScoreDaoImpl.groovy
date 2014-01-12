package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.hibernate.criterion.Restrictions
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Repository
@Transactional
@Slf4j
class ScoreDaoImpl extends BaseDaoImpl<GameResult> implements ScoreDao {

    @Override
    List<SearchResults> getScoreBoard() {
        /*
        def query =
                getSession().createCriteria(GameResult.class)
                    .setProjection(Projections.projectionList()
                        .add(Projections.property("player"), "player")
                        .add(Projections.sum("score"))
                        .add(Projections.groupProperty("player")))
                    .setResultTransformer(Transformers.aliasToBean(SearchResults.class))
        List<SearchResults> queryList = query.list()
        */

        def queryList = getSession().createSQLQuery("select p.name, sum(score) from game_results gr join players p on gr.player_id = p.id group by p.id").list()

        def returnList = queryList.collect {
            new SearchResults(name: it[0], score: it[1] + 1500)
        }

        returnList.sort { a,b ->
            a.score <=> b.score
        }
        return returnList
    }

    @Override
    List<GameResult> getPlayersScores(Player player) {
        def query = getSession().createCriteria(GameResult.class)
            .add(Restrictions.eq("player", player));

        return query.list()
    }


    static class SearchResults {
        def String name
        def int score
    }

}
