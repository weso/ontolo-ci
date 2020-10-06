package es.weso.ontology.test;

/**
 * Models a test case result. It is composed by a node, a node and a status.
 * 
 * @author Pablo Menéndez
 *
 */
public class ResultCase {

	private String node;
	private String shape;
	private String status;

	/**
	 * Default constructor. Takes the node, shape and status value of the result
	 * validation.
	 * 
	 * @param node
	 * @param shape
	 * @param status
	 */
	public ResultCase(String node, String shape, String status) {
		this.node = node;
		this.shape = shape;
		this.status = status;
	}

	/**
	 * Gets the node
	 * 
	 * @return node
	 */
	public String getNode() {
		return node;
	}

	/**
	 * Gets the shape
	 * 
	 * @return shape
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * Gets the status
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "ResultCase [node=" + node + ", shape=" + shape + ", status=" + status + "]";
	}

	/**
	 * This method compares two ResultCase by its node and Shape. Returns true if
	 * the node and the shape are the same in both cases. False otherwise.
	 * 
	 * @param restultCcase
	 * @return true/false
	 */
	public boolean compareNode(ResultCase r) {
		if (getNode().equals(r.getNode()) && getShape().equals(r.getShape())) {
			return true;
		}
		return false;
	}

	/**
	 * This method compares two ResultCase by its status. Returns true if the status
	 * is the same in both cases. False otherwise.
	 * 
	 * @param restultCcase
	 * @return true/false
	 */
	@Override
	public boolean equals(Object r) {
		return this.getStatus().equals(((ResultCase) r).getStatus());
	}

}
