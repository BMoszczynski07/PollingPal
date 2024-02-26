package com.example.pollingpalapi.API.Models.Polls;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddPollDTO {
    private int userId;
    private String nickname;
    private String poll_question;
    private List<String> options;
}
