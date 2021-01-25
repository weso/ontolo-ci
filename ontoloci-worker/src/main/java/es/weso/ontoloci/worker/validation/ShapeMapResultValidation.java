package es.weso.ontoloci.worker.validation;

import java.util.UUID;

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
    private PrefixedNode nodePrefix;
    private PrefixedNode shapePrefix;

    /**
     * Default constructor. Needed for the json parsers
     */
    public ShapeMapResultValidation(){}


    /**
     * Parametrized constructor
     *
     * @param node
     * @param shape
     * @param status
     * @param info
     * @param reason
     * @param nodePrefix
     * @param shapePrefix
     */
    public ShapeMapResultValidation(String node, String shape, String status, String info, String reason, PrefixedNode nodePrefix, PrefixedNode shapePrefix) {
        this.node = node;
        this.shape = shape;
        this.status = status;
        this.reason = reason;
        this.appInfo = info;
        this.nodePrefix = nodePrefix;
        this.shapePrefix = shapePrefix;
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

    /**
     * Gets the node prefix
     *
     * @return node prefix
     */
    public PrefixedNode getNodePrefix() { return nodePrefix; }

    /**
     * Sets the node prefix
     *
     * @param  nodePrefix
     */
    public void setNodePrefix(PrefixedNode nodePrefix) { this.nodePrefix = nodePrefix; }

    /**
     * Gets the shape prefix
     *
     * @return shape prefix
     */
    public PrefixedNode getShapePrefix() { return shapePrefix; }

    /**
     * Sets the shape prefix
     *
     * @param  shapePrefix
     */
    public void setShapePrefix(PrefixedNode shapePrefix) { this.shapePrefix = shapePrefix; }

    @Override
    public String toString() {
        return "ShapeMapResultValidation{" +
                "node='" + node + '\'' +
                ", shape='" + shape + '\'' +
                ", status='" + status + '\'' +
                ", appInfo='" + appInfo + '\'' +
                ", reason='" + reason + '\'' +
                ", nodePrefix=" + nodePrefix +
                ", shapePrefix=" + shapePrefix +
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


    /**
     * Returns an instance of this class as a processable json for the frontend.
     *
     * @return ShapeMapResultValidation as a json
     */
    public String toJson() {
        return "{ "+
                "\"node\":\""+nodePrefix.getPrefixedNode()+"\","+
                "\"shape\":\""+shapePrefix.getPrefixedNode()+"\","+
                "\"status\":\""+status+"\","+
                "\"appInfo\":\""+appInfo+"\","+
                "\"reason\":\""+reason+"\""+
            "}";
    }
}