package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import view.makeAccountFrame; 

class makeAccountFrameTest {

    static Stream<Arguments> provideRegistrationTestCases() {
        return Stream.of(
            //All CE1 Valid -> Expects true
            Arguments.of("Mario", "0001", "mario@mail.it", "password123", "NORMALE", true),

            //CE2 and CE3 Invalid for 'name' -> Expects false
            Arguments.of("", "0001", "mario@mail.it", "password123", "NORMALE", false),
            Arguments.of(null, "0001", "mario@mail.it", "password123", "NORMALE", false),

            //CE2 and CE3 Invalid for 'id' -> Expects false
            Arguments.of("Mario", "", "mario@mail.it", "password123", "NORMALE", false),
            Arguments.of("Mario", null, "mario@mail.it", "password123", "NORMALE", false),

            //CE2 and CE3 Invalid for 'email' -> Expects false
            Arguments.of("Mario", "0001", "", "password123", "NORMALE", false),
            Arguments.of("Mario", "0001", null, "password123", "NORMALE", false),

            //CE2 and CE3 Invalid for 'password' -> Expects false
            Arguments.of("Mario", "0001", "mario@mail.it", "", "NORMALE", false),
            Arguments.of("Mario", "0001", "mario@mail.it", null, "NORMALE", false),

            //CE2 and CE3 Invalid for 'role' -> Expects false
            Arguments.of("Mario", "0001", "mario@mail.it", "password123", "", false),
            Arguments.of("Mario", "0001", "mario@mail.it", "password123", null, false)
        );
    }

    @ParameterizedTest(name = "Test registrazione: nome={0}, id={1}, email={2}, psw={3}, role={4} => expected={5}")
    @MethodSource("provideRegistrationTestCases")
    void testIsRegistrationInputValid(String name, String id, String email, String password, String role, boolean expectedResult) {
        
    	boolean actualResult = makeAccountFrame.isRegistrationInputValid(name, id, email, password, role);
        
        assertEquals(expectedResult, actualResult);
    }
}


