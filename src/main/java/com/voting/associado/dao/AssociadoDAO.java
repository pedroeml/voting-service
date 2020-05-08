package com.voting.associado.dao;

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

@Repository
public class AssociadoDAO extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        this.setDataSource(dataSource);
    }

    public AssociadoEntity find(AssociadoEntity entity) {
        final String query = "SELECT * FROM ASSOCIADOS WHERE CPF = ?";
        final Object[] entries = new Object[]{entity.getCpf()};
        AssociadoEntity associado;

        try {
            associado = this.getJdbcTemplate().queryForObject(query, entries, new BeanPropertyRowMapper<>(AssociadoEntity.class));
        } catch (EmptyResultDataAccessException e) {
            associado = null;
        }

        return associado;
    }

    public AssociadoEntity get(long id) {
        final String query = "SELECT * FROM ASSOCIADOS WHERE ID = ?";
        final Object[] entries = new Object[]{id};
        AssociadoEntity entity;

        try {
            entity = this.getJdbcTemplate().queryForObject(query, entries, new BeanPropertyRowMapper<>(AssociadoEntity.class));
        } catch (EmptyResultDataAccessException e) {
            entity = null;
        }

        return entity;
    }

    public AssociadoEntity save(AssociadoEntity entity) {
        final String query = "INSERT INTO ASSOCIADOS (CPF) VALUES (?)";
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();
        AssociadoEntity associado;

        try {
            this.getJdbcTemplate().update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, entity.getCpf());
                return statement;
            }, holder);
            associado = this.get(holder.getKey().longValue());
        } catch (DataAccessException e) {
            associado = null;
        }

        return associado;
    }
}
