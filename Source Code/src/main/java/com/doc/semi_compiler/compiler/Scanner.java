package com.doc.semi_compiler.compiler;

import java.util.ArrayList;
import java.util.Arrays;

/*
    Author: - Omar Saleh
            - Ahmad Mahfouz
*/
public class Scanner {

    enum TokenType {
       SEMICOLON,
       IF,
       THEN,
       ELSE,
       END,
       REPEAT,
       UNTIL,
       IDENTIFIER,
       ASSIGN,
       READ,
       WRITE,
       LESSTHAN,
       EQUAL,
       PLUS,
       MINUS,
       MULT,
       DIV,
       OPENBRACKET,
       CLOSEDBRACKET,
       NUMBER,
       COMMENT,
       nothing
    }
 
 enum StateType {
       START,
       INASSIGN,
       INCOMMENT,
       INNUM,
       INID,
       DONE
    }
 
    private ArrayList<String> stringVal;
    private ArrayList<String> tokenType;
    private ArrayList<String> tokenLine;
    private Boolean error;
    private String errorStr;
 
 
    public Scanner(ArrayList<String> stringVal, ArrayList<String> tokenType , ArrayList<String> tokenLine ) 
    {
       this.stringVal = stringVal;
       this.tokenType = tokenType;
       this.tokenLine = tokenLine;
       this.error = false;
       this.errorStr = "";
    }
 
 
    public Boolean getError() {
        return error;
    }


    public String getErrorStr() {
        return errorStr;
    }


    public String scanner(ArrayList<String> lines) {
 
 
       ArrayList<String> reservedWords = new ArrayList<String>(
               Arrays.asList("if", "then", "else", "end", "repeat", "until", "read", "write"));
 
       TokenType currentToken = TokenType.nothing;
       StateType state = StateType.START;
 
       int i = 0, j;
       error = false;
       errorStr = "";
       int startChar = 0, endChar = 0;
       char currentChar;
       int commentStartLine = -1;
       while (i != lines.size()) {
           j = 0;
           char lineCharacters[] = lines.get(i).toCharArray();
 
           while (j < lineCharacters.length) {
               currentChar = lineCharacters[j];
 
               switch (state) {
                   case START:
                       if (currentChar == ' ')
                           state = StateType.START;
                       else if (Character.isDigit(currentChar)) {
                           currentToken = TokenType.NUMBER;
                           state = StateType.INNUM;
                       } else if (Character.isLetter(currentChar)) {
                           currentToken = TokenType.IDENTIFIER;
                           state = StateType.INID;
                       } else if (currentChar == ':') {
                           state = StateType.INASSIGN;
                       } else if (currentChar == '{') {
                           currentToken = TokenType.COMMENT;
                           state = StateType.INCOMMENT;
                           commentStartLine = i + 1;
                       } else {
                           switch (currentChar) {
                               case ';':
                                   currentToken = TokenType.SEMICOLON;
                                   break;
                               case '<':
                                   currentToken = TokenType.LESSTHAN;
                                   break;
                               case '=':
                                   currentToken = TokenType.EQUAL;
                                   break;
                               case '+':
                                   currentToken = TokenType.PLUS;
                                   break;
                               case '-':
                                   currentToken = TokenType.MINUS;
                                   break;
                               case '*':
                                   currentToken = TokenType.MULT;
                                   break;
                               case '/':
                                   currentToken = TokenType.DIV;
                                   break;
                               case '(':
                                   currentToken = TokenType.OPENBRACKET;
                                   break;
                               case ')':
                                   currentToken = TokenType.CLOSEDBRACKET;
                                   break;
                               default:
                                   error = true;
                                   break;
                           }
                           state = StateType.DONE;
                       }
                       startChar = endChar = j;
                       break;
                   case INASSIGN:
                       if (currentChar == '=')
                           currentToken = TokenType.ASSIGN;
                       else
                           error = true;
                       state = StateType.DONE;
                       break;
                   case INCOMMENT:
                       if (currentChar == '}') {
                           state = StateType.START;
                           commentStartLine = -1;
                       }
                       break;
                   case INNUM:
                       if (Character.isDigit(currentChar)) {
                       } else {
                           state = StateType.DONE;
                           endChar--;
                           j--;
                       }
                       break;
                   case INID:
                       if (Character.isLetter(currentChar)) {
                       } else {
                           state = StateType.DONE;
                           endChar--;
                           j--;
                       }
                       break;
                   case DONE:
                       break;
                   default:
                       break;
               }
               endChar++;
 
               if (state == StateType.DONE) {
                   if (error) {
                       errorStr += "UN Recongnized Token \"" + lines.get(i).substring(startChar, endChar).trim()
                               + "\" at line " + (i + 1) + " at pos " + (startChar+1) + "\n";
                       break;
                   } else {
                       tokenLine.add("at line " + (i + 1) + " at pos " + (startChar+1));
                       stringVal.add(lines.get(i).substring(startChar, endChar).trim());
 
                       if (currentToken == TokenType.IDENTIFIER &&
                               reservedWords.contains(lines.get(i).substring(startChar, endChar).toLowerCase())) {
                           tokenType.add(lines.get(i).substring(startChar, endChar).toUpperCase().trim());
                       } else {
                           tokenType.add("" + currentToken);
                       }
                   }
                   state = StateType.START;
               }
               j++;
           }
           i++;
           if (error)
               break;
 
           if ((i == lines.size() - 1) && (state == StateType.INCOMMENT))
               errorStr += "UNTERMINATED COMMENT at line " + commentStartLine + " at pos " + (startChar+1)+ "\n";
       }
 
       String content;
       if (!errorStr.isEmpty())
           content = "Error : " + errorStr + "\n\n" + "String Value \t Token Type"
                   + "\n------------       ----------\n";
       else
           content = "String Value \t Token Type" + "\n------------       ----------\n";
 
       for (i = 0; i < tokenType.size(); i++) {
           content += stringVal.get(i) + "\t\t\t   " + tokenType.get(i) + '\n';
       }
 
       return content;
 
   }
 
 }
