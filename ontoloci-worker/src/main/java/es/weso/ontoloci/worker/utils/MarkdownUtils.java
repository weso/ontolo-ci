package es.weso.ontoloci.worker.utils;

import es.weso.ontoloci.worker.test.TestCaseResult;

import java.util.Collection;
import java.util.List;

public class MarkdownUtils {

    /**
     * DESACTUALIZADO
     *
     * Ahora puede haber más de un expected y produced status
     * ya que puede ser que para un test haya más de una shape en el shapemap
     *


    public static String getMarkDownFromTests(Collection<TestCaseResult> tests){
        String md = "| Result   | Test Name |Data Node | Shape | Status | Time  \\n";
        md+= "|----------|-----------|----------|-------|--------|----------\\n";
        for(TestCaseResult test : tests){
            String result = ":x:";
            if(test.getStatus().getValue().equals("pass")){
                result = ":heavy_check_mark:";
            }
            String name = test.getTestCase().getName().replace("\n", "");
            String dataNode = test.getTestCase().getExpectedShapeMap().split("@")[0].replace("\n", "");
            String shape = test.getTestCase().getExpectedShapeMap().split("@")[1].replace("\n", "");
            //String status = test.getMetadata().get("validation_status").replace("\n", "");
            String status = "conformant";
            String executionTime = test.getMetadata().get("execution_time").replace("\n", "");
            md+="| "+result+" | "+name+" | "+dataNode+" | "+shape+" | "+status+" | "+executionTime+" \\n";
        }
        return md;
    }

     */

    public static String getMarkDownFromTests(Collection<TestCaseResult> tests){
        return "markdown-off";
    }

}
