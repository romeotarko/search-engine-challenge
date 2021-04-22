package com.intelycare.searchengine.commands;

import java.util.List;

public class IndexCommand extends AbstractCommand {

    public static final String INDEX = "index";

    private final Integer index;
    private final List<String> arguments;

    public IndexCommand(Integer index, List<String> arguments) {
        this.index = index;
        this.arguments = arguments;
    }

    @Override
    public String getCommandKey() {
        return INDEX;
    }

    public Integer getIndex() {
        return index;
    }

    public List<String> getArguments() {
        return arguments;
    }
}