import java.io.IOException;
import java.lang.reflect.Type;

public class Parser {
   private Lexer lex;
   private Token tok;

   public Parser(Lexer lex) throws IOException {
      this.lex = lex;
      this.tok = lex.getToken();
   }

   private void error(Token.T[] expected) {
      StringBuilder b = new StringBuilder();
      if (expected.length > 0) {
         b.append(expected[0]);
         for (int i = 1; i < expected.length; i++)
            b.append(", ").append(expected[i]);
      }
      ErrorMsg.error(tok.line, tok.col, "syntax error", "expecting " + b.toString() + ", found " + tok.type);
      System.exit(3);
   }

   private void advance() throws IOException {
      tok = lex.getToken();
   }

   private void eat(Token.T t) throws IOException {
      if (tok.type == t)
         advance();
      else
         error(new Token.T[]{t});
   }

   private void S() throws IOException {
      switch (tok.type) {
         case INT:
            Program();
            eat(Token.T.END);
            break;
         case BOOL:
            Program();
            eat(Token.T.END);
            break;
         default:
            error(new Token.T[]{Token.T.INT, Token.T.BOOL});
      }
   }

   private void Program() throws IOException {
      switch (tok.type) {
         case INT:
            Funs();
            break;
         case BOOL:
            Funs();
            break;
         default:
            error(new Token.T[]{Token.T.INT, Token.T.BOOL});
      }
   }

   private void Funs() throws IOException {
      switch (tok.type) {
         case INT:
            Fun();
            FunsLine();
            break;
         case BOOL:
            Fun();
            FunsLine();
            break;
         default:
            error(new Token.T[]{Token.T.INT, Token.T.BOOL});
      }
   }

   private void FunsLine() throws IOException {
      switch (tok.type) {
         case END:
            break;
         case INT:
            Funs();
            break;
         case BOOL:
            Funs();
            break;
         default:
            error(new Token.T[]{Token.T.END, Token.T.INT, Token.T.BOOL});
      }
   }

   private void Fun() throws IOException {
      switch (tok.type) {
         case INT:
            TypeId();
            eat(Token.T.LPAREN);
            TypeIds();
            eat(Token.T.RPAREN);
            eat(Token.T.EQ);
            Exp();
            break;
         case BOOL:
            TypeId();
            eat(Token.T.LPAREN);
            TypeIds();
            eat(Token.T.RPAREN);
            eat(Token.T.EQ);
            Exp();
            break;
         default:
            error(new Token.T[]{Token.T.INT, Token.T.BOOL});
      }
   }

   private void TypeId() throws IOException {
      switch (tok.type) {
         case INT:
            eat(Token.T.INT);
            eat(Token.T.ID);
            break;
         case BOOL:
            eat(Token.T.BOOL);
            eat(Token.T.ID);
            break;
         default:
            error(new Token.T[]{Token.T.INT, Token.T.BOOL});
      }
   }

   private void TypeIds() throws IOException {
      switch (tok.type) {
         case INT:
            TypeId();
            TypeIdsLine();
            break;
         case BOOL:
            TypeId();
            TypeIdsLine();
            break;
         default:
            error(new Token.T[]{Token.T.INT, Token.T.BOOL});
      }
   }

   private void TypeIdsLine() throws IOException {
      switch (tok.type) {
         case RPAREN:
            break;
         case COMMA:
            eat(Token.T.COMMA);
            TypeIds();
            break;
         default:
            error(new Token.T[]{Token.T.RPAREN, Token.T.COMMA});
      }
   }

   private void Exp() throws IOException {
      switch (tok.type) {
         case ID:
            A();
            ExpLine();
            break;
         case IF:
            eat(Token.T.IF);
            Exp();
            eat(Token.T.THEN);
            Exp();
            eat(Token.T.ELSE);
            Exp();
            break;
         case LET:
            eat(Token.T.LET);
            eat(Token.T.ID);
            eat(Token.T.EQ);
            Exp();
            eat(Token.T.IN);
            Exp();
            break;
         case NUM:
            A();
            ExpLine();
            break;
         default:
            error(new Token.T[]{Token.T.ID, Token.T.IF, Token.T.LET, Token.T.NUM});
      }
   }

   private void ExpLine() throws IOException {
      switch (tok.type) {
         case END:
            break;
         case RPAREN:
            break;
         case INT:
            break;
         case BOOL:
            break;
         case COMMA:
            break;
         case THEN:
            break;
         case ELSE:
            break;
         case IN:
            break;
         case LT:
            eat(Token.T.LT);
            A();
            break;
         default:
            error(new Token.T[]{Token.T.END, Token.T.RPAREN, Token.T.INT, Token.T.BOOL, Token.T.COMMA, Token.T.THEN, Token.T.ELSE, Token.T.IN, Token.T.LT});
      }
   }

   private void A() throws IOException {
      switch (tok.type) {
         case ID:
            B();
            ALine();
            break;
         case NUM:
            B();
            ALine();
            break;
         default:
            error(new Token.T[]{Token.T.ID, Token.T.NUM});
      }
   }

   private void ALine() throws IOException {
      switch (tok.type) {
         case END:
            break;
         case RPAREN:
            break;
         case INT:
            break;
         case BOOL:
            break;
         case COMMA:
            break;
         case THEN:
            break;
         case ELSE:
            break;
         case IN:
            break;
         case LT:
            break;
         case PLUS:
            eat(Token.T.PLUS);
            B();
            ALine();
            break;
         default:
            error(new Token.T[]{Token.T.END, Token.T.RPAREN, Token.T.INT, Token.T.BOOL, Token.T.COMMA, Token.T.THEN, Token.T.ELSE, Token.T.IN, Token.T.LT, Token.T.PLUS});
      }
   }

   private void B() throws IOException {
      switch (tok.type) {
         case ID:
            eat(Token.T.ID);
            BLine();
            break;
         case NUM:
            eat(Token.T.NUM);
            break;
         default:
            error(new Token.T[]{Token.T.ID, Token.T.NUM});
      }
   }

   private void BLine() throws IOException {
      switch (tok.type) {
         case END:
            break;
         case LPAREN:
            eat(Token.T.LPAREN);
            Exps();
            eat(Token.T.RPAREN);
            break;
         case RPAREN:
            break;
         case INT:
            break;
         case BOOL:
            break;
         case COMMA:
            break;
         case THEN:
            break;
         case ELSE:
            break;
         case IN:
            break;
         case LT:
            break;
         case PLUS:
            break;
         default:
            error(new Token.T[]{Token.T.END, Token.T.LPAREN, Token.T.RPAREN, Token.T.INT, Token.T.BOOL, Token.T.COMMA, Token.T.THEN, Token.T.ELSE, Token.T.IN, Token.T.LT, Token.T.PLUS});
      }
   }

   private void Exps() throws IOException {
      switch (tok.type) {
         case ID:
            Exp();
            ExpsLine();
            break;
         case IF:
            Exp();
            ExpsLine();
            break;
         case LET:
            Exp();
            ExpsLine();
            break;
         case NUM:
            Exp();
            ExpsLine();
            break;
         default:
            error(new Token.T[]{Token.T.ID, Token.T.IF, Token.T.LET, Token.T.NUM});
      }
   }

   private void ExpsLine() throws IOException {
      switch (tok.type) {
         case RPAREN:
            break;
         case COMMA:
            eat(Token.T.COMMA);
            Exps();
            break;
         default:
            error(new Token.T[]{Token.T.RPAREN, Token.T.COMMA});
      }
   }

   public void parse() throws IOException {
      S();
      eat(Token.T.EOF);
   }
}
