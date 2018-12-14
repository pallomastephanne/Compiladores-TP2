/* -*-Mode: java-*- */

%%

%public
%class Lexer
%type Token
%function getToken
%line
%column

%{  
    private Token tok(Token.T typ, Object val) {
        return new Token(typ, val, yyline, yycolumn);
    }

    private Token tok(Token.T typ) {
        return tok(typ, null);
    }
    
    private void error(String msg) {
    	ErrorMsg.error(yyline, yycolumn, "lexical error", msg);
    }
%}

%%

[ \t\f\n\r]+          { /* skip */ }


"bool"               { return tok(Token.T.BOOL); }
"int"                { return tok(Token.T.INT); }
"if"                 { return tok(Token.T.IF); }
"then"               { return tok(Token.T.THEN); }
"else"               { return tok(Token.T.ELSE); }
"let"                { return tok(Token.T.LET); }
"in"                 { return tok(Token.T.IN); }
"$"                  { return tok(Token.T.END); }

"="                   { return tok(Token.T.EQ); }
"<"                   { return tok(Token.T.LT); }
"+"                   { return tok(Token.T.PLUS); }
"("                   { return tok(Token.T.LPAREN); }
")"                   { return tok(Token.T.RPAREN); }
","                   { return tok(Token.T.COMMA); }

[0-9]+                  { return tok(Token.T.NUM, new Long(yytext())); }
[a-zA-Z][a-zA-Z0-9_]*   { return tok(Token.T.ID, new String(yytext())); }

<<EOF>>               { return tok(Token.T.EOF); }

.            { error("illegal character: " + yytext()); }
