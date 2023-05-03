package com.demo.jedimaster.model;

import lombok.Data;

import java.util.List;

@Data
public class PlanetApiResponse {
    private String name;
    private List<String> residents;
    private String url;
}
