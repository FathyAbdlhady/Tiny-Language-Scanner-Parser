package com.doc.semi_compiler.compiler;

import java.util.ArrayList;

public class Node {
   private String key;
   private String shape;
   private Node left;
   private ArrayList<Node> middle , right;


   public Node(String key) {
      this.key = key;
      this.shape = "ellipse";
      left = null;
      middle = new ArrayList<Node>();
      right = new ArrayList<Node>();
   }

   public Node(String key,String shape) {
      this.key = key;
      this.shape = shape;
      left = null;
      middle = new ArrayList<Node>();
      right = new ArrayList<Node>();
   }
 
   public String getKey() {
      return key;
   }

   public String getShape() {
      return shape;
   }

   public Node getLeft() {
      return left;
   }

   public ArrayList<Node> getMiddle() {
      return middle;
   }

   public ArrayList<Node> getRight() {
      return right;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public void setShape(String shape) {
      this.shape = shape;
   }

   public void setLeft(Node node) {
      this.left = node;
   }

   public void setMiddle(ArrayList<Node> middle) {
      this.middle = middle;
   }

   public void setRight(ArrayList<Node> right) {
      this.right = right;
   }

   public void addRight(Node node) {
      this.right.add(node);
   }
}
