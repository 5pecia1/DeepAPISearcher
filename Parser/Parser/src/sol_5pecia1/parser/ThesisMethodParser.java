package sol_5pecia1.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

/**
 * �ϳ��� �޼��带 ���������� Parsing�� String �� List�� �����Ѵ�.
 * @author sol
 *
 */
public abstract class ThesisMethodParser {
	public abstract NodeList<Node> getNodeList();
	public abstract String toString();
	
}
