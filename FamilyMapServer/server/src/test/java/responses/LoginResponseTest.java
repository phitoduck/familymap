package responses;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import requests.JsonPacket;

/**
 * Created by eric on 2/14/19.
 */
public class LoginResponseTest {

    private LoginResponse response;
    private String jsonResponse;
    private String jsonResponseBad;

    @Before
    public void setup() {

        // for deserialize test
        jsonResponse = "{\"authToken\":\"12345\"," +
                "\"userName\":\"eriddoch\"," +
                "\"personID\":\"678910\"}";

        // for deserialize test
        jsonResponseBad = "{\"authToken\":\"12345\"," +
                "\"userName\":\"\"," +
                "\"personID\":\"678910\"}";

        response = new LoginResponse("12345",
                "eriddoch",
                "678910");

    }

    /**
     * test for deserialize accuracy
     */
    @Test
    public void testDeserialize() {
        System.out.println(JsonPacket.deserialize(jsonResponse, LoginResponse.class));
        Assert.assertEquals(response, JsonPacket.deserialize(jsonResponse, LoginResponse.class));
    }

    /**
     * test for correct serialization
     */
    @Test
    public void testSerialize() {
        Assert.assertEquals(jsonResponse, response.serialize());
    }

    /**
     * test for not null object
     */
    @Test
    public void testSerializeMatch() {
        Assert.assertNotEquals(null, response.serialize());
    }

}