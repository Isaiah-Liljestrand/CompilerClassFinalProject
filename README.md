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
        -t for tokens. -a for abstract syntax tree
On the top tool bar, click the run main button (green right arrow)
On the Console on the bottom screen, re-enter the filepath if prompted

--Basic C items that where not implemented--
global varriables
<, > <=, >= in boolean logic statements
non-int varriables

--Interesting features--
Passes all pramaters to functions on the stack.
Num constants are auto mathed when possible.
Assigning 0 is done with xor.
When all registers are full, the stack and last two registers are used to accomplish everything until another register is free.


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
All "backend" varriables are protected when reasonable for ease of coding.\n
