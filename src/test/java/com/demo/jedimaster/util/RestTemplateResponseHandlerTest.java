package com.demo.jedimaster.util;

import com.demo.jedimaster.exception.RestTemplateException;
import com.demo.jedimaster.model.PeopleApiResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RestTemplateResponseHandlerTest {

    @InjectMocks
    private RestTemplateResponseHandler handler;

    @Test
    public void handleSuccessfulStringResponse() {
        String responseBody = "Success response body";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        String result = handler.handleResponse(responseEntity);
        assertEquals(responseBody, result);
    }

    @Test
    public void handleSuccessfulPeopleResponse() {
        PeopleApiResponse expectedPeopleApiResponse = new PeopleApiResponse();
        ResponseEntity<PeopleApiResponse> responseEntity = new ResponseEntity<>(expectedPeopleApiResponse, HttpStatus.OK);
        PeopleApiResponse actualPeopleApiResponse = handler.handleResponse(responseEntity);
        assertTrue(actualPeopleApiResponse instanceof PeopleApiResponse);
    }

    @Test(expected = RestTemplateException.class)
    public void handleUnsuccessfulResponse() {
        String responseBody = "The requested resource is not found";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        handler.handleResponse(responseEntity);
    }

}