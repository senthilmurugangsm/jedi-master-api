package com.demo.jedimaster.controller;

import com.demo.jedimaster.dto.StarWarsInfo;
import com.demo.jedimaster.dto.Starship;
import com.demo.jedimaster.model.PeopleApiResponse;
import com.demo.jedimaster.model.StarshipApiResponse;
import com.demo.jedimaster.service.JediMasterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore(value = {"javax.management.*"})
@SpringBootTest
public class JediMasterControllerTest {

    @InjectMocks
    JediMasterController jediMasterController;

    @Mock
    private JediMasterService jediMasterService;

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

    private static final String STARTSHIP_13 = "https://swapi.dev/api/starships/13/";

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(jediMasterController, "swapiApiBaseUrl",
                swapiApiBaseUrl);
        ReflectionTestUtils.setField(jediMasterController, "peopleDarthVader",
                peopleDarthVader);
        ReflectionTestUtils.setField(jediMasterController, "peopleLeiaOrgana",
                peopleLeiaOrgana);
        ReflectionTestUtils.setField(jediMasterController, "planetAlderaan",
                planetAlderaan);
        ReflectionTestUtils.setField(jediMasterController, "starshipDeathStar",
                starshipDeathStar);
    }

    private static PeopleApiResponse getPeopleApiResponse() {
        PeopleApiResponse peopleApiResponse = new PeopleApiResponse();
        peopleApiResponse.setName("Darth Vader");
        peopleApiResponse.setStarships(Arrays.asList(STARTSHIP_13));
        peopleApiResponse.setUrl("https://swapi.dev/api/people/4/");
        peopleApiResponse.setHomeworld("https://swapi.dev/api/planets/1/");
        return peopleApiResponse;
    }

    private static StarshipApiResponse getStarshipApiResponse() {
        StarshipApiResponse starshipApiResponse = new StarshipApiResponse();
        starshipApiResponse.setName("TIE Advanced x1");
        starshipApiResponse.setStarshipClass("Starfighter");
        starshipApiResponse.setModel("Twin Ion Engine Advanced x1");
        starshipApiResponse.setCrew("1");
        starshipApiResponse.setUrl(STARTSHIP_13);
        return starshipApiResponse;
    }

    @Test
    public void fetchStarWarsInformationTest() {
        String peopleDarthVaderUrl = swapiApiBaseUrl + peopleDarthVader;
        when(jediMasterService.fetchPeople(peopleDarthVaderUrl)).thenReturn(getPeopleApiResponse());
        when(jediMasterService.fetchStarship(STARTSHIP_13)).thenReturn(getStarshipApiResponse());

        String starshipDeathStarUrl = swapiApiBaseUrl + starshipDeathStar;
        when(jediMasterService.getCrewCount(starshipDeathStarUrl)).thenReturn(1);

        String planetAlderaanUrl = swapiApiBaseUrl + planetAlderaan;
        String peopleLeiaOrganaUrl = swapiApiBaseUrl + peopleLeiaOrgana;
        when(jediMasterService.isPeopleLiveOnPlanet(planetAlderaanUrl, peopleLeiaOrganaUrl))
                .thenReturn(true);

        ResponseEntity<Object> responseEntity = jediMasterController.fetchStarWarsInformation();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void fetchStarWarsInformationExceptionTest() {
        String peopleDarthVaderUrl = swapiApiBaseUrl + peopleDarthVader;
        when(jediMasterService.fetchPeople(peopleDarthVaderUrl)).thenThrow(RuntimeException.class);
        ResponseEntity<Object> responseEntity = jediMasterController.fetchStarWarsInformation();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void findDarthVaderStarshipTest() throws Exception {
        String peopleDarthVaderUrl = swapiApiBaseUrl + peopleDarthVader;
        when(jediMasterService.fetchPeople(peopleDarthVaderUrl)).thenReturn(getPeopleApiResponse());
        StarshipApiResponse starshipApiResponse = getStarshipApiResponse();
        when(jediMasterService.fetchStarship(STARTSHIP_13)).thenReturn(starshipApiResponse);

        StarWarsInfo starWarsInfo = mock(StarWarsInfo.class);
        Starship starship = new Starship(starshipApiResponse.getName(),
                starshipApiResponse.getStarshipClass(), starshipApiResponse.getModel());
        Whitebox.invokeMethod(jediMasterController, "findDarthVaderStarship", starWarsInfo);
        verify(starWarsInfo).setStarship(starship);
    }

    @Test
    public void findDarthVaderNoStarshipTest() throws Exception {
        String peopleDarthVaderUrl = swapiApiBaseUrl + peopleDarthVader;
        PeopleApiResponse peopleApiResponse = getPeopleApiResponse();
        peopleApiResponse.setStarships(null);
        when(jediMasterService.fetchPeople(peopleDarthVaderUrl)).thenReturn(peopleApiResponse);

        StarWarsInfo starWarsInfo = mock(StarWarsInfo.class);
        Whitebox.invokeMethod(jediMasterController, "findDarthVaderStarship", starWarsInfo);
        verify(starWarsInfo).setStarship(isNotNull());
    }

    @Test
    public void findDeathStarCrewCountTest() throws Exception {
        String starshipDeathStarUrl = swapiApiBaseUrl + starshipDeathStar;
        when(jediMasterService.getCrewCount(starshipDeathStarUrl)).thenReturn(100);

        StarWarsInfo starWarsInfo = mock(StarWarsInfo.class);
        Whitebox.invokeMethod(jediMasterController, "findDeathStarCrewCount", starWarsInfo);
        verify(starWarsInfo).setCrew(100);
    }

    @Test
    public void checkLeiaOnPlanetAlderaanTest() throws Exception {
        String planetAlderaanUrl = swapiApiBaseUrl + planetAlderaan;
        String peopleLeiaOrganaUrl = swapiApiBaseUrl + peopleLeiaOrgana;
        when(jediMasterService.isPeopleLiveOnPlanet(planetAlderaanUrl, peopleLeiaOrganaUrl))
                .thenReturn(true);

        StarWarsInfo starWarsInfo = mock(StarWarsInfo.class);
        Whitebox.invokeMethod(jediMasterController, "checkLeiaOnPlanetAlderaan", starWarsInfo);
        verify(starWarsInfo).setLeiaOnPlanet(true);
    }

}
