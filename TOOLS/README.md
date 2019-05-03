
## How to run AURORA
To start the evaluation of AURORA, open your Terminal, move to the folder where you store AURORA and run the following command:

```
python
```

Once you enter the Python console, type and run the following command:

```
import Runner as runner
```
In AURORA/TOOL/Neural-network/README.md, you will find all the commands need to perform evaluation on the dataset. For instance

```
runner.run('bGrams/CO2/Round1', 6895, 10, 9, 300, 3.0, 0.75)
```
This line will run AURORA on the dataset using bi-gram. Similarly, you can execute other experiments by changing the command line above with those are available in AURORA/TOOL/Neural-network/README.md.
