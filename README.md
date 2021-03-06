UbiquiJPerf
===========

Graph Ubiquiti AirOS stats in real-time. This is a Java port of Ubiquiperf 
(https://github.com/gillespiem/Ubiquiperf), allowing for the real-time display of signal stats, 
MCS index in use (ala TXRate/RXRate), and iperf throughput datapoints of Ubiquiti AirOS based gear. 

The PHP based Ubiquiperf generated graphs only after all data was collected, UbiquiJPerf will display datapoints
every second.

This project is an expanding from the JPerf codebase.

## Example graph

![UbiquiJperf Graph](https://raw.github.com/gillespiem/UbiquiJPerf/master/images/UbiquiJPerf.png)

##Known Issues

Similar to Ubiquiperf, you will most definitely need to compile iperf with pthread support (it's typically compiled
that way in most Linux distributions).  

Fonts may not render as intended, based on OS.

I'm actively testing this using AirOS versions 5.3 and 5.4.5, with NanoStation, NanoBridge, and Rocket Titanium 
equipment. If you'd like me to add support for XYZ, feel free to contact me.
