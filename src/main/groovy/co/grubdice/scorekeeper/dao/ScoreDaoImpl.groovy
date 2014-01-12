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
    Map<String, Integer> getScoreBoard() {
        /*
        def query =
                getSession().createCriteria(GameResult.class)
                    .setProjection(Projections.projectionList()
                        .add(Projections.property("player"), "player")
                        .add(Projections.sum("score"))
                        .add(Projections.groupProperty("player")))
                    .setResultTransformer(Transformers.aliasToBean(SearchResults.class))
        List<SearchResults> resultList = query.list()
        */

        def resultList = getSession().createSQLQuery("select p.name, sum(score) from game_results gr join players p on gr.player_id = p.id group by p.id").list()

        def returnMap = [:]
        resultList.each {
            returnMap[it[0]] = (int)it[1] + 1500
        }

        returnMap.sort { a,b ->
            a.value <=> b.value
        }
        return returnMap
    }

    @Override
    List<GameResult> getPlayersScores(Player player) {
        def query = getSession().createCriteria(GameResult.class)
            .add(Restrictions.eq("player", player));

        return query.list()
    }

    /*
    static class SearchResults {
        def Player player
        def int score
    }
    */
}
