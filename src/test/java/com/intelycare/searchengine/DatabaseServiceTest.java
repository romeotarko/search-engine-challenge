package com.intelycare.searchengine;

import com.intelycare.searchengine.commands.IndexCommand;
import com.intelycare.searchengine.commands.QueryCommand;
import com.intelycare.searchengine.searchengine.DatabaseService;
import org.junit.Test;
import java.util.List;

public class DatabaseServiceTest {


    @Test
    public void should_index_data_to_database() {
        var indexCommand = new IndexCommand(1, List.of("firstArg", "secondArg"));

        String returnMessage = DatabaseService.executeCommand(indexCommand);

        assert returnMessage != null;
        assert returnMessage.equals("Index ok 1");
    }

    @Test

    public void should_query_with_one_term() {
        DatabaseService.executeCommand(new IndexCommand(1, List.of("searchTerm", "anotherTerm")));
        DatabaseService.executeCommand(new IndexCommand(2, List.of("searchTerm", "secondTerm")));

        var queryCommand = new QueryCommand("searchTerm");

        var returnMessage = DatabaseService.executeCommand(queryCommand);

        assert returnMessage != null;
        assert returnMessage.equals("Query result 1,2");
    }

    @Test
    public void should_query_with_one_term1() {
        DatabaseService.executeCommand(new IndexCommand(1, List.of("searchTerm", "anotherTerm")));
        DatabaseService.executeCommand(new IndexCommand(2, List.of("searchTerm", "secondTerm")));

        var queryCommand = new QueryCommand("anotherTerm");

        var returnMessage = DatabaseService.executeCommand(queryCommand);

        assert returnMessage != null;
        assert returnMessage.equals("Query result 1");
    }

    @Test
    public void should_return_error_message_when_no_index_were_found(){
        DatabaseService.executeCommand(new IndexCommand(1, List.of("searchTerm", "anotherTerm")));
        DatabaseService.executeCommand(new IndexCommand(2, List.of("searchTerm", "secondTerm")));

        var queryCommand = new QueryCommand("testTerm");

        var returnMessage = DatabaseService.executeCommand(queryCommand);

        assert returnMessage != null;
        assert returnMessage.equals("Query error message");
    }
}
