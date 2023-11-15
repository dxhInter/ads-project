public class RBlackTree {
    private BookNode root;
    private static final boolean RED   = false;
    private static final boolean BLACK = true;
    private int countFlipColor =0;
    public RBlackTree(){
        root = null;
    }
    public int getCountFlipColor() {
        System.out.println("countFlipColor is"+countFlipColor);
        return countFlipColor;
    } //minus root's color flip
    public static class BookNode {
        int bookID;
        String bookName;
        String authorName;
        String availabilityStatus;
        int borrowedBy;
        boolean color;
        BookNode left, right, parent;
        reservationHeap minHeap;



        public BookNode(int bookID, String bookName, String authorName, String availabilityStatus){
            this.bookID = bookID;
            this.bookName = bookName;
            this.authorName = authorName;
            this.availabilityStatus = availabilityStatus;
            this.borrowedBy = -1;
            this.color = RED;
            this.minHeap = new reservationHeap();
            this.left = null;
            this.right = null;
            this.parent = null;
        }
        public int getBookID() {
            return bookID;
        }
        public String toString() {return ""+bookID+(this.color==RED?"(R)":"B");}
    }

    private BookNode parentOf(BookNode node) {
        return node!=null ? node.parent : null;
    } //get parent of node
    private void setBlack(BookNode node) {
        //if node is null, set node's color to black
        if (node!=null){
            node.color = BLACK;
        }
    }
    private void setRed(BookNode node) {
        //if node is null, set node's color to red
        if (node!=null){
            node.color = RED;
        }
    }
    private boolean getColorOf(BookNode node) {return node!=null ? node.color : BLACK;}
    private void setColor(BookNode node, boolean color) {
        if (node!=null) {
            node.color = color;
        }
    }
    private void setParent(BookNode node, BookNode parent) {
        if (node!=null)
            node.parent = parent;
    }
    private boolean checkRed(BookNode node) {return (node != null) && (node.color == RED);}
    private boolean checkBlack(BookNode node) {return !checkRed(node);}
    public BookNode getRoot(){return root;}
    private void leftRotate(BookNode x) {
        BookNode y = x.right;
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else {
            if (x.parent.left == x)
                x.parent.left = y;
            else
                x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }
    private void rightRotate(BookNode y) {
        BookNode x = y.left;
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) {
            this.root = x;
        } else {
            if (y == y.parent.right)
                y.parent.right = x;
            else
                y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }
    public void insert(BookNode newNode) {
        BookNode y = null;
        BookNode x = this.root;
        int cmp;
        while (x != null) {
            y = x;
            cmp = newNode.bookID- x.bookID;
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }

        newNode.parent = y;
        if (y!=null) {
            cmp = newNode.bookID- y.bookID;
            if (cmp < 0)
                y.left = newNode;
            else
                y.right = newNode;
        } else {
            this.root = newNode;
        }
        newNode.color = RED;
        insertFix(newNode);
    }

    private void insertFix(BookNode k){
        BookNode parent, gparent;
        while((parent = parentOf(k)) != null && checkRed(parent) && k.color != BLACK){
            gparent = parentOf(parent);
            if(parent == gparent.left){
                BookNode uncle = gparent.right;
                if(uncle != null && checkRed(uncle)){
                    // Case 1: Uncle is red, flip color
                    if(parent.color == RED) {
                        countFlipColor++;
                    }
                    if(uncle.color == RED) {
                        countFlipColor++;
                    }
                    if(gparent.color == BLACK&&gparent!=this.root) {
                        countFlipColor++;
                    }
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    k = gparent;
                } else {
                    boolean parentWasRed = (parent.color == RED);
                    boolean gparentWasBlack = (gparent.color == BLACK);

                    if(k == parent.right){
                        leftRotate(parent);
                        BookNode tmp = parent;
                        parent = k;
                        k = tmp;
                    }
                    rightRotate(gparent);


                    if(parentWasRed) {
                        countFlipColor++;
                    }
                    if(gparentWasBlack) {
                        countFlipColor++;
                    }
                    setBlack(parent);
                    setRed(gparent);
                }
            } else {

                BookNode uncle = gparent.left;
                if (uncle != null && checkRed(uncle)) {
                    if(parent.color == RED) {
                        countFlipColor++;
                    }
                    if(uncle.color == RED) {
                        countFlipColor++;
                    }
                    if(gparent.color == BLACK&&gparent!=this.root) {
                        countFlipColor++;
                    }
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    k = gparent;
                } else {
                    boolean parentWasRed = (parent.color == RED);
                    boolean gparentWasBlack = (gparent.color == BLACK);
                    if (k == parent.left) {
                        BookNode tmp;
                        rightRotate(parent);
                        tmp = parent;
                        parent = k;
                        k = tmp;
                    }
                    leftRotate(gparent);
                    if(parentWasRed) {
                        countFlipColor++;
                    }
                    if(gparentWasBlack) {
                        countFlipColor++;
                    }
                    setBlack(parent);
                    setRed(gparent);
                }
            }
        }
        setBlack(this.root);
    }

    public void insertNode(int bookID, String bookName, String authorName, String availabilityStatus) {
        BookNode node=new BookNode(bookID,bookName,authorName,availabilityStatus);
        if (node != null)
            insert(node);
    }
    private BookNode searchID(BookNode x, int bookID) {
        if (x==null)
            return x;
        int cmp = bookID - x.bookID;
        if (cmp < 0)
            return searchID(x.left, bookID);
        else if (cmp > 0)
            return searchID(x.right, bookID);
        else
            return x;
    }
    public BookNode searchID(int bookID) {
        return searchID(root, bookID);
    }
    public void deleteTree(int bookID){
        BookNode d=searchID(bookID);
        delete(d);
    }
    private void removeFixUp(BookNode node, BookNode parent) {
        BookNode brother;
        // node is black and not root
        while ((node==null || checkBlack(node)) && (node != this.root)) {
            // node is left child
            if (parent.left == node) {
                brother = parent.right;
                if (checkRed(brother)) {
                // Case 1:x's brother is red
                    setBlack(brother);
                    setRed(parent);
                    countFlipColor += 2;
                    leftRotate(parent);
                    brother = parent.right;
                }
            if ((brother.left==null || checkBlack(brother.left)) &&
                    (brother.right==null || checkBlack(brother.right))) {
                // Case 2: x's brother is black, and his children are black
                setRed(brother);
                countFlipColor++;
                node = parent;
                parent = parentOf(node);
            } else {
                if (brother.right==null || checkBlack(brother.right)) {
                    // Case 3: x's brother is black, his left child is red, right child is black
                    setBlack(brother.left);
                    setRed(brother);
                    rightRotate(brother);
                    countFlipColor += 2;
                    brother = parent.right;
                }
                // Case 4: x's brother is black, his right child is red, left child could be any color
                boolean colorOfParent = getColorOf(parent);
                setColor(brother, colorOfParent);
                setBlack(parent);
                setBlack(brother.right);
                countFlipColor += colorOfParent == RED ? 2 : 1;
                leftRotate(parent);
                node = this.root;
                break;
            }
        } else {
            brother = parent.left;
            if (checkRed(brother)) {
                // Case 1: x's brother is red
                setBlack(brother);
                setRed(parent);
                countFlipColor += 2;
                rightRotate(parent);
                brother = parent.left;
            }

            if ((brother.left==null || checkBlack(brother.left)) &&
                    (brother.right==null || checkBlack(brother.right))) {
                // Case 2: x's brother is black, and his children are black
                setRed(brother);
                node = parent;
                countFlipColor++;
                parent = parentOf(node);
            } else {
                if (brother.left==null || checkBlack(brother.left)) {
                    // Case 3: x's brother is black, his right child is red, left child is black
                    setBlack(brother.right);
                    setRed(brother);
                    countFlipColor += 2;
                    leftRotate(brother);
                    brother = parent.left;
                }
                // Case 4: x's brother is black, his left child is red, right child could be any color
                boolean colorOfParent = getColorOf(parent);
                setColor(brother, colorOfParent);
                setBlack(parent);
                setBlack(brother.left);
                countFlipColor += colorOfParent == RED ? 2 : 1;
                rightRotate(parent);
                node = this.root;
                break;
            }
        }
    }
    if (node!=null){
        if (node.color == RED) {
            countFlipColor++;
        }
            setBlack(node);
        }
    }

    public void delete(BookNode node) {
        BookNode child, parent;
        boolean color;

        if ((node.left != null) && (node.right != null)) {
            BookNode replace = node.left;
            while (replace.right != null) {
                replace = replace.right;
            }


            if (parentOf(node) != null) {
                if (parentOf(node).left == node) {
                    parentOf(node).left = replace;
                } else {
                    parentOf(node).right = replace;
                }
            } else {
                this.root = replace;
            }

            child = replace.left;
            parent = parentOf(replace);
            color = getColorOf(replace);

            if (parent == node) {
                parent = replace;
            } else {
                if (child != null) {
                    setParent(child, parent);
                }
                parent.right = child;

                replace.left = node.left;
                setParent(node.left, replace);
            }

            replace.parent = node.parent;
            replace.color = node.color;
            replace.right = node.right;
            if (node.right != null) {
                setParent(node.right, replace);
            }

            if (color == BLACK) {
                removeFixUp(child, parent);
            }else {
                if(node.color==BLACK){
                    countFlipColor++;
                }
            }
        } else {

            if (node.left != null) {
                child = node.left;
            } else {
                child = node.right;
            }

            parent = node.parent;
            color = node.color;

            if (child != null) {
                child.parent = parent;
            }

            if (parent != null) {
                if (parent.left == node) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
            } else {
                this.root = child;
            }

            if (color == BLACK) {

                removeFixUp(child, parent);
            }
        }
        node = null;
    }


    private BookNode producer(BookNode node){
        if(node.left!=null){
            node=node.left;
        }
        return node;
    }
    private void transplant(BookNode u, BookNode v){
        if(u.parent==null){
            root=v;
        }else if(u==u.parent.left){
            u.parent.left=v;
        }else{
            u.parent.right=v;
        }
        if(v!=null) v.parent=u.parent;
    }
    private void print(BookNode tree, int bookID, int direction) {
        if(tree != null) {
            if(direction==0)    // tree is root
                System.out.printf("%2d(B) is root\n", tree.bookID);
            else                // tree left or right
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.bookID, checkRed(tree)?"R":"B", bookID, direction==1?"right" : "left");
            print(tree.left, tree.bookID, -1);
            print(tree.right,tree.bookID,  1);
        }
    }
    public void print() {
        if (root != null)
            print(root, root.bookID, 0);
    }
}

