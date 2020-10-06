package es.weso.model;

public class Manifest {

	private String test_name;
	private String ontology;
	private String data;
	private String schema;
	private String in_shape_map;
	private String out_shape_map;

	public Manifest() {
	}

	public Manifest(String test_name, String ontology, String data, String schema, String in_shape_map,
			String out_shape_map) {
		this.test_name = test_name;
		this.ontology = ontology;
		this.data = data;
		this.schema = schema;
		this.in_shape_map = in_shape_map;
		this.out_shape_map = out_shape_map;
	}

	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getIn_shape_map() {
		return in_shape_map;
	}

	public void setIn_shape_map(String in_shape_map) {
		this.in_shape_map = in_shape_map;
	}

	public String getOut_shape_map() {
		return out_shape_map;
	}

	public void setOut_shape_map(String out_shape_map) {
		this.out_shape_map = out_shape_map;
	}

	@Override
	public String toString() {
		return "Manifest [test_name=" + test_name + ", ontology=" + ontology + ", data=" + data + ", schema=" + schema
				+ ", in_shape_map=" + in_shape_map + ", out_shape_map=" + out_shape_map + "]";
	}

}
