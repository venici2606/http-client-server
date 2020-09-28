package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QueryStringTest {
/*
    @Test
    void shouldRetrieveStatusCode() {
        QueryString queryString = new QueryString("status=200"); //klasse som heter queryString med enkel eksempel
        assertEquals("200", queryString.getParameter("status")); // sjekke at jeg får 200 tilbake getParameter status altså verdien av status skal være 200
    }
*/
    @Test
    void shouldRetrieveStatusCode_401() {
        QueryString queryString = new QueryString("status=401");
        assertEquals("401", queryString.getParameter("status"));
    }

    @Test
    void shouldReturnNullForMissingParameters() {
        QueryString queryString = new QueryString("body=Hello");
        assertNull(queryString.getParameter("status")); //intelliJ simplyfy this
    }

    @Test
    void shouldSupportMultipleParameters() {
        QueryString queryString = new QueryString("status=200&body=Hello");
        assertEquals("200", queryString.getParameter("status"));
        assertEquals("Hello", queryString.getParameter("body"));
    }

}

