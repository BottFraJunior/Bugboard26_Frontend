package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import view.makeAccountFrame;

class makeAccountFrameTest {

    static Stream<Arguments> provideRegistrationTestCases() {
        
        String str3Chars = "ABC";                               
        String str4Chars = "ABCD";                              
        String str32Chars = "12345678901234567890123456789012"; 
        String str33Chars = "123456789012345678901234567890123";
        
        String validId = "0001";
        String validRole = "NORMALE";

        return Stream.of(
            //MCE1, NCE1 and LCE1 Valid -> Expects true
            Arguments.of("Mario", validId, "mario@mail.it", "password123", validRole, true),
            

            //MCE1 Valid Boundaries and MCE2/MCE3 Invalid Boundaries for 'name' -> Expects false, true, true, false
            Arguments.of(str3Chars, validId, "mario@mail.it", "password123", validRole, false), 
            Arguments.of(str4Chars, validId, "mario@mail.it", "password123", validRole, true),  
            Arguments.of(str32Chars, validId, "mario@mail.it", "password123", validRole, true), 
            Arguments.of(str33Chars, validId, "mario@mail.it", "password123", validRole, false),

            //MCE4 Invalid for 'name' -> Expects false
            Arguments.of(null, validId, "mario@mail.it", "password123", validRole, false), 
            Arguments.of("   ", validId, "mario@mail.it", "password123", validRole, false),
            
            
            //MCE1 Valid Boundaries and MCE2/MCE3 Invalid Boundaries for 'email' -> Expects false, true, true, false
            Arguments.of("Mario", validId, str3Chars, "password123", validRole, false), 
            Arguments.of("Mario", validId, str4Chars, "password123", validRole, true),  
            Arguments.of("Mario", validId, str32Chars, "password123", validRole, true), 
            Arguments.of("Mario", validId, str33Chars, "password123", validRole, false),
            
            //MCE4 Invalid for 'email' -> Expects false
            Arguments.of("Mario", validId, null, "password123", validRole, false), 
            Arguments.of("Mario", validId, "   ", "password123", validRole, false),
            

            //MCE1 Valid Boundaries and MCE2/MCE3 Invalid Boundaries for 'password' -> Expects false, true, true, false
            Arguments.of("Mario", validId, "mario@mail.it", str3Chars, validRole, false), 
            Arguments.of("Mario", validId, "mario@mail.it", str4Chars, validRole, true),  
            Arguments.of("Mario", validId, "mario@mail.it", str32Chars, validRole, true), 
            Arguments.of("Mario", validId, "mario@mail.it", str33Chars, validRole, false),
            
            //MCE4 Invalid for 'password' -> Expects false
            Arguments.of("Mario", validId, "mario@mail.it", null, validRole, false), 
            Arguments.of("Mario", validId, "mario@mail.it", "   ", validRole, false),
            

            //NCE2 Invalid for 'ID' -> Expects false
            Arguments.of("Mario", null, "mario@mail.it", "password123", validRole, false),   
            Arguments.of("Mario", "   ", "mario@mail.it", "password123", validRole, false),
            
            
            //NCE2 Invalid for 'role' -> Expects false
            Arguments.of("Mario", validId, "mario@mail.it", "password123", null, false),   
            Arguments.of("Mario", validId, "mario@mail.it", "password123", "   ", false)
            
        );
    }

    @ParameterizedTest(name = "[{index}] N: ''{0}'', ID: ''{1}'', Mail: ''{2}'', Psw: ''{3}'', R: ''{4}'' => Expected: {5}")
    @MethodSource("provideRegistrationTestCases")
    void testIsRegistrationInputValid(String name, String id, String email, String psw, String role, boolean expectedResult) {
        
        boolean actualResult = makeAccountFrame.isRegistrationInputValid(name, id, email, psw, role, 4, 32);
        
        assertEquals(expectedResult, actualResult);
    }
    

    //Robustness checks for invalid minLength and maxLength scenarios
    static Stream<Arguments> provideLengthBoundaryTestCases() {
        String validId = "0001";
        String validMail = "mario@mail.it";
        String validPsw = "password123";
        String validRole = "NORMALE";
        String validName = "Mario"; 

        return Stream.of(
        	//LCE2 and LCE2 Valid -> Expects true	
            Arguments.of(validName, validId, validMail, validPsw, validRole, 4, 32, true),

            
            //LCE1 Invalid but name.length = 0 or null -> Expects false
            Arguments.of("   ", validId, validMail, validPsw, validRole, 0, 32, false),
            Arguments.of(null, validId, validMail, validPsw, validRole, 0, 32, false),
            Arguments.of("   ", validId, validMail, validPsw, validRole, -5, 32, false),
            Arguments.of(null, validId, validMail, validPsw, validRole, -5, 32, false),

            //LCE1 Invalid but name.length != 0 or null -> Expects true
            Arguments.of(validName, validId, validMail, validPsw, validRole, 0, 32, true),
            Arguments.of(validName, validId, validMail, validPsw, validRole, -5, 32, true),
            
            //LCE3 Invalid but name.length = 0 or null -> Expects false
            Arguments.of("   ", validId, validMail, validPsw, validRole, 32, 0, false),
            Arguments.of(null, validId, validMail, validPsw, validRole, 32, 0, false),
            Arguments.of("   ", validId, validMail, validPsw, validRole, 32, -5, false),
            Arguments.of(null, validId, validMail, validPsw, validRole, 32, -5, false),
            
            //LCE3 Invalid but name.length != 0 or null -> Expects false
            Arguments.of(validName, validId, validMail, validPsw, validRole, 32, 0, false),
            Arguments.of(validName, validId, validMail, validPsw, validRole, 32, -5, false)
            
        
        );
    }

    @ParameterizedTest(name = "[{index}] Nome: ''{0}'', Min: {5}, Max: {6} => Expected: {7}")
    @MethodSource("provideLengthBoundaryTestCases")
    void testRegistrationLengthBoundaries(String name, String id, String email, String psw, String role, int minLength, int maxLength, boolean expectedResult) {
        
        boolean actualResult = view.makeAccountFrame.isRegistrationInputValid(name, id, email, psw, role, minLength, maxLength);
        
        assertEquals(expectedResult, actualResult);
    }
}