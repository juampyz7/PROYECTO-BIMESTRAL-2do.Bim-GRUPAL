package com.streamflow.dao;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AtributosExtra {

    private AtributosExtra() {
    }

    public static String serializar(Map<String, String> atributos) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entrada : atributos.entrySet()) {
            if (builder.length() > 0) {
                builder.append(";");
            }
            builder.append(entrada.getKey()).append("=").append(entrada.getValue());
        }
        return builder.toString();
    }

    public static Map<String, String> deserializar(String texto) {
        Map<String, String> atributos = new LinkedHashMap<>();
        if (texto == null || texto.isBlank()) {
            return atributos;
        }
        for (String par : texto.split(";")) {
            String[] partes = par.split("=", 2);
            if (partes.length == 2) {
                atributos.put(partes[0], partes[1]);
            }
        }
        return atributos;
    }
}
