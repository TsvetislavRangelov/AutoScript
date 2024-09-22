grammar Binary;

// Parser Rules

start:          (line+) + EOF;

line:		    (expr | assignment | print) + line_separator;

print:          PRINT identifier;

expr:	        binary_num # Number | 
                identifier # Variable  |
		        TILDE right=expr # Negation |
                left=expr operator=SHIFT right=expr # BitShift |				
		        left=expr operator=XOR  right=expr  # ExclusiveOr |
		        OPENING_BRACKET inner=expr CLOSING_BRACKET # Parentheses |
		        left=expr operator=(TIMES | SLASH) right=expr # MultiplicationDivision |
		        left=expr operator=(MINUS | PLUS) right=expr # AdditionSubtraction ; 	  // this rule is left recursive

assignment:      identifier EQUALITY expr;

identifier:      ID;

line_separator:  LINE_SEPARATOR;

opening_bracket: OPENING_BRACKET; 

closing_bracket: CLOSING_BRACKET; 

variable_name:   ID;

binary_num:      BINARY_NUM;


// Lexer Rules

LINE_SEPARATOR:   ';';

OPENING_BRACKET:  '(';

CLOSING_BRACKET:  ')';

XOR:            '^';

TILDE:            '~';

SHIFT:            '<<';

TIMES:             '*';

SLASH:            '/';

MINUS:            '-';

PLUS:             '+';

EQUALITY:         '=';

PRINT:           'print';

ID:              [a-z]+[A-Z]?[0-9]?; 

BINARY_NUM:	     [0-1]+;

COMMENT:         '//' ~[\r\n]* -> skip;

WHITESPACE:	     [ \t\r\n]+ -> skip;
