import api.AnalysisService;
import mainPackage.StandardDeviationAnalyzer;
import mainPackage.MedianAnalyzer;
import mainPackage.VarianceAnalyzer;

module implementations {
    requires service;
    exports mainPackage;
    provides AnalysisService with MedianAnalyzer, StandardDeviationAnalyzer, VarianceAnalyzer;
}