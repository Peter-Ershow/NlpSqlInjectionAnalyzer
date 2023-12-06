package com.example.nlpsqlinjectionanalyzer.tokenizer;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
@Component
public class SQLSchemaTokenizer {

    public String getEntities(String text) throws IOException {
        // Load the NER model
        TokenNameFinderModel model;

        try (InputStream modelIn = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("en-ner-person.bin")) {

            if (modelIn == null) {
                throw new IllegalArgumentException("File not found!");
            }

            model = new TokenNameFinderModel(modelIn);

        // Create a NameFinder using the model
        NameFinderME nameFinder = new NameFinderME(model);

        // Tokenize the text
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text);

        // Find names
        Span[] nameSpans = nameFinder.find(tokens);

        // Print names
        StringBuilder output = new StringBuilder();
        for (Span s : nameSpans) {
            output.append("Entity: ").append(tokens[s.getStart()]).append("\n");
        }
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
