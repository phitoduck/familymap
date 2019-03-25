package models;

import java.util.UUID;

/**
 * Created by eric on 2/13/19.
 *
 * Data object for all types of data that can be stored in the sqlite database or
 * serialized/deserialized.
 *
 * This includes People, Events, AuthTokens, Users, etc.
 *
 */

public class Model {

    private static int ID_LENGTH = 8;

    /**
     * Generates a new universally unique identifier
     * @return
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().substring(0, ID_LENGTH);
    }

}
