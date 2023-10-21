import java.util.*;
import java.io.*;

import static java.lang.System.exit;

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
        System.out.println("Title = \""+node.bookName+"\"");
        System.out.println("Author = \""+node.authorName+"\"");
        System.out.println("Availability = \"" + (Objects.equals(node.availabilityStatus, "Yes") ? "Yes" : "No")+ "\"");
        if(node.borrowedBy!=-1){
            System.out.println("BorrowedBy = "+node.borrowedBy);
        }else {
            System.out.println("BorrowedBy = "+"None");
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
        exit(0);
    }
    public void testIntance(test temp){
        temp.insertBook(1,"book1","author1","Yes");
        temp.printBook(1);
        temp.borrowBook(101,1,1);
        temp.insertBook(2,"book2","author2","Yes");
        temp.borrowBook(102,1,2);
        temp.printBooks(1,2);
        temp.returnBook(101,1);
        temp.quit();
    }
    public void chooseFunction(String line){
        if(line.startsWith("InsertBook(")&&line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int bookID = Integer.parseInt(command[0].trim());
            String bookName = command[1].trim().replace("\"", "");//delete "
            String authorName = command[2].trim().replace("\"", "");
            String availabilityStatus = command[3].trim().replace("\"", "");
            insertBook(bookID, bookName, authorName, availabilityStatus);
//            printBook(bookID);
        }
        if (line.startsWith("BorrowBook(") && line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int patronID = Integer.parseInt(command[0]);
            int bookID = Integer.parseInt(command[1]);
            int patronPriority = Integer.parseInt(command[2]);
            borrowBook(patronID, bookID, patronPriority);
//            printBook(bookID);
        }
        if (line.startsWith("PrintBook(") && line.endsWith(")")) {
            String command = line.substring(10, line.length() - 1);
            int bookID = Integer.parseInt(command);
            printBook(bookID);
        }
        if (line.startsWith("PrintBooks(") && line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int bookID1 = Integer.parseInt(command[0]);
            int bookID2 = Integer.parseInt(command[1]);
            printBooks(bookID1, bookID2);
        }
        if (line.startsWith("ReturnBook(") && line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int patronID = Integer.parseInt(command[0]);
            int bookID = Integer.parseInt(command[1]);
            returnBook(patronID, bookID);
        }
        if (line.startsWith("Quit()")) {
            quit();
        }
    }

    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("Please provide input file name!!!");
            return;
        }
        test t=new test();
        String fileName=args[0]+".txt";
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                t.chooseFunction(line);
            }
        }catch (Exception e) {
            System.out.println("File not found");
            e.printStackTrace();
//        t.testIntance(t);
        }
    }
}

