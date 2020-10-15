package es.weso.ontoloci.worker.validation;

import cats.effect.IO;
import es.weso.rdf.RDFReader;
import es.weso.rdf.jena.RDFAsJenaModel;
import es.weso.rdf.nodes.IRI;
import es.weso.shapeMaps.ResultShapeMap;
import es.weso.shapeMaps.ShapeMap;
import es.weso.shex.ResolvedSchema;
import es.weso.shex.Schema;
import es.weso.shex.validator.Validator;
import es.weso.utils.eitherios.EitherIOUtils;
import scala.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * Redefines shexsjava library (https://github.com/weso/shexsjava)
 * 
 * @author Jose Emilio Labra Gayo
 *
 */
public class Validate {

	Logger log = Logger.getLogger(Validate.class.getName());

	// none object is required to pass no base
	Option<IRI> none = Option.empty();
	Option<RDFReader> noneRDF = Option.empty();
	
	/**
	 * This method performs the validation given the data as a String. Uses an
	 * ontology for the validation. Pass this parameter as an empty string in order
	 * not to use an ontology. This method doesn't support different formats for the
	 * inputs. Returns the result as a ShapeMap.
	 * 
	 * @param dataStr     RDF data in Turtle syntax
	 * @param ontologyStr Ontology in Turtle syntax (or empty string to not use an
	 *                    ontology)
	 * @param schemaStr   Schema in ShEx syntax
	 * @param shapeMapStr ShapeMap in ShapeMap syntax
	 * @return ResultShapeMap Validation result as a ShapeMap
	 */
	public IO<ResultShapeMap> validateStr(String dataStr, String ontologyStr, String schemaStr, String shapeMapStr) {
		return readRDFStr(dataStr, "TURTLE").flatMap(rdfData -> readRDFStr(ontologyStr, "TURTLE")
				.flatMap(ontologyData -> rdfData.merge(ontologyData).flatMap(merged -> Schema
						.fromString(schemaStr, "SHEXC", none, noneRDF)
						.flatMap(schema -> EitherIOUtils
								.eitherStr2IO(ShapeMap.fromString(shapeMapStr, "Compact", none, merged.getPrefixMap(),
										schema.prefixMap()))
								.flatMap(shapeMap -> ShapeMap
										.fixShapeMap(shapeMap, merged, merged.getPrefixMap(), schema.prefixMap())
										.flatMap(fixedShapeMap -> ResolvedSchema.resolve(schema, none)
												.flatMap(resolvedSchema -> Validator
														.validate(resolvedSchema, fixedShapeMap, merged)
														.flatMap(result -> result.toResultShapeMap().flatMap(
																resultShapeMap -> IO.pure(resultShapeMap))))))))));
	}

	public IO<ResultValidation> validateStrExpected(String ontologyStr, String dataStr, String schemaStr, String shapeMapStr,String expectedShapeMapStr) {
		return readRDFStr(dataStr, "TURTLE").flatMap(rdfData -> readRDFStr(ontologyStr, "TURTLE")
				.flatMap(ontologyData -> rdfData.merge(ontologyData).flatMap(merged -> Schema
						.fromString(schemaStr, "SHEXC", none, noneRDF)
						.flatMap(schema -> EitherIOUtils
								.eitherStr2IO(ShapeMap.fromString(shapeMapStr, "Compact", none, merged.getPrefixMap(),
										schema.prefixMap()))
								.flatMap(shapeMap -> ShapeMap
										.fixShapeMap(shapeMap, merged, merged.getPrefixMap(), schema.prefixMap())
										.flatMap(fixedShapeMap -> ResolvedSchema.resolve(schema, none).flatMap(
												resolvedSchema ->
														EitherIOUtils.eitherStr2IO(
																ShapeMap.fromString(expectedShapeMapStr, "Compact", none, merged.getPrefixMap(), schema.prefixMap()))
												.flatMap( expectedShapeMap
														-> Validator.validate(resolvedSchema, fixedShapeMap, merged)
														.flatMap(result -> result.toResultShapeMap().flatMap(
																resultShapeMap -> IO.pure(
																		new ResultValidation(resultShapeMap,expectedShapeMap)
																)))))))))));
	}



	public IO<RDFAsJenaModel> readRDFStr(String str, String format) {
		return RDFAsJenaModel.fromChars(str, format, none).handleErrorWith(
				e -> IO.raiseError(new RuntimeException("Cannot parse RDF from str: " + str + ":" + e.getMessage())));
	}

	public IO<String> getContents(String fileName) {
		try {
			Path path = Paths.get(fileName);
			List<String> lines = Files.readAllLines(path);
			String str = String.join("\n", lines);
			return IO.apply(() -> str);
		} catch (IOException e) {
			return IO.raiseError(e);
		}
	}

}