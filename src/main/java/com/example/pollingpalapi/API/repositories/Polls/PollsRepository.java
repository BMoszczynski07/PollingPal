package com.example.pollingpalapi.API.repositories.Polls;

import com.example.pollingpalapi.API.Mappers.Polls.LikeMapper;
import com.example.pollingpalapi.API.Mappers.Polls.OptionMapper;
import com.example.pollingpalapi.API.Mappers.Polls.PollsMapper;
import com.example.pollingpalapi.API.Mappers.Polls.VoteMapper;
import com.example.pollingpalapi.API.Models.Polls.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
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
                "LEFT JOIN users u ON p.user_id = u.id " +
                "LEFT JOIN poll_hearts ph ON p.id = ph.poll_id " +
                "LEFT JOIN poll_comments pc ON p.id = pc.poll_id " +
                "WHERE p.poll_date <= ? AND p.poll_date >= ? " +
                "GROUP BY p.id " +
                "ORDER BY hearts DESC LIMIT 25";

        List<Poll> selectedPolls = jdbc.query(sql, new PollsMapper(), new Object[]{currentDate, pastDate});

        Collections.shuffle(selectedPolls);

        return selectedPolls;
    }

    public String addUserVote(int optionId, int userId) {
        List<String> getPollId = getPollId(optionId);
        String pollId = getPollId.get(0);

        String sql1 = "SELECT * FROM poll_votes WHERE poll_id = ? AND user_id = ?";
        List<Vote> vote = jdbc.query(sql1, new VoteMapper(), new Object[]{pollId, userId});

        if (vote.isEmpty()) {
            String sql2 = "INSERT INTO poll_votes (user_id, option_id, poll_id) " +
                    "VALUES (?, ?, ?)";

            jdbc.update(sql2, new Object[]{userId, optionId, pollId});

            return "Dodano głos";
        }

        if (vote.get(0).getOption_id() != optionId) {
            String sql5 = "DELETE FROM poll_votes WHERE poll_id = ? AND user_id = ?";
            jdbc.update(sql5, new Object[]{pollId, userId});

            String sql4 = "INSERT INTO poll_votes (user_id, option_id, poll_id) " +
                    "VALUES (?, ?, ?)";

            jdbc.update(sql4, new Object[]{userId, optionId, pollId});

            return "Dodano głos";
        }

        String sql3 = "DELETE FROM poll_votes WHERE poll_id = ? AND user_id = ?";
        jdbc.update(sql3, new Object[]{pollId, userId});

        return "Usunięto głos";
    }

    public List<Vote> getVotesForPoll(String pollId, int userId) {
        String sql = "SELECT * FROM poll_votes WHERE poll_id = ? AND user_id = ?";

        return jdbc.query(sql, new VoteMapper(), new Object[]{pollId, userId});
    }

    public List<String> getPollId (int optionId) {
        String sql = "SELECT poll_id FROM poll_options WHERE id = ?";

        return jdbc.queryForList(sql, String.class, new Object[]{optionId});
    }

    public List<Boolean> isSelectedByUser(Integer userId, int optionId) {
        String sql = "SELECT (CASE WHEN user_id = ? THEN true ELSE false END) as selected FROM poll_votes " +
                "WHERE option_id = ? AND user_id = ?";

        return jdbc.query(sql, (rs, rowNum) -> rs.getBoolean("selected"), userId, optionId, userId);
    }

    public List<Vote> getOptionVotes(int optionId) {
        String sql = "SELECT * FROM poll_votes WHERE option_id = ?";

        return jdbc.query(sql, new VoteMapper(), new Object[]{optionId});
    }

    public List<Option> getPollOptions(String pollId) {
        String sql = "SELECT * FROM poll_options WHERE poll_id = ?";

        List<Option> selectedOptions = jdbc.query(sql, new Object[]{pollId}, new OptionMapper());

        return selectedOptions;
    }

    public List<Poll> searchPolls(String searchText) {
        String searchParam = "%" + searchText + "%";

        String sql = "SELECT p.*, u.username AS user, u.profile_pic AS profile_pic, COUNT(DISTINCT ph.id) " +
                "AS hearts, COUNT(DISTINCT pc.id) AS comments FROM polls p " +
                "LEFT JOIN users u ON p.user_id = u.id " +
                "LEFT JOIN poll_hearts ph ON p.id = ph.poll_id " +
                "LEFT JOIN poll_comments pc ON p.id = pc.poll_id " +
                "WHERE p.poll_question LIKE ? " +
                "GROUP BY p.id " +
                "ORDER BY hearts DESC LIMIT 25";

        List<Poll> selectedPolls = jdbc.query(sql, new Object[]{searchParam}, new PollsMapper());

        return selectedPolls;
    }

    public void addPoll(AddPollDTO newPoll) {
        Date currentDate = new Date();

        UUID uuid = UUID.randomUUID();
        String randomId = uuid.toString();

        String insertPollSQL = "INSERT INTO polls (id, user_id, poll_question, poll_date, nickname) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbc.update(insertPollSQL, new Object[]{randomId, newPoll.getUserId(), newPoll.getPoll_question(), currentDate, newPoll.getNickname()});

        for (String option : newPoll.getOptions()) {
            String insertOptionSQL = "INSERT INTO poll_options (poll_id, poll_option) VALUES (?, ?)";

            jdbc.update(insertOptionSQL, new Object[]{randomId, option});
        }
    }
}
