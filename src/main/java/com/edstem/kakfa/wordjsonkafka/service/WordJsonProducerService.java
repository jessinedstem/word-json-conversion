package com.edstem.kakfa.wordjsonkafka.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WordJsonProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String kafkaTopic = "word_json_converter";

    public void convertWordToJson(MultipartFile file) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();

        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                Map<String, String> rowData = new HashMap<>();
                rowData.put("line", paragraph.getText());
                data.add(rowData);
            }
        }

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> rowData = data.get(i);
            jsonBuilder.append("  {");
            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                jsonBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\", ");
            }
            jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
            jsonBuilder.append("}");
            if (i < data.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }
        jsonBuilder.append("\n]");

        String json = jsonBuilder.toString();

        Message<String> message = MessageBuilder
                .withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopic)
                .build();
        kafkaTemplate.send(message);
    }
}
