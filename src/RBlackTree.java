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
    }
    private void setBlack(BookNode node) {
        if (node!=null) {
            if (node.color == RED)
                countFlipColor++;
            node.color = BLACK;
        }
    }
    private void setRed(BookNode node) {
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
            System.out.println("flip color!!!");
            gparent=parentOf(parent);
            if(parent==gparent.left){
                BookNode uncle=gparent.right;
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
        BookNode de=searchID(bookID);
        delete(de);
    }
    private void removeFixUp(BookNode node, BookNode parent) {
        BookNode other;
        while ((node==null || checkBlack(node)) && (node != this.root)) {
            if (parent.left == node) {
                other = parent.right;
                if (checkRed(other)) {
                // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }
            if ((other.left==null || checkBlack(other.left)) &&
                    (other.right==null || checkBlack(other.right))) {
                // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                setRed(other);
                node = parent;
                parent = parentOf(node);
            } else {

                if (other.right==null || checkBlack(other.right)) {
                    // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                    setBlack(other.left);
                    setRed(other);
                    rightRotate(other);
                    other = parent.right;
                }
                // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                setColor(other, getColorOf(parent));
                setBlack(parent);
                setBlack(other.right);
                leftRotate(parent);
                node = this.root;
                break;
            }
        } else {

            other = parent.left;
            if (checkRed(other)) {
                // Case 1: x的兄弟w是红色的
                setBlack(other);
                setRed(parent);
                rightRotate(parent);
                other = parent.left;
            }

            if ((other.left==null || checkBlack(other.left)) &&
                    (other.right==null || checkBlack(other.right))) {
                // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                setRed(other);
                node = parent;
                parent = parentOf(node);
            } else {

                if (other.left==null || checkBlack(other.left)) {
                    // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                    setBlack(other.right);
                    setRed(other);
                    leftRotate(other);
                    other = parent.left;
                }

                // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                setColor(other, getColorOf(parent));
                setBlack(parent);
                setBlack(other.left);
                rightRotate(parent);
                node = this.root;
                break;
            }
        }
    }
    if (node!=null)
        setBlack(node);
}

/*
 * 删除结点(node)，并返回被删除的结点
 *
 * 参数说明：
 *     node 删除的结点
 */
public void delete(BookNode node) {
    BookNode child, parent;
    boolean color;

    // 被删除节点的"左右孩子都不为空"的情况。
    if ( (node.left!=null) && (node.right!=null) ) {
        // 被删节点的后继节点。(称为"取代节点")
        // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
        BookNode replace = node;

        // 获取后继节点
        replace = replace.right;
        while (replace.left != null)
            replace = replace.left;

        // "node节点"不是根节点(只有根节点不存在父节点)
        if (parentOf(node)!=null) {
            if (parentOf(node).left == node)
                parentOf(node).left = replace;
            else
                parentOf(node).right = replace;
        } else {
            // "node节点"是根节点，更新根节点。
            this.root = replace;
        }

        // child是"取代节点"的右孩子，也是需要"调整的节点"。
        // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
        child = replace.right;
        parent = parentOf(replace);
        // 保存"取代节点"的颜色
        color = getColorOf(replace);

        // "被删除节点"是"它的后继节点的父节点"
        if (parent == node) {
            parent = replace;
        } else {
            // child不为空
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
    // 保存"取代节点"的颜色
    color = node.color;

    if (child!=null)
        child.parent = parent;

    // "node节点"不是根节点
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

/*
 * 删除结点(z)，并返回被删除的结点
 *
 * 参数说明：
 *     tree 红黑树的根结点
 *     z 删除的结点
 */
//public void remove(T key) {
//    bookNode<T> node;
//
//    if ((node = search(mRoot, key)) != null)
//        remove(node);
//}
//    public void delete(BookNode node){
//        if (node == null) {
//            return;
//        }
//        if (root==null){
//            return;
//        }
//        BookNode child;
//        BookNode y=node;
//        boolean colorOfY=y.color;
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
//        if(colorOfY==BLACK){
//            //if y is black, then it will break the rule 5, so we need to fix it
//            deleteFix(child);
//        }
//    }
//    private void deleteFix(BookNode node){
////        System.out.println("node's color"+node.color);
//        while(node==null||node!=root&&(node.color==BLACK)) {
//            if (node == node.parent.left) {
//                BookNode brother = node.parent.right;
//                if (brother!=null&&brother.color == RED) {
//                    //case 1, brother is red, flip color
//                    setBlack(brother);
//                    setRed(node.parent);
//                    leftRotate(node.parent);
//                    brother = node.parent.right;
//                }
//                if ((brother.left==null||brother.left.color == BLACK) && (brother.right==null||brother.right.color == BLACK)) {
//                    //case 2, brother is black and both of brother's children are black
//                    setRed(brother);
//                    node = node.parent;//move up
//                } else {
//                    if (brother.right==null||brother.right.color == BLACK) {
//                        //case 3, brother is black and brother's left child is red, brother's right child is black
//                        setBlack(brother.left);
//                        setRed(brother);
//                        rightRotate(brother);
//                        brother = node.parent.right;
//                    }
//                    //case 4, brother is black and brother's right child is red, brother's left child could be any color
//                    setColor(brother, getColorOf(node.parent));
//                    setBlack(node.parent);
//                    setBlack(brother.right);
//                    leftRotate(node.parent);
//                    node = root;
//                }
//            } else {
//                BookNode brother = node.parent.left;
//                if (brother!=null&&brother.color == RED) {
//                    //case 1, brother is red, flip color
//                    setBlack(brother);
//                    setRed(node.parent);
//                    rightRotate(node.parent);
//                    brother = node.parent.left;
//                }
//                if ((brother.left==null||brother.left.color == BLACK) && (brother.right==null||brother.right.color == BLACK)) {
//                    //case 2, brother is black and both of brother's children are black
//                    setRed(brother);
//                    node = node.parent;//move up
//                } else {
//                    if (brother.left==null||brother.left.color == BLACK) {
//                        //case 3, brother is black and brother's right child is red, brother's left child is black
//                        setBlack(brother.right);
//                        setRed(brother);
//                        leftRotate(brother);
//                        brother = node.parent.left;
//                    }
//                    //case 4, brother is black and brother's left child is red, brother's right child could be any color
//                    setColor(brother, getColorOf(node.parent));
//                    setBlack(node.parent);
//                    setBlack(brother.left);
//                    rightRotate(node.parent);
//                    node = root;
//                }
//            }
//        }
//        if (node!=null) {
//            setBlack(node);
//        }
//    }
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
