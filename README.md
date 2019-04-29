# AURORA - AUtomated classification of metamodel RepOsitories using a neuRAl network.

This repository contains the source code implementation of AURORA and the datasets used to replicate the experimental results of our MODELS'19 paper:

_Automated Classification of Metamodel Repositories: A Machine Learning Approach_


## Introduction



## Repository Structure

This repository is organized as follows:

* The [TOOLS](./TOOLS) directory contains the implementation of the different tools we developed:
	* [TERM-EXTRACTOR](./TOOLS/TERM_EXTRACTOR): The Java implementation term extractor from metamodels;
	* [TDM_ENCODER](./TOOLS/TDM-ENCODER): A set of Python scripts allowing to compute TDMs;
	* [NEURAL-NETWORKS](./TOOLS/NEURAL-NETWORKS): This tools classifies metamodels according the TDM values and training set.
* The [DATASET](./DATASET) directory contains the datasets described in the paper that we use to evaluate AURORA:
	* [NORMALIZED_MM_REPRESENTATION](./DATASET/NORMALIZED_MM_REPRESENTATION): plain documents that represent metamodels;
	* [TDMS](./DATASET/TDMS): TDMs are extracted from _NORMALIZE\_MM\_REPRESENTATION_.

