/**
 * Created by xinhaodu on 10/23/2023
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static java.lang.System.exit;

public class gatorLibrary {
    RBlackTree rbt;
    private static String fileInName;
    private static String fileOutName;
    private static FileWriter myWriter;

    public gatorLibrary() {
        rbt = new RBlackTree();
    }

    public void insertBook(int bookID, String bookName, String authorName, String availabilityStatus) {
//        bookNode newNode= new bookNode(bookID,bookName,authorName,availabilityStatus);
        rbt.insertNode(bookID,bookName,authorName,availabilityStatus);
    }

    private void borrowBook(int patronID, int bookID, int patronPriority){
        RBlackTree.BookNode tmp=rbt.searchID(bookID);
        if(tmp!=null){
            if(tmp.availabilityStatus.equals("Yes")) {
                tmp.availabilityStatus = "No";
                tmp.borrowedBy = patronID;
//                System.out.println("Book " + bookID + " Borrowed by Patron " + patronID);
                writeInFile("Book " + bookID + " Borrowed by Patron " + patronID);
            }else{
                if(tmp.minHeap.size<20){
                    if(tmp.minHeap.contains(patronID)){
                        writeInFile("Book " + bookID + " Already Reserved by Patron " + patronID);
                    }else {
                        long currentTime = System.currentTimeMillis();
                        reservation newReservation = new reservation(patronID, patronPriority, currentTime);
                        tmp.minHeap.insert(newReservation);
                        writeInFile("Book " + bookID + " Reserved by Patron " + patronID);
                    }
                }else {
//                    System.out.println("Reservation list for Book " + bookID + " is full. Cannot reserve for Patron " + patronID);
                    writeInFile("Reservation list for Book " + bookID + " is full. Cannot reserve for Patron " + patronID);
                }
            }
        }else {
            System.out.println("Book borrowed not found in the Library");
        }
    }
    private void returnBook(int patronID, int bookID) {
        RBlackTree.BookNode tmp = rbt.searchID(bookID);
        if (tmp != null) {
            if (tmp.availabilityStatus.equals("No") && tmp.borrowedBy == patronID) {
                tmp.availabilityStatus = "Yes";
                tmp.borrowedBy = -1;
//                System.out.println("Book " + bookID + " Returned by Patron " + patronID);
                writeInFile("Book " + bookID + " Returned by Patron " + patronID);
                if (!tmp.minHeap.isEmpty()) {
                    reservation min = tmp.minHeap.extractMin();
                    tmp.availabilityStatus = "No";
                    tmp.borrowedBy = min.patronID;
//                    System.out.println("Book " + bookID + " Allotted by Patron " + min.patronID);
                    writeInFile("Book " + bookID + " Allotted by Patron " + min.patronID);
                }
            } else {
                System.out.println("Book returned not found in the Library");
            }
        }
    }
    public void printBook(int bookID){
        RBlackTree.BookNode tmp=rbt.searchID(bookID);
        if(tmp==null){
            System.out.println("BookID not found in the Library");
        }else {
            printDetails(tmp);
        }
    }
    public void printBooks(int bookID1, int bookID2){
        RBlackTree.BookNode root=rbt.getRoot();
        printBooksHelper(root,bookID1,bookID2);
    }
    private void printBooksHelper(RBlackTree.BookNode node,int bookID1,int bookID2){
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
    private static void printDetails(RBlackTree.BookNode node){
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
    public void writeBook(int bookID){
        RBlackTree.BookNode tmp=rbt.searchID(bookID);
        if(tmp==null){
            writeInFile("BookID " +bookID+ " not found in the Library");
        }else {
            writeBookDetails(tmp);
        }
    }
    public void writeBooks(int bookID1, int bookID2){
        RBlackTree.BookNode root=rbt.getRoot();
        writeBooksHelper(root,bookID1,bookID2);
    }
    public void writeBooksHelper(RBlackTree.BookNode node,int bookID1,int bookID2){
        if(node==null){
            return;
        }
        if(node.bookID>bookID1){
            writeBooksHelper(node.left,bookID1,bookID2);
        }
        if(node.bookID>=bookID1&&node.bookID<=bookID2){
            writeBookDetails(node);
        }
        if(node.bookID<bookID2){
            writeBooksHelper(node.right,bookID1,bookID2);
        }
    }
    public void writeBookDetails(RBlackTree.BookNode node){
        StringBuilder sb=new StringBuilder();
        sb.append("BookID = ").append(node.bookID).append("\n");
        sb.append("Title = \"").append(node.bookName).append("\"\n");
        sb.append("Author = \"").append(node.authorName).append("\"\n");
        sb.append("Availability = \"").append((Objects.equals(node.availabilityStatus, "Yes") ? "Yes" : "No")).append("\"\n");
        if(node.borrowedBy!=-1) {
            sb.append("BorrowedBy = ").append(node.borrowedBy).append("\n");
        }else {
            sb.append("BorrowedBy = ").append("None").append("\n");
        }
        sb.append("ReservationHeap = ");
        if(node.minHeap!=null&&!node.minHeap.isEmpty()) {
            sb.append("[");
            int n = node.minHeap.size;
            for (int i = 0; i < n - 1; i++) {
                sb.append(node.minHeap.heap[i].patronID).append(",");
            }
            sb.append(node.minHeap.heap[n - 1].patronID);
            sb.append("]");
        }else {
            sb.append("[]");
        }
        writeInFile(sb.toString());
    }

    public void writeInFile(String content){
        try {
            myWriter = new FileWriter(fileOutName,true);
            myWriter.write(content+"\n");
            myWriter.write("\n");
            myWriter.close();
        }catch (Exception e) {
            System.out.println("output File not found");
            e.printStackTrace();
        }
    }
    public void findClosestBook(int targetID){
        RBlackTree.BookNode cur=rbt.getRoot();
        RBlackTree.BookNode closestLeft=null;
        RBlackTree.BookNode closestRight=null;
        while(cur!=null){
            if(cur.bookID==targetID){
//                printDetails(cur);
                writeBookDetails(cur);
                return;
            }
            if(cur.bookID<targetID){
                if(closestLeft==null||closestLeft.bookID<cur.bookID)
                    closestLeft=cur;
                cur=cur.right;
            }else {
                if (closestRight == null || closestRight.bookID > cur.bookID)
                    closestRight=cur;
                cur=cur.left;
            }
        }
        if(closestLeft==null&&closestRight!=null){
//            printBook(closestRight.bookID);
            writeBook(closestRight.bookID);
        }else if(closestLeft!=null&&closestRight==null) {
//            printBook(closestLeft.bookID);
            writeBook(closestLeft.bookID);
        }else {
//            printBooks(closestLeft.bookID,closestRight.bookID);
            writeBooks(closestLeft.bookID,closestRight.bookID);
        }
    }
    public void deleteBook(int bookID){
        RBlackTree.BookNode tmp=rbt.searchID(bookID);
        if(tmp!=null) {
            if (tmp.availabilityStatus.equals("Yes")) {
                rbt.delete(tmp);
                writeInFile("Book " + bookID + " is no longer available");
            } else {
                rbt.delete(tmp);
                int size=tmp.minHeap.size;
                int []patronIDs=new int[size];
                for(int i=0;i<size;i++){
                    patronIDs[i]=tmp.minHeap.extractMin().patronID;
                }
                StringBuilder sb=new StringBuilder();
                for (int i=0;i<size;i++){
                    if(i!=size-1)
                        sb.append(patronIDs[i]).append(",");
                    else
                        sb.append(patronIDs[i]);
                }
                writeInFile("Book " + bookID + " is no longer available. Reservations made by Patrons "+sb+" have been cancelled!");
            }
        }
    }
    public int colorFilpCount(){
        return rbt.getCountFlipColor();
    }
    public void quit() throws IOException {
        print();
        System.out.println(rbt.getCountFlipColor());
        myWriter = new FileWriter(fileOutName,true);
        myWriter.write("Program Terminated!!");
        myWriter.close();
        exit(0);
    }
    public void testIntance(test temp) {
        temp.insertBook(4,"book4","author1","Yes");
        temp.insertBook(2,"book4","author1","Yes");
        temp.insertBook(5,"book4","author1","Yes");
        temp.insertBook(3,"book4","author1","Yes");
        System.out.println("quit");
//        temp.quit();
    }
    public void chooseFunction(String line) throws IOException {
        if(line.startsWith("InsertBook(")&&line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int bookID = Integer.parseInt(command[0].trim());
            String bookName = command[1].trim().replace("\"", "");//delete "
            String authorName = command[2].trim().replace("\"", "");
            String availabilityStatus = command[3].trim().replace("\"", "");
            insertBook(bookID, bookName, authorName, availabilityStatus);
        }
        if (line.startsWith("BorrowBook(") && line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int patronID = Integer.parseInt(command[0]);
            int bookID = Integer.parseInt(command[1]);
            int patronPriority = Integer.parseInt(command[2]);
            borrowBook(patronID, bookID, patronPriority);
        }
        if (line.startsWith("PrintBook(") && line.endsWith(")")) {
            String command = line.substring(10, line.length() - 1);
            int bookID = Integer.parseInt(command);
            writeBook(bookID);
        }

        if (line.startsWith("PrintBooks(") && line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int bookID1 = Integer.parseInt(command[0]);
            int bookID2 = Integer.parseInt(command[1]);
//            printBooks(bookID1, bookID2);
            writeBooks(bookID1,bookID2);
        }

        if (line.startsWith("ReturnBook(") && line.endsWith(")")) {
            String[] command = line.substring(11, line.length() - 1).split(", ");
            int patronID = Integer.parseInt(command[0]);
            int bookID = Integer.parseInt(command[1]);
            returnBook(patronID, bookID);
        }
        if(line.startsWith("FindClosestBook(")&&line.endsWith(")")){
            String command = line.substring(16, line.length() - 1);
            int bookID = Integer.parseInt(command);
            findClosestBook(bookID);
        }
        if (line.startsWith("DeleteBook(") && line.endsWith(")")) {
            String command = line.substring(11, line.length() - 1);
            int bookID = Integer.parseInt(command);
            deleteBook(bookID);
        }
        if (line.startsWith("ColorFlipCount()")) {
            int count=colorFilpCount();
            StringBuilder sb=new StringBuilder();
            sb.append("Colour Flip Count: ").append(count);
            writeInFile(String.valueOf(sb));
        }
        if (line.startsWith("Quit()")) {
            quit();
        }
    }
    private void print(RBlackTree.BookNode tree, int bookID, int direction) {
        if(tree != null) {
            if(direction==0)    // tree is root
                System.out.printf("%2d(B) is root\n", tree.bookID);
            else                // tree is left or right
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.bookID, tree.color==false?"R":"B", bookID, direction==1?"right" : "left");

            print(tree.left, tree.bookID, -1);
            print(tree.right,tree.bookID,  1);
        }
    }

    public void print() {
        RBlackTree.BookNode mRoot=rbt.getRoot();
        if (mRoot != null)
            print(mRoot, mRoot.bookID, 0);
    }

    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("Please provide input file name!!!");
            return;
        }
        gatorLibrary t=new gatorLibrary();
        fileInName=args[0]+".txt";
        fileOutName=args[0]+"_output_file.txt";
        try {
            File inFile = new File(fileInName);
            Scanner sc = new Scanner(inFile);
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                t.chooseFunction(line);
            }
        }catch (Exception e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
//        test t=new test();
//        t.testIntance(t);
//        t.print();
    }
}



