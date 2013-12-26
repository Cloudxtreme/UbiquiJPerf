package com.braindeadprojects.ubiquijperf.core;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import com.braindeadprojects.ubiquijperf.ubiquiti.UbntGatherer;
import com.braindeadprojects.ubiquijperf.ui.UbiquiJPerfUI;

import net.nlanr.jperf.core.JperfStreamResult;
import net.nlanr.jperf.core.Measurement;
import net.nlanr.jperf.ui.JPerfWaitWindow;

/**
 * Class to extend nlandr's work and add UBNT specific
 *  
 * @note: Most of this work is extended from the nlandr JPerf codebase
 * @author Matthew Gillespie
 *
 */
public class UbiquiJPerfThread extends Thread {
	
	protected String										command;
	protected Process										process;
	protected Vector<JperfStreamResult>	finalResults;

	protected BufferedReader						input;
	protected BufferedReader						errors;
	
	private boolean										isServerMode;

	private Object waitWindowMutex = new Object();
	private JPerfWaitWindow waitWindow;
	
	
	private UbntGatherer myUbnt;
	protected UbiquiJPerfUI frame;
	
	
	public UbiquiJPerfThread(boolean isServerMode, String command, UbiquiJPerfUI mainframe)
	{
		
		
		this.isServerMode = isServerMode;
		this.command = command;
		this.frame = mainframe;
		this.finalResults = new Vector<JperfStreamResult>();
		this.frame.logMessage(command);
		
		this.myUbnt = new UbntGatherer(frame.getCreds());
	}
	
	public void run()
	{
		try
		{
			frame.setStartedStatus();

			process = Runtime.getRuntime().exec(command);
			
			// read in the output from Iperf
			input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			errors = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String input_line = null;
			while ((input_line = input.readLine()) != null)
			{
				parseLine(input_line);
				frame.logMessage(input_line);
				
				//Grab UBNT stats
				this.myUbnt.grabStats();
			}

			String error_line = null;
			while ((error_line = errors.readLine()) != null)
			{
				frame.logMessage(error_line);
			}

			frame.logMessage("Done.\n");
		}
		catch (Exception e)
		{
			// don't do anything?
			frame.logMessage("\nIperf thread stopped [CAUSE=" + e.getMessage() + "]");
		}
		finally
		{
			quit();
		}
	}
	
	

	public synchronized void quit()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				if (process != null)
				{
					synchronized(waitWindowMutex)
					{
						if (waitWindow != null)
						{
							return;
						}
						waitWindow = new JPerfWaitWindow(frame);
						frame.setEnabled(false);
						waitWindow.setVisible(true);
					}
					Thread t = new Thread()
					{
						public void run()
						{
							process.destroy();
							
							if (!isServerMode)
							{
								try
								{
									process.getInputStream().close();
								}
								catch (Exception e)
								{
									// nothing
								}
				
								try
								{
									process.getOutputStream().close();
								}
								catch (Exception e)
								{
									// nothing
								}
				
								try
								{
									process.getErrorStream().close();
								}
								catch (Exception e)
								{
									// nothing
								}
								
								if (input != null)
								{
									try
									{
										input.close();
									}
									catch (Exception e)
									{
										// nothing
									}
									finally
									{
										input = null;
									}
								}
				
								if (errors != null)
								{
									try
									{
										errors.close();
									}
									catch (Exception e)
									{
										// nothing
									}
									finally
									{
										errors = null;
									}
								}
							}
							
							try
							{
								process.waitFor();
							}
							catch (Exception ie)
							{
								// nothing
							}
				
							process = null;
							
							synchronized(waitWindowMutex)
							{
								waitWindow.setVisible(false);
								waitWindow.dispose();
								waitWindow = null;
								frame.setStoppedStatus();
								frame.setEnabled(true);
							}
						}
					};
					t.setDaemon(true);
					t.start();
				}
			}
		});
	}
	
	
	public void parseLine(String line)
	{
		// only want the actual output lines
		if (line.matches("\\[[ \\d]+\\]\\s*[\\d]+.*"))
		{
			Pattern p = Pattern.compile("[-\\[\\]\\s]+");
			// ok now break up the line into id#, interval, amount transfered, format
			// transferred, bandwidth, and format of bandwidth
			String[] results = p.split(line);

			// get the ID # for the stream
			Integer temp = new Integer(results[1].trim());
			int id = temp.intValue();

			boolean found = false;
			JperfStreamResult streamResult = new JperfStreamResult(id);
			for (int i = 0; i < finalResults.size(); ++i)
			{
				if ((finalResults.elementAt(i)).getID() == id)
				{
					streamResult = finalResults.elementAt(i);
					found = true;
					break;
				}
			}

			if (!found)
			{
				finalResults.add(streamResult);
			}
			// this is TCP or Client UDP
			if (results.length == 9)
			{
				Double start = new Double(results[2].trim());
				Double end = new Double(results[3].trim());
				Double bw = new Double(results[7].trim());
				
				Measurement M = new Measurement(start.doubleValue(), end.doubleValue(), bw.doubleValue(), results[8]);
				streamResult.addBW(M);
				
				//Send our Measurements to the UI for processing.
				frame.addNewStreamBandwidthUBNTMeasurement(id, M, this.myUbnt);
	
			}
			else if (results.length == 14)
			{
				Double start = new Double(results[2].trim());
				Double end = new Double(results[3].trim());
				Double bw = new Double(results[7].trim());

				Measurement B = new Measurement(start.doubleValue(), end.doubleValue(), bw.doubleValue(), results[7]);
				streamResult.addBW(B);

				Double jitter = new Double(results[9].trim());
				Measurement J = new Measurement(start.doubleValue(), end.doubleValue(), jitter.doubleValue(), results[10]);
				streamResult.addJitter(J);
				frame.addNewStreamBandwidthAndJitterMeasurement(id, B, J);
			}
		}
	}

}
