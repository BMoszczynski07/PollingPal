package com.example.pollingpalapi.API.repositories.Polls;

import com.example.pollingpalapi.API.Mappers.Polls.LikeMapper;
import com.example.pollingpalapi.API.Mappers.Polls.OptionMapper;
import com.example.pollingpalapi.API.Mappers.Polls.PollsMapper;
import com.example.pollingpalapi.API.Models.Polls.Like;
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

    public void addLike (Like like) {
        String sql = "INSERT INTO poll_hearts (user_id, poll_id) VALUES (?, ?)";

        jdbc.update(sql, new Object[]{like.getUserId(), like.getPollId()});
    }

    public void removeLike (Like like) {
        String sql = "DELETE FROM poll_hearts WHERE user_id = ? AND poll_id = ?";

        jdbc.update(sql, new Object[]{like.getUserId(), like.getPollId()});
    }

    public List<Like> findLike (Like like) {
        String sql = "SELECT * FROM poll_hearts WHERE poll_id = ? AND user_id = ?";

        List<Like> query = jdbc.query(sql, new Object[]{like.getPollId(), like.getUserId()}, new LikeMapper());

        return query;
    }

    public List<Poll> getPolls(int minusDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate pastDate = currentDate.minusDays(minusDays);

        String sql = "SELECT polls.*, users.username AS user, users.profile_pic AS profile_pic, COUNT(poll_hearts.id) AS hearts\n" +
                "FROM polls\n" +
                "INNER JOIN users ON polls.user_id = users.id\n" +
                "LEFT JOIN poll_hearts ON polls.id = poll_hearts.poll_id\n" +
                "WHERE polls.poll_date >= ? AND polls.poll_date <= ?\n" +
                "GROUP BY polls.id, users.id\n" +
                "ORDER BY hearts DESC LIMIT 25";

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

        String sql = "SELECT polls.*, users.username AS user, users.profile_pic AS profile_pic, count(poll_hearts.id) AS hearts FROM polls INNER JOIN poll_hearts ON polls.id = poll_hearts.poll_id INNER JOIN users ON polls.user_id = users.id WHERE polls.poll_question LIKE ? OR users.username LIKE ? ORDER BY hearts DESC LIMIT 25";

        List<Poll> selectedPolls = jdbc.query(sql, new Object[]{searchParam, searchParam}, new PollsMapper());

        return selectedPolls;
    }
}
