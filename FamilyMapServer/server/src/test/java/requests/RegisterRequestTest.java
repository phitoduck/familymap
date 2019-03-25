package requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by eric on 2/14/19.
 */
public class RegisterRequestTest {

    private RegisterRequest request;
    private String jsonRequest;
    private String jsonRequestBadGender;

    @Before
    public void setup() {

        // for deserialize test
        jsonRequest = "{" +
                "\"userName\":\"eriddoch\"," +
                "\"password\":\"whyH311oth3re!\"," +
                "\"email\":\"eric.riddoch@gmail.com\"," +
                "\"firstName\":\"Eric\"," +
                "\"lastName\":\"Riddoch\"," +
                "\"gender\":\"m\"}";

        // for deserialize test
        jsonRequestBadGender = "{" +
                "\"userName\":\"eriddoch\"," +
                "\"password\":\"whyH311oth3re!\"," +
                "\"email\":\"eric.riddoch@gmail.com\"," +
                "\"firstName\":\"Eric\"," +
                "\"lastName\":\"Riddoch\"," +
                "\"gender\":\"XOXO\"}";

        request = new RegisterRequest("eriddoch",
                "whyH311oth3re!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m");

    }

    /**
     * test for deserialize accuracy
     */
    @Test
    public void testDeserialize() throws Exception {
        Assert.assertEquals(request, JsonPacket.deserialize(jsonRequest, RegisterRequest.class));
    }

    /**
     * test for ValueException when gender is wrong
     */
//    @Test(expected = Exception.class)
//    public void testDeserializeError() throws Exception {
//        RegisterRequest.deserialize(jsonRequestBadGender);
//    }

    /**
     * test for correct serialization
     */
    @Test
    public void testSerialize() {
        Assert.assertEquals(jsonRequest, request.serialize());
    }

    /**
     * test for not null object
     */
    @Test
    public void testSerializeMatch() {
        Assert.assertNotEquals(null, request.serialize());
    }



}