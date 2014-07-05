package com.marcomorain.topk;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class App {

    public static void main(String[] args) {

        Map<String, ReferenceTopK<String>> properties = Maps.newHashMap();
        final int k = 15;

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8));
            while (reader.ready()) {
                JSONObject json = new JSONObject(reader.readLine());
                if (!json.has("type")) {
                    continue;
                }
                if (!"user".equals(json.getString("type"))) {
                    continue;
                }
                JSONObject attributes = json.getJSONObject("parameters").getJSONObject("attributes");

                Set<String> keys = attributes.keySet();
                for (String key : keys) {
                    if (!properties.containsKey(key)) {
                        properties.put(key, new ReferenceTopK<String>(k));
                    }
                    properties.get(key).add(attributes.getString(key));
                }
            }
        } catch (IOException | JSONException e) {
            System.out.format("Error: %s%n", e);
        }

        for (Map.Entry<String, ReferenceTopK<String>> entry : properties.entrySet()) {
            System.out.format("%s: %s%n", entry.getKey(), entry.getValue());
        }
    }
}
