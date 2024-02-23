package com.example.pollingpalapi.API.repositories.Polls;

import com.example.pollingpalapi.API.Mappers.Polls.LikeMapper;
import com.example.pollingpalapi.API.Mappers.Polls.OptionMapper;
import com.example.pollingpalapi.API.Mappers.Polls.PollsMapper;
import com.example.pollingpalapi.API.Mappers.Polls.VoteMapper;
import com.example.pollingpalapi.API.Models.Polls.Like;
import com.example.pollingpalapi.API.Models.Polls.Option;
import com.example.pollingpalapi.API.Models.Polls.Poll;
import com.example.pollingpalapi.API.Models.Polls.Vote;
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

        List<Like> likeQuery = jdbc.query(sql, new Object[]{like.getPollId(), like.getUserId()}, new LikeMapper());

        return likeQuery;
    }

    public List<Poll> getPolls(int minusDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate pastDate = currentDate.minusDays(minusDays);

        String sql = "SELECT p.*, u.username AS user, u.profile_pic AS profile_pic, COUNT(DISTINCT ph.id) " +
                "AS hearts, COUNT(DISTINCT pc.id) AS comments FROM polls p " +
                "INNER JOIN users u ON p.user_id = u.id " +
                "LEFT JOIN poll_hearts ph ON p.id = ph.poll_id " +
                "LEFT JOIN poll_comments pc ON p.id = pc.poll_id " +
                "WHERE p.poll_date <= ? AND p.poll_date >= ? " +
                "GROUP BY p.id " +
                "ORDER BY hearts DESC LIMIT 25";

        List<Poll> selectedPolls = jdbc.query(sql, new PollsMapper(), new Object[]{currentDate, pastDate});

        Collections.shuffle(selectedPolls);

        return selectedPolls;
    }

    public List<Boolean> isSelectedByUser(Integer userId, int optionId) {
        String sql = "SELECT (CASE WHEN user_id = ? THEN true ELSE false END) as selected FROM poll_votes " +
                "WHERE option_id = ?";

        System.out.println();

        return jdbc.query(sql, (rs, rowNum) -> rs.getBoolean("selected"), userId, optionId);
    }

    public List<Vote> getOptionVotes(int optionId) {
        String sql = "SELECT * FROM poll_votes WHERE option_id = ?";

        return jdbc.query(sql, new VoteMapper(), new Object[]{optionId});
    }

    public List<Option> getPollOptions(int pollId) {
        String sql = "SELECT * FROM poll_options WHERE poll_id = ?";

        List<Option> selectedOptions = jdbc.query(sql, new Object[]{pollId}, new OptionMapper());

        return selectedOptions;
    }

    public List<Poll> searchPolls(String searchText) {
        String searchParam = "%" + searchText + "%";

        String sql = "SELECT p.*, u.username AS user, u.profile_pic AS profile_pic, COUNT(DISTINCT ph.id) " +
                "AS hearts, COUNT(DISTINCT pc.id) AS comments FROM polls p " +
                "INNER JOIN users u ON p.user_id = u.id " +
                "LEFT JOIN poll_hearts ph ON p.id = ph.poll_id " +
                "LEFT JOIN poll_comments pc ON p.id = pc.poll_id " +
                "WHERE p.poll_question LIKE ? " +
                "GROUP BY p.id " +
                "ORDER BY hearts DESC LIMIT 25";

        List<Poll> selectedPolls = jdbc.query(sql, new Object[]{searchParam}, new PollsMapper());

        return selectedPolls;
    }
}
