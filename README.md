# Bayesian Network Inference Project

This project focuses on the implementation of a Bayesian network and the comparison of different probabilistic inference algorithms. The primary objective is to investigate the performance and accuracy of these algorithms in various scenarios. The project is divided into three main components, each concentrating on a specific algorithm:

1. **`Basic Sampling:`**  Implement a basic sampling algorithm.
2. **`Variable Elimination:`** Implement variable elimination with the elimination of redundant variables at the start. The variable elimination order should follow the order ABC.
3. **`Variable Elimination with Heuristic Order:`** This part builds on the previous one, allowing for a heuristic-based approach to determine the order of variable elimination. This is where you can experiment and optimize the elimination order.

---
### Input and Output
The project takes input from two files: `input.txt` and an XML file that defines the Bayesian network's structure. The queries in `input.txt` are formatted to specify the query, evidence, and the chosen algorithm. The XML file contains details about the network structure, including variable definitions and conditional probability tables.

The output is written to `output.txt`, where the program calculates the requested probabilities and records the number of addition and multiplication operations performed for each query.

---
### Implementation
- **`BasicInference`:** This class implements the basic sampling. It is responsible for calculating the probability of a given query in the context of a Bayesian network
- **`BayesianNetwork`:** Its primary role is to represent and manage the Bayesian network structure, including nodes and conditional probability tables (CPTs).
- **`CPT`:** Represents a Conditional Probability Table in this project. It plays a crucial role in modeling the conditional probabilities of nodes in the Bayesian network.
- **`EnumerationAsk`:** It is designed for performing probabilistic inference using the Enumeration-Ask algorithm.
- **`Ex1`:** It performs several tasks related to Bayesian networks, reading data from both a text file and an XML file, constructing a Bayesian network, and then running various inference algorithms on this network.
- **`MyNode`:** It represents nodes in a Bayesian network. It includes various properties and methods for working with Bayesian network nodes.
- **`VariableElimination`:** It implements variable elimination, a common algorithm for exact inference in Bayesian networks.

---
### The Challenge
The main challenge in this project lies in efficiently calculating conditional probabilities within a Bayesian network, especially as the network's complexity increases. The project aims to strike a balance between minimizing the computational load and maintaining high accuracy.

This project offers an opportunity to gain hands-on experience with Bayesian networks and probabilistic inference algorithms, contributing to a better understanding of artificial intelligence and machine learning principles.
