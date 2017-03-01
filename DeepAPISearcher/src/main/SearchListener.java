package main;

public interface SearchListener {
	
	/**
	 * query에 맞는 API를 찾아준다.<br>
	 * 최종적으로 {@link main.SearchView}를 실행한다.
	 * 
	 * @param query
	 */
	void searchAPI(String query);
}
