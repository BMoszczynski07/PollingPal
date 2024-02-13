package com.example.pollingpalapi.API.routes.Polls;

import com.example.pollingpalapi.API.Models.Polls.Option;
import com.example.pollingpalapi.API.Models.Polls.Poll;
import com.example.pollingpalapi.API.Models.Response.Response;
import com.example.pollingpalapi.API.Models.Polls.SearchDTO;
import com.example.pollingpalapi.API.repositories.Polls.PollsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Polls {
    private final PollsRepository polls;

    @Autowired
    public Polls(PollsRepository polls) {
        this.polls = polls;
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

    @GetMapping("/get-polls/{lastDays}")
    public Response<Object> getPolls(@PathVariable int lastDays) {
        try {
            List<Poll> selectedPolls = polls.getPolls(lastDays);

            return new Response<Object>(200, selectedPolls);
        } catch (Exception e) {
            return new Response<Object>(500, "Wystąpił problem techniczny! \n " + e.getMessage());
        }
    }

    @GetMapping("/get-poll-options/{pollId}")
    public Response<Object> getPollsOptions(@PathVariable int pollId) {
        try {
            List<Option> selectedOptions = polls.getPollOptions(pollId);

            return new Response<Object>(200, selectedOptions);
        } catch (Exception e) {
            return new Response<Object>(500, "Wystąpił problem techniczny! \n" + e.getMessage());
        }
    }
}
