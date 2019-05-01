# Fontes-Barry_Fowler_Iiljestrand_Marks_423

--Usage--
Open Eclipse Oracle
Open the Following from the tool bar
        file -> import
        on the new window "import"
                git -> projects from git -> clone URL
                enter the URL "https://github.com/biopox/Fontes-Barry_Fowler_Liljestrand_Marks_423.git"
                hit next
                check mark only the master branch
                next -> next -> wait for a little bit -> next -> Finish
On the left tab open Compiler
Open the Following from the tool bar
        Run -> Run Configurations
        Go to the  Arguments tab and click apply once you've added your arguments.
        -t  -a "filepath"\Testfile.txt
        -t for tokens. -a for abstract syntax tree.
On the top tool bar, click the run main button (green right arrow)
On the Console on the bottom screen, re-enter the filepath if prompted

--C Tokens--
auto
break
case
char
const
continue
default
do
double
else
enum
extern
float
for
goto
if
int
long
register
return
short
signed
sizeof
static
struct
switch
typedef
union
unsigned
void
volatile
while
()
{}
[]
;

--Coding Standards--
Java based\n
Use branches for editing\n
Merge branches that work\n
Mostly following Google’s Java style guide: https://google.github.io/styleguide/javaguide.html\n
8 Spaces will be used instead of tabs.\n
Braces will always be used even when the body contains only one line.\n
Braces will be placed on the same line for if, else, for, while, and any other statements which need brackets.\n
Both braces for an empty block can be placed on the same line for conciseness.\n
Identifier names must match the regular expression “\w+”. Only ascii letters and digits.\n
Package names are all lower case.\n
Class names use UpperCamelCase.\n
Method names use lowerCamelCase.\n
Constant names use all capitals with each word separated by an underscore.\n
Parameter and local variable names use lowerCamelCase.\n
Type variable names will use the form used by classes followed by a capital T.\n

--CFlags--
System.out.println("./compiler [-t] [-p] [-s] [-ir] [-ar] [-readIR filename] [-exportIR filename] [-exportAR filename] [-f filename]");
Below is a list of the flags that can be used when running our compiler\n
[-t] This flag will print out the tokens created by the scanner\n
[-p] This flag will print out the parse tree\n
[-s] This flag will print out the symbol table\n
[-ir] This flag will print out the intermediate representation\n
[-ar] This flag will print out the assembly representation\n
[-readIR filename] This flag will read an intermediate representation in from the specified file\n
[-exportIR filename] This flag will export the intermediate representation out to the specified file\n
[-exportAR filename] This flag will export the assembly representation to the specified file\n