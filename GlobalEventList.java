import java.util.*;

public class GlobalEventList
{
    ArrayList<Event> events;
    
    public GlobalEventList()
    {
        this.events = new ArrayList<Event>();
    }
    
    // Gets an event from the GEL.
    public Event getIndex(int index)
    {
        return this.events.get(index);
    }
    
    // Removes an event from the GEL.
    public Event removeIndex(int index)
    {
        return this.events.remove(index); // Also return the element we just removed.
    }
    
    // Inserts an event (based on time) into the GEL.
    public void insert(Event e)
    {
        int i = 0;
        int length = events.size();
        
        while(i < length && e.getTime() >= events.get(i).getTime())
        {
            i++;
        }
        events.add(i, e);
    }
}