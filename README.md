/**
 * File: 	readme.md
 * Date: 	May 01, 2018
 * Author: 	Kitsao Emmanuel
 * Purpose: Implement Open Addressing and Chaining to store hashed data. 
 * 			Uses as keys the last names in the patient.txt 
 * =======================================================================================
 */
 
 My hash functions are as given below.
 //	hash function - Open Addressing
public int hash(P key) {
	int code = key.hashCode();
	return code % size;
}
 //	hash function - Chaining
 private int hash(String str) {
	int hashVal = str.hashCode();
	hashVal %= TABLE_SIZE;
	if (hashVal < 0)
		hashVal += TABLE_SIZE;
	return hashVal;
}

I chose hashCode() method. Using the method, the collection can easily determine the hash value of a given key, which can be referenced internally to store the data. 
This in turn makes access operations more efficient. To make the hashing better, the implementation returns the modulus of the total size of the collection, set to 25 and 50 for open addressing and chaining. 
This is reasonable, given that the size is consistent.

The implementation handles collisions by linear probing. Before a key is inserted, we first search linearly for the next available slot. As for chaining, several keys can be mapped to the same bucket or value, hence resolving collisions.