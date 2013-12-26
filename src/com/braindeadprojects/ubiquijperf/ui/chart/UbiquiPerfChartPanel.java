package com.braindeadprojects.ubiquijperf.ui.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.renderer.xy.XYSmoothLineAndShapeRenderer;

import net.nlanr.jperf.core.Measurement;
import net.nlanr.jperf.ui.chart.IPerfChartPanel;
import net.nlanr.jperf.ui.chart.SeriesColorGenerator;



public class UbiquiPerfChartPanel extends IPerfChartPanel
{

	/**
	 * 
	 */
	public UbiquiPerfChartPanel(String title, String bandwidthUnit, String jitterUnit, String timeAxisLabel, String bandwidthValueAxisLabel, String jitterValueAxisLabel, double delayInSeconds, double timeWindow, double reportInterval, Color backgroundColor, Color foregroundColor,
			Color gridColor)
	{
		super(title, bandwidthUnit, jitterUnit, timeAxisLabel, bandwidthValueAxisLabel, jitterValueAxisLabel, delayInSeconds, timeWindow, reportInterval, backgroundColor, foregroundColor, gridColor);
	}
	
	/**
	 * Alternate version of reconfigure, used to set Ubiquiperf specific labeling and font.
	 */
	public void reconfigure(boolean isServerMode, String title, String bandwidthUnit, String jitterUnit, String timeAxisLabel, String bandwidthValueAxisLabel, String jitterValueAxisLabel, double timeWindow, double reportInterval)
	{
		this.isServerMode = isServerMode;
		this.bandwidthUnit = bandwidthUnit;
		this.jitterUnit = jitterUnit;
		this.timeWindow = timeWindow;
		this.reportInterval = reportInterval;
		
		// reset the content pane
		this.removeAll();
		panelTextStats.removeAll();
		SeriesColorGenerator.reset();

		seriesData.clear();

		// creation of the chart group
		graphSet = new CombinedDomainXYPlot(new NumberAxis(timeAxisLabel));
		// space between charts
		graphSet.setGap(10.0);
		// creation of the jFreeChart
		
		jFreeChart = new JFreeChart(null, new Font("Serto Malankara", Font.BOLD, 26), graphSet, false);
		jFreeChart.setBackgroundPaint(backgroundColor);
		if (title != null)
		{
			//@note: Diff this
			TextTitle MyTextTitle = new TextTitle(title,new Font("Serto Malankara", Font.BOLD, 26)); 
			jFreeChart.setTitle(MyTextTitle);
			jFreeChart.getTitle().setPaint(foregroundColor);
		}
		// creation of the chart panel
		chartPanel = new ChartPanel(jFreeChart);
		chartPanel.setBackground(backgroundColor);
		// creation of the series set
		bandwidthCollection = new XYSeriesCollection();
		
		// creation of the renderer
		// We want lines, but not dots.
		//bandwidthRenderer = new XYLineAndShapeRenderer(true, false);
		bandwidthRenderer = new XYSplineRenderer();
		//bandwidthRenderer = new XYSmoothLineAndShapeRenderer();
		
		
		
		// set the UI presentation
		NumberAxis rangeAxis = new NumberAxis(bandwidthValueAxisLabel);
		rangeAxis.setLabelPaint(foregroundColor);

		// creation of the bandwidth plot
		XYPlot bandwidthPlot = new XYPlot(bandwidthCollection, null, rangeAxis, bandwidthRenderer);
		bandwidthPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		bandwidthPlot.setDomainCrosshairVisible(false);
		bandwidthPlot.setRangeCrosshairVisible(false);
		bandwidthPlot.setBackgroundPaint(backgroundColor);
		bandwidthPlot.setDomainGridlinePaint(gridColor);
		bandwidthPlot.setRangeGridlinePaint(gridColor);
		
		// add the plot
		graphSet.add(bandwidthPlot, proportion);

		// set up the domain axis
		ValueAxis axis = bandwidthPlot.getDomainAxis();
		if (timeAxisLabel != null)
		{
			axis.setTickLabelPaint(foregroundColor);
			axis.setLabel(timeAxisLabel);
			axis.setLabelPaint(foregroundColor);
		}
		else
		{
			axis.setVisible(false);
		}
		axis.setAutoRange(true);

		//Setting the timeWindow to 60
		//@todo: Also allow it to scroll using CTRL mouse
		axis.setFixedAutoRange((int)Math.min(timeWindow, 600)); 

		// set up the range axis
		axis = bandwidthPlot.getRangeAxis();
		axis.setTickLabelPaint(foregroundColor);

		if (isServerMode)
		{
			rangeAxis = new NumberAxis(jitterValueAxisLabel);
			rangeAxis.setLabelPaint(foregroundColor);

			// creation of the jitter plot
			jitterRenderer = new XYLineAndShapeRenderer();
			jitterCollection = new XYSeriesCollection();
			XYPlot jitterPlot = new XYPlot(jitterCollection, null, rangeAxis, jitterRenderer);
			jitterPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
			jitterPlot.setDomainCrosshairVisible(false);
			jitterPlot.setRangeCrosshairVisible(false);
			jitterPlot.setBackgroundPaint(backgroundColor);
			jitterPlot.setDomainGridlinePaint(gridColor);
			jitterPlot.setRangeGridlinePaint(gridColor);

			// add the plot
			graphSet.add(jitterPlot, proportion);

			// set up the range axis
			axis = jitterPlot.getRangeAxis();
			axis.setTickLabelPaint(foregroundColor);
		}

		// set up the hour&date label presentation
		labelDate.setHorizontalAlignment(JLabel.RIGHT);
		labelDate.setForeground(foregroundColor);

		panelTextStats.setBackground(backgroundColor);

		this.add(labelDate, BorderLayout.NORTH);
		this.add(chartPanel, BorderLayout.CENTER);
		this.add(panelTextStats, BorderLayout.SOUTH);

		this.setBackground(backgroundColor);
	}

	//Extended method
	public void addSeriesBandwidthMeasurement(String seriesId, Measurement measurement)
	{
		SeriesData data = seriesData.get(seriesId);
		
		if (measurement.getEndTime()-measurement.getStartTime() > reportInterval)
		{
			// this is the sum-up of the test
			data.seriesLabel.setText(String.format("<html><b>%s</b> [" + data.printfBandwidthValueExpression + "%s] </html>", data.bandwidthLegend, measurement.getValue(), measurement.getUnits() + "/s"));
			return;
		}
		
		if (isServerMode)
		{
			try
			{
				if (data.bandwidthSeries.getDataItem((int)measurement.getEndTime()) != null)
				{
					// clear the series
					data.bandwidthSeries.clear();
				}
			}
			catch(Exception e)
			{
				// nothing
			}
		}
		data.bandwidthSeries.add(measurement.getEndTime(), measurement.getValue());
		//data.seriesLabel.setText(String.format("<html><b>%s</b> " + data.printfBandwidthValueExpression + "%s </html>", data.bandwidthLegend, measurement.getValue(), bandwidthUnit+"/s"));
		data.seriesLabel.setText(String.format("<html><b>%s</b> " + data.printfBandwidthValueExpression + "%s </html>", data.bandwidthLegend, measurement.getValue(), measurement.getUnits()+"/s"));
	}
	
	
}