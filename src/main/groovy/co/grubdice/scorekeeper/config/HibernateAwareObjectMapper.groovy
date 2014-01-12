package co.grubdice.scorekeeper.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module
import com.fasterxml.jackson.datatype.joda.JodaModule

public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {
        registerModules(new Hibernate4Module(), new JodaModule());
    }
}
