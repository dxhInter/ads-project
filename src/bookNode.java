/***
 * Each node in the Red-Black tree will represent a book and will have the following structure:
 * ● BookId // Integer ID
 * ● BookName //Name of the book
 * ● AuthorName //Name of the Author
 * ● AvailabilityStatus //To indicate whether it is currently borrowed
 * ● BorrowedBy //ID of the Patron who borrowed the book
 * ● ReservationHeap: Implement Binary Min-heap for managing book reservations and waitlists for
 * the book ordered by the patron’s priority which is an integer. (Priority 1 has precedence over
 * Priority 2 and so on). Ties need be broken by considering the timestamp at which the reservation
 * was made (first come first serve basis). Every node of the Min-heap should contain (patronID,
 * priorityNumber, timeOfReservation)
 * Note*:
 * - Assume that each waitlist is limited to 20.
 * - While taking timestamps, ensure the precision is high enough
 */

enum Color{BLACK, RED}

public class bookNode {
    int bookID;
    String bookName;
    String authorName;
    String availabilityStatus;
    int borrowedBy;
    Color color;
    bookNode left, right, parent;
    public bookNode(int bookID,String bookName,String authorName){
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        this.availabilityStatus = "Yes";
        this.borrowedBy = -1;
        this.color = Color.RED;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}
class RedBlackTree{
    private bookNode root;
    private static bookNode nil = new bookNode(-1,"","");
    static {nil.color=Color.BLACK;}
    public RedBlackTree(){
        root = nil;
    }

}

