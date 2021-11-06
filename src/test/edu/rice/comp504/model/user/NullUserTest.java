package edu.rice.comp504.model.user;

import edu.rice.comp504.model.UserDB;
import org.junit.Test;

public class NullUserTest {

    @Test
    public void testNullUser() {
        User user = new NullUser("null","null","null",22,"null");

    }
}
