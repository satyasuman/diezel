# Syntax #

## Comments ##

Any // new line
or `/* */` pair

## Tokens ##

```
 < STAR : "`*`" >
 < PLUS : "+" >
 < OPT : "?" >
 < BANG : "&" >
 < SEL : "|" >
 < SEQ : "," >
 < LEFT : "(" >
 < RIGHT : ")" >
 <#LETTER : [ "_", "a"-"z", "A"-"Z"] >
 <#DIGIT : [ "0"-"9"] >
 < IDENTIFIER : < LETTER > (< LETTER > | < DIGIT > )* >

```

# Grammar #

```
Exp ::= Sel()  ( < SEQ > Sel() )*

Sel::=  Bang() ( < SEL > Bang() )*


Bang ::=  Unary() ( < BANG > Unary() )*

Unary ::= ( < LEFT > Exp() < RIGHT >  | Transition()  ) (  < STAR >  | < PLUS >  | < OPT >  )?

Transition::= < IDENTIFIER >
```

# Semantic #

Except for operators, every identifier will represent a unique transition, that has to be described.

## Postfix unary operators ##

> `*` `+` and `?` have the exact same meaning as for the usual regular expressions: meaning:
    * `*` :  zero or more repetition
    * `+` : one or more repetition
    * `?` : optional ( zero or one )

## Binary Operators ##

  * `,` : means `then`. It creates a sequence of items.
  * `|` : means `or`. It creates a multiple choice
  * `&` : means `bang` as it is explosive. It forces the flow to go through all the items, but in no particular order. **cave at** when saying `a&b&c&d&e` there are 120 classes generated already...