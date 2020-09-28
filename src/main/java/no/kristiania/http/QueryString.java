package no.kristiania.http;

import java.util.HashMap;
import java.util.Map;

public class QueryString {
    private final Map<String, String> parameters = new HashMap<>();

    public QueryString(String queryString) {
        // String parameter = queryString;
        for (String parameter : queryString.split("&")) {
            int equalPos = parameter.indexOf('='); //index og ser etter hvilke posisjon er = . status (0-5) neste er = (6.)
            String parameterName = parameter.substring(0, equalPos);
            //private String parameterValue; // ikke private final som den ble opprettet som standard
            //private String parameterName; // ikke private final som den ble opprettet som standard
            String parameterValue = parameter.substring(equalPos + 1); //substring som starter på den posisjonen + 1 substring tar det som er etter =
            parameters.put(parameterName, parameterValue); //parameters og legger inn navn og value inn her
        }


    }

    public String getParameter(String parameterName) {
        /*if (parameterName.equals(this.parameterName)) {
            //hvis parameterName er lik parametername så skal value bli vist == sjekker om to objekter er like derfor skal du ha equals
            return parameterValue;
        } else { */
            return parameters.get(parameterName);
        //}

    }
}
