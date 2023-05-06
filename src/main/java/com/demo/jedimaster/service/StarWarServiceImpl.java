package com.demo.jedimaster.service;

import com.demo.jedimaster.util.RestTemplateResponseHandler;
import com.demo.jedimaster.model.PeopleApiResponse;
import com.demo.jedimaster.model.PlanetApiResponse;
import com.demo.jedimaster.model.StarshipApiResponse;
import com.demo.jedimaster.util.JediMasterUtility;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Log4j2
public class StarWarServiceImpl implements StarWarService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RestTemplateResponseHandler restTemplateResponseHandler;
    
    @Value("${swapi.api.people.leia-organa.name}")
    private String leiaName;

    @Override
    public PeopleApiResponse fetchPeople(String peopleUrl) {
        try {
            ResponseEntity<PeopleApiResponse> peopleApiResponseEntity = restTemplate.getForEntity(peopleUrl, PeopleApiResponse.class);
            PeopleApiResponse peopleApiResponse = restTemplateResponseHandler.handleResponse(peopleApiResponseEntity);
            return peopleApiResponse;
        } catch (Exception ex) {
            log.error("peopleUrl : {}, Exception occurred : {}", peopleUrl, ex.getMessage());
            String message = String.format(JediMasterUtility.ERROR_MSG, "on fetching People information", ex.getMessage());
            throw new RuntimeException(message);
        }
    }

    @Override
    public StarshipApiResponse fetchStarship(String starshipUrl) {
        try {
            ResponseEntity<StarshipApiResponse> starshipApiResponseEntity = restTemplate.getForEntity(starshipUrl, StarshipApiResponse.class);
            StarshipApiResponse starshipApiResponse = restTemplateResponseHandler.handleResponse(starshipApiResponseEntity);
            return starshipApiResponse;
        } catch (Exception ex) {
            log.error("starshipUrl : {}, Exception occurred : {}", starshipUrl, ex.getMessage());
            String message = String.format(JediMasterUtility.ERROR_MSG, "on fetching Starship information", ex.getMessage());
            throw new RuntimeException(message);
        }
    }

    @Override
    public PlanetApiResponse fetchPlanet(String planetUrl) {
        try {
            ResponseEntity<PlanetApiResponse> planetApiResponseEntity = restTemplate.getForEntity(planetUrl, PlanetApiResponse.class);
            PlanetApiResponse planetApiResponse = restTemplateResponseHandler.handleResponse(planetApiResponseEntity);
            return planetApiResponse;
        } catch (Exception ex) {
            log.error("planetUrl : {}, Exception occurred : {}", planetUrl, ex.getMessage());
            String message = String.format(JediMasterUtility.ERROR_MSG, "on fetching Planet information", ex.getMessage());
            throw new RuntimeException(message);
        }
    }

    @Override
    public int getCrewCount(String starshipUrl) {
        try {
            StarshipApiResponse starshipApiResponse = fetchStarship(starshipUrl);
            String crewValue = starshipApiResponse.getCrew();
            log.info("Crew value : {}", crewValue);
            if (crewValue != null) {
                return Integer.parseInt(crewValue.replace(",", ""));
            }
            return 0;
        } catch (Exception ex) {
            log.error("starshipUrl : {}, Exception occurred : {}", starshipUrl, ex.getMessage());
            String message = String.format(JediMasterUtility.ERROR_MSG, "on fetching Crew count", ex.getMessage());
            throw new RuntimeException(message);
        }

    }

    @Override
    public boolean isPeopleLiveOnPlanet(String planetUrl, String peopleUrl) {
        try {
            log.info("planetUrl : {} and peopleUrl : {}", planetUrl, peopleUrl);
            PlanetApiResponse planetApiResponse = fetchPlanet(planetUrl);
            List<String> planetResidents = planetApiResponse.getResidents();

            // Check if Princess Leia Organa is listed as a resident of Alderaan
            boolean isPeopleExistOnResidents = false;
            if (planetResidents != null) {
                isPeopleExistOnResidents = planetResidents.stream()
                        .anyMatch(residentUrl -> residentUrl.equals(peopleUrl));
            }
            log.info("isPeopleExistOnResidents : {}", isPeopleExistOnResidents);

            // Double-check that Leia's homeworld is listed as Alderaan and name match.
            boolean isHomeworldUrlAndNameMatched = false;
            if (isPeopleExistOnResidents) {
                PeopleApiResponse peopleApiResponse = fetchPeople(peopleUrl);
                String peopleHomeworldUrl = peopleApiResponse.getHomeworld();
                isHomeworldUrlAndNameMatched = planetApiResponse.getUrl().equals(peopleHomeworldUrl)
                        && leiaName.equals(peopleApiResponse.getName());
            }
            log.info("isHomeworldUrlAndNameMatched : {}", isHomeworldUrlAndNameMatched);
            return (isPeopleExistOnResidents && isHomeworldUrlAndNameMatched);
        } catch (Exception ex) {
            log.error("planetUrl : {} and peopleUrl : {}, Exception occurred : {}", planetUrl, peopleUrl, ex.getMessage());
            String message = String.format(JediMasterUtility.ERROR_MSG, "on checking People live on planet", ex.getMessage());
            throw new RuntimeException(message);
        }
    }

}
