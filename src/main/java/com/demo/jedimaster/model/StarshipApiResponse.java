package com.demo.jedimaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StarshipApiResponse {
    private String name;
    @JsonProperty("starship_class")
    private String starshipClass;
    private String model;
    private String crew;
    private String url;
}
