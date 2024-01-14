package com.example.nlpsqlinjectionanalyzer.tester;

import com.example.nlpsqlinjectionanalyzer.client.RestClientService;
import com.example.nlpsqlinjectionanalyzer.tokenizer.SQLSchemaTokenizer;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PossibleCallsGeneratorTesterOrganization implements PossibleCallsGeneratorTester {

    @Autowired
    private RestClientService restClientService;

    private static final Logger logger = LoggerFactory.getLogger(SQLSchemaTokenizer.class);


    @Override
    public String generateAPICallsForOrganization(Span[] spans, String[] tokens) {
        StringBuilder output = new StringBuilder();
        output.append("Empty URL: ").append(runCalls(""));
        for (Span s : spans) {
            String possibleInput = tokens[s.getStart()].replaceAll(" ", "").toLowerCase();
            String result = runCalls(possibleInput);
            if(result != null && !result.isEmpty()) {
                output.append(result).append(", Next Call: ");
            }
        }
        return output.toString();
    }

    @Override
    public String runCalls(String text) {
        try {
            logger.info("Doing call for " + text);
            return restClientService.runQuery("organization/" + text + "/email");
        }
        catch (Exception e){
            return "";
        }
    }
}
