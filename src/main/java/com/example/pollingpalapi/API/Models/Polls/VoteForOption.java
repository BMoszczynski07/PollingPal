package com.example.pollingpalapi.API.Models.Polls;

public class VoteForOption {
    public String option;
    public int votesQty;

    public VoteForOption(String option, int votesQty) {
        this.option = option;
        this.votesQty = votesQty;
    }
}
