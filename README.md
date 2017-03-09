# gr.ece.tuc.comp211
Exercise 1 Data Structures and Files Beginning of Exercise 1 for the course "Data Structures and Files", Spring Semester 2017, ECE, Technical University of Crete. This is an introductory exercise to basic searching methods on disks and their complexity.  10^7 integers, ordered from 1 to 10^7, are stored in a file, assuming a page size of 512 bytes. This file is subsequently used to compare their average disk accesses of four methods that search for 10000 random integers: 1) sequential search (first come, first served basis), 2) binary search (first come, first served basis), 3) binary search on sorted and batch examined keys, 4) binary search with "page-caching" using a queue.