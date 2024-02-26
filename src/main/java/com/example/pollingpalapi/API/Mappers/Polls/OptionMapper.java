package com.example.pollingpalapi.API.Mappers.Polls;

import com.example.pollingpalapi.API.Models.Polls.Option;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OptionMapper implements RowMapper<Option> {
    @Override
    public Option mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Option option = new Option();

        option.setId(resultSet.getInt("id"));
        option.setPoll_id(resultSet.getString("poll_id"));
        option.setPoll_option(resultSet.getString("poll_option"));

        return option;
    }
}
