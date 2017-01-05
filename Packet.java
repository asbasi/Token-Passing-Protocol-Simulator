import java.util.*;

public class Packet
{
    private static Random rnd = new Random();  // Random number generator we'll be using.
    
    private static final int MIN_SIZE = 64;    // Minimum size of packet (in bytes).
    private static final int MAX_SIZE = 1518;  // Maximum size of packet (in bytes).
    
    private int destination;    // The destination of the packet.
    private int size;           // Size of the packet.
    private double arrivalTime; // Time that the packet arrives.
    
    public Packet(int host, int N, double arrivalTime)
    {
        this.destination = rnd.nextInt(N);
        while(this.destination == host)
        {
            this.destination = rnd.nextInt(N);
        }
        // Generates size from 64 to 1518 bytes.
        this.size = rnd.nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE;
        
        this.arrivalTime = arrivalTime;
    }
    
    // The getter functions for source, desination, and size.
    public int getDestination()
    {
        return this.destination;
    }
    
    public int getSize()
    {
        return this.size;
    }
    
    public double getArrivalTime()
    {
        return this.arrivalTime;
    }
}