import java.util.Objects;

/***
 *  1. PrintBook(bookID): Print information about a specific book identified by its unique bookID(e.g., title, author, availability status). Note*: If not found, Print “BookID not found in the Library”
 *  2. PrintBooks(bookID1, bookID2): Print information about all books with bookIDs in the range[bookID1, bookID2].
 *  3. InsertBook(bookID, bookName, authorName, availabilityStatus, borrowedBy, reservationHeap):
 *  Add a new book to the library. BookID should be unique, and availability indicates whether the book is
 * available for borrowing. BorrowBy and reservationHeap is empty intially when a book is inserted and gets updated when the book is borrowed.
 * Note*: There is only one copy of a book i.e. all books are unique.
 */

public class test {
    private static RedBlackTree rbt;

    public test() {
        this.rbt = new RedBlackTree();
    }

    public void insertBook(int bookID, String bookName, String authorName, String availabilityStatus) {
        bookNode newNode=new bookNode(bookID,bookName,authorName,availabilityStatus);
        rbt.insert(newNode);
    }

    private void borrowBook(int patronID, int bookID, int patronPriority){
        bookNode tmp=rbt.search(bookID);
        if(tmp!=null){
            if(tmp.availabilityStatus.equals("Yes")) {
                tmp.availabilityStatus = "No";
                tmp.borrowedBy = patronID;
                System.out.println("Book " + bookID + " Borrowed by Patron " + patronID);
                System.out.println();
            }else{
                if(tmp.minHeap.size<20){
                    long currentTime=System.currentTimeMillis();
                    reservation newReservation=new reservation(patronID,patronPriority,currentTime);
                    tmp.minHeap.insert(newReservation);
                    System.out.println("Book " + bookID + " Reserved by Patron " + patronID);
                    System.out.println();
                }else {
                    System.out.println("Reservation list for Book " + bookID + " is full. Cannot reserve for Patron " + patronID);
                }
            }
        }else {
            System.out.println("Book borrowed not found in the Library");
        }
    }
    private void returnBook(int patronID, int bookID) {
        bookNode tmp = rbt.search(bookID);
        if (tmp != null) {
            if (tmp.availabilityStatus.equals("No") && tmp.borrowedBy == patronID) {
                tmp.availabilityStatus = "Yes";
                tmp.borrowedBy = -1;
                System.out.println("Book " + bookID + " Returned by Patron " + patronID);
                System.out.println();
                if (!tmp.minHeap.isEmpty()) {
                    reservation min = tmp.minHeap.extractMin();
                    tmp.availabilityStatus = "No";
                    tmp.borrowedBy = min.patronID;
                    System.out.println("Book " + bookID + " Allotted by Patron " + min.patronID);
                    System.out.println();
                }
            } else {
                System.out.println("Book returned not found in the Library");
            }
        }
    }
    public void printBook(int bookID){
        bookNode tmp=rbt.search(bookID);
        if(tmp==null){
            System.out.println("BookID not found in the Library");
        }else {
            printDetails(tmp);
        }
    }
    public void printBooks(int bookID1, int bookID2){
        bookNode root=rbt.getRoot();
        printBooksHelper(root,bookID1,bookID2);
    }
    private void printBooksHelper(bookNode node,int bookID1,int bookID2){
        if(node==null){
            return;
        }
        if(node.bookID>bookID1){
            printBooksHelper(node.left,bookID1,bookID2);
        }
        if(node.bookID>=bookID1&&node.bookID<=bookID2){
            printDetails(node);
        }
        if(node.bookID<bookID2){
            printBooksHelper(node.right,bookID1,bookID2);
        }
    }
    private static void printDetails(bookNode node){
        System.out.println("BookID = "+node.bookID);
        System.out.println("Title = "+node.bookName);
        System.out.println("Author = "+node.authorName);
        System.out.println("Availability = " + (Objects.equals(node.availabilityStatus, "Yes") ? "Yes" : "No"));
        if(node.borrowedBy!=-1){
            System.out.println("BorrowedBy = "+node.borrowedBy);
        }else {
            System.out.println("BorrowedBy ="+"");
        }
        System.out.print("ReservationHeap = ");
        if(node.minHeap!=null&&!node.minHeap.isEmpty()){
            System.out.print("[");
            int n=node.minHeap.size;
            for(int i=0;i<n-1;i++){
                System.out.print(node.minHeap.heap[i].patronID+",");
            }
            System.out.print(node.minHeap.heap[n-1].patronID);
            System.out.println("]");
        }else {
            System.out.println("[]");
        }
        System.out.println();
    }
    public void quit() {
        System.out.println("Program Terminated!!");
    }

    public static void main(String[] args) {
        test t=new test();
        t.insertBook(1,"book1","author1","Yes");
        t.printBook(1);
        t.borrowBook(101,1,1);
        t.insertBook(2,"book2","author2","Yes");
        t.borrowBook(102,1,2);
        t.printBooks(1,2);
        t.returnBook(101,1);
        t.quit();
    }
}

