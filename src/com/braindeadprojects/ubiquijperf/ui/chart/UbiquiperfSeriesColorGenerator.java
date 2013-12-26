/**
 * - 02/2008: Class created by Nicolas Richasse
 * 
 * Changelog:
 * 	- class created
 * 
 * To do:
 * 	- add more colors
 */

package com.braindeadprojects.ubiquijperf.ui.chart;

import java.awt.Color;

public class UbiquiperfSeriesColorGenerator 
{
	private static Color[] allColors = {new Color(0x0066FF), Color.red, Color.orange, Color.blue, Color.gray, Color.magenta, Color.white, Color.orange};
	private static int currentIndex = 0;
	
	public static Color nextColor()
	{
		Color res = allColors[currentIndex];
		currentIndex = (currentIndex+1)%allColors.length;
		return res;
	}
	
	public static void reset()
	{
		currentIndex = 0;
	}
}
