package BotBinance;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParameterStringBuilder {
    public static String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
       if(result.charAt(result.length()-1)=='&') { result.deleteCharAt(result.length()-1);  }
       
System.out.println(result);
        String resultString = result.toString();
        
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() )
          : resultString;
    }
}