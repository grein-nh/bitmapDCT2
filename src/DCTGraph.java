/******************************************************************************/
/********************************GRAPH*****************************************/
/*******************Paint a Graph for a specific dataset***********************/
/******************************************************************************/
/******************************************************************************/

import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class DCTGraph extends JFrame {

  public static void paintGraph (long durationLowPerf, long durationJTransform) {

	  	//Create new chart
	    JFreeChart chart = null;
	    
	    //Create dataset 
	    DefaultCategoryDataset ds = new DefaultCategoryDataset();
	    ds.addValue(durationLowPerf, "Custom","Custom DCT2");
	    ds.addValue(durationJTransform, "Library","JTransform");
	
	    //Instanciate a barChart
	    chart = ChartFactory.createBarChart("", "Time execution comparison", "BarChart", ds, PlotOrientation.VERTICAL, true, true, false);
	
	    CategoryPlot plot = (CategoryPlot) chart.getPlot();                    
	
	    //Declare logarithmic Y axis
	    LogarithmicAxis axis = new LogarithmicAxis("Time (microseconds)");
	    axis.setAutoRange(true);
	    axis.setAllowNegativesFlag(true);
	    plot.setRangeAxis(axis);
	    
	    //Show chart frame
	    ChartFrame chartFrame = new ChartFrame("Time Execution Table", chart);
	    chartFrame.setVisible(true);
	    chartFrame.setSize(800, 650);

    }

}