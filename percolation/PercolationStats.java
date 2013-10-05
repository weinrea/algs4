

/*
 * Compilation:  javac PercolationStats.java
 * Execution:  java PercolationStats <N> <T>
 * Dependencies: Percolation.class
 *
 * Percolation Sats class.
 *
 */


public class PercolationStats {

    private double[] thresholds;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T)
    {
        if (N <= 0) throw new IllegalArgumentException("N value out of bounds");
        if (T <= 0) throw new IllegalArgumentException("T value out of bounds");


        thresholds = new double[T];

        for (int i = 0; i < T; i++)
        {
            //StdRandom.setSeed(i);
            int openCount = 0;
            Percolation p = new Percolation(N);
            while (!p.percolates())
            {
                int k, j;
                k = StdRandom.uniform(N) + 1;
                j = StdRandom.uniform(N) + 1;
                if (!p.isOpen(k, j))
                {
                    p.open(k, j);
                    openCount++;
                }
            }
            thresholds[i] = openCount / (double) (N * N);

        }
        
    }
    // sample mean of percolation threshold
    public double mean()                     
    {
        double sum = 0;
        for (int i = 0; i < thresholds.length; i++)
        {
            sum += thresholds[i];
        }

        return sum/thresholds.length;
    }

    // sample standard deviation of percolation threshold
    public double stddev()                   
    {
        double sum = 0;
        double mean = this.mean();

        for (int i = 0; i < thresholds.length; i++)
        {
            sum += (thresholds[i] - mean) * (thresholds[i] - mean);
        }

        return Math.sqrt(sum/(thresholds.length - 1));
    }
    // returns lower bound of the 95% confidence interval
    public double confidenceLo()             
    {
        double mean = this.mean();
        double stddev = this.stddev();
        return mean - (1.96 * stddev / Math.sqrt(thresholds.length));
    }
    // returns upper bound of the 95% confidence interval
    public double confidenceHi()             
    {
        double mean = this.mean();
        double stddev = this.stddev();
        return mean + (1.96 * stddev / Math.sqrt(thresholds.length));
    }
    // test client, described below
    public static void main(String[] args)   
    {
        if (args.length != 2)
        {
            StdOut.println("Must specify 2 arguments");

        }
        else
        {
                int N  = Integer.parseInt(args[0]);
                int T  = Integer.parseInt(args[1]);
                PercolationStats ps = new PercolationStats(N, T);
                StdOut.println("mean                     = " + ps.mean());
                StdOut.println("stddev                   = " + ps.stddev());
                StdOut.println("95% confidence interval  = " + ps.confidenceLo()
                               + ", " + ps.confidenceHi());
        }
    }
}

