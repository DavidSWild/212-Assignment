import java.util.List;

class DistributedAlgorithm {
    private static int totalLCRMessages = 0;

    public static void resetMessageCount() {
        totalLCRMessages = 0;
    }

    public static int getTotalLCRMessages() {
        return totalLCRMessages;
    }

    public static int simulate(List<Processor> processors) {
        resetMessageCount();
        int round = 0;
        boolean changed = true;
        int leaderId = -1;

        while (changed) {
            round++;
            changed = false;
            System.out.println("\n--- LCR Election: Round " + round + " ---");

            // Phase 1: Send messages
            for (Processor p : processors) {
                if (p.hasMessageToSend()) {
                    totalLCRMessages++;
                    p.sendMessage();
                    System.out.printf("Message #%d: Processor %d sends <M>: [%d] -> Processor %d%n", 
                        totalLCRMessages, p.getId(), p.getLeaderId(), p.getNextId());
                }
            }

            // Phase 2: Process received messages
            for (Processor p : processors) {
                int oldMax = p.getLeaderId();
                p.processReceivedMessages();
                if (p.getLeaderId() != oldMax) {
                    changed = true;
                }
                if (p.isLeader()) {
                    leaderId = p.getLeaderId();
                }
            }

            if (leaderId != -1) break;
        }

        // Print summary statistics
        System.out.println("\n=== LCR Algorithm Summary ===");
        System.out.println("Rounds completed: " + round);
        System.out.println("Messages sent: " + totalLCRMessages);
        System.out.println("Leader elected: Processor " + leaderId);
        System.out.println("============================");

        return round;
    }
}