package data;

public interface SearchModel {
	
	/**
	 * query�� �´� {@link data.APINode}���� �뤊�� �����Ѵ�.
	 * 
	 * @param query
	 * @return
	 */
	APINode searchAPI(String query);
}
