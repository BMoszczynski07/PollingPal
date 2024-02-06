package com.example.pollingpalapi.API.routes.Polls;

import com.example.pollingpalapi.API.Models.Poll;
import com.example.pollingpalapi.API.Models.Response;
import com.example.pollingpalapi.API.repositories.Polls.PollsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Polls {
    private final PollsRepository polls;

    @Autowired
    public Polls(PollsRepository polls) {
        this.polls = polls;
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
}
