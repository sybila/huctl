Simple Java CTL parser supporting quantified float propositions.

Parser provides basic interface for porcessing CTL formulae with float propositions. 
Parser accepts a string containing a formula and returns a tree of Formula objects representing the specified formula.
So far, only supported operators are EU, EX, AF and !, && (All other CTL operators can be derived using these).

This project also contains Antlr grammar, that can be used to generate parsers for other programming languages (such as C++).

Formula string syntax:

(spaces are optional, parenthesis are mandatory)

  - Negation: ! Formula
  - And: ( Formula && Formula )
  - Exists-Until: E ( Formula U Formula )
  - Exists-Next: EX Formula
  - All-Future: AF Formula
  - Proposition: { proposition_string }

Proposition string syntax:

variable_name operator float_value

Where variable_name is any nonempty lowercase string, float_value is a positive float with at least one decimal place (f.e. 12.0), and operator is one of these:
==, !=, <, >, <=, >=

Example of a well formed formula string:

AF E ( { a == 3.0 } U ( EX { b >= 3.14} && !{ c > 12.5 } ) )

You can add this repository directly as a module in IntelliJ IDEA or you can include the jar file provided in the out directory.

Future plans:

  - Support negative floats
  - More CTL operators
  - Formula transformation for purposes of CTL model checking.
