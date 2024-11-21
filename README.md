## TINY language Scanner & Parser project

**How to use:**
1. Download the **Executable** folder from the repository.
2. Place the code you want to scan in a input.txt file and place it next to the exe file **main.exe**.
3. Double click the exe file.
4. A new file named **output.txt** will be created containing all the token with their types in the input file.

### Sample Input1:

```
{ Sample program in TINY language - computes factorial }
read x; {input an integer }
if 0 < x then { don't compute if x <= 0 }
fact := 1;
repeat
fact := fact * x;
x := x - 1
until x = 0;
write fact { output factorial of x }
end
```

### Sample Output1:

```
String Value 	     Token Type
------------       ----------
read			         READ
x			             IDENTIFIER
;			             SEMICOLON
if			           IF
0			             NUMBER
<			             LESSTHAN
x			             IDENTIFIER
then			         THEN
fact			         IDENTIFIER
:=			           ASSIGN
1			             NUMBER
;			             SEMICOLON
repeat			       REPEAT
fact			         IDENTIFIER
:=			           ASSIGN
fact			         IDENTIFIER
*			             MULT
x			             IDENTIFIER
;			             SEMICOLON
x			             IDENTIFIER
:=			           ASSIGN
x			             IDENTIFIER
-			             MINUS
1			             NUMBER
until			         UNTIL
x			             IDENTIFIER
=			             EQUAL
0			             NUMBER
;			             SEMICOLON
write			         WRITE
fact			         IDENTIFIER
end			           END

```

### Sample Input2:
```
read x; {input an integer }
if 0 < x@a then { don’t compute if x <= 0 }
fact := 1;
repeat

```

### Sample Output2:
```
Error
------
UN Recongnized Token "x@" at line 1


String Value 	     Token Type
------------       ----------
read			         READ
x			             IDENTIFIER
;			             SEMICOLON
if			           IF
0			             NUMBER
<			             LESSTHAN

```
