## TINY language Scanner & Parser project



$\color{red}{Note\ :\ “modules”\ file\ in\ “Executable/jdk-19/lib”\ does\ not\ download\ when\ cloning\ }$
$\color{red}{because\ file\ is\ bigger\ than\ 100MB\ ,\ So\ you\ need\ to\ download\ it\ alone\ to\ be\ downloaded }$




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
READ                  read
IDENTIFIER               x
SEMICOLON                ;
IF                      if
NUMBER                   0
LESS                     <
IDENTIFIER               x
THEN                  then
IDENTIFIER            fact
ASSIGNMENT              :=
NUMBER                   1
SEMICOLON                ;
REPEAT              repeat
IDENTIFIER            fact
ASSIGNMENT              :=
IDENTIFIER            fact
MULT                     *
IDENTIFIER               x
SEMICOLON                ;
IDENTIFIER               x
ASSIGNMENT              :=
IDENTIFIER               x
MINUS                    -
NUMBER                   1
UNTIL                until
IDENTIFIER               x
EQUALS                   =
NUMBER                   0
SEMICOLON                ;
WRITE                write
IDENTIFIER            fact
END                    end

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
