package com.intelycare.searchengine;

import com.intelycare.searchengine.commands.AbstractCommand;
import com.intelycare.searchengine.commands.StringCommandParser;
import com.intelycare.searchengine.exception.SearchEngineException;
import com.intelycare.searchengine.searchengine.DatabaseService;
import java.util.Scanner;

public class SearchEngineRunner {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        while (true) {
            print("Enter 'index' command for inserting elements to a specific index to a database: (eg. index 2 soup salt ...etc)");
            print("Enter 'query' command for searching by value and returns the database index for that value: (eg. query soup)");
            print("Enter command: ");

            var input = in.nextLine();
            if (input.equals("exit")) {
                break;
            }

            try {
                AbstractCommand command = StringCommandParser.toCommand(input);
                print(DatabaseService.executeCommand(command));
            } catch (SearchEngineException exception) {
                print(exception.getMessage());
            } catch (NumberFormatException exception) {
                print("Index command first argument should be an integer!");
            }

        }
    }

    private static void print(String message){
        System.out.println(message);
    }

}
