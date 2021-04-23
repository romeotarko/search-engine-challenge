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
    private static String partOne;
    private static String partTwo;


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
                var searchTermInsideBraces = searchTerm.
                        substring(searchTerm.indexOf(OPEN_BRACE), searchTerm.indexOf(CLOSE_BRACE));

                var operator = searchTermInsideBraces.contains(OR) ? OR : AND;

                var firstElement = searchTerm
                        .substring(searchTerm.indexOf(OPEN_BRACE) + 1, searchTerm.indexOf(operator));
                var secondElement = searchTerm
                        .substring(searchTerm.indexOf(operator) + 1, searchTerm.indexOf(CLOSE_BRACE));

                var foundFirstIndex = getElementIndex(firstElement);

                var foundSecondIndex = getElementIndex(secondElement);

                if (foundFirstIndex != null || foundSecondIndex != null) {
                    if (searchTermInsideBraces.contains(OR)) {

                        if (foundFirstIndex != null) {

                            indexesToReturn.add(foundFirstIndex);

                        } else if (foundSecondIndex != null) {

                            indexesToReturn.add(foundSecondIndex);
                        }
                    } else if (searchTermInsideBraces.contains(AND)) {
                        indexesToReturn.add(foundFirstIndex);
                        indexesToReturn.add(foundSecondIndex);
                    }
                }
            }

            if (searchTerm.substring(searchTerm.indexOf(CLOSE_BRACE)).contains(OR) && indexesToReturn.isEmpty()) {

                var foundIndex = getElementIndex(searchTerm.substring(searchTerm.indexOf(OR) + 1));
                if(foundIndex != null){
                    indexesToReturn.add(foundIndex);
                }

            } else if (searchTerm.substring(searchTerm.indexOf(CLOSE_BRACE)).contains(AND)) {

                var foundIndex = getElementIndex(searchTerm.substring(searchTerm.lastIndexOf(AND) + 1));
                if(foundIndex != null){
                    indexesToReturn.add(foundIndex);
                }
            }

            return indexesToReturn.isEmpty() ? "Query error message" : "Query result " + String.join(",", indexesToReturn);


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

    public static String getElementIndex(String element) {
        var foundElement = DATA.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(element))
                .map(Map.Entry::getKey).findFirst();

        return foundElement.isEmpty() ? null : foundElement.get().toString();
    }

    private static void populateDatabase() {
        DATA.put(0, List.of("soup", "tomato", "cream", "salt"));
        DATA.put(1, List.of("cake", "sugar", "eggs", "flour", "sugar", "cocoa", "cream", "butter"));
        DATA.put(2, List.of("soup", "fish", "potato", "salt", "pepper"));
    }

}
