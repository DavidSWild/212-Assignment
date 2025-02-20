import java.util.List;
import java.util.Arrays;

class UnidirectionalFlooding {
    private static int totalFloodingMessages = 0;  // Counter for flooding messages

    public static void resetMessageCount() {
        totalFloodingMessages = 0;
    }

    public static int getTotalFloodingMessages() {
        return totalFloodingMessages;
    }

    public static int announceLeader(List<Processor> processors, int leaderId) {
        resetMessageCount();  // Reset flooding message counter at the start
        boolean[] received = new boolean[processors.size()];
        int round = 0;  // Count flooding rounds

        // Find the correct index of the leader in the processor list
        int leaderIndex = -1;
        for (int i = 0; i < processors.size(); i++) {
            if (processors.get(i).getId() == leaderId) {
                leaderIndex = i;
                break;
            }
        }

        if (leaderIndex == -1) {
            System.out.println("Error: Leader not found in processor list.");
            return 0;
        }

        System.out.println("\n--- Flooding: Round 0 ---");
        received[leaderIndex] = true;
        System.out.println("Processor " + leaderId + " announces itself as leader.");

        int remainingNodes = processors.size() - 1;
        boolean[] processed = new boolean[processors.size()];

        while (remainingNodes > 0) {
            round++;
            System.out.println("\n--- Flooding: Round " + round + " ---");

            boolean anySent = false;
            boolean[] newReceived = Arrays.copyOf(received, received.length);

            for (int i = 0; i < processors.size(); i++) {
                if (received[i] && !processed[i]) {
                    int nextIndex = (i + 1) % processors.size();

                    if (!received[nextIndex]) {
                        newReceived[nextIndex] = true;
                        totalFloodingMessages++;  // Increment message counter
                        System.out.println("Processor " + processors.get(nextIndex).getId() + 
                            " acknowledges leader " + leaderId + 
                            " (Message #" + totalFloodingMessages + ")");
                        anySent = true;
                        remainingNodes--;
                    }
                    processed[i] = true;
                }
            }

            received = newReceived;

            if (!anySent) break;
        }

        //System.out.println("\nFlooding completed in " + round + " rounds.");
        //System.out.println("Total flooding messages sent: " + totalFloodingMessages);
        // Print summary statistics
        System.out.println("\n=== Flooding Algorithm Summary ===");
        System.out.println("Rounds completed: " + round);
        System.out.println("Messages sent: " + totalFloodingMessages);
        System.out.println("============================");
        return round;  // Return the total flooding rounds
    }
}
