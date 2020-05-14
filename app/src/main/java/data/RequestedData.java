package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import db.State;

public class RequestedData {


    public static HashMap<String, String> statesMap = new HashMap<>();
    public static List<State> stateList = new ArrayList<>();
    public static TreeMap<String, String> dataMapNames = new TreeMap<>();
    public static int numberOfActions = 0;

    static {


        dataMapNames.put("positive", "Positive");
        dataMapNames.put("negative", "Negative");
        dataMapNames.put("hospitalizedCurrently", "Hospitalized currently");
        dataMapNames.put("hospitalizedCumulative", "Hospitalized Cumulative");
        dataMapNames.put("onVentilatorCurrently", "On ventilator currently");
        dataMapNames.put("onVentilatorCumulative", "On ventilator cumulative");
        dataMapNames.put("pending", "Pending");
        dataMapNames.put("recovered", "Recovered");
        dataMapNames.put("death", "Death");
        dataMapNames.put("totalTestResults", "Total test results");
        dataMapNames.put("hospitalized", "Hospitalized");
        dataMapNames.put("inIcuCurrently", "In ICU currently");
        dataMapNames.put("inIcuCumulative", "In ICU cumulative");


    }


}
