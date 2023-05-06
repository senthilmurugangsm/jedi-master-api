package com.demo.jedimaster.service;

import com.demo.jedimaster.model.PeopleApiResponse;
import com.demo.jedimaster.model.PlanetApiResponse;
import com.demo.jedimaster.model.StarshipApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface JediMasterService {
    PeopleApiResponse fetchPeople(String peopleUrl);

    StarshipApiResponse fetchStarship(String starshipUrl);

    PlanetApiResponse fetchPlanet(String planetUrl);

    int getCrewCount(String starshipUrl);

    boolean isPeopleLiveOnPlanet(String planetUrl, String peopleUrl);

}
