package com.example.pollingpalapi.API.Mappers.Polls;

import com.example.pollingpalapi.API.Models.Poll;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PollsMapper implements RowMapper<Poll> {
    @Override
    public Poll mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Poll poll = new Poll();

        poll.setId(resultSet.getInt("Id"));
        poll.setUser_id(resultSet.getInt("user_id"));
        poll.setUser(resultSet.getString("user"));
        poll.setUser(resultSet.getString("profile_pic"));
        poll.setPoll_date(resultSet.getString("poll_date"));
        poll.setPoll_question(resultSet.getString("poll_question"));
        poll.setPoll_hearts(resultSet.getInt("poll_hearts"));
        poll.setPoll_comments(resultSet.getInt("poll_comments"));

        return poll;
    }
}
