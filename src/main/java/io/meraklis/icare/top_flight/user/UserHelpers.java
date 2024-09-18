package io.meraklis.icare.top_flight.user;

import static io.meraklis.icare.top_flight.user.RoleType.INSTRUCTOR;

public class UserHelpers {


    public static boolean isInstructor(String username) {
        TopFlightUser user = findByUsername(username);
        return user.hasRole(INSTRUCTOR);
    }

    private static TopFlightUser findByUsername(String username) {
        return null;
    }
}
