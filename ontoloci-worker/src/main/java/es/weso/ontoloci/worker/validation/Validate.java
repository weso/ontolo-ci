package es.weso.ontoloci.worker.validation;

import es.weso.shapemaps.ResultShapeMap;
import java.util.logging.Logger;
import es.weso.shaclex.*;

/**
 * Redefines shexsjava library (https://github.com/weso/shexsjava)
 * 
 * @author Jose Emilio Labra Gayo
 *
 */
public class Validate {

	Logger log = Logger.getLogger(Validate.class.getName());

	/**
	 * This method performs the validation given the data as a String. Uses an
	 * ontology for the validation. Pass this parameter as an empty string in order
	 * not to use an ontology. This method doesn't support different formats for the
	 * inputs. Returns the result as a ShapeMap object.
	 * 
	 * @param dataStr     		RDF data in Turtle syntax
	 * @param ontologyStr 		Ontology in Turtle syntax (or empty string in order not to use an ontology)
	 * @param schemaStr   		Schema in ShEx syntax
	 * @param shapeMapStr 		ShapeMap in ShapeMap syntax
	 *
	 * @return Validation result as a ResultShapeMap
	 */
	public ResultShapeMap validateStr(String dataStr, String ontologyStr, String schemaStr, String shapeMapStr) {
		return ShExWrapper.validateStr(dataStr,ontologyStr,schemaStr,shapeMapStr);
	}


	/**
	 * This method performs the validation given the data as a String. Uses an
	 * ontology for the validation. Pass this parameter as an empty string in order
	 * not to use an ontology. This method doesn't support different formats for the
	 * inputs. Returns the result as a ResultValidation object.
	 *
	 * @param ontologyStr 			Ontology in Turtle syntax (or empty string in order not to use an ontology)
	 * @param dataStr     			RDF data in Turtle syntax
	 * @param schemaStr   			Schema in ShEx syntax
	 * @param shapeMapStr 			ShapeMap in ShapeMap syntax
	 * @param expectedShapeMapStr 	Expected ShapeMap in ShapeMap syntax
	 *
	 * @return Validation result as a ResultValidation
	 */
	public ResultValidation validateStrResultValidation(String ontologyStr, String dataStr, String schemaStr, String shapeMapStr,String expectedShapeMapStr) {
		return ShExWrapper.validateStrResultValidation(ontologyStr,dataStr,schemaStr,shapeMapStr,expectedShapeMapStr);
	}

}