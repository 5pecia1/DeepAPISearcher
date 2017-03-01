package data;

public interface SearchModel {
	
	/**
	 * query에 맞는 {@link data.APINode}값을 통쨰로 리턴한다.
	 * 
	 * @param query
	 * @return
	 */
	APINode searchAPI(String query);
}
