public class RBlackTree {
    private BookNode root;
    private static final boolean RED   = false;
    private static final boolean BLACK = true;
    private int countFlipColor =0;
    public RBlackTree(){
        root = null;
    }
    public int getCountFlipColor() {return countFlipColor-1;} //minus root's color flip
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
        //if node is not null and node's color is red, flip color and count
        if (node!=null) {
            if (node.color == RED)
                countFlipColor++;
            node.color = BLACK;
        }
    }
    private void setRed(BookNode node) {
        //if node is not null and node's color is black, flip color and count
        if (node!=null) {
            if (node.color == BLACK)
                countFlipColor++;
            node.color = RED;
        }
    }
    private boolean getColorOf(BookNode node) {return node!=null ? node.color : BLACK;}
    private void setColor(BookNode node, boolean color) {
        if (node!=null) {
            if(node.color != color){
                countFlipColor++;
            }
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
        BookNode parent,gparent;
        while((parent = parentOf(k))!=null&& checkRed(parent)&&k.color!=BLACK){
            //if parent is red, k is red and k is not root
            System.out.println("flip color!!!");
            gparent=parentOf(parent);
            //if parent is left child
            if(parent==gparent.left){
                BookNode uncle=gparent.right;
                //if uncle is red, flip color
                if(uncle!=null&& checkRed(uncle)){
                    //case 1, uncle is red, flip color
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    k=gparent;//move k to gparent
                } else {
                    //case 2, uncle is black and k is right child, left rotate
                    if(k==parent.right){
                        BookNode tmp;
                        leftRotate(parent);
                        tmp=parent;
                        parent=k;
                        k=tmp;
                    }
                    //case 3, uncle is black and k is left child, right rotate
                    rightRotate(gparent);
                    setBlack(parent);
                    setRed(gparent);
                }
            }else{
                BookNode uncle=gparent.left;
                if(uncle!=null&& checkRed(uncle)){
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    k=gparent;
                }else{
                    if(k==parent.left){
                        BookNode tmp;
                        rightRotate(parent);
                        tmp=parent;
                        parent=k;
                        k=tmp;
                    }
                    leftRotate(gparent);
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
                    leftRotate(parent);
                    brother = parent.right;
                }
            if ((brother.left==null || checkBlack(brother.left)) &&
                    (brother.right==null || checkBlack(brother.right))) {
                // Case 2: x's brother is black, and his children are black
                setRed(brother);
                node = parent;
                parent = parentOf(node);
            } else {

                if (brother.right==null || checkBlack(brother.right)) {
                    // Case 3: x's brother is black, his left child is red, right child is black
                    setBlack(brother.left);
                    setRed(brother);
                    rightRotate(brother);
                    brother = parent.right;
                }
                // Case 4: x's brother is black, his right child is red, left child could be any color
                setColor(brother, getColorOf(parent));
                setBlack(parent);
                setBlack(brother.right);
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
                rightRotate(parent);
                brother = parent.left;
            }

            if ((brother.left==null || checkBlack(brother.left)) &&
                    (brother.right==null || checkBlack(brother.right))) {
                // Case 2: x's brother is black, and his children are black
                setRed(brother);
                node = parent;
                parent = parentOf(node);
            } else {
                if (brother.left==null || checkBlack(brother.left)) {
                    // Case 3: x's brother is black, his right child is red, left child is black
                    setBlack(brother.right);
                    setRed(brother);
                    leftRotate(brother);
                    brother = parent.left;
                }
                // Case 4: x's brother is black, his left child is red, right child could be any color
                setColor(brother, getColorOf(parent));
                setBlack(parent);
                setBlack(brother.left);
                rightRotate(parent);
                node = this.root;
                break;
            }
        }
    }
    if (node!=null)
        setBlack(node);
}
    public void delete(BookNode node) {
        BookNode child, parent;
        boolean color;
        // node's left child is not null and right child is not null
        if ( (node.left!=null) && (node.right!=null) ) {
            // find node's successor and replace it with node
            BookNode replace = node;
            // get node's successor
            replace = replace.right;
            while (replace.left != null)
                replace = replace.left;
            // node is not root, because only root's parent is null
            if (parentOf(node)!=null) {
                if (parentOf(node).left == node)
                    parentOf(node).left = replace;
                else
                    parentOf(node).right = replace;
            } else {
                // node is root, replace is root
                this.root = replace;
            }
            // get successor's right child, which is the node that needs to be adjusted
            // also successor's left child is null, because successor is the smallest node in the right subtree
            child = replace.right;
            parent = parentOf(replace);
            color = getColorOf(replace);
            // replace is node's right child
            if (parent == node) {
                parent = replace;
            } else {
                if (child!=null)
                    setParent(child, parent);
                parent.left = child;
                replace.right = node.right;
                setParent(node.right, replace);
            }
            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;
            if (color == BLACK)
                removeFixUp(child, parent);
            node = null;
            return ;
        }
        if (node.left !=null) {
            child = node.left;
        } else {
            child = node.right;
        }
        parent = node.parent;
        color = node.color;
        if (child!=null)
            child.parent = parent;
        if (parent!=null) {
            if (parent.left == node)
                parent.left = child;
            else
                parent.right = child;
        } else {
            this.root = child;
        }
        if (color == BLACK)
            removeFixUp(child, parent);
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
    private static final int a[] = {4,2,5,3};

    public static void main(String[] args) {
        int i, ilen = a.length;
        RBlackTree tree = new RBlackTree();

        System.out.printf("== 原始数据: ");
        for (i = 0; i < ilen; i++)
            System.out.printf("%d ", a[i]);
        System.out.printf("\n");

        for (i = 0; i < ilen; i++) {
            tree.insertNode(a[i], "Book4", "Author1", "Yes");
            // 设置mDebugInsert=true,测试"添加函数"
            if (true) {
                System.out.printf("== 添加节点: %d\n", a[i]);
                System.out.printf("== 树的详细信息: \n");
                tree.print();
                System.out.printf("\n");
            }
        }
        System.out.printf("== 树的详细信息: \n");
        tree.print();
        System.out.printf("\n");
        if (true) {
            tree.deleteTree(2);
            System.out.printf("== 树的详细信息: \n");
            tree.print();
            System.out.printf("\n");
        }
    }
}
