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
 * DeepAPILearning�� ������ API usage sequence ��� ����Ʈ�� ����� �ش� �޼����� �ڸ�Ʈ�� ������.
 * <br>
 * {@link ThesisSequenceVisitorImplements#visit(com.github.javaparser.ast.body.MethodDeclaration, Void)}�� ȣ���� ����
 * {@link ThesisSequenceVisitorImplements#getNodeListSequenceAboutThesis()}�� ȣ���ϸ� ���ϴ� ���� ���� �� �ִ�.
 * <br>
 * �ʿ��� ��� ��ӹ޾� ����ϵ� <code>visit()</code>�� override �� �� �� <code>super.visit()</code>�� 
 * ���� �����ؾ� �ϴ��� �Ʒ��� �����ؾ��ϴ��� �� Ȯ���ϰ� �����Ѵ�. ��κ��� ��� ���� �����Ѵ�.
 * <br>
 * <p style="color:red">�ϳ��� ��ü�� �ϳ��� �޼��� ���������� ������.</p>
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
