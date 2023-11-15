public class reservationHeap {
    reservation[] heap;
    int size;
    static final int MAX_SIZE = 20;

    public reservationHeap() {
        heap = new reservation[MAX_SIZE];
        size = 0;
    }

    /**
     * Check if the patronID is in the heap
     *
     * @param patronID
     * @return
     */
    public boolean contains(int patronID) {
        for (int i = 0; i < size; i++) {
            if (heap[i].patronID == patronID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Insert a new reservation into the heap
     *
     * @param r
     */
    public void insert(reservation r) {
        if (size == MAX_SIZE) {
            System.out.println("Heap is full");
            return;
        }
        heap[size] = r;
        int i = size;
        while (i > 0 && heap[i].priorityNumber < heap[(i - 1) / 2].priorityNumber) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;//parent
        }
        size++;
    }

    /**
     * Remove the reservation with the highest priority
     *
     * @return
     */
    public reservation extractMin() {
        if (size == 0) {
            System.out.println("Heap is empty");
            return null;
        }
        reservation min = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return min;
    }

    /**
     * Create a min heap
     *
     * @param i
     */

    private void heapify(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;
        if (left < size && heap[left].priorityNumber < heap[smallest].priorityNumber) {
            smallest = left;
        }
        if (right < size && heap[right].priorityNumber < heap[smallest].priorityNumber) {
            smallest = right;
        }
        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    /**
     * Swap two reservations
     *
     * @param i
     * @param j
     */
    public void swap(int i, int j) {
        reservation temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Check if the heap is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }
}

class reservation implements Comparable<reservation> {
    int patronID;
    int priorityNumber;
    long timeOfReservation;

    public reservation(int patronID, int priorityNumber, long timeOfReservation) {
        this.patronID = patronID;
        this.priorityNumber = priorityNumber;
        this.timeOfReservation = System.nanoTime();
    }

    /**
     * Compare two reservations with their priority number and timestamp
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(reservation o) {
        if (this.priorityNumber != o.priorityNumber) {
            return this.priorityNumber - o.priorityNumber;//smaller number has higher priority
        } else {
            return Long.compare(this.timeOfReservation, o.timeOfReservation);//Ties need be broken by considering the timestamp
        }
    }
}

