package com.intelycare.searchengine.searchengine;

import com.intelycare.searchengine.commands.AbstractCommand;
import com.intelycare.searchengine.commands.IndexCommand;
import com.intelycare.searchengine.commands.QueryCommand;
import com.intelycare.searchengine.exception.SearchEngineException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {

    private static final Map<Integer, List<String>> DATA = new HashMap<>();

    static {
        populateDatabase();
    }

    public static String executeCommand(AbstractCommand abstractCommand) {

        if (abstractCommand instanceof QueryCommand) {
            QueryCommand queryCommand = (QueryCommand) abstractCommand;

            var searchTerm = queryCommand.getSearchTerm();

            // TODO implement query
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
