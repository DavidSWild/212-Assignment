import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

class Processor {
    int id;
    private int maxKnownId;
    Processor next;
    private List<Integer> pendingMessages = new ArrayList<>();
    private boolean isLeader = false;

    public Processor(int id) {
        this.id = id;
        this.maxKnownId = id;
    }

    public void setNext(Processor next) {
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public int getNextId() {
        return next != null ? next.id : -1;
    }

    // New method to check if processor will send a message
    public boolean hasMessageToSend() {
        return true; // In LCR, processors always send in round 1
                    // In subsequent rounds, they forward only if received value > their id
    }

    public void sendMessage() {
        if (next != null) {
            next.pendingMessages.add(maxKnownId);
        }
    }

    public void processReceivedMessages() {
        if (!pendingMessages.isEmpty()) {
            int receivedMax = Collections.max(pendingMessages);
            System.out.println("Processor " + id + " received <M>: " + pendingMessages);
            
            if (receivedMax > maxKnownId) {
                maxKnownId = receivedMax;
            } else if (receivedMax == id) {
                isLeader = true;
            }
        }
        pendingMessages.clear();
    }

    public boolean isLeader() {
        return isLeader;
    }

    public int getLeaderId() {
        return maxKnownId;
    }
}