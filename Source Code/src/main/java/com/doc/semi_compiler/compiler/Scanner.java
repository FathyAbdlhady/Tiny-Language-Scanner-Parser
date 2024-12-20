package com.doc.semi_compiler.compiler;

import java.util.ArrayList;
import java.util.Arrays;

/*
*  Author Omar Saleh 
*         Ahmed Mahfuz
*/
/**
 * The Scanner class performs lexical analysis on a list of input lines.
 * It tokenizes the input based on predefined token types and manages states
 * to handle different parts of the input, such as identifiers, numbers, comments, etc.
 */
public class Scanner {

    /**
     * Enumeration of possible token types that the scanner can recognize.
     */
    enum TokenType {
        SEMICOLON,    // ;
        IF,           // if
        THEN,         // then
        ELSE,         // else
        END,          // end
        REPEAT,       // repeat
        UNTIL,        // until
        IDENTIFIER,   // Variable names and identifiers
        ASSIGN,       // :=
        READ,         // read
        WRITE,        // write
        LESSTHAN,     // <
        EQUAL,        // =
        PLUS,         // +
        MINUS,        // -
        MULT,         // *
        DIV,          // /
        OPENBRACKET,  // (
        CLOSEDBRACKET,// )
        NUMBER,       // Numeric literals
        COMMENT,      // Comments enclosed in {}
        NOTHING        // Represents no token
    }

    /**
     * Enumeration of possible states the scanner can be in during tokenization.
     */
    enum StateType {
        START,      // Initial state
        INASSIGN,   // Inside an assignment operator
        INCOMMENT,  // Inside a comment block
        INNUM,      // Inside a number literal
        INID,       // Inside an identifier
        DONE        // Tokenization completed for current token
    }

    // Lists to store the string values of tokens, their types, and their positions (line and character).
    private ArrayList<String> stringVal;
    private ArrayList<String> tokenType;
    private ArrayList<String> tokenLine;

    // Flags and messages for error handling.
    private Boolean error;
    private String errorStr;

    /**
     * Constructor to initialize the Scanner with empty token lists and no errors.
     *
     * @param stringVal  List to store the string values of tokens.
     * @param tokenType  List to store the types of tokens.
     * @param tokenLine  List to store the position of each token.
     */
    public Scanner(ArrayList<String> stringVal, ArrayList<String> tokenType, ArrayList<String> tokenLine) {
        this.stringVal = stringVal;
        this.tokenType = tokenType;
        this.tokenLine = tokenLine;
        this.error = false;
        this.errorStr = "";
    }

    /**
     * Getter for the error flag.
     *
     * @return True if an error was encountered during scanning, else false.
     */
    public Boolean getError() {
        return error;
    }

    /**
     * Getter for the error message.
     *
     * @return The error message encountered during scanning.
     */
    public String getErrorStr() {
        return errorStr;
    }

    /**
     * The scanner method performs lexical analysis on the provided lines of code.
     * It tokenizes the input based on predefined token types and handles different states
     * to manage multi-character tokens like identifiers, numbers, comments, etc.
     *
     * @param lines The list of code lines to be scanned and tokenized.
     * @return A formatted string containing the list of tokens and their types.
     */
    public String scanner(ArrayList<String> lines) {

        // List of reserved keywords in the language.
        ArrayList<String> reservedWords = new ArrayList<String>(
                Arrays.asList("if", "then", "else", "end", "repeat", "until", "read", "write"));

        // Initialize the current token type and scanner state.
        TokenType currentToken = TokenType.NOTHING;
        StateType state = StateType.START;

        // Index variables for iterating through lines and characters.
        int i = 0, j;
        error = false;
        errorStr = "";
        int startChar = 0, endChar = 0;
        char currentChar;
        int commentStartLine = -1; // To track the start line of a comment for error reporting.

        // Iterate through each line of input.
        while (i != lines.size()) {
            j = 0;
            char lineCharacters[] = lines.get(i).toCharArray(); // Convert current line to a char array.

            // Iterate through each character in the current line.
            while (j < lineCharacters.length) {
                currentChar = lineCharacters[j];

                switch (state) {
                    case START:
                        if (currentChar == ' ') {
                            // Ignore whitespace and remain in START state.
                            state = StateType.START;
                        } else if (Character.isDigit(currentChar)) {
                            // Encountered a digit, switch to INNUM state.
                            currentToken = TokenType.NUMBER;
                            state = StateType.INNUM;
                        } else if (Character.isLetter(currentChar)) {
                            // Encountered a letter, switch to INID state.
                            currentToken = TokenType.IDENTIFIER;
                            state = StateType.INID;
                        } else if (currentChar == ':') {
                            // Potential start of ASSIGN token (:=).
                            state = StateType.INASSIGN;
                        } else if (currentChar == '{') {
                            // Start of a comment block.
                            currentToken = TokenType.COMMENT;
                            state = StateType.INCOMMENT;
                            commentStartLine = i + 1; // Record the line number where comment started.
                        } else {
                            // Handle single-character tokens.
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
                                    // Unrecognized character, set error flag.
                                    error = true;
                                    break;
                            }
                            state = StateType.DONE; // Move to DONE state to process the token.
                        }
                        startChar = endChar = j; // Mark the start position of the token.
                        break;

                    case INASSIGN:
                        if (currentChar == '=') {
                            // Complete assignment operator ':='.
                            currentToken = TokenType.ASSIGN;
                        } else {
                            // Invalid token if '=' does not follow ':'.
                            error = true;
                        }
                        state = StateType.DONE; // Move to DONE state to process the token.
                        break;

                    case INCOMMENT:
                        if (currentChar == '}') {
                            // End of comment block.
                            state = StateType.START;
                            commentStartLine = -1; // Reset comment start line.
                        }
                        // Remain in INCOMMENT state; skip all characters inside comment.
                        break;

                    case INNUM:
                        if (Character.isDigit(currentChar)) {
                            // Continue reading digits in a number.
                            // No state change needed.
                        } else {
                            // Non-digit character signifies end of number token.
                            state = StateType.DONE;
                            endChar--; // Adjust endChar as current character is not part of number.
                            j--;      // Step back to reprocess the current character.
                        }
                        break;

                    case INID:
                        if (Character.isLetter(currentChar)) {
                            // Continue reading letters in an identifier.
                            // No state change needed.
                        } else {
                            // Non-letter character signifies end of identifier token.
                            state = StateType.DONE;
                            endChar--; // Adjust endChar as current character is not part of identifier.
                            j--;      // Step back to reprocess the current character.
                        }
                        break;

                    case DONE:
                        // No specific action needed in DONE state.
                        break;

                    default:
                        // Handle any unexpected states.
                        break;
                }
                endChar++; // Move endChar to include current character in the token.

                // If the scanner has completed a token, process it.
                if (state == StateType.DONE) {
                    if (error) {
                        // If an error was encountered, append to error string with details.
                        errorStr += "UN Recongnized Token \"" + lines.get(i).substring(startChar, endChar).trim()
                                + "\" at line " + (i + 1) + " at pos " + (startChar + 1) + "\n";
                        break; // Stop processing further tokens due to error.
                    } else {
                        // Record the position of the token.
                        tokenLine.add("at line " + (i + 1) + " at pos " + (startChar + 1));
                        // Extract the token string and trim any whitespace.
                        stringVal.add(lines.get(i).substring(startChar, endChar).trim());

                        // If the token is an identifier, check if it's a reserved keyword.
                        if (currentToken == TokenType.IDENTIFIER &&
                                reservedWords.contains(lines.get(i).substring(startChar, endChar).toLowerCase())) {
                            // Assign the appropriate reserved word token type.
                            tokenType.add(lines.get(i).substring(startChar, endChar).toUpperCase().trim());
                        } else {
                            // Assign the detected token type.
                            tokenType.add("" + currentToken);
                        }
                    }
                    state = StateType.START; // Reset state to START for next token.
                }
                j++; // Move to the next character.
            }
            i++; // Move to the next line.

            if (error)
                break; // Stop scanning if an error was encountered.

            // After the last line, check if a comment was left unterminated.
            if ((i == lines.size() - 1) && (state == StateType.INCOMMENT))
                errorStr += "UNTERMINATED COMMENT at line " + commentStartLine + " at pos " + (startChar + 1) + "\n";
        }

        // Prepare the content string to display tokens and any errors encountered.
        String content;
        if (!errorStr.isEmpty())
            content = "Error : " + errorStr + "\n\n" + "String Value \t Token Type"
                    + "\n------------       ----------\n";
        else
            content = "String Value \t Token Type" + "\n------------       ----------\n";

        // Append all tokens and their types to the content string.
        for (i = 0; i < tokenType.size(); i++) {
            content += stringVal.get(i) + "\t\t\t   " + tokenType.get(i) + '\n';
        }

        return content; // Return the formatted token list string.
    }

}
