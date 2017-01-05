public class Event
{  
    private int type;       // What type of event.
    private int host;       // Which host the event is associated with.
    private double time;    // The time of the event.
    
    public Event(int type, int host, double time)
    {
        this.type = type;
        this.host = host;
        this.time = time;
    }
    
    public int getType() 
    {
        return this.type;
    }
    
    public int getHost()
    {
        return this.host;
    }
    
    public double getTime()
    {
        return this.time;
    }
}