package mainPackage;


import api.AnalysisException;
import api.AnalysisService;
import api.DataSet;

import java.util.ArrayList;
import java.util.Collections;

public class MedianAnalyzer implements AnalysisService {

    private int numbers = 0;
    private DataSet dataSet = null;


    @Override
    public void setOptions(String[] strings) throws AnalysisException {
        if (strings.length == 0) {
            throw new AnalysisException("Parameter is needed!");
        } else if (Integer.parseInt(strings[0]) == 0) {
            numbers = 0;
        } else {
            numbers = Integer.parseInt(strings[0]);
        }
    }

    @Override
    public String getName() {
        return "Median";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        int rows = ds.getData().length;
        int columns = ds.getData()[0].length;
        ArrayList<ArrayList<Double>> list = new ArrayList<>();

        if (numbers != 0)
            columns = numbers;

        for (int i = 0; i < rows; i++) {
            list.add(new ArrayList<>());
            for (int j = 0; j < columns; j++) {
                list.get(i).add(Double.parseDouble(ds.getData()[i][j]));
            }
            Collections.sort(list.get(i));
        }

        String[][] result = new String[rows][1];

        if (columns % 2 == 0) {
            for (int i = 0; i < rows; i++) {
                result[i][0] = Double.toString((list.get(i).get(columns / 2) + list.get(i).get(columns / 2 - 1)) / 2);
            }
        } else {
            for (int i = 0; i < rows; i++) {
                result[i][0] = list.get(i).get(columns / 2).toString();
            }
        }

        DataSet dataSet = new DataSet();
        dataSet.setData(result);
        this.dataSet = dataSet;
        this.dataSet.setHeader(new String[]{this.getName()});
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        if (dataSet == null) {
            throw new AnalysisException("There is no result, because nothing was submitted.");
        }
        if (clear == true) {
            DataSet newSet = new DataSet();
            newSet.setData(dataSet.getData());
            newSet.setHeader(dataSet.getHeader());
            dataSet = null;
            return newSet;
        } else
            return dataSet;
    }
}
