# Sorting exercise
## Motivation

Share:
* Implementation of concurrent merge-sort with k parts using ForkJoinPool. 
* Implementation of limited size list in cache.

## Problem Description
Consider a CSV input file.
1. Design a program which sorts efficiently the file based on string value of its key field. The program should never have more than x number of records in memory.
2. What is the complexity of your program?
3. Implement the above program using java. The key field index and the max number of records in memory are read as input arguments..
4. Design a performance improvement using parallel processing.
5. What is the complexity of the parallel processing design?
6. Implement the above using java.

## Design and Solution Elements
* Manage limited number of records in memory (ConcurrentRecordList in the implementation)
* Merge sort with k parts (instead of splitting to 2 - split to k parts)
* Use ForkJoinPool for concurrency
* The program deliberately do most of the sore on the disk - as the question limits hardly the use of cache. It's done by using multiple files (the merge-sort split and merges files when it's handles more than k elements).

## Disclaimer
I had no time to refine the solution. It works yet most of the elements may be easily generified (e.g., sort any kind of objects; list of generic type of data for the concurrent data).
I had no time to refat

###Few issues that were not handled because of lack of time
1. Deletion of tmp folder doesn't work.
2. Improving exception handling and logs.
3. Refactoring to make the code cleaner and more generic.
4. no validations on inputs.

###Run instruction
Change the constants in MyApplication (or the input parameters in the main).
Verify the workspace folder and the output file do not exist.
Run.

Before re-run you need to delete the workspace folder and the output file.
