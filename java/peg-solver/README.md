Peg Solver
==========

We have all seen the "peg puzzle" on the tables of our favorite restaurant Cracker Barrel. After spending literally 
decades of failing to complete the puzzle, I've created the ultimate weapon against it! Behold, the peg-solver.

To build:

     mvn clean install
     
To run:

     java -cp target/peg-solver-1.0.0-SNAPSHOT-jar-with-dependencies.jar com.github.jschmidt10.peg.Solver
     
The program will brute force the peg puzzle, give you the stats on how many solutions there are, and then selects one 
winning strategy and allows you to follow it graphically.
