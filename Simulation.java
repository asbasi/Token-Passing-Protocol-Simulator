import java.lang.Math;
import java.util.*;

public class Simulation
{
    // Declare an object for the random number generator.
    private static Random rnd = new Random();

    private static int NUM_DESIRED = 100000;   // Number of packet deliveries that'll be simulated.  
    
    private static final int PKT_ARRIVAL = 1; // Event type: Packet Arrival
    private static final int TKN_ARRIVAL = 2; // Event type: Token Arrival

    private static final double PROP_DELAY = 0.00001;   // 10 microseconds propogation delay.
    private static final double TRANSMISSION_RATE = 100000000; // 100 Mbps.
    
    public static void main(String [] args)
    {
        // Declare a scanner object to get the user input.
        Scanner input = new Scanner(System.in);
        
        int N = 0;  // Number of hosts.
        
        double currentTime = 0.0;   // The current time.
	    int currentHost = 0;	    // The current host.
	    int currentType = 0;	    // The current event type.

	    double totalTime = 0.0;	// Total time of the simulation 

	    int queueSize = 0;    // Size of a particular host's queue.
        
        double aRate = 0.0;   // Arrival rate lambda (packets/second).
        
        int numPackets = 0;   // Number of packets that were delivered.

	    double transmission_delay = 0.0;    // Transmission delay of frame.

        double averagePacketDelayAcc = 0.0;	// Accumulator variable for the packet delay.        
        long totalBytesTransferred = 0; 	// Accumulator variable for the throughput. 
        
        // Prompt the user for the values of N and aRate.
        System.out.print("Enter the number of hosts: ");
        N = input.nextInt();
        
        System.out.print("Enter the arrival rate (lambda): ");
        aRate = input.nextDouble();
        
        // Initialize the global event list.
        GlobalEventList gel = new GlobalEventList();
        
        // The queue for each host (represented using linked lists of packets).
        // Note that in Java a Linked List can function as a queue (if we add
        // to the end and remove from the front).
        LinkedList<LinkedList<Packet>> queue = new LinkedList<LinkedList<Packet>>();
    
        for(int i = 0; i < N; i++)
        {
            // Create the queue for each host.
            queue.add(new LinkedList<Packet>());
            
            // Create the first arrival event for each host and insert it into the GEL.
            gel.insert(new Event(PKT_ARRIVAL, i, negExpoTime(aRate) + currentTime));
        }
        
        gel.insert(new Event(TKN_ARRIVAL, 0, currentTime));
        
        Event currentEvent;     // Holds the current event being processed.
        Packet currentPacket; 	// Holds the current packet being processed.
        
        // Loop until we simulated the desired number of packets.
	    while(numPackets != NUM_DESIRED)
        {
            currentEvent = gel.removeIndex(0); // Get the next event from the GEL.
            
            currentHost = currentEvent.getHost();
            currentTime = currentEvent.getTime();
	        currentType = currentEvent.getType(); 
            
            // Packet Arrival
            if(currentType == PKT_ARRIVAL)
            {
                // Create the packet and add it to the queue of the specified host.
                (queue.get(currentHost)).add(new Packet(currentHost, N, currentTime));
                
                // Schedule the next packet arrival event.
                gel.insert(new Event(PKT_ARRIVAL, currentHost, negExpoTime(aRate) + currentTime));
            }
            else // Token Arrival
            {
		        queueSize = queue.get(currentHost).size();
		        
		        // Have something to send.
                if(queueSize > 0) 
                {
		            // Calculate the total transmission time of the frame.
		            for(int i = 0; i < queueSize; i++)
		            {
		                transmission_delay += (queue.get(currentHost).get(i).getSize() * 8) / TRANSMISSION_RATE;
		            }
	            
                    // Calculate the throughput/average packet delay for each of the packets being sent. 
		            for(int i = 0; i < queueSize; i++)
		            {
			            numPackets++;
		    	        currentPacket = queue.get(currentHost).remove();
			            totalBytesTransferred += currentPacket.getSize();
			            averagePacketDelayAcc += ((currentPacket.getDestination() + N - currentHost) % N) 
			                                      * (transmission_delay + PROP_DELAY) - 
			                                      currentPacket.getArrivalTime() + currentTime;
			            
			            // Simulated the desired number of packets so we stop.
			            if(numPackets == NUM_DESIRED)
			            {
				            totalTime = ((currentPacket.getDestination() + N - currentHost) % N) 
				                        * (transmission_delay + PROP_DELAY) + currentTime;
				            break;			
			            }
                    }
	
		            // Create next token arrival event.
                    gel.insert(new Event(TKN_ARRIVAL, (currentHost + 1)  % N, currentTime 
                                + (N *(transmission_delay + PROP_DELAY)) + PROP_DELAY));

		            transmission_delay = 0;
                }
                else // Nothing to send so we just pass the token.
                {
                    gel.insert(new Event(TKN_ARRIVAL, (currentHost + 1)  % N, currentTime + PROP_DELAY));
                }
            }
        }
	
        // Print out all the performance measures. 
        System.out.println("\nNumber of Packets Processed: " + numPackets);
	    System.out.println("Throughput: " + (totalBytesTransferred / totalTime));
        System.out.println("Average Packet Delay: " + (averagePacketDelayAcc / numPackets));
    }
    
    // Models the negative exponential distribution.
    static double negExpoTime(double rate)
    {
        double u;
        u = rnd.nextDouble(); // The java equivalent of drand48().
        return ((-1/rate) * Math.log(1-u));
    }
}
