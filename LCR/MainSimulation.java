import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Random;
import java.util.HashSet;

public class MainSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numProcessors;

        // Get user input for number of processors
        while (true) {
            try {
                System.out.print("Enter the number of processors (>= 2): ");
                numProcessors = Integer.parseInt(scanner.nextLine());

                if (numProcessors >= 2) {
                    break; // Valid input, exit loop
                } else {
                    System.out.println("Number of processors must be at least 2. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }

        // Generate unique random IDs
        Random random = new Random();
        HashSet<Integer> uniqueIds = new HashSet<>();
        List<Integer> ids = new ArrayList<>();

        while (uniqueIds.size() < numProcessors) {
            int newId = random.nextInt(1000) + 1; // Generate ID in range [1,1000]
            if (uniqueIds.add(newId)) {  // Ensures uniqueness
                ids.add(newId);
            }
        }

        System.out.println("\nGenerated Random " + numProcessors + " IDs: " + ids);

        boolean shouldShuffle = false;
        while (true) {
            System.out.println("\nSelect ID configuration:");
            System.out.println("1 - Increasing Order");
            System.out.println("2 - Decreasing Order");
            System.out.println("3 - Random Order");
            System.out.print("Enter your choice (1-3): ");

            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                Collections.sort(ids); // Sort IDs in increasing order
                break;
            } else if (choice.equals("2")) {
                ids.sort(Collections.reverseOrder()); // Sort IDs in decreasing order
                break;
            } else if (choice.equals("3")) {
                Collections.shuffle(ids); // Random order
                shouldShuffle = true;
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        // Initialize processors with random unique IDs
        List<Processor> processors = new ArrayList<>();
        for (int id : ids) {
            processors.add(new Processor(id));
        }

        // Only shuffle if the user selected "Random Order"
        if (shouldShuffle) {
            Collections.shuffle(processors);
        }

        // Create a ring topology
        for (int i = 0; i < numProcessors; i++) {
            processors.get(i).setNext(processors.get((i + 1) % numProcessors));
        }

        // Print the ring topology for tracing
        System.out.println("\nRing Topology:");
        for (Processor p : processors) {
            System.out.println("Processor " + p.getId() + " -> Next: Processor " + p.getNextId());
        }

        // Run the LCR leader election algorithm
        // Run the LCR leader election algorithm
        int lcrRounds = DistributedAlgorithm.simulate(processors);
        int leaderId = processors.get(0).getLeaderId();

        // Announce leader using unidirectional flooding
        int floodingRounds = UnidirectionalFlooding.announceLeader(processors, leaderId);

        // Print final statistics
        System.out.println("\n======= Final Statistics =======");
        System.out.println("LCR Algorithm:");
        System.out.println("  - Rounds: " + lcrRounds);
        System.out.println("  - Messages: " + DistributedAlgorithm.getTotalLCRMessages());
        System.out.println("\nFlooding Algorithm:");
        System.out.println("  - Rounds: " + floodingRounds);
        System.out.println("  - Messages: " + UnidirectionalFlooding.getTotalFloodingMessages());
        System.out.println("\nTotal:");
        System.out.println("  - Rounds: " + (lcrRounds + floodingRounds));
        System.out.println("  - Messages: " + 
            (DistributedAlgorithm.getTotalLCRMessages() + UnidirectionalFlooding.getTotalFloodingMessages()));
        System.out.println("=============================");


        scanner.close();
    }
}
