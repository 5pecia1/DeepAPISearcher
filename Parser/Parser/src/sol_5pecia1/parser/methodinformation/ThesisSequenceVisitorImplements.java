package sol_5pecia1.parser.methodinformation;

import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * DeepAPILearning에 나오는 API usage sequence 대로 리스트를 만들고 해당 메서드의 코멘트를 가져옴.
 * <br>
 * {@link ThesisSequenceVisitorImplements#visit(com.github.javaparser.ast.body.MethodDeclaration, Void)}을 호출한 다음
 * {@link ThesisSequenceVisitorImplements#getNodeListSequenceAboutThesis()}을 호출하면 원하는 값을 얻을 수 있다.
 * <br>
 * 필요할 경우 상속받아 사용하되 <code>visit()</code>을 override 할 때 때 <code>super.visit()</code>은 
 * 위에 선언해야 하는지 아래에 선언해야하는지 잘 확인하고 선언한다. 대부분의 경우 위에 선언한다.
 * <br>
 * <p style="color:red">하나의 객체는 하나의 메서드 시퀸스만을 가진다.</p>
 * @author sol
 *
 */
public class ThesisSequenceVisitorImplements extends VoidVisitorAdapter<Void>{
	private LinkedList<Node> nodeList = new LinkedList<>();
	private JavadocComment comment = null;
	
	public List<Node> getNodeListSequenceAboutThesis() {
		return nodeList;
	}
	
	public JavadocComment getComment() {
		return comment;
	}
	
	@Override
	public void visit(JavadocComment n, Void arg) {
		comment = n;
		super.visit(n, arg);
	}
	
	@Override
	public void visit(ObjectCreationExpr n, Void arg) {
		super.visit(n, arg);
		nodeList.add(n);
	}
	
	@Override
	public void visit(MethodCallExpr n, Void arg) {
		super.visit(n, arg);
		nodeList.add(n);
	}
	
	@Override
	public void visit(WhileStmt n, Void arg) {
		n.getCondition().accept(this, arg);
		n.getBody().accept(this, arg);
		n.getComment().ifPresent((l) -> {
			l.accept(this, arg);
		});
	}
	
	@Override
	public void visit(ForeachStmt n, Void arg) {
		n.getIterable().accept(this, arg);
		n.getBody().accept(this, arg);
		n.getVariable().accept(this, arg);
		n.getComment().ifPresent((l) -> {
			l.accept(this, arg);
		});
	}
	@Override
	public void visit(ForStmt n, Void arg) {
		n.getInitialization().forEach((p) -> {
			p.accept(this, arg);
		});
		
		n.getBody().accept(this, arg);
		
		n.getUpdate().forEach((p) -> {
			p.accept(this, arg);
		});
		n.getCompare().ifPresent((l) -> {
			l.accept(this, arg);
		});
		n.getComment().ifPresent((l) -> {
			l.accept(this, arg);
		});
	}
}
