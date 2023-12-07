package com.example.nlpsqlinjectionanalyzer.tokenizer;

import com.example.nlpsqlinjectionanalyzer.client.RestClientService;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Component
public class SQLSchemaTokenizer {

    @Autowired
    private RestClientService restClientService;

    private static final Logger logger = LoggerFactory.getLogger(SQLSchemaTokenizer.class);

    public String getEntities(String text){
        String outputNames = generateEntities(text,"en-ner-person.bin");

        String outputLocations  = generateEntities(text, "en-ner-location.bin");

        String outputOrg  = generateEntities(text, "en-ner-organization.bin");

        String outputDates  = generateEntities(text, "en-ner-date.bin");

        return outputLocations +
                System.lineSeparator() +
                outputNames +
                System.lineSeparator() +
                outputOrg +
                System.lineSeparator() +
                outputDates;
    }

    private String generateEntities(String text, String file) {
        TokenNameFinderModel model;
        try (InputStream modelIn = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(file)) {

            if (modelIn == null) {
                throw new IllegalArgumentException("File not found!");
            }

            model = new TokenNameFinderModel(modelIn);

            // create a NameFinder using the model
            NameFinderME nameFinder = new NameFinderME(model);

            // tokenize the text
            String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text);
            Span[] nameSpans = nameFinder.find(tokens);

            // remove duplicates
            Set<Span> distinctSpans = new TreeSet<>(Span::compareTo);
            distinctSpans.addAll(Arrays.asList(nameSpans));
            Span[] distinctNameSpans = distinctSpans.toArray(new Span[0]);

            // print names
            StringBuilder output = new StringBuilder();
            output.append("Empty URL: ").append(runCalls(""));
            for (Span s : distinctNameSpans) {
                String possibleInput = tokens[s.getStart()].replaceAll(" ", "").toLowerCase();
                String result = runCalls(possibleInput);
                if(!result.isEmpty()) {
                    output.append(result).append(", Next Call: ");
                }
            }

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String runCalls(String text){
        try {
            logger.info("Doing call for " + text);
            return restClientService.runQuery(text);
        }
        catch (Exception e){
            return "";
        }
    }
}
