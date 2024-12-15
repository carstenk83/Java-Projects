package project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Date;
import java.util.ArrayList;

/**
* This class is responsible for:
* opening and reading the data file 
* obtaining user input
* performing some data validation and handling all errors that may occur 
*
* @author Carsten Kaiser
*/
public class LoginStats{
    
    // to store session records
    private static ArrayList<Session> sessions = new ArrayList<>(); 


    /**
     * The main() method of this program
     *
     * @param args array of Strings provided on the command line when the program is started
     * the first string should be the name of the input file containing the logs
     */
    public static void main(String[] args){
        
        /**
        * Code borrowed from Professor Joanna Klukowska's Project 1 ColorConverter.java
        * https://edstem.org/us/courses/65533/workspaces/pDhkhhnAQVfyAsVRdA7kY7fD6g3NY1Rp
        * Accessed on: [10/05/2024]
        */
        //verifies that the command line argument exists
        if (args.length == 0 ) {
            System.err.println("Usage Error: the program expects file name as an argument.\n");
            System.exit(1);
        }

        //verifies that command line argument contains a name of an existing file
        File userLog = new File(args[0]);
        if (!userLog.exists()) {
            System.err.println("Error: the file "+userLog.getAbsolutePath()+" does not exist.\n");
            System.exit(1);
        }
        if (!userLog.canRead()) {
            System.err.println("Error: the file "+userLog.getAbsolutePath()+
                               " cannot be opened for reading.\n");
            System.exit(1);
        }

        //open the file for reading
        Scanner logFile = null;

        try {
            logFile = new Scanner (userLog);
        } catch (FileNotFoundException e) {
            System.err.println("Error: the file "+userLog.getAbsolutePath()+
                               " cannot be opened for reading.\n");
            System.exit(1);
        }

        //read file and save it in list of records
        RecordList list = new RecordList();
        String line;
        Scanner parseLine;
        int inputTerminal;
        long inputTime;
        String inputUsername;
        Record current;

        while (logFile.hasNextLine()) {
            line = logFile.nextLine();
            parseLine = new Scanner(line);
            parseLine.useDelimiter(" ");

            try {
            inputTerminal = Integer.parseInt(parseLine.next().trim()); // parse to int
            inputTime = Long.parseLong(parseLine.next().trim()); // parse to long
            inputUsername = parseLine.next().trim();
            
            boolean isLoggedIn = inputTerminal > 0;
            Date dateTime = new Date(inputTime);

            current = new Record(inputTerminal, isLoggedIn, inputUsername, dateTime);
            list.add(current);
            }
            catch (NoSuchElementException ex ) {
                //caused by an incomplete or miss-formatted line in the input file
                System.err.println(line);
                continue;
            }
            catch (IllegalArgumentException ex ) {
                //ignore this exception and skip to the next line
            }
        }


        //interactive mode
        Scanner input = new Scanner(System.in);
        String userValue = "";
        
        System.out.println("Welcome to Login Stats!\n");
        System.out.println("Available commands: ");
        System.out.println("  first USERNAME   -   retrieves first login session for the USER");
        System.out.println("  last USERNAME    -   retrieves last login session for the USER");
        System.out.println("  total USERNAME   -   retrieves total amount of time logged in for the USER");
        System.out.println("  all USERNAME     -   retrieves list of all login sessions for the USER");
        System.out.println("  quit             -   terminates this program\n");



        do {
            
            System.out.println();
            //get value from the user
            userValue = input.nextLine().trim();
    
            if (userValue.equalsIgnoreCase("quit")){
                break;
            }
                
            int spaceIndex = userValue.indexOf(' ');

            //check if there's a space separating the command and username
            if (spaceIndex == -1) {
                System.out.println("Error: There must be a username after the 'first' or 'last' command.");
            } else {
                String command = userValue.substring(0, spaceIndex).toLowerCase().trim();
                String username = userValue.substring(spaceIndex + 1).trim();

                //handle the "first" command
                if (command.equals("first")) {                       
                    try {
                        Session firstSession = list.getFirstSession(username);
                       System.out.println(firstSession);
                   } catch (NoSuchElementException e) {
                       System.out.println("No user matching " + username + " found.");
                   }
               } 
               //handle the "last" command
               else if (command.equals("last")) {
                   try {
                       Session lastSession = list.getLastSession(username);
                       System.out.println(lastSession);
                   } catch (NoSuchElementException e){
                       System.out.println("No user matching " + username + " found.");
                   } 
               } 
                //handle the "all" command
               else if (command.equals("all")) {
                   try{
                        SortedLinkedList<Session> allSessions = list.getAllSessions(username);
                        for (Session session : allSessions) {
                            System.out.println(session);
                            System.out.println();
                        }
                   } catch (NoSuchElementException e){
                       System.out.println("No user matching " + username + " found.");
                   } 
               } 
                //handles the "total" command
               else if (command.equals("total")) {
                   try {
                       long sessionTotalTime = list.getTotalTime(username);
                       System.out.println(username + ", total duration " + formatDuration(sessionTotalTime));
                   } catch (NoSuchElementException e){
                       System.out.println("No user matching " + username + " found.");
                   } 
               } 
               //invalid command
               else {
                   System.out.println("Error: This is not a valid query. Try again.");
                }
            }

        } while (true);

    input.close();

    }




    /**
    * formats the given duration in milliseconds into days, hours, minutes, and seconds
    *
    * @param durationMillis the duration in milliseconds
    * @return a formatted string representing the duration
    */
    private static String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000 % 60;
        long minutes = durationMillis / (60 * 1000) % 60;
        long hours = durationMillis / (60 * 60 * 1000) % 24;
        long days = durationMillis / (24 * 60 * 60 * 1000);

        return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
    

    }

}   
