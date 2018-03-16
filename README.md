# Analyzing-buying-patterns-using-Data-Mining-Techniques
Given Input of continuous stream of 0's and 1's from server, one thread of this program accepts those inputs while another thread accepts
user queries about the data and provide appropriate results. E.g. "What is the number of ones in last N data?" this N can be any number less
than number of bits received. Even if used buffer capacity is not as big as N, this program can answer the query with an approximate value
which has around 67%+ accuracy at any given instance.
  If the value of N is less than or equal to size of the buffer used then program returns exact result.
This can be viewed as Business Problem where 1's are users who bought the product and 0' are users who didn't buy the product. Therefore,
this program can be used to find "How many of last N users have bought this product?", while keep receiving bits at the same time.
DGIM algorithm is used for the implementation of this program to use buffer space efficiently for large data streams.
