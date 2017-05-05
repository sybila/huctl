[![Release](https://jitpack.io/v/sybila/huctl.svg)](https://jitpack.io/#sybila/huctl)
[![Build Status](https://travis-ci.org/sybila/huctl.svg?branch=master)](https://travis-ci.org/sybila/huctl)
[![codecov.io](https://codecov.io/github/sybila/huctl/coverage.svg?branch=master)](https://codecov.io/github/sybila/huctl?branch=master)
[![javadoc](https://img.shields.io/badge/docs-javadoc-blue.svg)](http://biodivine.fi.muni.cz/docs/huctl/latest/)
[![License](https://img.shields.io/badge/License-GPL%20v3-blue.svg?style=flat)](https://github.com/sybila/ctl-parser/blob/master/LICENSE.txt)
[![Biodivine](https://img.shields.io/badge/powered_by-biodivine-green.svg)](http://biodivine.fi.muni.cz)

## HUCTLp

HUCTLp is a temporal logic (based on CTL) designed for analysis of dynamical systems. This repository provides a format for representing HUCTLp formulas as text and as JVM objects. The project is written in Kotlin, but should be usable in any common JVM language.

##### Include in project

The code is currently published using [jitpack](https://jitpack.io/#sybila/huctl) so you can use it as a standard maven dependency (you will have to explicitly add the maven repository - for more instructions see our [jitpack](https://jitpack.io/#sybila/huctl) page).

##### Older versions

This repository started as a pure CTL formula parser and normalizer. For anyone interested in the original CTL code, see version [2.2.3](https://github.com/sybila/huctl/releases/tag/2.2.3) which contains both CTL and HUCTLp parsers. Also, some very old versions of BioDivine Model Checker use the version [0.1.0](https://github.com/sybila/huctl/releases/tag/0.1) as a submodule.

### Format description

The formal logic syntax and semantics are described in the article [A Model Checking Approach to Discrete Bifurcation Analysis](https://link.springer.com/chapter/10.1007/978-3-319-48989-6_6). Therefore here we only focus on the technical details of the syntax and differences compared to the version of the logic presented in the original paper.

We start by briefly describing the API of the JVM objects which describe HUCTLp formulas in memory and then show the description of the grammar which is used to represent these formulas in text.
 
### Class API

The library provides several tree-like data structures which directly translate to some part of HUCTLp formula. Namely [Expression](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression), used as an arithmetic expression in floating point propositions, [DirFormula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula) used to restrict the type of allowed paths in temporal operators and [Formula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula) which represents the HUCTLp operator tree.
  
##### Tree structures
  
To provide a common interface for working with the types of trees, we define three interfaces: [TreeNode](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila/-tree-node/), [Unary](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila/-unary) and [Binary](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila/-binary). TreeNode must be implemented by each element in the tree and effectively declares the type of a tree you are dealing with (So if Foo : TreeNode\<Expression> you know Foo can be placed only in the Expression tree). Additionally, Unary (res. Binary) are implemented by nodes which have one (res. two) child nodes. Objects that implement TreeNode, but not Unary or Binary are the atomic propositions, since they can be present only in the tree leaves. Finally, to provide an easy way to transform and traverse trees, we define [fold](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila/fold.html) and [map](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila/map.html). 

##### Expression

Expressions are very simple - they can represent basic arithmetic ([plus](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression/-add), [minus](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression/-sub), [multiplication](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression/-mul), [division](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression/-div)) and as propositions, they use either floating point [constants](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression/-constant) or variable [names](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-expression/-variable) (these are assumed to be part of the analysed model). Once the values for variable names are known, each Expression should be evaluable to an exact floating point number.
 
##### DirFormula

Direction formulas (or DirFormulas for short) are restrictions which can be placed on paths considered by temporal operators (for example, you can force the future operator to only consider increasing paths). They support standard boolean logic ([not](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-not), [and](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-and), [or](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-or), [implies](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-implies), [equals](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-equals)) and as propositions, they use either constants [true](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-true/index.html), [false](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-false), [loop](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-loop) (loop is a special predicate satisfied by the self-loop transitions) or the so called [direction propositions](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-proposition/index.html) which specify a variable name (from the model) and a [direction](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-direction/index.html) (increase or decrease). Finally, you can also use special [text](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-dir-formula/-text/index.html) propositions, which don't have any defined semantics, but you can use them to extend the logic with your own functionality.
  
##### Formula

Formulas are the full HUCTLp formula trees. They support standard boolean logic ([not](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-not/index.html), [and](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-and/index.html), [or](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-or/index.html), [implies](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-implies/index.html), [equals](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-equals/index.html)) as well as temporal ([next](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-next/index.html), [weak next](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-weak-next/index.html), [future](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-future/index.html), [weak future](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-weak-future/index.html), [globally](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-globally/index.html), [until](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-until/index.html)) and hybrid ([forall](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-for-all/index.html), [exists](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-exists/index.html), [bind](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-bind/index.html), [at](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-at/index.html)) operators. Furthermore, each temporal formula has a [path quantifier](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-path-quantifier/index.html) (exists, all, past exists, past all) and an optional direction constraint (defaults to true). We don't implement until operators with two direction constraints, instead, the parser translates them to an equivalent combination of until and next. Finally, the proposition can be either a constant ([true](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-true/index.html), [false](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-false/index.html)), a [numeric comparison](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-numeric/index.html) (a [comparison](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-compare-op/index.html) of two expressions), a [transition existence predicate](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-transition/index.html) (variable name, [direction](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-direction/index.html) and [flow type](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-flow/index.html)) or the special [text](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-text/index.html) proposition. Finally, you can also use a special [reference](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/-reference/index.html) proposition which is used together with the hybrid operators to create formulas that hold in state with specified name.

##### DSL

Finally, the package `com.sybila.github.huctl.dcl` contains a set of useful functions for creating HUCTLp properties directly in code. Using these functions, you can write code such as 

```
val f = Bind("x", in = Bind("z", AX("z".toReference())), target = AF("x".toReference()))
```

(This specific formula is a very convoluted way of saying 'all paths from this state lead to a sink')

### `.ctl` text format

You can also represent HUCTLp formulas as plain text and parse them directly into classes described above. In general, we call this the `.ctl` text format, because usually it is read from a file with the ctl extension, but you are free to provide it also as a String.

The `.ctl` format currently supports:
 
 - **Aliases** You can define an alias for any type (expression, direction formula or formula) simply by writing `my_alias = ...`. The order of declaration is irrelevant, but you can't use cyclic aliases and each alias usage has to follow the type rules (you can't use an expression where formula is expected, etc.). The only exception are formulas which are valid as both direction and full HUCTLp formulas (for example "True && False"). These are automatically cast to the appropriate type when used.
 - **Includes** You can use an inlcude statement `:include 'path/to/file.ctl''` to import all declarations from the specified file. Each file is resolved only once (so you don't have to worry about cyclic or duplicate includes).
 - **Flagged aliases** Using a special `:?` prefix, you can make any alias "flagged". This has no formal semantics, but this flag usually marks formulas which are in some way interesting for the user (for example which need to be printed in the result)

#### Parser API

The library defines several functions for parsing full HUCTLp files, single formulas, single direction formulas and single expressions. Each function has a standard and extension variant. Standard functions start with `read` and extension functions follow the `to` convention. Specifically, you can use [readHUCTLp](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/read-h-u-c-t-lp.html), [readFormula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/read-formula.html), [readDirFormula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/read-dir-formula.html), [readExpression](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/read-expression.html) and their extension counterparts [toHUCTLp](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/kotlin.-string/to-h-u-c-t-lp.html), [toFormula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/kotlin.-string/to-formula.html), [toDirFormula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/kotlin.-string/to-dir-formula.html) and [toExpression](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl.parser/kotlin.-string/to-expression.html).

Note that all toString methods should produce output which can be correctly parsed back to an semantically equal object. So for example `f == f.toString().toFormula()` where `f` is a [Formula](http://biodivine.fi.muni.cz/docs/huctl/3.0.0/com.github.sybila.huctl/-formula/index.html). 

#### Grammar

We describe the text format using an Antlr-like grammar syntax. The full Antlr grammar is also [available in this repository](https://github.com/sybila/huctl/blob/master/src/main/antlr/com/github/sybila/huctl/antlr/HUCTL.g4).

 - `.ctl` supports standard `#` and `//` single line comments and nested (similar to Swift or Kotlin) `/*` `*/` multi line comments.
 - Tokens used by our grammar definition are
 
   - `NAME` Standard variable identifier (alphanumeric string with _ which has to start with a letter).
   - `STRING` A string of text surrounded by either ' or ". Note that our strings don't support any escaping! 
   - `FLOAT` A floating point number in the standard decimal notation. The scientific notation, i.e. `2.2e10`, is not supported.
   - `TRUE` is either `true`, `True` or `tt`. Similar for `FALSE`.
   - `PATH` is one of the `E`, `A`, `pE`, `pA`.
    
 - All operators follow standard priority and associativity rules (implication and until are right associative, add/sub has lower priority than mul/div, unary operators have higher priority than binary operators, etc.). 

 - Each HUCTLp file contains a non-negative number of new line or semicolon separated statements, where each statement is either an include or an (optionally flagged) alias:
 
```
file : (statement (';'|'\r?\n'))*
statement : ':include' STRING 
          | ':?'? NAME '=' (NAME | formula | dirFormula | expression)
```

 - Each expression is either a name (alias or model variable name), constant, expression in parentheses, or some arithmetic combination of two expressions:
   
```
expression : NAME | FLOAT | '(' expression ')' 
           | expression ('+' | '-' | '*' | '/') expression
```

 - Each direction formula is either an alias name, one of the constants, a text proposition, a direction proposition, a direction formula in parentheses or a boolean combination of direction formulas:
```
dirFormula : NAME | TRUE | FALSE | LOOP | STRING | '(' dirFormula ')' | '!' dirFormula
           | dirFormula ('&&' | '||' | '->' | '<->') dirFormula
```

 - Finally, each formula is either an alias name, true, false, a text proposition, numeric proposition, transition predicate, formula in parentheses, boolean combination of formulas or application of some temporal or hybrid operators:
 
```
formula : NAME | TRUE | FALSE | STRING
        | expression ('>' | '>=' | '<' | '<=' | '==' | '!=') expression
        | NAME ':' ('in' | 'out') ('+' | '-')
        | '(' formula ')' | '!' formula
        | formula ('&&' | '||' | '->' | '<->') formula
        | (modifier)? PATH ( ('X' | 'F' | 'G' | 'wX' | 'wF')) formula
        | formula modifier? PATH 'U' modifier? formula
        | ('forall' | 'exists') NAME ('in' formula)? ':' formula
        | ('bind' | 'at') NAME ':' formula

```
 - The `modifier` is the direction restriction imposed on the runs considered by the temporal operator. It is a standard `dirFormula` enclosed in `{` `}`.
 
Syntax usage example:

```
:include "propositions.ctl"

:? reach_x_increasing = EX {v1+}EF prop // up is defined in propositions.ctl

stable = bind x: AX AF x
:? all_reach_stable = forall s in stable : AF s
 
sum = v1 + v2 + v3
prop = sum > 0 pEU sum == 0
```