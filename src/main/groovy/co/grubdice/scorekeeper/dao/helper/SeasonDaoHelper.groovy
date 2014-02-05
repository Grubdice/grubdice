package co.grubdice.scorekeeper.dao.helper

import co.grubdice.scorekeeper.dao.SeasonDao
import co.grubdice.scorekeeper.exception.SeasonNotFoundException
import co.grubdice.scorekeeper.model.persistant.Season
import org.joda.time.DateTime

class SeasonDaoHelper {

    public static Season getCurrentSeason(SeasonDao dao) {
        return getSeason(dao, DateTime.now())
    }

    public static Season getSeason(SeasonDao dao, Integer id) {
        def season = dao.findOne(id)
        return verifySeason(season)
    }

    public static Season getSeason(SeasonDao dao, DateTime time){
        def season = dao.findCurrentSeason(time)
        return verifySeason(season, time)
    }

    private static Season verifySeason(Season season, DateTime time = null) {
        if (!season) {
            throw new SeasonNotFoundException(time)
        }
        return season
    }
}
