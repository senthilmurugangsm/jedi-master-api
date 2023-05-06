package com.demo.jedimaster.service;

import com.demo.jedimaster.util.RestTemplateResponseHandler;
import com.demo.jedimaster.model.PeopleApiResponse;
import com.demo.jedimaster.model.PlanetApiResponse;
import com.demo.jedimaster.model.StarshipApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore(value = {"javax.management.*"})
@PrepareForTest({ RestTemplate.class})
public class StarWarServiceImplTest {

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

    @Value("${swapi.api.people.leia-organa.name}")
    private String leiaName;

    @InjectMocks
    private StarWarServiceImpl starWarService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateResponseHandler restTemplateResponseHandler;

    private static final String SWAPI_ERROR_RESPONSE = "Swapi api is failed";

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(starWarService, "leiaName",
                leiaName);
    }

    @Test
    public void fetchPeopleTest() {
        PeopleApiResponse expectedResponse = getLeiaPeopleApiResponse();
        when(restTemplate.getForEntity(swapiApiBaseUrl + peopleLeiaOrgana, PeopleApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedResponse);

        PeopleApiResponse actualResponse = starWarService.fetchPeople(swapiApiBaseUrl + peopleLeiaOrgana);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getUrl(), actualResponse.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void fetchPeopleExceptionTest() {
        when(restTemplate.getForEntity(swapiApiBaseUrl + peopleLeiaOrgana, PeopleApiResponse.class))
                .thenThrow(new RuntimeException(SWAPI_ERROR_RESPONSE));
        starWarService.fetchPeople(swapiApiBaseUrl + peopleLeiaOrgana);
    }

    @Test
    public void fetchStarshipTest() {
        StarshipApiResponse expectedResponse = getStarshipApiResponse();
        when(restTemplate.getForEntity(swapiApiBaseUrl + starshipDeathStar, StarshipApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedResponse);

        StarshipApiResponse actualResponse = starWarService.fetchStarship(swapiApiBaseUrl + starshipDeathStar);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getUrl(), actualResponse.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void fetchStarshipExceptionTest() {
        when(restTemplate.getForEntity(swapiApiBaseUrl + starshipDeathStar, StarshipApiResponse.class))
                .thenThrow(new RuntimeException(SWAPI_ERROR_RESPONSE));
        starWarService.fetchStarship(swapiApiBaseUrl + starshipDeathStar);
    }

    @Test
    public void fetchPlanetTest() {
        PlanetApiResponse expectedResponse = getPlanetApiResponse();
        when(restTemplate.getForEntity(swapiApiBaseUrl + planetAlderaan, PlanetApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedResponse);

        PlanetApiResponse actualResponse = starWarService.fetchPlanet(swapiApiBaseUrl + planetAlderaan);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getUrl(), actualResponse.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void fetchPlanetExceptionTest() {
        when(restTemplate.getForEntity(swapiApiBaseUrl + planetAlderaan, PlanetApiResponse.class))
                .thenThrow(new RuntimeException(SWAPI_ERROR_RESPONSE));
        starWarService.fetchPlanet(swapiApiBaseUrl + planetAlderaan);
    }

    @Test
    public void getCrewCountTest() {
        StarshipApiResponse expectedResponse = getStarshipApiResponse();
        when(restTemplate.getForEntity(swapiApiBaseUrl + starshipDeathStar, StarshipApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedResponse);
        int actualCrewCount = starWarService.getCrewCount(swapiApiBaseUrl + starshipDeathStar);

        assertEquals(Integer.parseInt(expectedResponse.getCrew().replace(",", "")), actualCrewCount);
    }

    @Test
    public void noCrewTest() {
        StarshipApiResponse expectedResponse = getStarshipApiResponse();
        expectedResponse.setCrew(null);
        when(restTemplate.getForEntity(swapiApiBaseUrl + starshipDeathStar, StarshipApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedResponse);
        int actualCrewCount = starWarService.getCrewCount(swapiApiBaseUrl + starshipDeathStar);

        assertEquals(0, actualCrewCount);
    }

    @Test(expected = RuntimeException.class)
    public void getCrewCountExceptionTest() {
        when(restTemplate.getForEntity(swapiApiBaseUrl + starshipDeathStar, StarshipApiResponse.class))
                .thenThrow(new RuntimeException(SWAPI_ERROR_RESPONSE));
        starWarService.getCrewCount(swapiApiBaseUrl + starshipDeathStar);
    }

    @Test
    public void isPeopleLiveOnPlanetTest() {
        PlanetApiResponse expectedPlanetResponse = getPlanetApiResponse();
        when(restTemplate.getForEntity(swapiApiBaseUrl + planetAlderaan, PlanetApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedPlanetResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedPlanetResponse);

        PeopleApiResponse expectedPeopleResponse = getLeiaPeopleApiResponse();
        when(starWarService.fetchPeople(swapiApiBaseUrl + peopleLeiaOrgana))
                .thenReturn(expectedPeopleResponse);

        assertTrue(starWarService.isPeopleLiveOnPlanet(swapiApiBaseUrl + planetAlderaan, swapiApiBaseUrl + peopleLeiaOrgana));
    }

    @Test
    public void isPeopleNotLiveOnPlanetTest() {
        PlanetApiResponse expectedPlanetResponse = new PlanetApiResponse();
        expectedPlanetResponse.setName("Alderaan");
        expectedPlanetResponse.setResidents(Arrays.asList(
                "https://swapi.dev/api/people/68/", "https://swapi.dev/api/people/81/"));
        expectedPlanetResponse.setUrl("https://swapi.dev/api/planets/2/");

        when(restTemplate.getForEntity(swapiApiBaseUrl + planetAlderaan, PlanetApiResponse.class))
                .thenReturn(new ResponseEntity<>(expectedPlanetResponse, HttpStatus.OK));
        when(restTemplateResponseHandler.handleResponse(any(ResponseEntity.class)))
                .thenReturn(expectedPlanetResponse);

        PeopleApiResponse expectedPeopleResponse = getLeiaPeopleApiResponse();
        when(starWarService.fetchPeople(swapiApiBaseUrl + peopleLeiaOrgana))
                .thenReturn(expectedPeopleResponse);

        assertFalse(starWarService.isPeopleLiveOnPlanet(swapiApiBaseUrl + planetAlderaan, swapiApiBaseUrl + peopleLeiaOrgana));
    }

    @Test(expected = RuntimeException.class)
    public void isPeopleNotLiveOnPlanetExceptionTest() {
        when(restTemplate.getForEntity(swapiApiBaseUrl + peopleLeiaOrgana, PeopleApiResponse.class))
                .thenThrow(new RuntimeException(SWAPI_ERROR_RESPONSE));
        starWarService.isPeopleLiveOnPlanet(swapiApiBaseUrl + planetAlderaan, swapiApiBaseUrl + peopleLeiaOrgana);
    }

    private static PeopleApiResponse getLeiaPeopleApiResponse() {
        PeopleApiResponse peopleApiResponse = new PeopleApiResponse();
        peopleApiResponse.setName("Leia Organa");
        peopleApiResponse.setStarships(new ArrayList<>());
        peopleApiResponse.setUrl("https://swapi.dev/api/people/5/");
        peopleApiResponse.setHomeworld("https://swapi.dev/api/planets/2/");
        return peopleApiResponse;
    }

    private static StarshipApiResponse getStarshipApiResponse() {
        StarshipApiResponse starshipApiResponse = new StarshipApiResponse();
        starshipApiResponse.setName("TIE Advanced x1");
        starshipApiResponse.setStarshipClass("Starfighter");
        starshipApiResponse.setModel("Twin Ion Engine Advanced x1");
        starshipApiResponse.setCrew("32,495");
        starshipApiResponse.setUrl("https://swapi.dev/api/starships/13/");
        return starshipApiResponse;
    }

    private static PlanetApiResponse getPlanetApiResponse() {
        PlanetApiResponse planetApiResponse = new PlanetApiResponse();
        planetApiResponse.setName("Alderaan");
        planetApiResponse.setResidents(Arrays.asList("https://swapi.dev/api/people/5/",
                "https://swapi.dev/api/people/68/", "https://swapi.dev/api/people/81/"));
        planetApiResponse.setUrl("https://swapi.dev/api/planets/2/");
        return planetApiResponse;
    }
}

