Description of Evaluation Type B (aka Ingo's Evaluation)

# Overview #

  1. Copy Evaluator
  1. CO2 Absolute Evaluator
  1. Reduce Dataset Evaluator
  1. Pick Dataset Evaluator


# Details #

## Copy Evaluator (multiple input, singel output) ##
  * Read all files in the input directory
  * Copy the header to a new output file
  * copy all remaining lines (except headers) into output file

## CO2 Absolute Evaluator (single input/output ##
  * Compute for each line the CO2 Absolute value by
```
abs(12CO2_dry + 13CO2_dry)
```

## Reduce Datasets (single input/output) ##

  * Result: Only one dataset per minute instead of every second
  * Collect 60 lines
  * Compute the mean for
    * CO2 Absolute (from the step before)
    * 12CO2
    * 13CO2
    * 12CO2\_dry
    * 13CO2\_dry
    * delta raw

## Pick Datasets (single input/output) ##

  * Further reducing of the dataset by just transfering the last dataset before a switch of the solenoid valve to the new output file