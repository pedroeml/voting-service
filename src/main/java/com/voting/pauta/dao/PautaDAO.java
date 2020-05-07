package com.voting.pauta.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Statement;

@Repository
public class PautaDAO extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        this.setDataSource(dataSource);
    }

    public PautaEntity get(long id) {
        final String query = "SELECT * FROM PAUTAS WHERE ID = ?";
        final Object[] entries = new Object[]{id};
        PautaEntity entity;

        try {
            entity = this.getJdbcTemplate().queryForObject(query, entries, new BeanPropertyRowMapper<>(PautaEntity.class));
        } catch (EmptyResultDataAccessException e) {
            entity = null;
        }

        return entity;
    }

    public PautaEntity save() {
        final String query = "INSERT INTO PAUTAS () VALUES ()";
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();
        PautaEntity entity;

        try {
            this.getJdbcTemplate().update(con -> con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS), holder);
            entity = this.get(holder.getKey().longValue());
        } catch (DataAccessException e) {
            entity = null;
        }

        return entity;
    }
}
