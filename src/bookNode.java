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
    reservationHeap minHeap;
    public bookNode(int bookID,String bookName,String authorName){
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        this.availabilityStatus = "Yes";
        this.borrowedBy = -1;
        this.color = Color.RED;
        this.minHeap = new reservationHeap();
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}
class RedBlackTree{
    private bookNode root;
    private int countFlipColor =0;
    private static bookNode nil = new bookNode(-1,"","");
    static {nil.color=Color.BLACK;}
    public RedBlackTree(){
        root = nil;
    }
    //left ratation
    private void leftRotate(bookNode node){
        bookNode temp = node.right;
        node.right = temp.left;
        if (temp.left != null) {
            temp.left.parent = node;
        }
        temp.parent = node.parent;
        if (node.parent == null) {
            root = temp;
        } else if (node == node.parent.left) {
            node.parent.left = temp;
        } else {
            node.parent.right = temp;
        }
        temp.left = node;
        node.parent = temp;
    }
    private void rightRotate(bookNode node){
        bookNode temp = node.left;
        node.left = temp.right;
        if (temp.right != null) {
            temp.right.parent = node;
        }
        temp.parent = node.parent;
        if (node.parent == null) {
            root = temp;
        } else if (node == node.parent.right) {
            node.parent.right = temp;
        } else {
            node.parent.left = temp;
        }
        temp.right = node;
        node.parent = temp;
    }
    public void insert(bookNode z){
        if (this.root == null) {
            this.root = z;
            this.root.color = Color.BLACK;
            return;
        }
        bookNode y=nil;
        bookNode x=this.root;
        while(!checkNIL(x)){
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
    private void fixViolations(bookNode node) {
        bookNode parent = null;
        bookNode grandParent = null;
        while (node != root && node.color != Color.BLACK && node.parent.color == Color.RED) {
            parent = node.parent;
            grandParent = parent.parent;
            if (parent == grandParent.left) {
                bookNode uncle = grandParent.right;
                if (uncle != null && uncle.color == Color.RED) {
                    grandParent.color = Color.RED;
                    parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        leftRotate(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rightRotate(grandParent);
                    Color tempColor = parent.color;
                    parent.color = grandParent.color;
                    grandParent.color = tempColor;
                    node = parent;
                }
            } else {
                bookNode uncle = grandParent.left;
                if (uncle != null && uncle.color == Color.RED) {
                    grandParent.color = Color.RED;
                    parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node = grandParent;
                } else {
                    if (node == parent.left) {
                        rightRotate(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    leftRotate(grandParent);
                    Color tempColor = parent.color;
                    parent.color = grandParent.color;
                    grandParent.color = tempColor;
                    node = parent;
                }
            }
        }
        root.color = Color.BLACK;
    }
    private void insertFix(bookNode k){
        bookNode parent=null;
        bookNode gparent=null;
        while(checkNIL(k.parent)&&k.parent.color==Color.RED&&k.color!=Color.BLACK){
            parent=k.parent;
            gparent=parent.parent;
            if(parent==gparent.left){
                bookNode uncle=gparent.right;
                if(!checkNIL(uncle)&&uncle.color==Color.RED){
                    //case 1, uncle is red, flip color
                    uncle.color=Color.BLACK;
                    countFlipColor++;
                    parent.color=Color.BLACK;
                    countFlipColor++;
                    gparent.color=Color.RED;
                    k=gparent;//move k to gparent
                } else {
                    //case 2, uncle is black and k is right child, left rotate
                    if(k==parent.right){
                        leftRotate(k);
                        k=parent;
                        parent=k.parent;
                    }
                    //case 3, uncle is black and k is left child, right rotate
                    rightRotate(gparent);
//                    Color tmp=parent.color;
                    parent.color=Color.BLACK;
                    countFlipColor++;
                    gparent.color=Color.RED;
                    countFlipColor++;
                    k=parent;
                }
            }else{
                bookNode uncle=gparent.left;
                if(checkNIL(uncle)&&uncle.color==Color.RED){
                    uncle.color=Color.BLACK;
                    countFlipColor++;
                    parent.color=Color.BLACK;
                    countFlipColor++;
                    gparent.color=Color.RED;
                    k=gparent;
                }else{
                    if(k==parent.left){
                        rightRotate(k);
                        k=parent;
                        parent=k.parent;
                    }
                    rightRotate(gparent);
//                    Color tmp=parent.color;
                    parent.color=Color.BLACK;
                    countFlipColor++;
                    gparent.color=Color.RED;
                    countFlipColor++;
                    k=parent;
                }
            }
            root.color = Color.BLACK;
            countFlipColor++;
        }
    }
    public bookNode search(int bookID) {
        return searchTree(this.root, bookID);
    }
    private bookNode searchTree(bookNode node, int bookID){
        if(node==null||node.bookID==bookID){
            return node;
        }
        if(bookID<node.bookID){
            return searchTree(node.left,bookID);
        }else {
            return searchTree(node.right,bookID);
        }
    }
    public int getCountFlipColor(){
        return countFlipColor;
    }
    public boolean checkNIL(bookNode node){
        return node==nil;
    }
}

