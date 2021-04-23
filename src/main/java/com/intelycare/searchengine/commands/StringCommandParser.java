package com.intelycare.searchengine.commands;

import com.intelycare.searchengine.exception.SearchEngineException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.intelycare.searchengine.commands.IndexCommand.INDEX;
import static com.intelycare.searchengine.commands.QueryCommand.QUERY;

public class StringCommandParser {

    public static AbstractCommand toCommand(String line) {
        var arguments = new ArrayList<>(List.of(line.split(" ")));

        if (arguments.size() < 2) {
            throw new SearchEngineException("Line should not be empty");
        }

        var command = arguments.get(0);

        switch (command) {
            case INDEX:
                return parseIndexCommand(arguments);
            case QUERY:
                return parseQueryCommand(arguments);
            default:
                throw new SearchEngineException(command + " is not allowed");
        }
    }

    private static QueryCommand parseQueryCommand(List<String> arguments) {
        var searchTerm = String.join("", arguments.subList(1, arguments.size()));
        return new QueryCommand(searchTerm);
    }

    private static IndexCommand parseIndexCommand(List<String> arguments) {
        var index = Integer.parseInt(arguments.get(1));

        if (arguments.size() < 3) {
            throw new SearchEngineException("Index query should have at least one argument");
        }

        var indexArguments = arguments.subList(2, arguments.size());
        validateForSpecialChars(indexArguments);

        return new IndexCommand(index, indexArguments);
    }

    private static void validateForSpecialChars(List<String> arguments) {
        var argumentsLine = String.join("", arguments);
        if (!(argumentsLine.matches("[a-zA-Z]+"))) {
            throw new SearchEngineException("Index arguments should not have special characters");
        }
    }

}
