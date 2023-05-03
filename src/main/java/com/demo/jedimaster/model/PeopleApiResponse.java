package com.demo.jedimaster.model;

import lombok.Data;

import java.util.List;

@Data
public class PeopleApiResponse {
    private String name;
    private List<String> starships;
    private String url;
    private String homeworld;
}
