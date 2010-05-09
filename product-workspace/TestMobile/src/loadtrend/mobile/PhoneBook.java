/*
 * Created on 2005-6-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.io.Serializable;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PhoneBook implements Serializable, Cloneable, Comparable
{
    private int index = -1;
    
    private String teleNum = "";
    
    private String name = "";
    
    private String time = "";
    
    /**
     * Implement Cloneable Interface
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( CloneNotSupportedException cnse )
        {
            return null;
        }
    }
    
    /**
     * Implement Comparable Interface
     * For using TreeSet to store this object, the order will be followed by this method ( default order by index ),
     * or you will be failure to store this object if you don't implement comparable interface
     */
	public int compareTo( Object object )
	{
		PhoneBook phoneBook = (PhoneBook) object;
		if ( this.index < phoneBook.getIndex() ) return -1;
		if ( this.index > phoneBook.getIndex() ) return 1;
        return 0;
	}

    /**
     * Rewrite the equals method for using LinkedHashSet.
     */
    public boolean equals( Object obj )
    {
        // Step 1
        if ( this == obj ) return true;
        
        // Step 2
        if ( obj == null ) return false;
        
        // Step 3
        if ( this.getClass() != obj.getClass() ) return false;
        
        // Step 4
        PhoneBook phoneBook = (PhoneBook) obj;
        
        if ( this.teleNum.equals( phoneBook.getTeleNum() ) && 
             this.name.equals( phoneBook.getName() ) &&
             this.time.equals( phoneBook.getTime() ) )
        {
                  return true;
        }
        
        return false;
    }

    /**
     * Rewrite hashCode if rewrite the equals( Object ) method.
     */
    public int hashCode()
    {
        int result = 17;
        
        result = result * 37 + this.teleNum.hashCode();
        result = result * 37 + this.name.hashCode();
        result = result * 37 + this.time.hashCode();
        
        return result;
    }
    
    /**
     * @return Returns the index.
     */
    public int getIndex()
    {
        return index;
    }
    /**
     * @param index The index to set.
     */
    public void setIndex( int index )
    {
        this.index = index;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName( String name )
    {
        this.name = name;
    }
    /**
     * @return Returns the teleNum.
     */
    public String getTeleNum()
    {
        return teleNum;
    }
    /**
     * @param teleNum The teleNum to set.
     */
    public void setTeleNum( String teleNum )
    {
        this.teleNum = teleNum;
    }
    
    /**
     * @return Returns the time.
     */
    public String getTime()
    {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime( String time )
    {
        this.time = time;
    }
}
