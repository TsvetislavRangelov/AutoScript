grammar Binary;

// Parser Rules

start:           (line+) + EOF;

line:		     expr + line_separator | variable | variable_expr | print;

expr:		     opening_bracket expr binary_op expr closing_bracket | 
                 expr binary_op expr |
                 binary_num; // this rule is left recursive


print:           PRINT + variable_name + line_separator;

variable:        variable_name + equality + binary_num + line_separator;

variable_expr:   variable_name + binary_op + variable_name + equality variable_name + line_separator;

line_separator:  LINE_SEPARATOR;

opening_bracket: OPENING_BRACKET; 

closing_bracket: CLOSING_BRACKET; 

variable_name:   VARIABLE_NAME;

binary_op:	     BINARY_OP;

binary_num:      BINARY_NUM;

equality:        EQUALITY; 


// Lexer Rules

BINARY_OP:	    '*' | '/' | '+' | '-' | '~' | 'v' | '<<'; // v = XOR, ~ = NOT

LINE_SEPARATOR: ';';

OPENING_BRACKET: '(';

CLOSING_BRACKET: ')'; 

EQUALITY:        '=';

PRINT:           'print';

VARIABLE_NAME:   [a-z]+; 

BINARY_NUM:	     [0-1]+;

COMMENT:         '//' ~[\r\n]* -> skip;

WHITESPACE:	     [ \t\r\n]+ -> skip;
