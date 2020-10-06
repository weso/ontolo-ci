package es.weso.model.manifestImpl;

import es.weso.model.Manifest;

public class ManifestPath extends Manifest {

	public ManifestPath() {
		super();
	}

	public ManifestPath(String test_name, String ontology, String data, String schema, String in_shape_map,
			String out_shape_map) {
		super(test_name, ontology, data, schema, in_shape_map, out_shape_map);
	}

}
