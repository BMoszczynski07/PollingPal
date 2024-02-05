package com.example.pollingpalapi.API.routes.Polls;

import com.example.pollingpalapi.API.repositories.Polls.PollsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Polls {
    private final PollsRepository polls;

    @Autowired
    public Polls(PollsRepository polls) {
        this.polls = polls;
    }
}
