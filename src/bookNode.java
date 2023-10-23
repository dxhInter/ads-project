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
    public bookNode(int bookID,String bookName,String authorName, String availabilityStatus){
        this.bookID = bookID;
        this.bookName = bookName;
        this.authorName = authorName;
        this.availabilityStatus = availabilityStatus;
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
    private static bookNode nil = new bookNode(-1,"","","");
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
    public void insert(bookNode newNode) {
        if (this.root == null) {
            this.root = newNode;
            this.root.color = Color.BLACK;
            return;
        }
        bookNode y = null;
        bookNode x = this.root;

        while (x != null) {
            y = x;
            if (newNode.bookID < x.bookID) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        newNode.parent = y;
        if (newNode.bookID < y.bookID) {
            y.left = newNode;
        } else {
            y.right = newNode;
        }

        // Fix violations
        fixViolations(newNode);
    }
    private void fixViolations(bookNode z) {
        while (z.parent != null && z.parent.color == Color.RED) {
            if (z.parent == z.parent.parent.left) {
                bookNode y = z.parent.parent.right; // uncle
                if (y != null && y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                bookNode y = z.parent.parent.left; // uncle
                if (y != null && y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }
//    public void insert(bookNode z){
//        if (this.root == null) {
//            this.root = z;
//            this.root.color = Color.BLACK;
//            return;
//        }
//        bookNode y=nil;
//        bookNode x=this.root;
//        while(!checkNIL(x)){
//            y=x;
//            if(z.bookID<x.bookID){
//                x=x.left;
//            }else{
//                x=x.right;
//            }
//        }
//        z.parent=y;
//        if(!checkNIL(y)){
//            if(z.bookID<y.bookID){
//                y.left=z;
//            }else{
//                y.right=z;
//            }
//        }else{
//            root=z;
//        }
//        z.left=nil;
//        z.right=nil;
//        z.color=Color.RED;
//        insertFix(z);
//    }
//    private void insertFix(bookNode k){
//        bookNode parent=null;
//        bookNode gparent=null;
//        while(checkNIL(k.parent)&&k.parent.color==Color.RED&&k.color!=Color.BLACK){
//            System.out.println("flip color!!!");
//            parent=k.parent;
//            gparent=parent.parent;
//            if(parent==gparent.left){
//                bookNode uncle=gparent.right;
//                if(!checkNIL(uncle)&&uncle.color==Color.RED){
//                    //case 1, uncle is red, flip color
//                    uncle.color=Color.BLACK;
//                    countFlipColor++;
//                    parent.color=Color.BLACK;
//                    countFlipColor++;
//                    gparent.color=Color.RED;
//                    k=gparent;//move k to gparent
//                } else {
//                    //case 2, uncle is black and k is right child, left rotate
//                    if(k==parent.right){
//                        leftRotate(k);
//                        k=parent;
//                        parent=k.parent;
//                    }
//                    //case 3, uncle is black and k is left child, right rotate
//                    rightRotate(gparent);
////                    Color tmp=parent.color;
//                    parent.color=Color.BLACK;
//                    countFlipColor++;
//                    gparent.color=Color.RED;
//                    countFlipColor++;
//                    k=parent;
//                }
//            }else{
//                bookNode uncle=gparent.left;
//                if(checkNIL(uncle)&&uncle.color==Color.RED){
//                    uncle.color=Color.BLACK;
//                    countFlipColor++;
//                    parent.color=Color.BLACK;
//                    countFlipColor++;
//                    gparent.color=Color.RED;
//                    k=gparent;
//                }else{
//                    if(k==parent.left){
//                        rightRotate(k);
//                        k=parent;
//                        parent=k.parent;
//                    }
//                    rightRotate(gparent);
////                    Color tmp=parent.color;
//                    parent.color=Color.BLACK;
//                    countFlipColor++;
//                    gparent.color=Color.RED;
//                    countFlipColor++;
//                    k=parent;
//                }
//            }
//        }
//        root.color = Color.BLACK;
//        countFlipColor++;
//        System.out.println("insert"+countFlipColor);
//    }
//    public void delete(bookNode node){
//        if (node == null) {
//            return;
//        }
//        if (root==null){
//            return;
//        }
//        System.out.println("delete set up");
//        bookNode child;
//        bookNode y=node;
//        Color colorOfY=y.color;
//        if(node.left==null){
//            child=node.right;
//            transplant(node,node.right);
//        }else if(node.right==null){
//            child=node.left;
//            transplant(node,node.left);
//        }else{
//            //node deleted has two children
//            y=producer(node.right);//find the smallest node in the right subtree, which means get a producer
//            child=y.right;
//            colorOfY=y.color;
//            if(y.parent==node) {
//                if(child!=null)
//                    child.parent = y;
//            }else{
//                transplant(y,y.right);//replace y with y.right
//                y.right=node.right;//set node deletes right child to y's right child
//                y.right.parent=y;//set node's right child's parent to y
//            }
//            transplant(node,y);//replace node with y
//            y.left=node.left;//set node deletes left child to y's left child
//            y.left.parent=y;//set node's left child's parent to y
//            y.color=node.color;//set node's color to y's color
//        }
//        if(colorOfY==Color.BLACK){
//            //if y is black, then it will break the rule 5, so we need to fix it
//            deleteFix(child);
//        }
//    }
public void delete(bookNode nodeToDelete) {
    if (root == null) return;  // If tree is empty
    if (nodeToDelete == null) return;  // If the bookID doesn't exist in the tree

    bookNode y = nodeToDelete;
    bookNode x;
    Color originalYColor = y.color;
    System.out.println(originalYColor);
    System.out.println(y.right.bookID);
    System.out.println(y.right.color);
    Color thcolor = y.right.color;
    System.out.println(root.bookID);
    System.out.println(root.color);
    if (nodeToDelete.left == null) {
        x = nodeToDelete.right;
        transplant(nodeToDelete, nodeToDelete.right);
    } else if (nodeToDelete.right == null) {
        x = nodeToDelete.left;
        transplant(nodeToDelete, nodeToDelete.left);
    } else {
        y = producer(nodeToDelete.right);
        originalYColor = y.color;
        x = y.right;
        if (y.parent == nodeToDelete) {
            if (x != null) x.parent = y;
        } else {
            transplant(y, y.right);
            y.right = nodeToDelete.right;
            y.right.parent = y;
        }
        transplant(nodeToDelete, y);
        y.left = nodeToDelete.left;
        y.left.parent = y;
        y.color = nodeToDelete.color;
    }
    if (originalYColor == Color.BLACK) {
        System.out.println("thcolor"+thcolor);
        deleteFix(x);
    }
}
    private void deleteFix(bookNode node){
//        System.out.println("node's color"+node.color);
        while(node==null||node!=root&&(node.color==Color.BLACK)) {
            System.out.println("deleteFix");
            if (node == node.parent.left) {
                bookNode brother = node.parent.right;
                if (brother!=null&&brother.color == Color.RED) {
                    //case 1, brother is red, flip color
                    brother.color = Color.BLACK;
                    countFlipColor++;
                    node.parent.color = Color.RED;
                    countFlipColor++;
                    leftRotate(node.parent);
                    brother = node.parent.right;
                }
                if ((brother.left==null||brother.left.color == Color.BLACK) && (brother.right==null||brother.right.color == Color.BLACK)) {
                    //case 2, brother is black and both of brother's children are black
                    brother.color = Color.RED;
                    countFlipColor++;
                    node = node.parent;//move up
                } else {
                    if (brother.right==null||brother.right.color == Color.BLACK) {
                        //case 3, brother is black and brother's left child is red, brother's right child is black
                        brother.left.color = Color.BLACK;
                        countFlipColor++;
                        brother.color = Color.RED;
                        countFlipColor++;
                        rightRotate(brother);
                        brother = node.parent.right;
                    }
                    //case 4, brother is black and brother's right child is red, brother's left child could be any color
                    brother.color = node.parent.color;
                    countFlipColor++;
                    node.parent.color = Color.BLACK;
                    countFlipColor++;
                    brother.right.color = Color.BLACK;
                    countFlipColor++;
                    leftRotate(node.parent);
                    node = root;
                }
            } else {
                bookNode brother = node.parent.left;
                if (brother!=null&&brother.color == Color.RED) {
                    //case 1, brother is red, flip color
                    brother.color = Color.BLACK;
                    countFlipColor++;
                    node.parent.color = Color.RED;
                    countFlipColor++;
                    rightRotate(node.parent);
                    brother = node.parent.left;
                }
                if ((brother.left==null||brother.left.color == Color.BLACK) && (brother.right==null||brother.right.color == Color.BLACK)) {
                    //case 2, brother is black and both of brother's children are black
                    brother.color = Color.RED;
                    countFlipColor++;
                    node = node.parent;//move up
                } else {
                    if (brother.left==null||brother.left.color == Color.BLACK) {
                        //case 3, brother is black and brother's right child is red, brother's left child is black
                        brother.right.color = Color.BLACK;
                        countFlipColor++;
                        brother.color = Color.RED;
                        countFlipColor++;
                        leftRotate(brother);
                        brother = node.parent.left;
                    }
                    //case 4, brother is black and brother's left child is red, brother's right child could be any color
                    brother.color = node.parent.color;
                    countFlipColor++;
                    node.parent.color = Color.BLACK;
                    countFlipColor++;
                    brother.left.color = Color.BLACK;
                    countFlipColor++;
                    rightRotate(node.parent);
                    node = root;
                }
            }
        }
        if (node!=null) {
            node.color = Color.BLACK;
            countFlipColor++;
            System.out.println("delete" + countFlipColor);
        }
    }
    private bookNode producer(bookNode node){
        if(node.left!=null){
            node=node.left;
        }
        return node;
    }
    private void transplant(bookNode u, bookNode v){
        if(u.parent==null){
            root=v;
        }else if(u==u.parent.left){
            u.parent.left=v;
        }else{
            u.parent.right=v;
        }
        if(v!=null) v.parent=u.parent;
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
    public bookNode getRoot(){ return root; }
}

