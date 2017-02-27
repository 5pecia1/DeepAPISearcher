package main;

import data.APINode;

public interface SearchView {
	/**
	 * apiNode의 값을 리스트로 출력해준다.
	 *  
	 * @param apiNode
	 */
	void showAPIList(APINode apiNode);
}
