Message Queues
--------------
Your task is to design and implement a CSV parser to read and print some stock prices.

IMPORTANT
---------
Please take the time to read this document carefully before commencing. In
particular, please note the section titled "What we are looking for" below.


Background
----------
The attached csv snippet contains some historical stock prices, including open/close/high/low price etc
The daily task is to analyze each individual stock's high/low/open/close price and how they change across a 
date range.


Behavior
--------
 # Design a small program which comes a CSV parser to analyze the stock prices.
 # The parser can load all historical prices and print a metric price (high, open, close etc) for a stock on demand. For example, it can find and print the open price for FAX on 2017-03-31
 # The program can calculate the average price of a metric by a date. For example, it can calculate the average high price of all stocks on 2017-03-31
 # If you don't have time finish implementing all above steps, appreciate that you can describe how you want to implement it.  

Building and Running
--------------------
You should be able to import this project into any conventional IDE as a Maven
project. As a fallback, you can use Maven to build and run tests from the
command-line with:
  mvn package

You can use other language than Java, but preferrable languages include Java/kotlin/golang, or other JVM languages. But it will be good if you can provide instructions on how to build/run the program if 
you don't use maven/gradle.

What we are looking for
-----------------------
- The entire implementation shouldn't really need more than 7-10
  classes, tests included.
- Simple but correct solution
- Readable code
- Meaningful class/function/variable names (consistent naming convention)
- Unit testable code is highly desirable

Submission Checklist
--------------------
Your submission should:
 - source code
 - a zipped package or a git URL to clone from
