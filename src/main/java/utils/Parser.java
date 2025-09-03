package utils;

import configuration.ConfApp;
import exception.PropertyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private static final String spliterator= ConfApp.get("properties.pairs.spliterator");

    public static Pair<String,String> parseAppProperty(String rawProperty) throws PropertyException {
        String[] arr= rawProperty.split(spliterator);
        if(arr.length!=2){
            throw new PropertyException("property isn't a pair:"+ Arrays.toString(arr));
        }
        return new Pair<>(arr[0],arr[1]);
    }

    public static List<Pair<String,String>> parseAppProperties(String rawProperty) throws PropertyException {
        List<Pair<String,String>> list= new ArrayList<>();
        if (rawProperty != null && !rawProperty.isEmpty()) {
            String[] properties = rawProperty.split(",");
            for (String pair : properties) {
                list.add(Parser.parseAppProperty(pair));
            }
        }
        return list;
    }
}
