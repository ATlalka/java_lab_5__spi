package mainPackage;

import api.AnalysisException;
import api.AnalysisService;
import api.DataSet;

public class VarianceAnalyzer implements AnalysisService {
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
        return "Variance";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        int rows = ds.getData().length;
        int columns = ds.getData()[0].length;

        if (numbers != 0)
            columns = numbers;

        double[] averages = new double[rows];

        for (int i = 0; i < rows; i++) {
            double count = 0;
            for (int j = 0; j < columns; j++) {
                count += Double.parseDouble(ds.getData()[i][j]);
            }
            averages[i] = count / columns;
        }

        String[][] answers = new String[rows][1];

        for (int i = 0; i < rows; i++) {
            double count = 0;
            for (int j = 0; j < columns; j++) {
                count += Math.pow(Double.parseDouble(ds.getData()[i][j]) - averages[i], 2);
            }
            answers[i][0] = Double.toString(count / columns);
        }

        DataSet dataSet = new DataSet();
        dataSet.setData(answers);
        this.dataSet = dataSet;
        this.dataSet.setHeader(new String[]{this.getName()});
    }

    @Override
    public DataSet retrieve(boolean b) throws AnalysisException {
        if (dataSet == null) {
            throw new AnalysisException("There is no result, because nothing was submitted..");
        }

        if (b == true) {
            DataSet newSet = new DataSet();
            newSet.setData(dataSet.getData());
            newSet.setHeader(dataSet.getHeader());
            dataSet = null;
            return newSet;
        } else
            return dataSet;
    }
}
