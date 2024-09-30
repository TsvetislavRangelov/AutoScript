grammar AutoScript;

// Parser Rules


entry: ((singleExpression | assignmentExpression) + LINE_SEPARATOR)+ | EOF; 

assignmentExpression: TYPE ID EQUALS (NUMBER_LITERAL | STRING_LITERAL | CHARACTER_LITERAL | BOOLEAN_LITERAL); 

singleExpression:   NUMBER_LITERAL # Number | 
                    ID # Variable  |
		            TILDE right=singleExpression # Negation |
		            left=singleExpression operator=CARET  right=singleExpression  # ExclusiveOr |
		            OPENING_BRACKET inner=singleExpression CLOSING_BRACKET # Parentheses |
		            left=singleExpression operator=(TIMES | SLASH) right=singleExpression # MultiplicationDivision |
		            left=singleExpression operator=(MINUS | PLUS) right=singleExpression # AdditionSubtraction ; 	  // this rule is left recursive


// Lexer rules
// Keywords

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

LINE_SEPARATOR:   ';';

OPENING_BRACKET:  '(';

CLOSING_BRACKET:  ')';

CARET:            '^';

TILDE:            '~';

LESS_THAN:        '<';

GREATER_THAN:     '>';

TIMES:            '*';

SLASH:            '/';

MINUS:            '-';

PLUS:             '+';

EQUALS:           '=';

//Type literals

BOOLEAN_LITERAL:	'true' | 'false';

NULL:			    'null';

NUMBER_LITERAL:		[0-9]+;

STRING_LITERAL:		('"' ~["]* '"') | ESCAPE_SEQUENCE;

CHARACTER_LITERAL: '\'' CHAR_SEQUENCE '\'';

fragment CHAR_SEQUENCE: 	CHARACTER+;

fragment CHARACTER:    ~['\\\r\n] | ESCAPE_SEQUENCE;		

fragment ESCAPE_SEQUENCE: 	'\\' ['"?abfnrtv\\];

COMMENT:         ':)' ~[\r\n]* -> skip;

ID:		 [a-zA-Z][a-zA-Z0-9]*;


WHITESPACE:	     [ \t\r\n]+ -> skip;