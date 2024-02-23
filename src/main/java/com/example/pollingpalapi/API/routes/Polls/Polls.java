package com.example.pollingpalapi.API.routes.Polls;

import com.example.pollingpalapi.API.Models.Polls.*;
import com.example.pollingpalapi.API.Models.Response.Response;
import com.example.pollingpalapi.API.repositories.Polls.PollsRepository;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class Polls {
    private final PollsRepository polls;

    @Autowired
    public Polls(PollsRepository polls) {
        this.polls = polls;
    }

    @PostMapping("/like-poll")
    public Response likePoll(@RequestBody Like like) {
        try {
            List<Like> findLike = polls.findLike(like);

            System.out.println(findLike.toString());

            if (!findLike.isEmpty()) {
                polls.removeLike(like);

                System.out.println("removing like...");

                return new Response<Integer>(200, 0);
            }

            System.out.println("adding like...");

            polls.addLike(like);

            return new Response<Integer>(200, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<Object>(500, "Wystąpił problem techniczny " + e.toString());
        }
    }

    @PostMapping("/search-polls")
    public Response<Object> searchPolls(@RequestBody SearchDTO search) {
        try {
            List<Poll> selectedPolls = polls.searchPolls(search.getContent());

            return new Response<Object>(200, selectedPolls);
        } catch (Exception e) {
            return new Response<Object>(500, "Wystąpił problem techniczny! \n " + e.getMessage());
        }
    }

    @PostMapping("/add-vote")
    public Response<Object> addVote(@RequestBody OptionDTO option) {
        try {
            List<Integer> getPollId = polls.getPollId(option.getOptionId());
            Integer pollId = getPollId.get(0);

            polls.removeUserVote(pollId, option.getUserId());

            polls.addUserVote(pollId, option.getOptionId(), option.getUserId());

            return new Response<Object>(200, "Dodano głos");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<Object>(500, "Wystąpił problem techniczny! \n " + e.toString());
        }
    }

    @GetMapping("/get-polls/{minusDays}")
    public Response<Object> getPolls(@PathVariable int minusDays) {
        try {
            List<Poll> selectedPolls = polls.getPolls(minusDays);

            return new Response<Object>(200, selectedPolls);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<Object>(500, "Wystąpił problem techniczny! \n " + e.getMessage());
        }
    }

    @GetMapping("/get-votes-for-user/{pollId}/{userId}")
    public Response<Object> getVotesForUser(@PathVariable int pollId, @PathVariable Integer userId) {
        try {
            List<Option> pollOptions = polls.getPollOptions(pollId);

            ArrayList<VoteForOption> votes = new ArrayList<VoteForOption>();

            for (Option option : pollOptions) {
                List<Vote> votesForOption = polls.getOptionVotes(option.getId());

                List<Boolean> isSelectedByUser = polls.isSelectedByUser(userId, option.getId());

                votes.add(new VoteForOption(option.getPoll_option(), votesForOption.size(), !isSelectedByUser.isEmpty()));
            }

            return new Response<Object>(200, votes);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<Object>(500, "Wystapił problem techniczny \n " + e.toString());
        }
    }

    @GetMapping("/get-votes/{pollId}")
    public Response<Object> getVotes(@PathVariable int pollId) {
        try {
            List<Option> pollOptions = polls.getPollOptions(pollId);

            ArrayList<VoteForOption> votes = new ArrayList<VoteForOption>();

            for (Option option : pollOptions) {
                List<Vote> votesForOption = polls.getOptionVotes(option.getId());

                votes.add(new VoteForOption(option.getPoll_option(), votesForOption.size(), false));
            }

            return new Response<Object>(200, votes);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<Object>(500, "Wystapił problem techniczny \n " + e.toString());
        }
    }

    @GetMapping("/get-poll-options/{pollId}")
    public Response<Object> getPollOptions(@PathVariable int pollId) {
        try {
            List<Option> selectedOptions = polls.getPollOptions(pollId);

            return new Response<Object>(200, selectedOptions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<Object>(500, "Wystąpił problem techniczny! \n" + e.getMessage());
        }
    }
}
