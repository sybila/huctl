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
