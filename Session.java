package project3;

import java.util.Date;

/**
* This class represents a single login session
*
* @author Carsten Kaiser
*/
public class Session implements Comparable<Session>{
    
    private Record login;
    private Record logout;

    /**
    * two-parameter constructor that constructs a Session object 
    * based on a login record and logout record
    *
    * @throws IllegalArgumentException if a valid pair of records does not have 
    * matching usernames and matching terminal numbers
    * @throws IllegalArgumentException if the login record is null
    * @throws IllegalArgumentException if the time associated with the login time 
    * is greater than the time associated with the logout time
    *
    * @param login
    * @param logout
    */
        public Session(Record login, Record logout) {
            if (login == null) {
                throw new IllegalArgumentException("Login record cannot be null.");
            }
            if (logout != null) {
                if (!login.getUsername().equals(logout.getUsername())) {
                    throw new IllegalArgumentException("Login and logout usernames do not match.");
                }
                if (login.getTerminal() != logout.getTerminal()) {
                    throw new IllegalArgumentException("Login and logout terminal numbers do not match.");
                }
                if (login.getTime().after(logout.getTime())) {
                    throw new IllegalArgumentException("Login time cannot be after logout time.");
                }
                if (!login.isLogin() || !logout.isLogout()) {
                    throw new IllegalArgumentException("Login time cannot be after logout time.");
                }
            }
            this.login = login;
            this.logout = logout;

        }


    

    /**
    * Returns a string representation of the logs
    * @return A string representation of the logs
    */
    @Override
    public String toString(){
        String duration;
        String logoutTime;

        if (logout == null) {
            duration = "active session";
            logoutTime = "still logged in";
        } else {
            long durationMillis = getDuration();
            duration = formatDuration(durationMillis);
            logoutTime = logout.getTime().toString();
        }



        return String.format("%s, terminal %d, duration %s\n    logged in: %s\n    logged out: %s",
                getUsername(), getTerminal(), duration, getLoginTime().toString(), logoutTime);
    }

    /**
    * returns the integer terminal
    * @return terminal value
    */
    public int getTerminal(){
        return login.getTerminal();
    }

    /**
    * returns the Login time
    * @return Date loginTime 
    */
    public Date getLoginTime(){
        return login.getTime();
    }

    /**
    * returns the Logout time
    * @return Date logoutTime
    */
    public Date getLogoutTime(){
        try{
            return logout.getTime();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    /**
    * returns the username of the user
    * @return username
    */
    public String getUsername() {
        return login.getUsername();
    }

    /**
    * returns the number of milliseconds ellapsed between 
    * the login time and logout time, or -1 if the session is still active;
    *
    * @return the long milliseconds or the int -1
    */
    public long getDuration(){
        if(logout == null){
            return -1;
        }
        return logout.getTime().getTime() - login.getTime().getTime();
    }


    /**
    * formats the given duration in milliseconds into days, hours, minutes, and seconds
    *
    * @param durationMillis the duration in milliseconds
    *
    * @return a formatted string representing the duration
    */
    private String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000 % 60;
        long minutes = durationMillis / (60 * 1000) % 60;
        long hours = durationMillis / (60 * 60 * 1000) % 24;
        long days = durationMillis / (24 * 60 * 60 * 1000);

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }


     /**
     * compares this Session with another Session based on login time
     *
     * @param other the Session to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this Session
     * is less than, equal to, or greater than the specified Session
     */
    @Override
    public int compareTo(Session other) {
        return this.login.getTime().compareTo(other.login.getTime());
    }

    /**
     * compares this Session to obj for equality
     *
     * @param obj the object to compare to.
     *
     * @return true if the specified object is 
     * equal to this Session false if otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Session)){
            return false;
        }

        Session other = (Session) obj;

        if(!this.login.equals(other.login)){
            return false;
        } 
        if(this.logout == null && other.logout == null){
            return true;
        }
        
            return this.logout.equals(other.logout);
        

    }

}



