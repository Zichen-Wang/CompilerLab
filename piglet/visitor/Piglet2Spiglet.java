//
// Generated by JTB 1.3.2
//

package piglet.visitor;
import piglet.syntaxtree.*;
import java.util.*;
import piglet.piglet2spiglet.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class Piglet2Spiglet extends GJDepthFirst<String,String> {

   private boolean isMOVE = false, allowInteger = false;
	
   /*
    * 修改遍历的部分
    */
   //构造参数表的时候需要
   public String visit(NodeListOptional n, String argu) {
      String _ret="";
      if ( n.present() ) {
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
             _ret += e.nextElement().accept(this,argu);
      }
      return _ret;
   }
   
   //f0 -> ( ( Label() )? Stmt() )*的时候用，打印label

   public String visit(NodeOptional n, String argu) {
      if ( n.present() )
         PrintSpiglet.print(n.node.accept(this,argu));
      return null;
   }


   //
   // User-generated visitor methods below
   //
   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public String visit(Goal n, String argu) {
	  PrintSpiglet.pMain();
	  n.f1.accept(this, "MAIN");
	  PrintSpiglet.pEnd();
	  n.f3.accept(this, argu);
      return null;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public String visit(StmtList n, String argu) {
      n.f0.accept(this, argu);
      return null;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public String visit(Procedure n, String argu) {
	  String procedureName = n.f0.f0.toString();
	  PrintSpiglet.println(procedureName + " [" + n.f2.f0.toString() + "]");
      PrintSpiglet.pBegin();
      String _ret = n.f4.accept(this, procedureName);
      PrintSpiglet.pReturn();
      PrintSpiglet.println(_ret);
      PrintSpiglet.pEnd();
      return null;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public String visit(Stmt n, String argu) {
      PrintSpiglet.println(n.f0.accept(this, argu));
      return null;
   }

   /**
    * f0 -> "NOOP"
    */
   public String visit(NoOpStmt n, String argu) {
	  return "NOOP";
   }

   /**
    * f0 -> "ERROR"
    */
   public String visit(ErrorStmt n, String argu) {
	  return "ERROR";
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Exp()
    * f2 -> Label()
    */
   public String visit(CJumpStmt n, String argu) {
      return "CJUMP " + n.f1.accept(this, argu) + n.f2.f0.toString();
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public String visit(JumpStmt n, String argu) {
      return "JUMP " + n.f1.f0.toString();
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Exp()
    * f2 -> IntegerLiteral()
    * f3 -> Exp()
    */
   public String visit(HStoreStmt n, String argu) {
      return "HSTORE " + n.f1.accept(this, argu) + n.f2.accept(this, argu) + n.f3.accept(this, argu);
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Exp()
    * f3 -> IntegerLiteral()
    */
   public String visit(HLoadStmt n, String argu) {
      return "HLOAD " + n.f1.accept(this, argu) + n.f2.accept(this, argu) + n.f3.accept(this, argu);
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public String visit(MoveStmt n, String argu) {
	  isMOVE = true;
      return "MOVE " + n.f1.accept(this, argu) + n.f2.accept(this, argu);
   }

   /**
    * f0 -> "PRINT"
    * f1 -> Exp()
    */
   public String visit(PrintStmt n, String argu) {
      allowInteger = true;
      return "PRINT " + n.f1.accept(this, argu);
   }

   /**
    * f0 -> StmtExp()
    *       | Call()
    *       | HAllocate()
    *       | BinOp()
    *       | Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public String visit(Exp n, String argu) {
	  //防止产生冗余
	  if(isMOVE) {
		  isMOVE = false;
		  return n.f0.accept(this, argu);
	  }
	  if(n.f0.which == 5 && allowInteger) {
         allowInteger = false;
	     return n.f0.accept(this, argu);
      }
      allowInteger = false;
	  if(n.f0.which == 4) return n.f0.accept(this, argu);
	  
	  //需要展开表达式
	  int curNum = PrintSpiglet.TmpNum.get(argu);
      PrintSpiglet.TmpNum.put(argu, curNum + 1);
      PrintSpiglet.println("MOVE TEMP " + curNum + " " + n.f0.accept(this, argu));
      return "TEMP " + curNum + " ";
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> Exp()
    * f4 -> "END"
    */
   public String visit(StmtExp n, String argu) {
      n.f1.accept(this, argu);
      allowInteger = true;
      return n.f3.accept(this, argu);
   }

   /**
    * f0 -> "CALL"
    * f1 -> Exp()
    * f2 -> "("
    * f3 -> ( Exp() )*
    * f4 -> ")"
    */
   public String visit(Call n, String argu) {
      allowInteger = true;
      String _ret = "CALL " + n.f1.accept(this, argu);
      allowInteger = true;
      return _ret + "( " + n.f3.accept(this, argu) + ")";
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> Exp()
    */
   public String visit(HAllocate n, String argu) {
      allowInteger = true;
      return "HALLOCATE " + n.f1.accept(this, argu);
   }

   /**
    * f0 -> Operator()
    * f1 -> Exp()
    * f2 -> Exp()
    */
   public String visit(BinOp n, String argu) {
      String curOp = n.f0.accept(this, argu);
      String _ret = curOp + n.f1.accept(this, argu);
      if(curOp.equals("LT ") || curOp.equals("PLUS ")) allowInteger = true;
      return _ret + n.f2.accept(this, argu);
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    */
   public String visit(Operator n, String argu) {
	  String[] _ret = {"LT ", "PLUS ", "MINUS ", "TIMES "};
      return _ret[n.f0.which];
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public String visit(Temp n, String argu) {
      return "TEMP " + n.f1.accept(this, argu);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n, String argu) {
      return n.f0.toString() + " ";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Label n, String argu) {
      return n.f0.toString() + " ";
   }

}
