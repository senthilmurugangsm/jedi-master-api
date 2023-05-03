package com.demo.jedimaster.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Starship {

    private String name;
    @JsonProperty("class")
    private String starshipClass;
    private String model;

}
