package com.example.pollingpalapi.API.Models.Polls;

public class VoteForOption {
    public String option;
    public int votesQty;
    public boolean selected;

    public VoteForOption(String option, int votesQty, boolean selected) {
        this.option = option;
        this.votesQty = votesQty;
        this.selected = selected;
    }
}
