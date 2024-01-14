package com.example.nlpsqlinjectionanalyzer.tester;


import opennlp.tools.util.Span;
import org.springframework.stereotype.Service;

public interface PossibleCallsGeneratorTester {

    String generateAPICallsForOrganization(Span[] spans, String[] tokens);

    String runCalls(String text);
}
