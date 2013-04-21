/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
    protected int size;
    protected int numOfBucketsUsed;// n
    protected int numOfBuckets; // N
    protected Object[] hashTable;


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
      hashTable = new Object[sizeEstimate];
      size = 0;
      numOfBuckets = sizeEstimate;
      for (int i = 0; i<numOfBuckets; i++) { // Inserts a new DList in all the buckets.
          hashTable[i] = new DList();
      }
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
      Object[] hashTable = new Object[101];
      size = 0;
      numOfBuckets = 101;
      for (int i = 0; i<numOfBuckets; i++) { // Inserts a new DList in all the buckets.
          hashTable[i] = new DList();
      }
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
      return Math.abs(((127*code + 129) % 16908799) % numOfBuckets); 
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
      return (size == 0);
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
      Entry returnEntry = new Entry();
      returnEntry.key = key;
      returnEntry.value = value;
      if (((DList) hashTable[compFunction(key.hashCode())]).length() == 0) numOfBucketsUsed++;
      ((DList) hashTable[compFunction(key.hashCode())]).insertFront(returnEntry);
      size++;
      double loadFactor = (numOfBucketsUsed/((float) numOfBuckets));
      if (loadFactor > .75) {
    	  hashTable.resize();
      }
    return returnEntry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
      Entry returnEntry = new Entry();
      returnEntry = null;
      DListNode cursor = (DListNode) ((DList) hashTable[compFunction(key.hashCode())]).front();
      try {
          for(int i = 0; i<((DList) hashTable[compFunction(key.hashCode())]).length(); i++) { 
              /*Checks every item in bucket for the key given. If it finds a match, set the returnEntry to that match & break.*/ 
              if (((Entry) cursor.item()).key.equals(key)) {
                  /* If we find the item, return & break*/
                  returnEntry = (Entry) cursor.item();
                  break;
              } else { //If this is not the item, continue looking.
                  cursor =(DListNode) cursor.next();
              }
          }
      }
      catch (InvalidNodeException INE) {
          System.err.println(INE);
      }
    return returnEntry;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
      Entry returnEntry;
      returnEntry = null;
      DListNode cursor = (DListNode) ((DList) hashTable[compFunction(key.hashCode())]).front();
      try {
          for(int i = 0; i<((DList) hashTable[compFunction(key.hashCode())]).length(); i++) {
              /*Finds if the entry exists in the bucket & if it does, removes it*/
              if(((Entry) cursor.item()).key.equals(key)) {
                  /*If we find the item we were looking for, remove it*/
                  returnEntry = (Entry) cursor.item();
                  cursor.remove();
                  if (((DList) hashTable[compFunction(key.hashCode())]).length() == 0) numOfBucketsUsed--;
                  size--;
                  break;
              } else {
                  cursor = (DListNode) cursor.next();
              }
          }
      }
      catch (InvalidNodeException INE) {
          System.err.println(INE);
      }
    return returnEntry;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
      for (int i = 0; i<numOfBuckets; i++) { // Makes a new DList for every bucket.
          hashTable[i] = new DList();
      }
      size = 0;
  }
    public void testHelper() {
        double loadFactor = (numOfBucketsUsed/((float) numOfBuckets));
        for (int i = 0; i<this.numOfBuckets; i++) {
            System.out.println(hashTable[i] + " has " + ((DList) hashTable[i]).length() + " elements!");
        }
        System.out.println("n/N ratio is: " + loadFactor + ". It should be around .64 with numOfBucketsUsed being " + numOfBucketsUsed + " and buckets being " + numOfBuckets + " & number of entries stored (size): " + size);
    }
    
    public void resize() {
    	HashTableChained resizedHashTable = new HashTableChained(numOfBuckets*2);
    	for (int i = 0; i<numOfBuckets; i++) {
    		DListNode cursor = hashTable[i].front();
    		for (int j = 0; j<((DList) hashTable[i]).length(); j++) {
    			try {
    				resizedHashTable.insert(cursor.item().key, cursor.item().value);
    				cursor = cursor.next();
    			} catch(InvalidNodeException INE) {
    				System.err.println(INE);
    			}
    		}
    	}
    	this = resizedHashTable;
    }
}
