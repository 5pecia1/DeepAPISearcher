package sol_5pecia1.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

/**
 * 하나의 메서드를 논문형식으로 Parsing한 String 및 List를 전달한다.
 * @author sol
 *
 */
public abstract class ThesisMethodParser {
	public abstract NodeList<Node> getNodeList();
	public abstract String toString();
	
}
