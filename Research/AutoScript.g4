grammar AutoScript;

// Parser Rules

// Lexer rules


// Keywords

singleExpression:	

FOR:            'for';

WHILE:          'while';
IF:             'if';
ELSE:           'else';

TYPE:           INTEGER_TYPE |
                STRING_TYPE |
                CHARACTER_TYPE |
                BOOLEAN_TYPE;

INTEGER_TYPE:        'integer';
STRING_TYPE:         'string';
CHARACTER_TYPE:      'character';
BOOLEAN_TYPE:        'boolean';




// Literals
ID:		  [a-z]+[A-Z]?[0-9]?;

LINE_SEPARATOR:   ';';

OPENING_BRACKET:  '(';

CLOSING_BRACKET:  ')';

CARET:            '^';

TILDE:            '~';

LESS_THAN:        '<<';

TIMES:            '*';

SLASH:            '/';

MINUS:            '-';

PLUS:             '+';

EQUALS:           '=';

//type literals

BOOLEAN_LITERAL:	'true' | 'false';
NULL:			'null';
NUMBER_LITERAL:		[0-9]+;
STRING_LITERAL:		('"' ~["]* '"');

fragment CHARACTER_LITERAL: 	'\'' CHAR_SEQUENCE '\'';

fragment CHAR_SEQUENCE: 	CHARACTER+;

fragment CHARACTER: 		~['\\\r\n] | 
				EscapeSequence;		



fragment ESCAPE_SEQUENCE: 	'\\' ['"?abfnrtv\\];
COMMENT:         ';)' ~[\r\n]* -> skip;

WHITESPACE:	     [ \t\r\n]+ -> skip;
