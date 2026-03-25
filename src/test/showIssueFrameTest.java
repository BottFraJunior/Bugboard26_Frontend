package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import view.showIssueFrame; 
import model.issueModel;
import model.utenteModel;

class showIssueFrameTest {

    private issueModel createMockIssue(String title, String desc, String type, String assigneeEmail) {
        
     
        issueModel issue = new issueModel(title, desc, type, "Media", null, "fakeAuthor@mail.com", "Nessuno");
        
        if (assigneeEmail != null) {
            utenteModel assegnatario = new utenteModel("NomeTest", assigneeEmail, "psw123", "NORMALE", "9999");
            issue.setAssegnatario(assegnatario);
        }
        
        return issue;
    }

    static Stream<Arguments> provideWhiteBoxFilterTests() {
        return Stream.of(

            //Path 1 & 2: Textual match on title or description)
            Arguments.of("login", "Qualsiasi", null, false, "me@mail.com", 
                         "Errore Login", "Pulsante rotto", "Bug", "other@mail.com", true), //Matches title
            Arguments.of("rotto", "Qualsiasi", null, false, "me@mail.com", 
                         "Errore UI", "Pulsante rotto", "Bug", "other@mail.com", true),    //Matches description
            Arguments.of("inesistente", "Qualsiasi", null, false, "me@mail.com", 
                         "Errore UI", "Pulsante rotto", "Bug", "other@mail.com", false),   //Total mismatch 

            //Path 3 & 4: Type filter 
            Arguments.of("", "Tipologia", "Bug", false, "me@mail.com", 
                         "Bug critico", "Non va", "Bug", "other@mail.com", true),          //Matches type
            Arguments.of("", "Tipologia", "Feature", false, "me@mail.com", 
                         "Bug critico", "Non va", "Bug", "other@mail.com", false),         //Mismatches type          

            //Path 5 & 6: Issues assigned to me filter
            Arguments.of("", "Qualsiasi", null, true, "me@mail.com", 
                         "Test", "Test", "Bug", "me@mail.com", true),                      //Matches assignee 
            Arguments.of("", "Qualsiasi", null, true, "me@mail.com", 
                         "Test", "Test", "Bug", "other@mail.com", false),                  //Mismatches assignee
            Arguments.of("", "Qualsiasi", null, true, "me@mail.com", 
                         "Test", "Test", "Bug", null, false),                              //Assignee null

            //Path 7: Null Safety Control
            Arguments.of("filtro", "Tipologia", "Bug", false, "me@mail.com", 
                         null, null, null, "me@mail.com", false)                           
        );
    }

    @ParameterizedTest(name = "[{index}] textFilter: ''{0}'', Mode: ''{1}'', OnlyMine: {3} => Expected: Kept={9}")
    @MethodSource("provideWhiteBoxFilterTests")
    void testFilterIssuesWhiteBox(String textFilter, String mainMode, String selectedType, boolean showOnlyMine, String myEmail, 
                                  String issueTitle, String issueDesc, String issueType, String assigneeEmail, 
                                  boolean expectedKept) {
        
        showIssueFrameTest testInstance = new showIssueFrameTest();
        issueModel mockIssue = testInstance.createMockIssue(issueTitle, issueDesc, issueType, assigneeEmail);
        
        List<issueModel> rawIssues = new ArrayList<>();
        rawIssues.add(mockIssue);
        
        List<issueModel> resultList = showIssueFrame.filterIssues(rawIssues, textFilter, mainMode, selectedType, showOnlyMine, myEmail);
        
        int expectedSize = expectedKept ? 1 : 0;
        assertEquals(expectedSize, resultList.size());
    }
}