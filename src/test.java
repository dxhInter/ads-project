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
    private bookNode root;
    public void insertBook(int bookID, String bookName, String authorName){
        bookNode newNode=new bookNode(bookID,bookName,authorName);
        RedBlackTree rbt=new RedBlackTree();
        rbt.insert(newNode);
    }
    private bookNode searchBook(bookNode node, int bookID){
        if(node==null||node.bookID==bookID){
            return node;
        }
        if(bookID<node.bookID){
            return searchBook(node.left,bookID);
        }else {
            return searchBook(node.right,bookID);
        }
    }
    public void printBook(int bookID){
        bookNode tmp=searchBook(root,bookID);
        if(tmp==null){
            System.out.println("BookID not found in the Library");
        }else {
            printDetails(tmp);
        }
    }

    private void printDetails(bookNode node){
        System.out.println("BookID: "+node.bookID);
        System.out.println("BookName: "+node.bookName);
        System.out.println("AuthorName: "+node.authorName);
        System.out.println("Availability: " + (Objects.equals(node.availabilityStatus, "YES") ? "Yes" : "No"));
        if(node.borrowedBy!=-1){
            System.out.println("BorrowedBy: "+node.borrowedBy);
        }else {
            System.out.println("BorrowedBy"+"");
        }
        System.out.println("ReservationHeap: ");
        if(node.minHeap!=null&&!node.minHeap.isEmpty()){
            System.out.println("[");
            int n=node.minHeap.size;
            for(int i=0;i<n;i++){
                System.out.println(node.minHeap.heap[i].patronID+",");
            }
            System.out.println("]");
        }else {
            System.out.println("[]");
        }
    }
    public static void quit() {
        System.out.println("Program Terminated!!");
    }

    public static void main(String[] args) {
        test t=new test();
        t.insertBook(1,"book1","author1");
        t.printBook(1);
        quit();
    }
}

