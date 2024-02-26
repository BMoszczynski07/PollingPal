package com.example.pollingpalapi.API.Mappers.Polls;

import com.example.pollingpalapi.API.Models.Polls.Like;
import com.example.pollingpalapi.API.Models.Polls.Option;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Like like = new Like();

        like.setUserId(resultSet.getInt("user_id"));
        like.setPollId(resultSet.getString("poll_id"));

        return like;
    }
}
