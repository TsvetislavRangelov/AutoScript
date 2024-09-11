grammar Binary;

// Parser Rules

line:		expr+ EOF;

expr:		expr binary_op expr | INTEGER;

binary_op:	BINARY_OP;	

//Lexer Rules

BINARY_OP:	'*' | '/' | '+' | '-' | '~' | '&&' | 'v' | '<<'; // v = XOR, ~ = NOT
INTEGER:	[0-9]+;

WHITESPACE:	[ \t\r\n]+ -> skip;
