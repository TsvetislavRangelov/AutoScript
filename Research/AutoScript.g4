grammar AutoScript;

// Parser Rules

entry: ((singleExpression | assignmentExpression | collectionAssignment | ifStatement | whileStatement | forStatement | arrowFunction) + LINE_SEPARATOR)+ | EOF;

singleExpression:   NUMBER_LITERAL # Number |
                    STRING_LITERAL # String |
                    CHARACTER_LITERAL # Character |
                    BOOLEAN_LITERAL # Boolean |
                    ID # Variable  |
		            TILDE right=singleExpression # Negation |
		            left=singleExpression operator=CARET  right=singleExpression  # ExclusiveOr |
					left=singleExpression operator=LESS_THAN  right=singleExpression  # LessThan |
					left=singleExpression operator=GREATER_THAN  right=singleExpression  # Greater_Than |
		            OPENING_BRACKET inner=singleExpression CLOSING_BRACKET # Parentheses |
		            left=singleExpression operator=(TIMES | SLASH) right=singleExpression # MultiplicationDivision |
		            left=singleExpression operator=(MINUS | PLUS) right=singleExpression # AdditionSubtraction ; 	  // this rule is left recursive

assignmentExpression: TYPE? ID EQUALS (singleExpression);

collectionAssignment: TYPE '[]' ID EQUALS ('[' (NUMBER_LITERAL (COMMA NUMBER_LITERAL)* | STRING_LITERAL (COMMA STRING_LITERAL)* | CHARACTER_LITERAL (COMMA CHARACTER_LITERAL)* | BOOLEAN_LITERAL (COMMA BOOLEAN_LITERAL)*) ']' | '[]');


ifStatement: 		IF OPENING_BRACKET condition CLOSING_BRACKET 
						OPENING_CURLY_BRACKET body+ CLOSING_CURLY_BRACKET
					(ELSE IF OPENING_BRACKET condition CLOSING_BRACKET
						OPENING_CURLY_BRACKET body+ CLOSING_CURLY_BRACKET)*
					(ELSE OPENING_CURLY_BRACKET body+ CLOSING_CURLY_BRACKET)?;


whileStatement: 	WHILE OPENING_BRACKET condition CLOSING_BRACKET 
					OPENING_CURLY_BRACKET body+ CLOSING_CURLY_BRACKET;	

forStatement: 		FOR OPENING_BRACKET assignmentExpression COMMA condition COMMA (singleExpression| assignmentExpression)
					CLOSING_BRACKET OPENING_CURLY_BRACKET body+ CLOSING_CURLY_BRACKET;

body: (singleExpression | assignmentExpression | ifStatement | whileStatement | forStatement) LINE_SEPARATOR; 

condition:      ID # ConditionID| 
				singleExpression #ConditionExpr|
				BOOLEAN_LITERAL #ConditionBoolean|
				OPENING_BRACKET inner=condition CLOSING_BRACKET #ConditionBrackets|
				left=condition operator=AND right=condition # ConditionAnd |
				left=condition operator=OR right=condition  # ConditionOr |
				left=condition operator=STRICT_EQUALS right=condition # ConditionStrictEqual |
				left=condition operator=NOT_EQUALS right=condition # ConditionNotEqual; 

arrowFunction: CONST ID ':' (TYPE | VOID) EQUALS OPENING_BRACKET paramSequence? CLOSING_BRACKET ARROW 
				OPENING_CURLY_BRACKET body+ CLOSING_CURLY_BRACKET;


paramSequence: param (COMMA param)*;

param: 		TYPE ID; 

// Lexer rules
// Keywords

FOR:            'for';

WHILE:          'while';

BREAK: 			'break';

RETURN: 		'return';

VOID: 			'void';

IF:             'if';

ELSE:           'else';

CONST: 			'const';

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

OPENING_CURLY_BRACKET: '{';

CLOSING_CURLY_BRACKET: '}';

CARET:            '^';

TILDE:            '~';

LESS_THAN:        '<';

GREATER_THAN:     '>';

TIMES:            '*';

SLASH:            '/';

MINUS:            '-';

PLUS:             '+';

EQUALS:           '=';

STRICT_EQUALS:    '==';

NOT_EQUALS: 	  '!=';

AND: 			  '&&';

OR: 			  '||';

ARROW: 			  '=>';

COMMA: 			  ',';


//Type literals

BOOLEAN_LITERAL:	'true' | 'false';

NULL:			    'null';

NUMBER_LITERAL:		[0-9]+;

STRING_LITERAL:		('"' ~["]* '"') | ESCAPE_SEQUENCE;

CHARACTER_LITERAL: '\'' CHARACTER '\'';

fragment CHARACTER:    ~['\\\r\n] | ESCAPE_SEQUENCE;		

fragment ESCAPE_SEQUENCE: 	'\\' ['"?abfnrtv\\];

COMMENT:         ':)' ~[\r\n]* -> skip;

ID:		 [a-zA-Z][a-zA-Z0-9]*;

WHITESPACE:	     [ \t\r\n]+ -> skip;