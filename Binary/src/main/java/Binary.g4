grammar Binary;

// Parser Rules

start:     (line+) + EOF;

line:		expr + line_separator;

expr:		expr binary_op expr | INTEGER; // this rule is left recursive

binary_op:	BINARY_OP;	

line_separator: LINE_SEPARATOR;

// Lexer Rules

BINARY_OP:	'*' | '/' | '+' | '-' | '~' | '&&' | 'v' | '<<'; // v = XOR, ~ = NOT
LINE_SEPARATOR: ';';
INTEGER:	[0-9]+;

WHITESPACE:	[ \t\r\n]+ -> skip;
