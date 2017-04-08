package sol_5pecia1;

import com.github.javaparser.ast.body.MethodDeclaration;

import sol_5pecia1.parser.methodinformation.ThesisSequenceVisitorImplements;


/**
 * java 파일에서 단순 sequence 얻기
 * @author sol
 *
 */
public class GetSequence extends UseIOUtils{
	@Override
	MethodDeclaration start() {
		MethodDeclaration node = super.start();
		
		ThesisSequenceVisitorImplements thesisSequenceVisitorImplements = new ThesisSequenceVisitorImplements();
		thesisSequenceVisitorImplements.visit(node, null);
		System.out.println("comment : ");
		System.out.println(thesisSequenceVisitorImplements .getComment());
		System.out.println("Sequences : ");
		thesisSequenceVisitorImplements .getNodeListSequenceAboutThesis().forEach(System.out::println);
		
		return null;
	}
}
