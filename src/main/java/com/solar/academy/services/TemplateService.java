package com.solar.academy.services;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateService {

    public <T> String processTemplate(String template, T dto) {
        Map<String, String> placeholders = getPlaceholders(dto);

        String result = Stream.of(template.split("\n"))
                .map(line -> replacePlaceholders(line, placeholders))
                .collect(Collectors.joining("\n"));

        return result;
    }

    private <T> Map<String, String> getPlaceholders(T dto) {
        return Stream.of(dto.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toMap(
                        field -> "${" + field.getName() + "}",
                        field -> {
                            try {
                                return String.valueOf(field.get(dto));
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                ));
    }

    private String replacePlaceholders(String line, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            line = line.replace(entry.getKey(), entry.getValue());
        }
        return line;
    }
}