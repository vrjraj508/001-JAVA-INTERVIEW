package com.basics.basics;

class Node {
    int data;
    Node left;
    Node right;

    Node(int data){
        this.data = data;
        left = null;
        right = null;
    }
}


class BasicTree{

    static void inorder(Node root){
        if(root ==null) return;
        inorder(root.left);
        System.out.print(root.data+"-->");
        inorder(root.right);

    }

    static void preorder(Node root){
        if(root ==null) return;
        System.out.print(root.data+"-->");
        preorder(root.left);
        preorder(root.right);
    }

    static void postorder(Node root){
        if(root ==null) return;
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.data+"-->");
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        System.out.println("Root :"+root.data);
        System.out.println("Left Child of root: "+root.left.data);
        System.out.println("Right Child of root: "+root.right.data);

        inorder(root);
        System.out.println();
        preorder(root);
        System.out.println();
        postorder(root);

    }
}

public class Tree01Basics {
}
