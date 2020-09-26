import javax.swing.*;
import java.io.*;

public class BinaryTreeDemo {
    static String filename = "tree.ser";
    public static void main(String[] args) {
        // Create a tree
        BinaryTree<String> testTree = new BinaryTree<String>();
        createTree1(testTree);
       // testTree.inorderTraverse();

        game:
        while(true) {
            BinaryNodeInterface<String> current = testTree.getRootNode();
            // To get to a leaf node
            while (!current.isLeaf()) {
                String ans = JOptionPane.showInputDialog(null, current.getData()); // get yes or no answer from user
                if (ans.equals("yes")) {
                    current = current.getLeftChild();
                } else if (ans.equals("no")) {
                    current = current.getRightChild();
                }
            }
            //Check if right or wrong
            if (current.isLeaf()) {
                String ans = JOptionPane.showInputDialog(null, "I think you are thinking of a " + current.getData() + "?");
                //correct guess
                if (ans.equals("yes")) {
                    JOptionPane.showMessageDialog(null, "The tree guessed correctly!!");
                    int num = 0;
                    while(num == 0) {
                        String choice = JOptionPane.showInputDialog(null, "[1] Play again?\n[2] Store the tree?\n[3] Load a stored tree?\n[4] Quit?");
                        try {
                            num = Integer.parseInt(choice);
                        } catch (NumberFormatException e) { // anything that is not a number will set num to 0
                            num = 0;
                        }
                    }
                    switch (num) {
                        case 1:
                            continue; // go through game again
                        case 2:
                            storeTree(testTree); // store tree
                            continue;
                        case 3:
                            testTree = loadTree(); // load stored tree
                            continue;
                        case 4:
                            break game; // break out of while loop for game
                        }
                }
                //incorrect guess
                else {
                    String userAnswer = JOptionPane.showInputDialog(null, "I don't know: what is the correct answer?");
                    String userQuestion = JOptionPane.showInputDialog(null, "Distinguishing question?");
                    String guessAnswerToQuestion = JOptionPane.showInputDialog(null, "Right answer for " + current.getData() + ":");

                    BinaryNode<String> oldLeaf = new BinaryNode<String>(current.getData());
                    BinaryNode<String> newLeaf = new BinaryNode<String>(userAnswer);
                    current.setData(userQuestion); //replace current node

                    if (guessAnswerToQuestion.equals("yes")) { // set correct paths
                        current.setLeftChild(oldLeaf);
                        current.setRightChild(newLeaf);
                    } else if (guessAnswerToQuestion.equals("no")) {
                        current.setLeftChild(newLeaf);
                        current.setRightChild(oldLeaf);
                    }
                }
            }
        }
    } // end of main


    public static void createTree1(BinaryTree<String> tree)
    {
        // To create a tree, build it up from the bottom:
        // create subtree for each leaf, then create subtrees linking them,
        // until we reach the root.
        // First the leaves
        BinaryTree<String> hTree = new BinaryTree<String>();
        hTree.setTree("Dog");
        // neater to use the constructor the initialisation ...
        BinaryTree<String> iTree = new BinaryTree<String>("Elephant");
        BinaryTree<String> jTree = new BinaryTree<String>("Penguin");
        BinaryTree<String> kTree = new BinaryTree<String>("Salmon");
        BinaryTree<String> lTree = new BinaryTree<String>("Usain Bolt");
        BinaryTree<String> mTree = new BinaryTree<String>("Donald Trump");
        BinaryTree<String> nTree = new BinaryTree<String>("Ireland");
        BinaryTree<String> oTree = new BinaryTree<String>("Ball");

        // Now the subtrees joining leaves:
        BinaryTree<String> dTree = new BinaryTree<String>("Considered a pet?", hTree, iTree);
        BinaryTree<String> eTree = new BinaryTree<String>("Is it a bird?", jTree, kTree);
        BinaryTree<String> fTree = new BinaryTree<String>("Are they an athlete?", lTree, mTree);
        BinaryTree<String> gTree = new BinaryTree<String>("Is it a place?", nTree, oTree);

        BinaryTree<String> bTree = new BinaryTree<String>("Is it a mammal?", dTree, eTree);
        BinaryTree<String> cTree = new BinaryTree<String>("Is it a person?", fTree, gTree);

        // Now the root
        tree.setTree("Are you thinking of an animal?", bTree, cTree);
    } // end createTree1

    public static void displayStats(BinaryTree<String> tree)
    {
        if (tree.isEmpty())
            System.out.println("The tree is empty");
        else
            System.out.println("The tree is not empty");

        System.out.println("Root of tree is " + tree.getRootData());
        System.out.println("Height of tree is " + tree.getHeight());
        System.out.println("No. of nodes in tree is " + tree.getNumberOfNodes());
    } // end displayStats


    // Takes in a BinaryTree to write to file
    public static void storeTree(BinaryTree<String> t){
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(t);

            out.close();
            file.close();
            System.out.println("Object has been serialized");
        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    // Loads the tree from the file and returns it
    public static BinaryTree<String> loadTree(){
        BinaryTree<String> t = null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            t = (BinaryTree<String>)in.readObject();

            in.close();
            file.close();
        }

        catch(IOException ex) {
            System.out.println("IOException is caught");
        }

        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        return t;
    }
}
