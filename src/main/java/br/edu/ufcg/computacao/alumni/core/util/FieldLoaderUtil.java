package br.edu.ufcg.computacao.alumni.core.util;

import org.json.JSONObject;

public class FieldLoaderUtil {
    public static String load(JSONObject obj, String field) {
        String value = null;
        try {
            value = obj.getString(field);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }
}
