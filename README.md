# ANNTrainer
Artificial neural network trainer

This is a project made for the course Solving Optimisation Problems With Evoulution Algorithms in Java 
on University of Zagreb, Faculty of Electrical Engineering and Computing.

Project can be imported to Eclipse or ran from the command line.

Program expects 5 command line arguments: file (file containing dataset), algorithm ('pso-a', 'pso-b-d'or 'clonalg'), 
n (population size), maxerr (maximum mean squared error), maxiter (maximum number of generations).

example of running from command line:
<code>java -cp bin hr.fer.zemris.optjava.Main data/iris.data pso-b-3 30 1e-3 10000</code>
