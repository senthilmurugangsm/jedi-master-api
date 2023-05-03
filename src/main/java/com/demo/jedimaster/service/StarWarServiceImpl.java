package com.demo.jedimaster.service;

import com.demo.jedimaster.exception.RestTemplateResponseHandler;
import com.demo.jedimaster.model.PeopleApiResponse;
import com.demo.jedimaster.model.PlanetApiResponse;
import com.demo.jedimaster.model.StarshipApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class StarWarServiceImpl implements StarWarService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RestTemplateResponseHandler restTemplateResponseHandler;

    @Override
    public PeopleApiResponse fetchPeople(String peopleUrl) {
        ResponseEntity<PeopleApiResponse> peopleApiResponseEntity = restTemplate.getForEntity(peopleUrl, PeopleApiResponse.class);
        PeopleApiResponse peopleApiResponse = restTemplateResponseHandler.handleResponse(peopleApiResponseEntity);
        return peopleApiResponse;
    }

    @Override
    public StarshipApiResponse fetchStarship(String starshipUrl) {
        ResponseEntity<StarshipApiResponse> starshipApiResponseEntity = restTemplate.getForEntity(starshipUrl, StarshipApiResponse.class);
        StarshipApiResponse starshipApiResponse = restTemplateResponseHandler.handleResponse(starshipApiResponseEntity);
        return starshipApiResponse;
    }

    @Override
    public PlanetApiResponse fetchPlanet(String planetUrl) {
        ResponseEntity<PlanetApiResponse> planetApiResponseEntity = restTemplate.getForEntity(planetUrl, PlanetApiResponse.class);
        PlanetApiResponse planetApiResponse = restTemplateResponseHandler.handleResponse(planetApiResponseEntity);
        return planetApiResponse;
    }

    @Override
    public int getCrewCount(String starshipUrl) {
        StarshipApiResponse starshipApiResponse = fetchStarship(starshipUrl);
        String crewValue = starshipApiResponse.getCrew();
        if (crewValue != null) {
            return Integer.parseInt(crewValue.replace(",", ""));
        }
        return 0;
    }

    @Override
    public boolean isPeopleLiveOnPlanet(String planetUrl, String peopleUrl) {
        PlanetApiResponse planetApiResponse = fetchPlanet(planetUrl);
        List<String> planetResidents = planetApiResponse.getResidents();

        // Check if Princess Leia Organa is listed as a resident of Alderaan
        boolean isPeopleExistOnResidents = false;
        if (planetResidents != null) {
            isPeopleExistOnResidents = planetResidents.stream()
                    .anyMatch(residentUrl -> residentUrl.equals(peopleUrl));
        }

        // Double-check that Leia's homeworld is listed as Alderaan
        boolean isHomeworldUrlMatched = false;
        if (isPeopleExistOnResidents) {
            PeopleApiResponse peopleApiResponse = fetchPeople(peopleUrl);
            String peopleHomeworldUrl = peopleApiResponse.getHomeworld();
            isHomeworldUrlMatched = planetApiResponse.getUrl().equals(peopleHomeworldUrl);
        }
        return (isPeopleExistOnResidents && isHomeworldUrlMatched);
    }
}
