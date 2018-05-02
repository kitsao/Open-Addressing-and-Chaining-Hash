import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * File: 	HashTableChain.java
 * Date: 	May 01, 2018
 * Author: 	Kitsao Emmanuel
 * Purpose: Implement Chaining to store hashed data. 
 * 			Uses as keys the last names in the patient.txt 
 * =======================================================================================
 */

/* Class ChainedHashEntry */
class ChainedHashEntry {
	String key;
	ChainedHashEntry next;

	/* Constructor */
	ChainedHashEntry(String key) {
		this.key = key;
		this.next = null;
	}
}

/* Class HashTable */
class HashTable {
	private int capacity = 50;
	private int size;
	private ChainedHashEntry[] hashTable;

	/* Constructor */
	public HashTable() {
		size = 0;
		hashTable = new ChainedHashEntry[capacity];
		for (int i = 0; i < capacity; i++)
			hashTable[i] = null;
	}

	/* Function to get number of elements */
	public int getSize() {
		return size;
	}

	/* Function to clear hash table */
	public void empty() {
		for (int i = 0; i < capacity; i++)
			hashTable[i] = null;
	}

	/* Function to get value of a key */
	public String get(String key) {
		int hash = (hash(key) % capacity);
		if (hashTable[hash] == null)
			return null;
		else {
			ChainedHashEntry entry = hashTable[hash];
			while (entry != null && !entry.key.equals(key))
				entry = entry.next;
			if (entry == null)
				return null;
			else
				return entry.key;
		}
	}

	/* Function to insert a key */
	public void insert(String key) {
		int hash = (hash(key) % capacity);
		if (hashTable[hash] == null)
			hashTable[hash] = new ChainedHashEntry(key);
		else {
			ChainedHashEntry entry = hashTable[hash];
			while (entry.next != null && !entry.key.equals(key))
				entry = entry.next;
			if (!entry.key.equals(key))
				entry.next = new ChainedHashEntry(key);
		}
		size++;
	}

	public void remove(String key) {
		int hash = (hash(key) % capacity);
		if (hashTable[hash] != null) {
			ChainedHashEntry prevEntry = null;
			ChainedHashEntry entry = hashTable[hash];
			while (entry.next != null && !entry.key.equals(key)) {
				prevEntry = entry;
				entry = entry.next;
			}
			if (entry.key.equals(key)) {
				if (prevEntry == null)
					hashTable[hash] = entry.next;
				else
					prevEntry.next = entry.next;
				size--;
			}
		}
	}

	/* Function to give a hash value for a given string */
	private int hash(String str) {
		int hashVal = str.hashCode();
		hashVal %= capacity;
		if (hashVal < 0)
			hashVal += capacity;
		return hashVal;
	}
	
	/* Function to redo hashing */
	public void rehash() throws Exception {
		empty();
		HashTable temp = new HashTable();

		for (int i = 0; i < capacity; i++) { // copy all elements to temp
			if (temp.hashTable[i] != null) {
				temp.hashTable[i] = hashTable[i];
			}
		}

		capacity = capacity * 2;

		for (int i = 0; i < capacity / 2; i++) {
			ChainedHashEntry entry = hashTable[i];
			if (entry != null) {
				insert(entry.key);
			}
		}
	}

	/* Function to display hash table */
	public void display() {
		for (int i = 0; i < capacity; i++) {
			System.out.print("\nBucket " + (i + 1) + " : ");
			ChainedHashEntry entry = hashTable[i];
			while (entry != null) {
				System.out.print(entry.key + " ");
				entry = entry.next;
			}
		}
		System.out.println();
	}
}

/* Class HashTableChain Test */
class HashTablesChain {
	public static void main(String[] args) throws IOException {
		/* Make object of HashTable */
		HashTable ht = new HashTable();

		String inputFile = "patient.txt";
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		try {
			String entry = br.readLine();
			while (entry != null) {
				String[] splited = entry.split(",");
				String lastName = splited[0];

				try {
					ht.insert(lastName);
				} catch (Exception e) {

				}
				entry = br.readLine();
			}

		} finally {
			br.close();
		}

		boolean proceed = true;
		Scanner in = new Scanner(System.in);

		while (proceed) { // user choice/entry
			System.out.println("_________________________________________________________________________");
			System.out.println("\nChaining to store hashed data");
			System.out.println("_________________________________________________________________________");
			System.out.println("Pick an option: \t (d) display table \t (r) rehash \t (q) quit ");
			String command = in.next();
			char chr = command.charAt(0);
			switch (chr) {

			case 'd': {

				ht.display();

				break;
			}

			case 'r': {

				try {
					ht.rehash();
				} catch (Exception ex) {
					Logger.getLogger(HashTable.class.getName()).log(Level.SEVERE, null, ex);
				}

				break;
			}

			case 'q': {
				proceed = false;
				System.out.println("Thanks for stopping by!");
				break;
			}
			default: {
				System.out.println("Invalid entry. Please try again");
			}

			}
		}
		in.close();
	}
}