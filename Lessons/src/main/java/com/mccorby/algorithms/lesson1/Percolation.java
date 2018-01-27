package com.mccorby.algorithms.lesson1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Hints:
 * - Come up with a scheme for uniquely mapping 2D coordinates to 1D coordinates
 * - The open method must do three things:
 * 1. Validate the indices of the site that it receives
 * 2. Mark the site as open.
 * 3. Perform some sequence of WeightedQuickUnionUF operations that links the site in question to its open neighbors
 *    Connect it to all of its adjacent open sites (4 calls if all open).
 * - System percolates iff top and bottom are connected by open sites
 *
 * Clever trick. Introduce 2 virtual sites (and connections to top and bottom).
 * Percolates iff virtual top site is connected to virtual bottom site.
 */
public class Percolation {

    private static final String ILLEGAL_INIT_VALUE_MSG = "Init value must be greater than 0";
    private static final String ILLEGAL_PARAM_VALUE_MSG = "Rows and columns must be greater than 0 and less or equal that the rank";

    private final int size;
    private final WeightedQuickUnionUF weightedQuickUnion;

    /*
     * This auxiliary data structure is used to keep track of the open sites
     */
    private boolean[] openSites;

    private int topSite;
    private int bottomSite;

    /**
     * Create n-by-n openSites, with all sites blocked
     * @param n
     */
    public Percolation(int n) {
        checkConstructorPrecondition(n);
        size = n;
        openSites = new boolean[n * n];
        weightedQuickUnion = new WeightedQuickUnionUF(size * size);
        topSite = 0;
        bottomSite = size;
    }

    /**
     * Is site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        checkParamsPreconditions(row, col);
        int index = getIndexForCoordinates(row, col);
        return openSites[index];
    }

    /**
     * Open site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        checkParamsPreconditions(row, col);
        int index = getIndexForCoordinates(row, col);
        // Open the site if it is not already open
        if (!isOpen(row, col)) {
            openSites[index] = true;
            connectToRight(row, col);
            connectToLeft(row, col);
            connectToTop(row, col);
            connectToBottom(row, col);
        }
    }

    private void connectToTop(int row, int col) {
        try {
            int index = getIndexForCoordinates(row, col);
            int adjacentIndex = getIndexForCoordinates(row - 1 , col);
            if (isOpen(row - 1, col)) {
                weightedQuickUnion.union(index, adjacentIndex);
            }
        } catch(IllegalArgumentException iae) {
            // NoOp The adjacent site is out of bounds
        }
    }

    private void connectToBottom(int row, int col) {
        try {
            int index = getIndexForCoordinates(row, col);
            int adjacentIndex = getIndexForCoordinates(row + 1 , col);
            if (isOpen(row + 1, col)) {
                weightedQuickUnion.union(index, adjacentIndex);
            }
        } catch(IllegalArgumentException iae) {
            // NoOp The adjacent site is out of bounds
        }
    }


    private void connectToLeft(int row, int col) {
        try {
            int index = getIndexForCoordinates(row, col);
            int adjacentIndex = getIndexForCoordinates(row, col - 1);
            if (isOpen(row, col - 1)) {
                weightedQuickUnion.union(index, adjacentIndex);
            }
        } catch(IllegalArgumentException iae) {
            // NoOp The adjacent site is out of bounds
        }
    }

    private void connectToRight(int row, int col) {
        try {
            int index = getIndexForCoordinates(row, col);
            int adjacentIndex = getIndexForCoordinates(row, col + 1);
            if (isOpen(row, col + 1)) {
                weightedQuickUnion.union(index, adjacentIndex);
            }
        } catch(IllegalArgumentException iae) {
            // NoOp The adjacent site is out of bounds
        }
    }

    /**
     * Is site (row, col) full?
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        checkParamsPreconditions(row, col);
        int index = getIndexForCoordinates(row, col);
        boolean result = false;
        for (int i = 0; i < size; i++) {
            if (isOpen(1, i + 1)) {
                result = weightedQuickUnion.connected(index, i);
            }
        }
        return result;
    }

    /**
     * Does the system percolate?
     * @return
     */
    public boolean percolates() {
        // Check that all bottom open sites are connected to top open sites
        boolean result = true;
        for (int col = 1; col < size; col++) {
            result = result && isFull(size, col);
        }
        return false;
    }

    /**
     * Visible for testing
     * @return size of the internal openSites
     */
    final int getSize() {
        return size;
    }

    /**
     * Visible for testing
     * Get the index in 1D for a 2D coordinate
     * @param row
     * @param col
     * @return
     */
    final int getIndexForCoordinates(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    final boolean isConnected(int row1, int col1, int row2, int col2) {
        int p = getIndexForCoordinates(row1, col1);
        int q = getIndexForCoordinates(row2, col2);
        return weightedQuickUnion.connected(p, q);
    }

    private void checkConstructorPrecondition(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(ILLEGAL_INIT_VALUE_MSG);
        }
    }

    private void checkParamsPreconditions(int row, int col) {
        if (row < 1 || col < 1 || row > getSize() || col > getSize()) {
            throw new IllegalArgumentException(ILLEGAL_PARAM_VALUE_MSG);
        }
    }
}
