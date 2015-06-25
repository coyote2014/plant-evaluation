# Input #

Two different input files are taken into account by this tool:

  * Raw data from the isotope laser analyzer (denoted as data files)
  * Temperature data from a hobo device (denoted as temperature files)

**Important** Have a look at the supllied example files. It is important to match the exact file format provided in these files!

## Data Files Format ##
|DATE|	TIME|	FRAC\_DAYS\_SINCE\_JAN1|	FRAC\_HRS\_SINCE\_JAN1|	EPOCH\_TIME|	ALARM\_STATUS|	CavityPressure|	CavityTemp|	DasTemp	|EtalonTemp|	WarmBoxTemp	|species|	!MPVPosition|	InletValve|	OutletValve|	solenoid\_valves|	12CO2|	12CO2\_dry|	13CO2|	13CO2\_dry|	H2O|	CH4|	Delta\_Raw|	Delta\_30s|	Delta\_2min|	Delta\_5min|	Ratio\_Raw|	Ratio\_30s|	Ratio\_2min|	Ratio\_5min|	CH4\_High\_Precision|	peak\_75|	ch4\_splinemax\_for\_correct	|peak87\_baseave\_spec|	peak88\_baseave|
|:---|:----|:-----------------------|:----------------------|:-----------|:-------------|:--------------|:----------|:--------|:---------|:------------|:------|:------------|:----------|:-----------|:----------------|:-----|:----------|:-----|:----------|:---|:---|:----------|:----------|:-----------|:-----------|:----------|:----------|:-----------|:-----------|:--------------------|:--------|:-----------------------------|:--------------------|:---------------|
|31.10.11|	23:59:57.049|	304.04163252           |	7296.999181           |	1320105597050|	0            |	1.3999838257E+002|	4.5000297546E+001|	3.0875000000E+001|	4.4937644958E+001|	4.5000358582E+001|	1.1000000000E+001|	0.0000000000E+000|	5.0000000000E+004|	2.0186303906E+004|	4.0000000000E+000|	4.6958215978E+002|	4.7303233806E+002|	5.2187105563E+000|	5.2570610074E+000|	5.1204351229E-001|	2.0884960865E+000|	-1.3837887614E+001|	-1.3614125870E+001	|-1.3910323159E+001	|-1.3853592494E+001	|1.1100237998E-002|	1.1102651310E-002|	1.1099456768E-002|	1.1100068618E-002|	0.0000000000E+000   |	5.1102146935E+001	|4.5180420294E+002	            |2.7925265283E+002	   |8.2267922673E+000	|

## Temperature File Format ##

# Output #
|DATE	|TIME|	MEAN 12CO2\_dry	|MEAN 13CO2\_dry|	MEAN delta5Minutes|	MEAN H2O|	solenoid\_valves|	CO2 ABS|	Zeit|	CO2Diff|	δ13Pflanze|	Temperature in °C|	PSR|	Standard Derivation of PSR|	Standard Derivation of δ13|
|:----|:---|:----------------|:--------------|:------------------|:--------|:----------------|:-------|:----|:-------|:-----------|:------------------|:---|:--------------------------|:---------------------------|