# Java Network Analysis Tool

## Overview
The Java Network Analysis Tool is a comprehensive application designed to simulate and evaluate the connectivity between nodes in various network types, such as social, communication, and data networks. It features an advanced union-find data structure, an interactive GUI, real-time statistics, and the ability to save and load network states.

## Features
- **Network Simulation**: Simulates and evaluates connectivity in different types of networks.
- **Union-Find Data Structure**: Implements path compression and union by rank for efficient management of network components.
- **Interactive GUI**: Allows users to interact with the network through draggable nodes and Bezier curve connections.
- **Real-Time Statistics**: Displays real-time updates on operations and performance metrics.
- **Persistence**: Supports saving and loading network states for continuous analysis.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- JavaFX library

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/okalmanwa/network-analysis-tool.git
    ```
2. Navigate to the project directory:
    ```sh
    cd network-analysis-tool
    ```
3. Compile the project:
    ```sh
    javac -cp .:path/to/javafx-sdk/lib/* org/example/Javafx.java
    ```
4. Run the application:
    ```sh
    java -cp .:path/to/javafx-sdk/lib/* org.example.Javafx
    ```

## Usage

### Interactive GUI
- **Draggable Nodes**: Click and drag nodes to reposition them within the network.
- **Union Operation**: Enter two node numbers and click "Union" to connect them.
- **Check Connection**: Enter two node numbers and click "Check Connection" to verify if they are connected.
- **Save State**: Click "Save State" to save the current network configuration to a file.
- **Load State**: Click "Load State" to load a previously saved network configuration.

### Example
1. Launch the application.
2. Drag nodes to desired positions.
3. Enter node numbers in the text fields and click "Union" to connect them.
4. Observe the real-time statistics and logs in the GUI.
5. Save the network state by clicking "Save State".
6. Load the network state by clicking "Load State".

## Code Structure

### `org.example.Javafx`
The main class that sets up the JavaFX application and handles user interactions.

### `org.example.UnionFind`
The class that implements the union-find data structure with path compression and union by rank.

## Contact
For any questions or suggestions, please open an issue or contact me on com34@cornell.edu. cheers!

