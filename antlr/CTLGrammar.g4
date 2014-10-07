/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

grammar CTLGrammar;
formula : NEG formula #Negation
        | EX formula #ExistsFuture
        | AF formula #AllFuture 
        | B_OPEN formula AND formula B_CLOSE #And
        | E B_OPEN formula U formula B_CLOSE #ExistsUntil
        | CB_OPEN BOOLEAN CB_CLOSE # Proposition;
BOOLEAN : 'True' | 'False' ;
NEG : '!';
EX : 'EX';
AF : 'AF';
E : 'E';
U : 'U';
AND : '&&';
B_OPEN : '(';
B_CLOSE : ')';
CB_OPEN : '{';
CB_CLOSE : '}';
