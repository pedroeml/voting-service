package com.voting.sessao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Repository
public class SessaoDAO extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        this.setDataSource(dataSource);
    }

    public SessaoEntity get(long id) {
        final String query = "SELECT * FROM SESSOES WHERE ID = ?";
        final Object[] entries = new Object[]{id};
        SessaoEntity entity;

        try {
            entity = this.getJdbcTemplate().queryForObject(query, entries, new BeanPropertyRowMapper<>(SessaoEntity.class));
        } catch (EmptyResultDataAccessException e) {
            entity = null;
        }

        return entity;
    }

    public List<SessaoEntity> getSessoesBy(long idPauta) {
        final String query = "SELECT * FROM SESSOES WHERE ID_PAUTA = ?";
        final Object[] entries = new Object[]{idPauta};
        List<SessaoEntity> entities;

        try {
            entities = this.getJdbcTemplate().query(query, entries, new BeanPropertyRowMapper<>(SessaoEntity.class));
        } catch (DataAccessException e) {
            entities = Collections.emptyList();
        }

        return entities;
    }

    public SessaoEntity save(SessaoEntity entity) {
        final String query = "INSERT INTO SESSOES (ID_PAUTA, ABERTURA, FECHAMENTO) VALUES (?, ?, ?)";
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();
        SessaoEntity sessao = entity;

        try {
            this.getJdbcTemplate().update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, entity.getIdPauta());
                statement.setString(2, entity.getAbertura());
                statement.setString(3, entity.getFechamento());
                return statement;
            }, holder);
            sessao.setId(holder.getKey().longValue());
        } catch (DataAccessException e) {
            sessao = null;
        }

        return sessao;
    }
}
