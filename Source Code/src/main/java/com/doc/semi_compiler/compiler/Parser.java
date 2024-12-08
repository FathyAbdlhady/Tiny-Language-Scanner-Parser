package com.doc.semi_compiler.compiler;

import java.util.ArrayList;

 
public class Parser 
{
  
   private String token;
   private String currentStringVal;
   private String currentTokenLine;
   private int currentTokenNumber;
 
   private ArrayList<String> stringVal;
   private ArrayList<String> tokenType;
   private ArrayList<String> tokenLine;
   private Boolean error;
   private String errorStr;
 
   public Parser(ArrayList<String> stringVal, ArrayList<String> tokenType , ArrayList<String> tokenLine) 
   {
      this.tokenType = tokenType;
      this.stringVal = stringVal;
      this.tokenLine = tokenLine;
      this.error = false;
      this.errorStr = "";
      currentTokenNumber = 0;
      token = tokenType.get(currentTokenNumber);
      currentStringVal = stringVal.get(currentTokenNumber);
      currentTokenLine = tokenLine.get(currentTokenNumber);
   }
 
   public Boolean getError() {
      return error;
   }

   public String getErrorStr() {
      return errorStr;
   }

   private void getNextTokenAndStringVal() 
   {
      currentTokenNumber++;
      token = tokenType.get(currentTokenNumber);
      currentStringVal = stringVal.get(currentTokenNumber);
      currentTokenLine = tokenLine.get(currentTokenNumber);
   }
 
   public Node program()
   {
      tokenType.add("EOF");
      stringVal.add("eof");
      tokenLine.add("eof");

      Node tempNode = new Node("Program","box");
     
      tempNode.setMiddle(stmtSequence());
      if(error == true || !token.equals("EOF")) 
      {
         tempNode = null;
         error = true;
      }
      
 
      return tempNode;
   }
 
   private ArrayList<Node> stmtSequence()
   {
      ArrayList<Node> tempNode = new ArrayList<Node>();
 
      tempNode.add(statment());
      if(error == true) tempNode = null;
      else
      {
         while(token.equals("SEMICOLON"))
         {
            getNextTokenAndStringVal();
            tempNode.add(statment());
            if(error == true)
            {
               tempNode = null;
               break;
            }
         }
      }
      return tempNode;
   }
 
   private Node statment()
   {
      Node tempNode = null;
 
      if(token.equals("IF")) tempNode = ifStmt();
      else if(token.equals("REPEAT")) tempNode = repeatStmt();
      else if(token.equals("IDENTIFIER")) tempNode = assignStmt();
      else if(token.equals("READ")) tempNode = readStmt();
      else if(token.equals("WRITE")) tempNode = writeStmt();
      else {
         error = true;
         errorStr = "syntax error " + currentTokenLine + ", expected a \'write\' or \'read\' or \'identifier\' or \'repeat\' or \'if\' statment";
      }
 
      if(error == true) tempNode = null;
      
      
      return tempNode;
   }
 
   private Node ifStmt()
   {
      Node tempNode = null;
 
      if(token.equals("IF"))
      {
         tempNode = new Node("if","box");
         getNextTokenAndStringVal();
        
         tempNode.setLeft(exp());
         if(error == true) tempNode = null;
         else
         {
            if(token.equals("THEN"))
            {
               getNextTokenAndStringVal();
               tempNode.setMiddle(stmtSequence());
 
               if(token.equals("ELSE"))
               {
                  getNextTokenAndStringVal();
                  tempNode.setRight(stmtSequence());
                  if(error == true) tempNode = null; 
               }
 
               if(token.equals("END") && error != true) 
                  getNextTokenAndStringVal();
               else
               {
                  error = true;
                  errorStr = "syntax error " + currentTokenLine + ", expected a \'end\' ";
                  tempNode = null; 
               }
            }
            else
            {
               error = true;
               errorStr = "syntax error " + currentTokenLine + ", expected a \'then\' ";
               tempNode = null;
            }
         }
      }
      else error = true;
      return tempNode;
   }
 
   private Node repeatStmt()
   {
      Node tempNode = null;
      if(token.equals("REPEAT"))
      {
         tempNode = new Node("repeat","box");
         getNextTokenAndStringVal();
 
         tempNode.setMiddle(stmtSequence());
         if(error == true) tempNode = null;
         else
         {
            if(token.equals("UNTIL"))
            {
               getNextTokenAndStringVal();
               tempNode.addRight(exp());
               if(error == true) tempNode = null;
            }
            else
            {
               error = true;
               errorStr = "syntax error " + currentTokenLine + ", expected a \'until\'  ";
               return null;
            }
         }  
      }
      else error = true;
      return tempNode;
   }
 
   private Node assignStmt()
   {
      Node tempNode = null;
      if(token.equals("IDENTIFIER"))
      {
         tempNode = new Node(currentStringVal,"box");
         getNextTokenAndStringVal();
 
         if(token.equals("ASSIGN"))
         {
            tempNode.setKey("assign\n(" + tempNode.getKey() + ")");
            getNextTokenAndStringVal();
            tempNode.setLeft(exp());
            if(error == true) tempNode = null;
         } 
         else
         {
            error = true;
            errorStr = "syntax error " + currentTokenLine + ", expected a \':=\'  ";
            return null;
         }
      }
      else error = true;
      return tempNode;
   }
 
   private Node readStmt()
   {
      Node tempNode = null;
      if(token.equals("READ"))
      {
         tempNode = new Node("read\n(","box");
         getNextTokenAndStringVal();
 
         if(token.equals("IDENTIFIER"))
         {
           tempNode.setKey(tempNode.getKey() + currentStringVal + ")");
            getNextTokenAndStringVal();
         } 
         else
         {
            error = true;
            errorStr = "syntax error " + currentTokenLine + ", expected an \'identifier\' ";
            tempNode = null;
         }
      }
      else error = true;
      return tempNode;
   }
 
   private Node writeStmt()
   {
      Node tempNode = null;
      if(token.equals("WRITE"))
      {
         tempNode = new Node("write","box");
         getNextTokenAndStringVal();
 
         tempNode.setLeft(exp());
         if(error == true) tempNode = null;
      }
      else {
         error = true;
      }
      return tempNode;
   }
 
   private Node exp()
   {
      Node tempNode = null,newTempNode = null;
 
      tempNode = simpleExp();
      if(error == true) tempNode = null;
      else
      {
         newTempNode = expPrime();
         if(error == true) tempNode = null;
         else if(newTempNode != null)
         {
            newTempNode.setLeft(tempNode);
            tempNode = newTempNode;
         }
      }
      return tempNode;
   }
 
   private Node expPrime() 
   {
      Node tempNode = null;
 
      if(token.equals("EQUAL")  || token.equals("LESSTHAN"))
      {
         tempNode = new Node("op\n("+currentStringVal+")");
         getNextTokenAndStringVal();

         tempNode.addRight(simpleExp());
         if(error == true) tempNode = null;
      }
      else if(token.equals("CLOSEDBRACKET")  || token.equals("THEN")  || token.equals("SEMICOLON")  || token.equals("ELSE")  || 
               token.equals("END")  || token.equals("UNTIL")  || token.equals("EOF") ) {}
      else {
         errorStr = "syntax error " + currentTokenLine + ", expected a \'=\' or \'<\' or \')\' or \'then\' or \';\' or \'end\' ";
         error = true;
      }
      return tempNode;
   }
 
   private Node simpleExp() 
   {
      Node tempNode = null,newTempNode = null;
 
      tempNode = term();
      if(error == true) {}
      else
      {
         while(token.equals("PLUS")  || token.equals("MINUS")) 
         {
            newTempNode = new Node("op\n("+currentStringVal+")");
            getNextTokenAndStringVal();
            newTempNode.setLeft(tempNode);

            newTempNode.addRight(term());
            if(error == true) 
            {
               tempNode = null;
               break;
            }
            tempNode = newTempNode;
         }
      }
      return tempNode;
   }
 
   private Node term() 
   {
      Node tempNode = null,newTempNode = null;
 
      tempNode = factor();
      if(error == true) {}
      else
      {
         while(token.equals("MULT")  || token.equals("DIV")) 
         {
            newTempNode = new Node("op\n("+currentStringVal+")");
            getNextTokenAndStringVal();
            newTempNode.setLeft(tempNode);
            newTempNode.addRight(factor());
            if(error == true) 
            {
               tempNode = null;
               break;
            }
            tempNode = newTempNode;
         }
      }   
      return tempNode;
   }
 
   private Node factor()
   {
      Node tempNode = null;
      if(token.equals("OPENBRACKET"))
      {
         getNextTokenAndStringVal();
 
         tempNode = exp();
         if(error == true) tempNode = null;
         else
         {
            if(token.equals("CLOSEDBRACKET")) getNextTokenAndStringVal();
            else
            {
               error = true;
               errorStr = "syntax error " + currentTokenLine + ", expected a \')\' ";
               tempNode = null;
            }
         }
      }
      else if(token.equals("NUMBER") || token.equals("IDENTIFIER"))
      {
         if(token.equals("NUMBER")) tempNode = new Node("const\n("+currentStringVal+")");
         else tempNode = new Node("id\n("+currentStringVal+")");
         getNextTokenAndStringVal();
      }
      else {
         error = true;
         errorStr = "syntax error " + currentTokenLine + ", expected a  \'(\' or \'Number\' or \'identifier\' ";
      } 
      return tempNode;
   }   
 
}
