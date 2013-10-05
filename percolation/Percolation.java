
/*
 * Compilation:  javac Percolation.java
 * Execution:  java Percolation < input.txt
 * Dependencies: 
 * Execution:
 *
 * Percolation class.
 *
 */


public class Percolation {

    // size of rows columns of grid
    private int gridSize ;

    // 2d array to hold whether opne or closed
    // false is closed true is open
    private boolean[][] grid;

    // to connect adjacent open nodes in grid
    WeightedQuickUnionUF UF;


    private int xyTo1D(int i, int j)
    {
        return  (i - 1) * grid_size + j;
    }

    private void validate_indices(int i, int j)
    {
        if (i <= 0 || i > grid_size) throw new IndexOutOfBoundsException("row index i out of bounds"); 
        if (j <= 0 || j > grid_size) throw new IndexOutOfBoundsException("column index j out of bounds"); 
    }

    private void connectNeighbor(int i, int j, int x, int y)
    {
        if (x <= 0 || x > grid_size) return;
        if (y <= 0 || y > grid_size) return;

	// if open connect
	if(isOpen(x,y))
	{
            UF.union(xyTo1D(i, j), xyTo1D(x, y)); 
	    if(isFull(x,y) && x == grid_size)
	    {
		UF.union(xyTo1D(x, x) + 1, xyTo1D(x, y)); 
	    }
	}

        return;
    }

    // create N-by-N grid, with all sites blocked
    public Percolation(int N)
    {
        grid_size = N;

        grid = new boolean[N][N];

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                grid[i][j] = false;

        // add 2 for top and bottom
        UF = new WeightedQuickUnionUF(N*N + 2);
    }

    private void set_open(int i, int j)
    {
        grid[i-1][j-1] = true;
    }

    // open site (row i, column j) if it is not already
    // UF:union
    public void open(int i, int j)
    {
        // 1. validate input
        validate_indices(i, j);
        set_open(i,j);

        int index1D = xyTo1D(i,j);

        // first row
        if(i == 1)
        {
            UF.union(0, xyTo1D(i, j)); 
        }    

        // left 
	connectNeighbor(i, j, i-1, j);
	connectNeighbor(i, j, i+1, j);
	connectNeighbor(i, j, i, j-1);
	connectNeighbor(i, j, i, j+1);

        // last row
        if(i == grid_size && isFull(i,j)) // if last row
        {
            UF.union(xyTo1D(i, i) + 1, index1D); 
        }    

    }

    // is site (row i, column j) open?
    // UF:connected
    public boolean isOpen(int i, int j)
    {
        validate_indices(i, j);
        return grid[i-1][j-1];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        validate_indices(i, j);
        return UF.connected(0, xyTo1D(i, j));
        
    }

    // does the system percolate?
    public boolean percolates()
    {
        return UF.connected(0, grid_size * grid_size + 1);
    }


    // test app
    public static void main(String args[])
    {
        //test 1
        StdOut.print("test 1: ");
        Percolation p = new Percolation(10);
        p.open(1, 1);
        p.open(1, 2);
        if(p.UF.connected(p.xyTo1D(1,1), p.xyTo1D(1,2)))
        {
            StdOut.print("ok\n");
            
        }
        else
        {
            StdOut.print("not ok\n");
        }

    }
}

