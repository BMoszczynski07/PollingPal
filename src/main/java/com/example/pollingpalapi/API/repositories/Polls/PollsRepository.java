package com.example.pollingpalapi.API.repositories.Polls;

import com.example.pollingpalapi.API.Mappers.Polls.OptionMapper;
import com.example.pollingpalapi.API.Mappers.Polls.PollsMapper;
import com.example.pollingpalapi.API.Models.Polls.Option;
import com.example.pollingpalapi.API.Models.Polls.Poll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class PollsRepository {
    private final JdbcTemplate jdbc;

    public PollsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Poll> getPolls(int minusDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate pastDate = currentDate.minusDays(minusDays);

        String sql = "SELECT polls.*, users.username AS user, users.profile_pic AS profile_pic " +
                "FROM polls " +
                "INNER JOIN users ON polls.user_id = users.id " +
                "WHERE polls.poll_date >= ? AND polls.poll_date <= ? " +
                "ORDER BY polls.poll_hearts DESC";

        List<Poll> selectedPolls = jdbc.query(sql, new Object[]{pastDate, pastDate.plusDays(14)}, new PollsMapper());

        Collections.shuffle(selectedPolls);

        return selectedPolls;
    }

    public List<Option> getPollOptions(int pollId) {
        String sql = "SELECT * FROM poll_options WHERE poll_id = ?";

        List<Option> selectedOptions = jdbc.query(sql, new Object[]{pollId}, new OptionMapper());

        return selectedOptions;
    }

    public List<Poll> searchPolls(String searchText) {
        String searchParam = "%" + searchText + "%";

        String sql = "SELECT polls.*, users.username AS user, users.profile_pic AS profile_pic FROM polls INNER JOIN users ON polls.user_id = users.id WHERE polls.poll_question LIKE ? OR users.username LIKE ? ORDER BY polls.poll_hearts DESC LIMIT 25";

        List<Poll> selectedPolls = jdbc.query(sql, new Object[]{searchParam, searchParam}, new PollsMapper());

        return selectedPolls;
    }
}
