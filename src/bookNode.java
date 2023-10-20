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
 * was made (first came and first served basis). Every node of the Min-heap should contain (patronID,
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
    //left ratation
    private void leftRotate(bookNode x){
        bookNode y=x.right;
        x.right=y.left;
        if(y.left!=null){
            y.left.parent=x;
        }
        y.parent=x.parent;
        if(checkNIL(x.parent)){
            root=y;
        }else {
            if(x.parent.left==x)
                x.parent.left=y;
            else
                x.parent.right=y;
        }
        y.left=x;
        x.parent=y;
    }
    private void rightRotate(bookNode y){
        bookNode x=y.left;
        y.left=x.right;
        if(x.right!=null){
            x.right.parent=y;
        }
        x.parent=y.parent;
        if(checkNIL(y.parent)) {
            root = x;
        }else {
            if (y.parent.right == y) {
                y.parent.right = x;
            } else {
                y.parent.left = x;
            }
        }
        x.right=y;
        y.parent=x;
    }
    private void insert(bookNode z){
        bookNode y=nil;
        bookNode x=root;
        while(!checkNIL(root)){
            y=x;
            if(z.bookID<x.bookID){
                x=x.left;
            }else{
                x=x.right;
            }
        }
        z.parent=y;
        if(!checkNIL(y)){
            if(z.bookID<y.bookID){
                y.left=z;
            }else{
                y.right=z;
            }
        }else{
            root=z;
        }
        z.left=nil;
        z.right=nil;
        z.color=Color.RED;
        insertFix(z);
    }
    private void insertFix(bookNode k){
        bookNode gparent;
        while(!checkNIL(k.parent)&&k.parent.color==Color.RED){
            gparent=k.parent.parent;
            if(k.parent==gparent.left){
                bookNode uncle=gparent.right;
                if(!checkNIL(uncle)&&uncle.color==Color.RED){
                    uncle.color=Color.BLACK;
                    k.parent.color=Color.BLACK;
                    gparent.color=Color.RED;
                    k=gparent;
                } else {
                    if(k.parent.right==k){
                        k=k.parent;
                        leftRotate(k);
                    }
                    k.parent.color=Color.BLACK;
                    gparent.color=Color.RED;
                    rightRotate(gparent);
                }
            }else{
                bookNode uncle=gparent.left;
                if(!checkNIL(uncle)&&uncle.color==Color.RED){
                    uncle.color=Color.BLACK;
                    k.parent.color=Color.BLACK;
                    gparent.color=Color.RED;
                    k=gparent;
                }else{
                    if(k.parent.left==k){
                        k=k.parent;
                        rightRotate(k);
                    }
                    k.parent.color=Color.BLACK;
                    gparent.color=Color.RED;
                    leftRotate(gparent);
                }
            }
            root.color=Color.BLACK;
        }
    }
    public boolean checkNIL(bookNode root){
        return root==nil;
    }
}

