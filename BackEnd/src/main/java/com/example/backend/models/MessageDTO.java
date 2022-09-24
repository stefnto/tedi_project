package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO {

    private String dateSent;
    private String text;
    private String sender;
}
