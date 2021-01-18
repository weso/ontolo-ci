package es.weso.ontoloci.worker.validation;

/**
 * Represents a node with it´s prefix
 * @author Pablo Menéndez
 */
public class PrefixedNode {

    private String qname;
    private String iri;
    private String node;

    /**
     * Default constructor
     */
    public PrefixedNode() {
    }

    /**
     * Constructor with fields
     *
     * @param qname     prefix alias
     * @param iri       prefix iri
     * @param node      node
     */
    public PrefixedNode(String qname, String iri, String node) {
        this.qname = qname;
        this.iri = iri;
        this.node = node;
    }

    /**
     * Gets the qname
     * @return qname as a string
     */
    public String getQname() {
        return qname;
    }

    /**
     * Sets the qname
     * @param qname as a string
     */
    public void setQname(String qname) {
        this.qname = qname;
    }

    /**
     * Gets the iri
     * @return iri as a string
     */
    public String getIri() {
        return iri;
    }

    /**
     * Sets the iri
     * @param iri as a string
     */
    public void setIri(String iri) {
        this.iri = iri;
    }

    /**
     * Gets the node
     * @return node as a string
     */
    public String getNode() {
        return node;
    }

    /**
     * Sets the node
     * @param node as a string
     */
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * Gets the representation of the node with it´s prefix qname
     * @return prefixed node
     */
    public String getPrefixedNode(){
        return this.qname+':'+this.node;
    }


    @Override
    public String toString() {
        return "PrefixedNode{" +
                "qname='" + qname + '\'' +
                ", iri='" + iri + '\'' +
                ", node='" + node + '\'' +
                '}';
    }
}
