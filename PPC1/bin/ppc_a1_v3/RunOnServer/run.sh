#!/bin/bash
#SBATCH --job-name=ppc_a1      # Job name
#SBATCH --nodes=1                    # Run all processes on a single node	
#SBATCH --ntasks=1                   # Run a single task		
#SBATCH --cpus-per-task=64            # Number of CPU cores per task
#SBATCH --mem=60gb                    # Job memory request
#SBATCH --time=00:05:00              # Time limit hrs:min:sec
#SBATCH --output=parallel_%j.log     # Standard output and error log
rm -rf records.csv
java -jar benchmark.jar > records.csv