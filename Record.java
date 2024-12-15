package project3;

import java.util.Date;

/**
* This class provides the four-parameter constructor that validates the information and creates the requested Record object
*
* @author Carsten Kaiser
*/
public class Record implements Comparable<Record>{
    
    private int terminal;
    private boolean login;
    private String username;
    private Date time;
    
    /**
    * four-parameter constructor that validates the information and creates the requested Record object
    *
    * @throws IllegalArgumentException if called with invalid value for the terminal number
    *
    * @param terminal positive integer
    * @param login indicates if a given Record object represents a login record
    * @param username name of the user
    * @param time represents the date and time at which the user logged in or logged out
    */
    public Record(int terminal, boolean login, String username, Date time){
        if(terminal == 0){
            throw new IllegalArgumentException("Invalid Terminal Number: Terminal number must be a positive integer.");
        }
        
        this.terminal = Math.abs(terminal);
        this.login = login;
        this.username = username;
        this.time = time;

        
    }

    /**
    * returns the integer terminal
    * @return terminal value
    */
    public int getTerminal(){
        return terminal;
    }

    /**
    * returns whether a given Record object represents a login record
    * @return true if a given Record object represents a login record, or false if not
    */
    public boolean isLogin(){
        return login;
    }

    /**
    * returns whether a given Record object represents a logout record
    * @return true if a given Record object represents a logout record, or false if not
    */
    public boolean isLogout(){
        return !login;
    }

    /**
    * Returns the username
    * @return username
    **/
    public String getUsername(){
        return username;
    }

    /**
    * returns date and time at which the user logged in or logged out
    * @return date and time 
    */
    public Date getTime(){
        return time;
    }

    /**
     * compares this Record with another Record based on time
     *
     * @param other the Record to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this Record
     * is less than, equal to, or greater than the specified Record
     */
    @Override
    public int compareTo(Record other) {
        return this.time.compareTo(other.time);
    }

    /**
     * compares this Record to obj for equality
     *
     * @param obj the object to compare to.
     *
     * @return true if the specified object is equal to this Record false if otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Record)){
            return false;
        }

        Record other = (Record) obj;

        if(terminal == other.terminal && login == other.login && 
        username.equals(other.username) && time.equals(other.time)){
            return true;
        }

        return false;
    }









}
