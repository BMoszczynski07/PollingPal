package com.example.pollingpalapi.API.Models.Polls;

public class VoteForOption {
    public int optionId;
    public String option;
    public int votesQty;
    public boolean selected;

    public VoteForOption(int optionId, String option, int votesQty, boolean selected) {
        this.optionId = optionId;
        this.option = option;
        this.votesQty = votesQty;
        this.selected = selected;
    }
}
