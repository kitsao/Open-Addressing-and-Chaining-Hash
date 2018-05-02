import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * File: 	HashTableWithOpenAddressing.java
 * Date: 	May 01, 2018
 * Author: 	Kitsao Emmanuel
 * Purpose: Implement Open Addressing to store hashed data. 
 * 			Uses as keys the last names in the patient.txt 
 * =======================================================================================
 */
public class HashTableWithOpenAddressing<P> {

	private Entry<P> tbl[];
	private int size = 25;
	private int elements = 0;

	public HashTableWithOpenAddressing() {
		tbl = new Entry[size];
	}

	private class Entry<P> {

		private P key;
		private boolean exists;

		public Entry(P eKey) {
			key = eKey;
			exists = true;
		}
		//	getter function to return key
		public P getKey() {
			return key;
		}
		//	setter function to set key
		public void setKey(P key) {
			this.key = key;
		}
		//	boolean function to check if entry exists
		public boolean isExists() {
			return exists;
		}
		//	affirm and set to true if entry exists
		public void setExists(boolean exists) {
			this.exists = exists;
		}
		//	prepare to remove element
		public void prepDelete() {
			exists = false;
		}
	}
	/**
	 * insert method.
	 *
	 * insert an element to the hash table.
	 *
	 * @param int key
	 *  Integer key.
	 * @param int i
	 *  elements.
	 * @return void
	 */
	public void insert(P key) throws Exception {

		if (key == null) { // key cannot be null
			throw new Exception("Key cannot be null");
		}

		int index = search(key, true);
		tbl[index] = new Entry(key);
		elements++;

		if (elements == size / 2) { // rehash if half full
			rehash();
		}
	}
	//	hash function
	public int hash(P key) {
		int code = key.hashCode();
		return code % size;
	}
	/**
	 * Search method.
	 *
	 * Search an element in hash table.
	 *
	 */
	private int search(P key, boolean empty) throws Exception {
		int initialPoint = hash(key);
		int index = initialPoint;

		while (true) {
			try {
				if (tbl[index].key == key && empty == false) {
					return index;
				}
			} catch (Exception e) {
				if (empty) {
					return index;
				}
			}

			index = (index + 1) % size;

			if (index == initialPoint) {
				throw new Exception("Element not found");
			}
		}
	}
	/**
	 * Remove method.
	 *
	 * Remove an element from the hash table.
	 */
	public boolean remove(P key) throws Exception {

		if (key == null) { // key cannot be null
			throw new Exception("Key cannot be null");
		}

		try {
			int index = search(key, false);
			tbl[index].prepDelete(); // marks entry for deletion
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	/**
	 * Delete method.
	 *
	 * Delete an element from the hash table.
	 */
	private void delete() {

		for (int i = 0; i < tbl.length; i++) {

			try {
				if (tbl[i].exists == false) {
					tbl[i] = null;
				}
			} catch (Exception e) {

			}
		}
	}
	//	Function to redo hashing
	private void rehash() throws Exception {
		delete();
		Entry<P> temp[];
		temp = new Entry[size];

		for (int i = 0; i < size; i++) { // copy all elements to temp
			if (tbl[i] != null) {
				temp[i] = tbl[i];
			}
		}

		size = size * 2;
		elements = 0;

		tbl = new Entry[size];
		for (int i = 0; i < size / 2; i++) {
			Entry<P> entry = temp[i];
			if (entry != null) {
				insert(entry.key);
			}
		}
	}
	//	Function to display the hash table
	public void display() {
		System.out.println("\nHash Table");
		System.out.println("Table Size : " + size);
		System.out.println("Index\t\tName\t\tStatus");
		for (int i = 0; i < size; i++) {
			Entry<P> e = tbl[i];
			if (e == null) {
				System.out.println(i + " empty");
			} else {
				String status;
				if (e.isExists() == true) {
					status = "Found";
				} else {
					status = "Candidate for deletion";
				}
				System.out.println(i + "\t\t" + e.key + "\t\t" + status);
			}
		}
	}
	//	simple user interface for interaction
	public void menu() // user menu
	{

		boolean proceed = true;
		Scanner in = new Scanner(System.in);

		while (proceed) { // user choice/entry
			System.out.println("_________________________________________________________________________");
			System.out.println("\nOpen Addressing to store hashed data");
			System.out.println("_________________________________________________________________________");
			System.out.println("Pick an option: \t (d) display table \t (r) rehash \t (q) quit ");
			String command = in.next();
			char chr = command.charAt(0);
			switch (chr) {

			case 'd': {

				display();

				break;
			}

			case 'r': {

				try {
					rehash();
				} catch (Exception ex) {
					Logger.getLogger(HashTableWithOpenAddressing.class.getName()).log(Level.SEVERE, null, ex);
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

	public static void main(String[] args) throws IOException {

		HashTableWithOpenAddressing<String> tbl = new HashTableWithOpenAddressing<>();

		String inputFile = "patient.txt";
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		try {
			String entry = br.readLine();
			while (entry != null) {
				String[] splited = entry.split(",");
				String lastName = splited[0];

				try {
					tbl.insert(lastName);
				} catch (Exception e) {

				}
				entry = br.readLine();
			}

		} finally {
			br.close();
		}
		tbl.menu();
	}
}