# Overview #

Processing takes places in different steps. Each steps relies on the results of the previous step:

  1. Copy Evaluator
  1. Mean Evaluator
  1. CO2Diff Evaluator
  1. Delta13 Evaluator
  1. Temperature Evalutor
  1. Plant Divider
  1. PhotoSynthesis Evalutor
  1. StandardDerivation Evaluator

## Copy Evaluator ##

Iterates over all files in the configured input directory and copies each line into one file. All lines are sorted ascending by date and time. The resulting file is passed to the mean evaluator as input.

## Mean Evaluator ##

In the second step only relevant data is processed to the output files. In this first step the means (last 5 minutes for each solenoid valve) for
  * 12CO<sub>2</sub> Dry
  * 13CO<sub>2</sub> Dry
  * Delta 5 Minutes
  * H<sub>2</sub>O

Besides these mean values the resulting output contains the date and time and the solenoid valve number.

The lines used to comoute the mean values are saved to a different file, so standard derivation computation is possible later.

## CO2Diff Evaluator ##

In this step the CO<sub>2</sub> difference between a line and the corresponding reference valve (solenoid valve = 1) is computed. As reference line the one that is nearest in time to the current line is choosen.

In short:
CO<sub>2</sub>-Diff<sub>i</sub> = CO<sub>2</sub>-Abs<sub>i</sub> - CO<sub>2</sub>-Abs<sub>reference</sub>


## Delta13 Evaluator ##

In this step the Delta13 values for each line are computed. The selection of reference line is as before:

delta13<sub>i</sub> = ((CO<sub>2</sub>-Abs<sub>i</sub> x delta5Minutes<sub>i</sub>) - (CO<sub>2</sub>-Abs<sub>reference</sub> x delta5Minutes<sub>reference</sub>)) / (CO<sub>2</sub>-Abs<sub>i</sub> - CO<sub>2</sub>-Abs<sub>reference</sub>)


## Temperature Evaluator ##

Looks in the seperate temperature input file for the temperature that is nearest to date and time of the current line.

## Plant Divider ##

In this step the configured dates for each plant are taken into account. As a result the inout file is seperated into one file for each plant, that contains only the date between start and end date of plant<sub>i</sub>

## PhotoSynthesis Evalutor ##

Computes the PSR for each line (where applicable)

## StandardDerivation Evaluator ##

For each line the lines used for mean value computation are processed and the values for PSR and delta13 are collected. These value are used to compute the standard derivation:

```java

double sd = Math.sqrt(StatUtils.variance(values));
```