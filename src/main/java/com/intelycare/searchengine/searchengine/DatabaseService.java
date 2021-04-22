package com.intelycare.searchengine.searchengine;

import com.intelycare.searchengine.commands.AbstractCommand;
import com.intelycare.searchengine.commands.IndexCommand;
import com.intelycare.searchengine.commands.QueryCommand;
import com.intelycare.searchengine.exception.SearchEngineException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatabaseService {

    private static final Map<Integer, List<String>> DATA = new HashMap<>();
    private static final String OPEN_BRACE = "(";
    private static final String CLOSE_BRACE = ")";
    private static final String OR = "|";
    private static final String AND = "&";
    private static final Set<String> ALLOWED_OPERATORS = Set.of(OPEN_BRACE, CLOSE_BRACE, OR, AND);


    static {
        populateDatabase();
    }

    public static String executeCommand(AbstractCommand abstractCommand) {

        if (abstractCommand instanceof QueryCommand) {
            QueryCommand queryCommand = (QueryCommand) abstractCommand;

            var searchTerm = queryCommand.getSearchTerm();
            var indexesToReturn = new HashSet<String>();

            if (!ALLOWED_OPERATORS.stream().anyMatch(searchTerm::contains)) {
                var foundIndexes = DATA.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().contains(searchTerm))
                        .map(Map.Entry::getKey)
                        .map(Object::toString)
                        .collect(Collectors.toList());
                if (!foundIndexes.isEmpty()) {
                    return "Query result " + String.join(",", foundIndexes);
                }
                return "Query error message";
            }

            if (searchTerm.contains("(") && searchTerm.contains(")")) {
                var operator = searchTerm.contains(OR) ? OR : AND;
                var firstElement = searchTerm.substring(searchTerm.indexOf(OPEN_BRACE) + 1, searchTerm.indexOf(operator));
                var secondElement = searchTerm.substring(searchTerm.indexOf(operator) + 1, searchTerm.indexOf(CLOSE_BRACE));
                var foundFirstIndex = DATA.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().contains(firstElement))
                        .map(Map.Entry::getKey).findFirst().get();

                var foundSecondIndex = DATA.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().contains(secondElement))
                        .map(Map.Entry::getKey).findFirst().get();

                if (searchTerm.contains(OR)) {

                    if (foundFirstIndex != null) {

                        indexesToReturn.add(foundFirstIndex.toString());

                    } else if (foundSecondIndex != null) {

                        indexesToReturn.add(foundSecondIndex.toString());
                    }
                } else if (searchTerm.contains(AND)) {
                    indexesToReturn.add(foundFirstIndex.toString());
                    indexesToReturn.add(foundSecondIndex.toString());
                }
                return "Query result " + String.join(",", indexesToReturn);
            }


            throw new SearchEngineException("Searching not implemented yet!");
        }

        if (abstractCommand instanceof IndexCommand) {
            IndexCommand indexCommand = (IndexCommand) abstractCommand;
            DATA.put(indexCommand.getIndex(), indexCommand.getArguments());
            return "Index ok " + indexCommand.getIndex();
        }

        throw new IllegalArgumentException("Unknown command!");
    }

    public static Map<Integer, List<String>> getAllData() {
        return new HashMap<>(DATA);
    }

    public static List<String> getTokensByIndex(Integer index) {
        return DATA.get(index);
    }

    private static void populateDatabase() {
        DATA.put(0, List.of("soup", "tomato", "cream", "salt"));
        DATA.put(1, List.of("cake", "sugar", "eggs", "flour", "sugar", "cocoa", "cream", "butter"));
        DATA.put(2, List.of("soup", "fish", "potato", "salt", "pepper"));
    }

}
