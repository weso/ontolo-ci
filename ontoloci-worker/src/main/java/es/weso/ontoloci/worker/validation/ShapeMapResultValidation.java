package es.weso.ontoloci.worker.validation;
/**
 * Models a test case result. It is composed by a node, a node and a status.
 *
 * @author Pablo Menéndez
 *
 */
public class ShapeMapResultValidation {

    private String node;
    private String shape;
    private String status;
    private String appInfo;
    private String reason;

    public ShapeMapResultValidation(){}

    /**
     * Default constructor. Takes the node, shape and status value of the result
     * validation.
     *
     * @param node
     * @param shape
     * @param status
     */
    public ShapeMapResultValidation(String node, String shape, String status,String appInfo,String reason) {
        this.node = node;
        this.shape = shape;
        this.status = status;
        this.appInfo = appInfo;
        this.reason = reason;
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

    /**
     * Gets the appInfo
     *
     * @return status
     */
    public String getAppInfo() {
        return appInfo;
    }

    /**
     * Gets the reason
     *
     * @return status
     */
    public String getReason() {
        return reason;
    }


    @Override
    public String toString() {
        return "ShapeMapResultValidation{" +
                "node='" + node + '\'' +
                ", shape='" + shape + '\'' +
                ", status='" + status + '\'' +
                ", appInfo='" + appInfo + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    /**
     * This method compares two ResultCase by its node and Shape. Returns true if
     * the node and the shape are the same in both cases. False otherwise.
     *
     * @param r ShapeMapResultValidation
     * @return true/false
     */
    public boolean compareNode(ShapeMapResultValidation r) {
        if (getNode().equals(r.getNode()) && getShape().equals(r.getShape())) {
            return true;
        }
        return false;
    }

    /**
     * This method compares two ResultCase by its status. Returns true if the status
     * is the same in both cases. False otherwise.
     *
     * @param r ShapeMapResultValidation
     * @return true/false
     */
    @Override
    public boolean equals(Object r) {
        return this.getStatus().equals(((ShapeMapResultValidation) r).getStatus());
    }

}