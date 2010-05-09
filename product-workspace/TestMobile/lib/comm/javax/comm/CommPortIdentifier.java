package javax.comm;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package javax.comm:
//            CommDriver, CommPort, CommPortEnumerator, CommPortOwnershipListener, 
//            CpoList, CpoListEntry, NoSuchPortException, OwnershipEventThread, 
//            PortInUseException, UnsupportedCommOperationException

public class CommPortIdentifier
{

    String name;
    private int portType;
    public static final int PORT_SERIAL = 1;
    public static final int PORT_PARALLEL = 2;
    private boolean nativeObjectsCreated;
    private int ownedEvent;
    private int unownedEvent;
    private int ownershipRequestedEvent;
    private int shmem;
    private String shname;
    private boolean maskOwnershipEvents;
    OwnershipEventThread oeThread;
    CpoList cpoList;
    CommPortIdentifier next;
    private CommPort port;
    private CommDriver driver;
    static Object lock = new Object();
    static String propfilename;
    static CommPortIdentifier masterIdList;
    boolean owned;
    String owner;

	private static String propertieFileFullName;

    public static Enumeration getPortIdentifiers()
    {
		try
		{
			masterIdList = null;
		    loadDriver( propertieFileFullName );
		}
		catch ( IOException ioe )
		{
			System.err.println( ioe );
		}
        SecurityManager securitymanager = System.getSecurityManager();
        if(securitymanager != null)
        {
            securitymanager.checkDelete(propfilename);
        }
        return new CommPortEnumerator();
    }

    public static CommPortIdentifier getPortIdentifier(String s)
        throws NoSuchPortException
    {
        SecurityManager securitymanager = System.getSecurityManager();
        if(securitymanager != null)
        {
            securitymanager.checkDelete(propfilename);
        }
        CommPortIdentifier commportidentifier = null;
        synchronized(lock)
        {
            for(commportidentifier = masterIdList; commportidentifier != null; commportidentifier = commportidentifier.next)
            {
                if(commportidentifier.name.equals(s))
                {
                    break;
                }
            }

        }
        if(commportidentifier != null)
        {
            return commportidentifier;
        } else
        {
            throw new NoSuchPortException();
        }
    }

    public static CommPortIdentifier getPortIdentifier(CommPort commport)
        throws NoSuchPortException
    {
        SecurityManager securitymanager = System.getSecurityManager();
        if(securitymanager != null)
        {
            securitymanager.checkDelete(propfilename);
        }
        CommPortIdentifier commportidentifier = null;
        synchronized(lock)
        {
            for(commportidentifier = masterIdList; commportidentifier != null; commportidentifier = commportidentifier.next)
            {
                if(commportidentifier.port == commport)
                {
                    break;
                }
            }

        }
        if(commportidentifier != null)
        {
            return commportidentifier;
        } else
        {
            throw new NoSuchPortException();
        }
    }

    private static void addPort(CommPort commport, int i)
    {
        SecurityManager securitymanager = System.getSecurityManager();
        if(securitymanager != null)
        {
            securitymanager.checkDelete(propfilename);
        }
        CommPortIdentifier commportidentifier = new CommPortIdentifier(commport.getName(), commport, i, null);
        CommPortIdentifier commportidentifier1 = masterIdList;
        CommPortIdentifier commportidentifier2 = null;
        synchronized(lock)
        {
            for(; commportidentifier1 != null; commportidentifier1 = commportidentifier1.next)
            {
                commportidentifier2 = commportidentifier1;
            }

            if(commportidentifier2 != null)
            {
                commportidentifier2.next = commportidentifier;
            } else
            {
                masterIdList = commportidentifier;
            }
        }
    }

    public static void addPortName(String s, int i, CommDriver commdriver)
    {
        SecurityManager securitymanager = System.getSecurityManager();
        if(securitymanager != null)
        {
            securitymanager.checkDelete(propfilename);
        }
        CommPortIdentifier commportidentifier = new CommPortIdentifier(s, null, i, commdriver);
        CommPortIdentifier commportidentifier1 = masterIdList;
        CommPortIdentifier commportidentifier2 = null;
        synchronized(lock)
        {
            for(; commportidentifier1 != null; commportidentifier1 = commportidentifier1.next)
            {
                commportidentifier2 = commportidentifier1;
            }

            if(commportidentifier2 != null)
            {
                commportidentifier2.next = commportidentifier;
            } else
            {
                masterIdList = commportidentifier;
            }
        }
    }

    public String getName()
    {
        return name;
    }

    public int getPortType()
    {
        return portType;
    }

    private native int nCreateMutex(String s);

    private native int nCreateEvent(String s);

    private native boolean nClaimMutex(int i, int j);

    private native boolean nReleaseMutex(int i);

    private native boolean nPulseEvent(int i);

    private native boolean nSetEvent(int i);

    private native boolean nCloseHandle(int i);

    private void createNativeObjects()
    {
        if(ownershipRequestedEvent == 0)
        {
            ownershipRequestedEvent = nCreateEvent("javax.comm." + name + "-ownershipRequestedEvent");
        }
        if(ownedEvent == 0)
        {
            ownedEvent = nCreateEvent("javax.comm." + name + "-ownedEvent");
        }
        if(unownedEvent == 0)
        {
            unownedEvent = nCreateEvent("javax.comm." + name + "-unownedEvent");
        }
        nativeObjectsCreated = true;
    }

    public synchronized CommPort open(String s, int i)
        throws PortInUseException
    {
        if(!nativeObjectsCreated)
        {
            createNativeObjects();
        }
        if(owned)
        {
            maskOwnershipEvents = true;
            fireOwnershipEvent(3);
            maskOwnershipEvents = false;
            if(owned)
            {
                throw new PortInUseException(owner);
            }
        }
        port = driver.getCommPort(name, portType);
        if(port == null)
        {
            nSetEvent(ownershipRequestedEvent);
            for(int j = i <= 200 ? 200 : i; j > 0; j -= 200)
            {
                try
                {
                    wait(200L);
                }
                catch(InterruptedException _ex) { }
                port = driver.getCommPort(name, portType);
                if(port != null)
                {
                    break;
                }
            }

            if(port == null)
            {
                String s1 = nGetOwner(shname);
                if(s1 == null || s1.length() == 0)
                {
                    s1 = "Unknown Windows Application";
                }
                throw new PortInUseException(s1);
            }
        }
        owned = true;
        owner = s;
        if(s == null || s.length() == 0)
        {
            shmem = nSetOwner(shname, "Unspecified Java Application", true);
        } else
        {
            shmem = nSetOwner(shname, s, true);
        }
        if(!nSetEvent(ownedEvent))
        {
            System.err.println("Error pulsing ownedEvent");
        }
        return port;
    }

    private native int nSetOwner(String s, String s1, boolean flag);

    private native String nGetOwner(String s);

    private native void nUnsetOwner(String s);

    public String getCurrentOwner()
    {
        if(owned)
        {
            return owner;
        }
        String s = nGetOwner(shname);
        if(s == null || s.length() == 0)
        {
            return "Port currently not owned";
        } else
        {
            return s;
        }
    }

    public boolean isCurrentlyOwned()
    {
        if(owned)
        {
            return true;
        }
        String s = nGetOwner(shname);
        return s != null && s.length() != 0;
    }

    public void addPortOwnershipListener(CommPortOwnershipListener commportownershiplistener)
    {
        cpoList.add(commportownershiplistener);
        if(oeThread == null)
        {
            oeThread = new OwnershipEventThread(this);
            oeThread.start();
        }
    }

    public void removePortOwnershipListener(CommPortOwnershipListener commportownershiplistener)
    {
        cpoList.remove(commportownershiplistener);
    }

    private native int nWaitForEvents(int i, int j, int k);

    void ownershipThreadWaiter()
    {
        int i;
        if((i = nWaitForEvents(ownedEvent, unownedEvent, ownershipRequestedEvent)) >= 0)
        {
            maskOwnershipEvents = true;
            switch(i)
            {
            case 0: // '\0'
                fireOwnershipEvent(1);
                break;

            case 1: // '\001'
                fireOwnershipEvent(2);
                break;

            case 2: // '\002'
                fireOwnershipEvent(3);
                break;
            }
            maskOwnershipEvents = false;
        }
    }

    synchronized void internalClosePort()
    {
        owned = false;
        owner = null;
        port = null;
        nUnsetOwner(shname);
        nSetEvent(unownedEvent);
    }

    CommPortIdentifier(String s, CommPort commport, int i, CommDriver commdriver)
    {
        cpoList = new CpoList();
        name = s;
        port = commport;
        portType = i;
        next = null;
        driver = commdriver;
        shname = "javax.comm-" + s;
        shmem = nSetOwner(shname, "", false);
    }

    void fireOwnershipEvent(int i)
    {
        CpoList cpolist = cpoList.clonelist();
        cpolist.fireOwnershipEvent(i);
    }

    private static String[] parsePropsFile(InputStream inputstream)
    {
        Vector vector = new Vector();
        try
        {
            byte abyte0[] = new byte[4096];
            int i = 0;
            boolean flag = false;
            int k;
            while((k = inputstream.read()) != -1) 
            {
                switch(k)
                {
                case 9: // '\t'
                case 32: // ' '
                    break;

                case 10: // '\n'
                case 13: // '\r'
                    if(i > 0)
                    {
                        String s = new String(abyte0, 0, 0, i);
                        vector.addElement(s);
                    }
                    i = 0;
                    flag = false;
                    break;

                case 35: // '#'
                    flag = true;
                    if(i > 0)
                    {
                        String s1 = new String(abyte0, 0, 0, i);
                        vector.addElement(s1);
                    }
                    i = 0;
                    break;

                default:
                    if(!flag && i < 4096)
                    {
                        abyte0[i++] = (byte)k;
                    }
                    break;
                }
            }
        }
        catch(Throwable throwable)
        {
            System.err.println("Caught " + throwable + " parsing prop file.");
        }
        if(vector.size() > 0)
        {
            String as[] = new String[vector.size()];
            for(int j = 0; j < vector.size(); j++)
            {
                as[j] = (String)vector.elementAt(j);
            }

            return as;
        } else
        {
            return null;
        }
    }

    private static void loadDriver(String s)
        throws IOException
    {
        File file = new File(s);
        BufferedInputStream bufferedinputstream = new BufferedInputStream(new FileInputStream(file));
        String as[] = parsePropsFile(bufferedinputstream);
        if(as != null)
        {
            for(int i = 0; i < as.length; i++)
            {
                if(as[i].regionMatches(true, 0, "driver=", 0, 7))
                {
                    String s1 = as[i].substring(7);
                    s1.trim();
                    try
                    {
                        CommDriver commdriver = (CommDriver)Class.forName(s1).newInstance();
                        commdriver.initialize();
                    }
                    catch(Throwable throwable)
                    {
                        System.err.println("Caught " + throwable + " while loading driver " + s1);
                    }
                }
            }

        }
    }

    private static String findPropFile()
    {
        String s = System.getProperty("java.class.path");
        StreamTokenizer streamtokenizer = new StreamTokenizer(new StringReader(s));
        streamtokenizer.whitespaceChars(File.pathSeparatorChar, File.pathSeparatorChar);
        streamtokenizer.wordChars(File.separatorChar, File.separatorChar);
        streamtokenizer.ordinaryChar(46);
        streamtokenizer.wordChars(46, 46);
        try
        {
            while(streamtokenizer.nextToken() != -1) 
            {
                int i = -1;
                if(streamtokenizer.ttype == -3 && (i = streamtokenizer.sval.indexOf("comm.jar")) != -1)
                {
                    String s1 = new String(streamtokenizer.sval);
                    File file = new File(s1);
                    if(file.exists())
                    {
                        String s2 = s1.substring(0, i);
                        if(s2 != null)
                        {
                            s2 = s2 + "." + File.separator + "javax.comm.properties";
                        } else
                        {
                            s2 = "." + File.separator + "javax.comm.properties";
                        }
                        File file1 = new File(s2);
                        if(file1.exists())
                        {
                            return new String(s2);
                        } else
                        {
                            return null;
                        }
                    }
                }
            }
        }
        catch(IOException _ex) { }
        return null;
    }

    public CommPort open(FileDescriptor filedescriptor)
        throws UnsupportedCommOperationException
    {
        throw new UnsupportedCommOperationException();
    }

    static 
    {
        String s;
        if((s = System.getProperty("javax.comm.properties")) != null)
        {
            System.err.println("Comm Drivers: " + s);
        }
        propertieFileFullName = System.getProperty("java.home") + File.separator + "lib" + File.separator + "javax.comm.properties";
        try
        {
            loadDriver(propertieFileFullName);
            propfilename = new String(propertieFileFullName);
        }
        catch(IOException _ex)
        {
            propfilename = findPropFile();
            try
            {
                if(propfilename != null)
                {
                    loadDriver(propfilename);
                }
            }
            catch(IOException ioexception)
            {
                propfilename = new String(" ");
                System.err.println(ioexception);
            }
        }
    }
}
