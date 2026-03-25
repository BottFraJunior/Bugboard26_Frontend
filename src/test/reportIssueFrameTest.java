package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import view.reportIssueFrame;

class reportIssueFrameTest {

	static Stream<Arguments> provideIssueValidationTestCases() {
        String title30Chars = "123456789012345678901234567890"; 
        String title31Chars = "1234567890123456789012345678901"; 

        return Stream.of(
        	//TCE1, DCE1 and MCE1 Valid -> Expects true
            Arguments.of("Bug nel login", "Il pulsante non funziona", 30, true),

            //TCE1 Valid Boundary and TCE4 Invalid Boundary for 'name' -> Expects true and false
            Arguments.of(title30Chars, "Descrizione valida", 30, true),   
            Arguments.of(title31Chars, "Descrizione valida", 30, false),  

            //TCE2 Invalid -> Expects false
            Arguments.of("", "Descrizione valida", 30, false),            
            Arguments.of("   ", "Descrizione valida", 30, false),    
            
            //TCE3 Invalid -> Expects false
            Arguments.of(null, "Descrizione valida", 30, false),          

            
            //DCE2 Invalid -> Expects false
            Arguments.of("Bug", "", 30, false),                       
            Arguments.of("Bug", "     ", 30, false),  
            
            //DCE3 Invalid -> Expects false
            Arguments.of("Bug", null, 30, false),
            
            
            //MCE2 Invalid Boundary for two cases -> Expects false and false
            Arguments.of("Bug", "Il pulsante non funziona", 0, false),
            Arguments.of("Bug", "Il pulsante non funziona", -30, false)
        );
    }
    
	@ParameterizedTest(name = "[{index}] Titolo: ''{0}'', Desc: ''{1}'', MaxLen: {2} => Expected: {3}")
    @MethodSource("provideIssueValidationTestCases")
    void testIsIssueInputValid(String title, String desc, int maxLen, boolean expectedResult) {
        
        boolean actualResult = view.reportIssueFrame.isIssueInputValid(title, desc, maxLen);
        
        assertEquals(expectedResult, actualResult);
    }
}