package com.demo.jedimaster.controller;

import com.demo.jedimaster.dto.StarWarsInfo;
import com.demo.jedimaster.dto.Starship;
import com.demo.jedimaster.model.PeopleApiResponse;
import com.demo.jedimaster.model.StarshipApiResponse;
import com.demo.jedimaster.service.StarWarService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/jedi-master")
@Log4j2
public class JediMasterController {

    @Autowired
    StarWarService starWarService;

    @Value("${swapi.api.base-url}")
    private String swapiApiBaseUrl;

    @Value("${swapi.api.people.darth-vader}")
    private String peopleDarthVader;

    @Value("${swapi.api.people.leia-organa}")
    private String peopleLeiaOrgana;

    @Value("${swapi.api.planet.alderaan}")
    private String planetAlderaan;
    @Value("${swapi.api.starship.death-star}")
    private String starshipDeathStar;

    @GetMapping(value = "/information")
    public ResponseEntity<Object> fetchStarWarsInformation() {
        try {
            StarshipApiResponse starshipApiResponse = null;
            StarWarsInfo starWarsInfo = new StarWarsInfo();

            String peopleDarthVaderUrl = swapiApiBaseUrl + peopleDarthVader;
            PeopleApiResponse response = starWarService.fetchPeople(peopleDarthVaderUrl);

            List<String> starships = response.getStarships();

            if (starships != null && starships.size() > 0) {
                String starshipDarthVaderUrl = starships.get(0);
                starshipApiResponse = starWarService.fetchStarship(starshipDarthVaderUrl);
                Starship starship = new Starship(starshipApiResponse.getName(), starshipApiResponse.getStarshipClass(), starshipApiResponse.getModel());
                starWarsInfo.setStarship(starship);
            } else {
                starWarsInfo.setStarship(null);
            }

            String starshipDeathStarUrl = swapiApiBaseUrl + starshipDeathStar;
            int crew = starWarService.getCrewCount(starshipDeathStarUrl);
            starWarsInfo.setCrew(crew);

            String planetAlderaanUrl = swapiApiBaseUrl + planetAlderaan;
            String peopleLeiaOrganaUrl = swapiApiBaseUrl + peopleLeiaOrgana;
            boolean isLeiaOnPlanet = starWarService.isPeopleLiveOnPlanet(planetAlderaanUrl, peopleLeiaOrganaUrl);
            starWarsInfo.setLeiaOnPlanet(isLeiaOnPlanet);

            return new ResponseEntity<>(starWarsInfo, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Server error : {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
