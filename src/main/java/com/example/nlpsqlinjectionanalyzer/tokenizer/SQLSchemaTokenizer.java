package com.example.nlpsqlinjectionanalyzer.tokenizer;

import com.example.nlpsqlinjectionanalyzer.client.RestClientService;
import com.example.nlpsqlinjectionanalyzer.tester.PossibleCallsGeneratorTester;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.*;

@Component
public class SQLSchemaTokenizer {

    @Autowired
    private PossibleCallsGeneratorTester possibleCallsGeneratorTester;

    private static final Logger logger = LoggerFactory.getLogger(SQLSchemaTokenizer.class);

    public String getEntities(String text){
        return generateEntities(text, "en-ner-organization.bin");
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
            output.append(possibleCallsGeneratorTester.generateAPICallsForOrganization(distinctNameSpans, tokens));
            //all other models

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
