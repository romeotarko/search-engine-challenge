package com.intelycare.searchengine;

import com.intelycare.searchengine.commands.IndexCommand;
import com.intelycare.searchengine.commands.QueryCommand;
import com.intelycare.searchengine.commands.StringCommandParser;
import com.intelycare.searchengine.exception.SearchEngineException;
import org.junit.Test;
import java.util.List;

public class StringCommandParseTest {

    @Test
    public void should_parse_index_command() {
        String stringCommand = "index 1 firstArg secondArg";

        var parsedCommand = StringCommandParser.toCommand(stringCommand);

        assert parsedCommand != null;
        assert parsedCommand instanceof IndexCommand;
        var indexCommand = (IndexCommand) parsedCommand;

        assert indexCommand.getIndex() == 1;
        assert indexCommand.getArguments().size() == 2;
        assert indexCommand.getArguments().containsAll(List.of("firstArg", "secondArg"));
    }

    @Test
    public void should_parse_query_command() {
        String stringCommand = "query test";

        var parsedCommand = StringCommandParser.toCommand(stringCommand);

        assert parsedCommand != null;
        assert parsedCommand instanceof QueryCommand;
        var indexCommand = (QueryCommand) parsedCommand;

        assert indexCommand.getSearchTerm() != null;
        assert indexCommand.getSearchTerm().equals("test");
    }

    @Test(expected = SearchEngineException.class)
    public void should_fail_if_no_or_invalid_command_is_passed() {

        String command = "invalidCommand 1 test";

        StringCommandParser.toCommand(command);
    }

    @Test(expected = NumberFormatException.class)
    public void should_fail_if_index_is_not_integer() {
        String stringCommand = "index random";

        StringCommandParser.toCommand(stringCommand);
    }

    @Test(expected = SearchEngineException.class)
    public void should_fail_if_index_arguments_contains_special_characters() {
        String stringCommand = "index 1 arg1 arg!";

        StringCommandParser.toCommand(stringCommand);
    }

    @Test(expected = SearchEngineException.class)
    public void should_fail_if_no_arguments_are_passed_to_index_query() {
        String stringCommand = "index 1";

        StringCommandParser.toCommand(stringCommand);
    }

    @Test(expected = SearchEngineException.class)
    public void should_fail_if_no_search_term_is_passed_to_query() {
        String stringCommand = "query";

        StringCommandParser.toCommand(stringCommand);
    }

}
