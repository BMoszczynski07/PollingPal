package com.example.pollingpalapi.API.Mappers.Polls;

import com.example.pollingpalapi.API.Models.Polls.Vote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper<Vote> {
    @Override
    public Vote mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Vote vote = new Vote();

        vote.setId(resultSet.getInt("id"));
        vote.setUser_id(resultSet.getInt("user_id"));
        vote.setOption_id(resultSet.getInt("option_id"));
        vote.setPoll_id(resultSet.getInt("poll_id"));

        return vote;
    }
}
