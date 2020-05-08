package com.voting.voto.dao;

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
public class VotoDAO extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        this.setDataSource(dataSource);
    }

    public VotoEntity get(long id) {
        final String query = "SELECT * FROM VOTOS WHERE ID = ?";
        final Object[] entries = new Object[]{id};
        VotoEntity entity;

        try {
            entity = this.getJdbcTemplate().queryForObject(query, entries, new BeanPropertyRowMapper<>(VotoEntity.class));
        } catch (EmptyResultDataAccessException e) {
            entity = null;
        }

        return entity;
    }

    public List<VotoEntity> getVotosBy(long idSessao) {
        final String query = "SELECT * FROM VOTOS WHERE ID_SESSAO = ?";
        final Object[] entries = new Object[]{idSessao};

        return this.queryEntities(query, entries);
    }

    public List<VotoEntity> getVotosBy(long idSessao, long idAssociado) {
        final String query = "SELECT * FROM VOTOS WHERE ID_SESSAO = ? AND ID_ASSOCIADO = ?";
        final Object[] entries = new Object[]{idSessao, idAssociado};

        return this.queryEntities(query, entries);
    }

    private List<VotoEntity> queryEntities(String query, Object[] entries) {
        List<VotoEntity> entities;

        try {
            entities = this.getJdbcTemplate().query(query, entries, new BeanPropertyRowMapper<>(VotoEntity.class));
        } catch (DataAccessException e) {
            entities = Collections.emptyList();
        }

        return entities;
    }

    public VotoEntity save(VotoEntity entity) {
        final String query = "INSERT INTO VOTOS (ID_SESSAO, ID_ASSOCIADO, VOTO) VALUES (?, ?, ?)";
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();
        VotoEntity voto = entity;

        try {
            this.getJdbcTemplate().update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, entity.getIdSessao());
                statement.setLong(2, entity.getIdAssociado());
                statement.setBoolean(3, entity.isVoto());
                return statement;
            }, holder);
            voto.setId(holder.getKey().longValue());
        } catch (DataAccessException e) {
            voto = null;
        }

        return voto;
    }
}
