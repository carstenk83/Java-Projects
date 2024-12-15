package project3;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

/**
* This class stores all the Record objects
* Inherits ArrayList<Record>
*
* @author Carsten Kaiser
*/
public class RecordList extends SortedLinkedList<Record>{

    /**
    * default constructor that creates an empty RecordList object
    **/
    public RecordList() {
    }

    /**
    * returns and constructs the first login session for the specified user
    *
    * if there are multiple login session for the 
    * specified user, the first one is the one with the earliest login time
    *
    * @return first login session for user
    * @param user specified user
    *
    * @throws NoSuchElementException if the specified user does not match any of the records in the list
    * @throws IllegalArgumentException if the function is called with an invalid argument
    */
    public Session getFirstSession(String user){
        //check if null or empty string to throw IllegalArgumentException
        if (user == null || user.isEmpty()){
            throw new IllegalArgumentException("Invalid user argument");
        }

        //iterate through array while searching for first session using .before()
        Record loginRecord = null;
        Record logoutRecord = null;

        for(Record recordLog : this){
            if (recordLog.getUsername().equals(user) && recordLog.isLogin()) {
                if (loginRecord == null || recordLog.getTime().before(loginRecord.getTime())) {
                    loginRecord = recordLog;
                }
            }
        }

        //if not found, throw NoSuchElementException
        if(loginRecord == null){
            throw new NoSuchElementException("Error: the specified user does not match any of the records in the list.");
        }

        //find the logout session
        for(Record recordLog : this){
            if(recordLog.getTerminal() == loginRecord.getTerminal()
            && recordLog.getUsername().equals(user)
            && recordLog.isLogout()
            && recordLog.getTime().after(loginRecord.getTime())){
                logoutRecord = recordLog;
                break;
            }

        }

        //constructs and returns first login sesison
        return new Session(loginRecord, logoutRecord);

    }

    /**
    * returns the last login session for the specified user
    *
    * if there are multiple login session for the 
    * specified user, the last one is the one with the latest login time
    * 
    * @return last login session for user
    * @param user specified user
    *
    * @throws NoSuchElementException if the specified user 
    * does not match any of the records in the list
    * @throws IllegalArgumentException if the function is called with an invalid argument
    */
    public Session getLastSession(String user){
        //check if null or empty string to throw IllegalArgumentException
        if (user == null || user.isEmpty()){
            throw new IllegalArgumentException("Invalid user argument");
        }

        //iterate through array while searching for last login using .after() 
        Record loginRecord = null;
        for(Record recordLog : this){
            if (recordLog.getUsername().equals(user) && recordLog.isLogin()) {
                if (loginRecord == null || recordLog.getTime().after(loginRecord.getTime())) {
                    loginRecord = recordLog;
                }
            }
        }

        //if not found, throw NoSuchElementException
        if(loginRecord == null){
            throw new NoSuchElementException("Error: the specified user does not match any of the records in the list.");
        }

        
        //find the logout session
        Record logoutRecord = null;
        for(Record recordLog : this){
            if(recordLog.getTerminal() == loginRecord.getTerminal()
            && recordLog.getUsername().equals(user)
            && recordLog.isLogout()
            && recordLog.getTime().after(loginRecord.getTime())){
                logoutRecord = recordLog;
                break;
            }

        }

        //constructs and returns first login sesison
        return new Session(loginRecord, logoutRecord);
    }


    /**
     * calculates the total amount of time the specified user 
     * has been logged in across all sessions
     *
     * overlapping session times will be counted in full for each session
     *
     * only sessions with a logout time are included in the total time
     *
     * @param user the specified user
     * 
     * @return the total time in milliseconds the user has been logged in
     *
     * @throws NoSuchElementException if the specified user does not match any records in the list
     * @throws IllegalArgumentException if the function is called with an invalid argument
     */
    public long getTotalTime(String user){
        //handle exceptions
        if(user == null || user.isEmpty()){
            throw new IllegalArgumentException("Invalid user argument: Username must be non-null and non-empty.");
        }

        long totalTime = 0;
        boolean userFound = false;
        List<Record> activeLogins = new ArrayList<>();


        for(Record recordLog : this){
            if(recordLog.getUsername().equals(user)){
                userFound = true;

                if(recordLog.isLogin()){
                    activeLogins.add(recordLog);

                } else if(recordLog.isLogout()){
                    for(int i = 0; i < activeLogins.size(); i++){
                        Record loginRecord = activeLogins.get(i);

                        if(loginRecord.getTerminal() == recordLog.getTerminal()
                        && recordLog.getTime().after(loginRecord.getTime())){
                            totalTime += recordLog.getTime().getTime() - loginRecord.getTime().getTime();
                            activeLogins.remove(i);
                            break;
                        }
                    }

                    
                }
            }
        }

        if(userFound == false){
            throw new NoSuchElementException("No records found for the specified user.");
        }

        return totalTime;

    }


    /**
    * returns a list of all login sessions associated with the specified user, 
    * ordered from earliest login time to latest login time
    *
    * sessions without a logout record are also included
    *
    * @param user the specified username to retrieve sessions for
    *
    * @return a sorted list of all login sessions associated with the specified user
    *
    * @throws IllegalArgumentException if the specified user is null or an empty string
    * @throws NoSuchElementException if no records match the specified user
    */
    public SortedLinkedList<Session> getAllSessions(String user){

        //check for null parameter
        if(user == null || user.isEmpty()){
            throw new IllegalArgumentException("Invalid user argument: Username must be non-null and non-empty.");
        }

        
        SortedLinkedList<Session> userSessions = new SortedLinkedList<>(); //initalize SortedLinkedList of all sessions
        boolean userFound = false;  //if user is not found
        List<Record> activeLogins = new ArrayList<>(); //initalize ArrayList for keeping track of all login records

        //iterate through original SortedLinkedList
        for(Record recordLog : this){
            //check to see if username matches, if so userFound = true
            if(recordLog != null && recordLog.getUsername().equals(user)){
                userFound = true;

                //check if login record, if so add to ArrayList
                if(recordLog.isLogin()){
                    activeLogins.add(recordLog);
                } 
                //check if logout record, if so, itterate through ArrayList to match with login record
                else if(recordLog.isLogout()){
                    for(int i = 0; i < activeLogins.size(); i++){
                        Record loginRecord = activeLogins.get(i);

                        //if login record matches with logout record, create new Session and add to Session list
                        if(loginRecord.getTerminal() == recordLog.getTerminal()
                        && recordLog.getTime().after(loginRecord.getTime())){

                            Session session = new Session(loginRecord, recordLog);
                            userSessions.add(session);
                            activeLogins.remove(i); //removes the matching login so there are no repeats
                            break;
                        }
                    }

                }
            }
        }

        //any leftover logins means it is an active session, add new Session to list
        for(Record openLogin : activeLogins){
            userSessions.add(new Session(openLogin, null));

        }

        //if userFound was never made true, there was no record in the list
        if(userFound == false){
            throw new NoSuchElementException("No records found for the specified user.");
        }

        
        return userSessions;

    }


}
