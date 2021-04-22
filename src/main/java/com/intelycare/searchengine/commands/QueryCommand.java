package com.intelycare.searchengine.commands;

public class QueryCommand extends AbstractCommand {

    public static final String QUERY = "query";

    private final String searchTerm;

    public QueryCommand(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public String getCommandKey() {
        return QUERY;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

}
