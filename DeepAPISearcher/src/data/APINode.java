package data;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * api 정보를 담고있는 node.<br>
 * 다음 api 정보를 {@link LinkedList}로 가지고 있다.
 * 
 * @author 5pecia1
 *
 */
public class APINode {
	private final String name;
	private final String information;
	private final LinkedList<APINode> nextAPISequence;
	
	public APINode(String name, String information) {
		this.name = name; 
		this.information = information;
		nextAPISequence = new LinkedList<>();
	}
	
	public APINode(String name, String information, APINode ... nextAPISequences) {
		this(name, information);
		getNextAPISequence().addAll(Arrays.asList(nextAPISequences));
	}

	public String getName() {
		return name;
	}

	public String getInformation() {
		return information;
	}

	public LinkedList<APINode> getNextAPISequence() {
		return nextAPISequence;
	}
	
	
}
