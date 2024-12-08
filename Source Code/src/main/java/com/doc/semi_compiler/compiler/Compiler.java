package com.doc.semi_compiler.compiler;

import java.util.ArrayList;


import guru.nidi.graphviz.attribute.Attributes;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.model.*;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;



public class Compiler
{
   

    private Graph syntaxTree;
    private ArrayList<String> stringVal;
    private ArrayList<String> tokenType;
    private ArrayList<String> tokenLine;
    private Boolean scannerError;
    private Boolean parserError;
    private String errorStr;


    public Compiler() {
        stringVal = new ArrayList<String>();
        tokenType = new ArrayList<String>();
        tokenLine = new ArrayList<String>();
        scannerError = false;
        parserError = false;
        errorStr = new String();
    }

    private void generate(Node node) {
        if (node == null) {
            return;
        }

        syntaxTree = syntaxTree.with(
            node(node.toString()).with(Label.of(node.getKey()),Attributes.attr("shape", node.getShape())));

        
        if (node.getLeft() != null) {
            generate(node.getLeft());
            syntaxTree = syntaxTree.with(
                node(node.toString()).link(node(node.getLeft().toString())));
        }
    
        int index = 0;
        Node prevChild = null;
        for (Node middleChild : node.getMiddle()) {
            generate(middleChild);
            if (index == 0 && middleChild != null) { 
                syntaxTree = syntaxTree.with(
                node(node.toString()).link(node(middleChild.toString())));
            }
            else if(middleChild != null)
            {
                syntaxTree = syntaxTree.with(
                node(node.toString()).link(to((node(middleChild.toString()))).with(Attributes.attr("style", "invis"))),    
                node(prevChild.toString()).link(to(node(middleChild.toString())).with(Attributes.attr("constraint", "false"))));
            }
            prevChild = middleChild;
            index++;
        }

    
        index = 0;
        prevChild = null;
        for (Node rightChild : node.getRight()) {
            generate(rightChild);
            if (index == 0 && rightChild != null) { 
                syntaxTree = syntaxTree.with(
                node(node.toString()).link(node(rightChild.toString())));
            }
            else if(rightChild != null)
            {
                syntaxTree = syntaxTree.with(
                node(node.toString()).link(to(node((rightChild.toString()))).with(Attributes.attr("style", "invis"))),    
                node(prevChild.toString()).link(to(node(rightChild.toString())).with(Attributes.attr("constraint", "false"))));
            }
            prevChild = rightChild;
            index++;
        }
    }

    public void generateSyntaxTree(Node root) {

        syntaxTree = graph("syntaxtree");

        int index = 0;
        Node prevChild = null;
        for (Node Child : root.getMiddle()) {
            generate(Child);
            if (index == 0 && Child != null) {}
            else if(Child != null)
            {
                syntaxTree = syntaxTree.with(  
                node(prevChild.toString()).link(to(node(Child.toString())).with(Attributes.attr("constraint", "false"))));
            }
            prevChild = Child;
            index++;
        }
    }

    public Boolean getScannerError() {
        return scannerError;
    }

    public Boolean getParserError() {
        return parserError;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public Graph getSyntaxTree() {
        return syntaxTree;
    }

    public String scanner(ArrayList<String> lines) {

        Scanner newScan = new Scanner(stringVal, tokenType, tokenLine);
        String content = newScan.scanner(lines);

        scannerError = newScan.getError();
        errorStr = newScan.getErrorStr();

        return content;

    }

    public Node parse()
    {
        Parser newParser = new Parser(stringVal, tokenType,tokenLine);

        Node root = newParser.program();

        parserError = newParser.getError();
        errorStr = newParser.getErrorStr();

        return root;
    }


}