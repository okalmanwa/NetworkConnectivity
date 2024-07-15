package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Union-Find data structure with path compression and union by rank.
 */
public class UnionFind {
    private int[] parent;
    private int[] rank;
    private List<BiConsumer<Integer, Integer>> unionListeners = new ArrayList<>();
    private List<String> operationLog = new ArrayList<>();
    private int operationCount = 0;

    /**
     * Initializes an empty union-find data structure with {@code size} elements.
     *
     * @param size the number of elements
     */
    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Adds a union listener to be notified when a union operation occurs.
     *
     * @param listener the listener to add
     */
    public void addUnionListener(BiConsumer<Integer, Integer> listener) {
        unionListeners.add(listener);
    }

    /**
     * Finds the root of the element {@code p} with path compression.
     *
     * @param p the element to find
     * @return the root of the element
     */
    public int find(int p) {
        operationCount++;
        if (parent[p] != p) {
            parent[p] = find(parent[p]);  // Path compression
        }
        return parent[p];
    }

    /**
     * Unites the sets containing elements {@code p} and {@code q}.
     *
     * @param p one element
     * @param q the other element
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP != rootQ) {
            if (rank[rootP] > rank[rootQ]) {
                parent[rootQ] = rootP;
                notifyUnion(rootP, rootQ);
            } else if (rank[rootP] < rank[rootQ]) {
                parent[rootP] = rootQ;
                notifyUnion(rootQ, rootP);
            } else {
                parent[rootQ] = rootP;
                rank[rootP]++;
                notifyUnion(rootP, rootQ);
            }
            logOperation("Union", p, q);
        }
    }

    /**
     * Checks if elements {@code p} and {@code q} are in the same set.
     *
     * @param p one element
     * @param q the other element
     * @return {@code true} if {@code p} and {@code q} are in the same set, {@code false} otherwise
     */
    public boolean connected(int p, int q) {
        operationCount++;
        boolean isConnected = find(p) == find(q);
        logOperation("Find", p, q);
        return isConnected;
    }

    /**
     * Returns the number of elements in this union-find data structure.
     *
     * @return the number of elements
     */
    public int getSize() {
        return parent.length;
    }

    /**
     * Returns the number of components (sets) in this union-find data structure.
     *
     * @return the number of components
     */
    public int getComponentCount() {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of operations performed by this union-find data structure.
     *
     * @return the number of operations
     */
    public int getOperationCount() {
        return operationCount;
    }

    /**
     * Returns the log of operations performed by this union-find data structure.
     *
     * @return the operation log
     */
    public List<String> getOperationLog() {
        return operationLog;
    }

    /**
     * Notifies all registered union listeners of a union operation.
     *
     * @param rootP the root of the first element
     * @param rootQ the root of the second element
     */
    private void notifyUnion(int rootP, int rootQ) {
        unionListeners.forEach(listener -> listener.accept(rootP, rootQ));
    }

    /**
     * Logs an operation performed by this union-find data structure.
     *
     * @param operation the operation performed
     * @param p         one element involved in the operation
     * @param q         the other element involved in the operation
     */
    private void logOperation(String operation, int p, int q) {
        operationLog.add(operation + " operation on nodes " + p + " and " + q);
    }
}
