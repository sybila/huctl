Simple Java CTL Parser.

###API

- FormulaParser - Parses a file or string into object representing a formula.
- FormulaNormalizer - Normalizes given formula to the form that can be used for model checking.

###Input Syntax
```
#define propositionName variableName <= floatValue
#property A ((True => AF proposotion) U EX False)
```

- operators (temporal and boolean) should be separated by whitespace (wrong: AFEX, UEX, !AX)
- binary operators have to be enclosed in brackets: (f && g), (f => g), E(f U g), A(f U g)
- all standard boolean and CTL operators are supported except reversed implication (<=): 
!, &&, ||, =>, <=>, AX, EX, AF, EF, AG, EG, AU, EU
- all standard float comparison operators are supported: <,>,<=,>=,==,!=
- boolean literals are accepted as proposition names: True, False
- variable and proposition names cannot contain digits

###Building and using

Whole repository is a gradle project, so you can include it into an existing gradle workspace.

To build this project, you'll need antlr4 installed.

Alternatively, you can build whole project into a jar file (located in build/lib).
- use 'gradle fullJar' to create jar with all required dependencies
- use 'gradle jar' to create jar without dependencies (antlr needs to be included at runtime)
