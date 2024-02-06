package com.example.pollingpalapi.API.repositories.Polls;

import com.example.pollingpalapi.API.Mappers.Polls.PollsMapper;
import com.example.pollingpalapi.API.Models.Poll;
import com.example.pollingpalapi.API.Models.Response;
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

        String sql = "SELECT polls.*, users.username AS user " +
                "FROM polls " +
                "INNER JOIN users ON polls.user_id = users.id " +
                "WHERE polls.poll_date >= ? AND polls.poll_date <= ? " +
                "ORDER BY polls.poll_hearts DESC";

        List<Poll> selectedPolls = jdbc.query(sql, new Object[]{pastDate, pastDate.plusDays(14)}, new PollsMapper());

        Collections.shuffle(selectedPolls);

        return selectedPolls;
    }
}
