/***
 * ReservationHeap: Implement Binary Min-heap for managing book reservations and waitlists for
 * the book ordered by the patron’s priority which is an integer. (Priority 1 has precedence over
 * Priority 2 and so on). Ties need be broken by considering the timestamp at which the reservation
 * was made (first came and first served basis). Every node of the Min-heap should contain (patronID,
 * priorityNumber, timeOfReservation)
 * Note*:
 * - Assume that each waitlist is limited to 20.
 * - While taking timestamps, ensure the precision is high enough
 */

public class reservationHeap {
    reservation[] heap;
    int size;
    static final int MAX_SIZE = 20;
    public reservationHeap(){
        heap = new reservation[MAX_SIZE];
        size = 0;
    }
    public void insert(reservation r){
        if(size == MAX_SIZE){
            System.out.println("Heap is full");
            return;
        }
        heap[size] = r;
        int i = size;
        while(i>0 && heap[i].priorityNumber < heap[(i-1)/2].priorityNumber){
            swap(i,(i-1)/2);
            i = (i-1)/2;//parent
        }
        size++;
    }
    public reservation extractMin(){
        if(size == 0){
            System.out.println("Heap is empty");
            return null;
        }
        reservation min = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return min;
    }

    private void heapify(int i) {
        int left=2*i+1;
        int right=2*i+2;
        int smallest = i;
        if(left<size && heap[left].priorityNumber < heap[smallest].priorityNumber){
            smallest = left;
        }
        if(right<size && heap[right].priorityNumber < heap[smallest].priorityNumber){
            smallest = right;
        }
        if(smallest != i){
            swap(i,smallest);
            heapify(smallest);
        }
    }
    public void swap(int i,int j){
        reservation temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    public boolean isEmpty(){
        return size == 0;
    }
}

class reservation implements Comparable<reservation>{
    int patronID;
    int priorityNumber;
    long timeOfReservation;
    public reservation(int patronID,int priorityNumber,long timeOfReservation){
        this.patronID = patronID;
        this.priorityNumber = priorityNumber;
        this.timeOfReservation = System.nanoTime();
    }
    @Override
    public int compareTo(reservation o) {
        if(this.priorityNumber!=o.priorityNumber){
            return this.priorityNumber-o.priorityNumber;//smaller number has higher priority
        }else{
            return Long.compare(this.timeOfReservation,o.timeOfReservation);//Ties need be broken by considering the timestamp
        }
    }
}

