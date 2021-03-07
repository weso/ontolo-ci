package es.weso.ontoloci.worker.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.weso.ontoloci.worker.test.TestCaseResult;
import es.weso.ontoloci.worker.test.TestCaseResultStatus;
import es.weso.ontoloci.worker.validation.ShapeMapResultValidation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MarkdownUtils {


    public static String getMarkDownFromTests(Collection<TestCaseResult> tests){
        String md = "";
        for(TestCaseResult test : tests){
            String name = test.getTestCase().getName().replace("\n", "");
            String capitalizeName = name.substring(0, 1).toUpperCase() + name.substring(1);
            String executionTime = test.getMetadata().get("execution_time").replace("\n", "");
            String testResult = getTestCaseResult(test);

            md +="### "+capitalizeName+ " ( "+executionTime+" ) "+testResult+"\\n";
            md += "| Result   | Data Node | Shape | Status  | Expected Status \\n";
            md += "|----------|-----------|----------|-------|-------\\n";

            List<ShapeMapResultValidation> produced = MarkdownUtils.getResults(test.getMetadata().get("produced"));
            List<ShapeMapResultValidation> expected = MarkdownUtils.getResults(test.getMetadata().get("expected"));

            for(int i=0;i<produced.size();i++){
                String dataNode = produced.get(i).getNode();
                String shape = produced.get(i).getShape();
                String status = produced.get(i).getStatus();
                String expectedStatus = expected.get(i).getStatus();
                String result = ":x:";
                if(status.equals(expectedStatus))
                    result = ":heavy_check_mark:";
                md+="| "+result+" | "+dataNode+" | "+shape+" | "+status+" | "+expectedStatus+" \\n";
            }

        }
        return md;
    }

    public static String getTestCaseResult(TestCaseResult test){
        TestCaseResultStatus status = test.getStatus();
        if(status.equals(TestCaseResultStatus.SUCCESS))
            return ":heavy_check_mark:";
        else if (status.equals(TestCaseResultStatus.FAILURE))
            return ":x:";
        return ":warning:";
    }


    public static  List<ShapeMapResultValidation> getResults(String json){
        ObjectMapper jsonMapper  = new ObjectMapper(new JsonFactory());
        try {
            return  Arrays.asList(jsonMapper.readValue(json, ShapeMapResultValidation[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
