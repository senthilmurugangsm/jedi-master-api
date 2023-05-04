package com.demo.jedimaster.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StarWarsInfo {

    private Object starship; // Object type used to return empty {} response, if no starships.
    private int crew;
    @JsonProperty("isLeiaOnPlanet")
    private boolean leiaOnPlanet;

}
