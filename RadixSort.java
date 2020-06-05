import java.io.*;
import java.util.*;
import java.lang.*;

class RadixSort
{
	int tableSize;
	LLQ hashTable[][];
	int data;
	int currentTable;
	int previousTable;
	int maxDigits;
	int offset;
	int currentDigit;

	RadixSort()
	{
		data 	      = -1;
		maxDigits     = -1;
		tableSize     = 10;
		currentDigit  = 0;
		currentTable  = 0;
		previousTable = 1;
		hashTable     = new LLQ[2][tableSize];

		for(int i = 0; i < 2; i++){
			for(int j = 0; j < tableSize; j++){
				LLQ temp = new LLQ();
				hashTable[i][j] = temp;
			}
		}
	}

	public int firstReading(String inputFileName, int maxNum) throws IOException
	{
		Scanner inFile = new Scanner( new FileReader(inputFileName) );
		int minNum     = 0;
		maxNum         = 0;
		String str;

		while(inFile.hasNext()){

			str  =  inFile.next();
			data = Integer.parseInt(str);
		
			if(data > maxNum)
				maxNum = data;
			if(data < minNum)
				minNum = data;
		}

		inFile.close();
		offset = Math.abs(minNum);

		return maxNum;
	}

	public listNode loadStack(String inputFileName, String outputFileName) throws IOException{

		LLStack s  = new LLStack();
		String str = "";

		int maxNum = 0;
		maxNum     = firstReading( inputFileName,maxNum );
		maxNum    += offset;

		Scanner inFile = new Scanner( new FileReader(inputFileName) );

		while(inFile.hasNext()){

			str  =  inFile.next();
			data = Integer.parseInt(str);
			data += offset;

			listNode newNode = new listNode(data);
			s.Push(newNode);
		}
		
		String header = "*** Below is the constructed stack ***\r\n";
		print(header,outputFileName);
		print(s.printStack(),outputFileName);

		inFile.close();

		int d = 0;

		while(maxNum > 0 ){
			maxNum /= 10;
			d++;
		}

		maxDigits = d;

		return s.top;
	}

	public int getMaxDigits(){

		return maxDigits;
	}

	public int getDigit(listNode node, int currentDigit){

		int d = node.data;

		for(int i = 0; i < currentDigit; i++)
			d/= 10;

		return (d % 10);
	}

	public int hashIndex(int index){

		return index % 10;
	}

	public String printTable(int tableIndex){

		String line = "";

		for(int i = 0; i < tableSize; i++){
			if( !hashTable[tableIndex][i].isEmpty(hashTable[tableIndex][i]) )
				line += hashTable[tableIndex][i].printQueue(i);
		}
	
		return line;
	}

	public void dumpStack(listNode Top, int currDigit, int currTable, String outputFileName) throws IOException{

		listNode ptr = Top;
		int digit;
		int index;

		while(ptr != null){

			digit         = getDigit(ptr, currDigit);
			index         = hashIndex(digit);
			listNode node = new listNode(ptr.data);
			hashTable[currTable][index].addTail(hashTable[currTable][index], node);

			ptr = ptr.next; // Advance to the next node in the list
		}

		currentDigit++;

		//Print the non-empty queues

		String line = printTable(currentTable);
		print(line, outputFileName);
	}

	public void RxSort(String outputFileName) throws IOException{

		while(currentDigit < maxDigits){

			//Swapping the working hashTable
			int tempTableIndex = currentTable;
			currentTable       = previousTable;
			previousTable      = tempTableIndex;

		//Scanning throught the current table
		for(int i = 0; i < tableSize; i++){

			//export every node until queue is empty
			if(!hashTable[previousTable][i].
					isEmpty( hashTable[previousTable][i]) ){

				while(!hashTable[previousTable][i].
						isEmpty(hashTable[previousTable][i])){

					int curT	   = currentTable;
					int preT           = previousTable;

					int tempData       = hashTable[preT][i].
						deleteFront(hashTable[preT][i]).data;

					listNode tempNode = new listNode(tempData);
					int tempDigit      = getDigit(tempNode,currentDigit);
					int tempHashIndex  = hashIndex(tempDigit);

					hashTable[curT][tempHashIndex].
						addTail(hashTable[curT][tempHashIndex], tempNode);
				}
			}
		}

		currentDigit++;
	}

	String line = "\r\n\r\nFinal hash table:\r\n";
	line += printTable(currentTable);
	print(line, outputFileName);
}

	public class listNode{

		int data;
		listNode next;

		listNode(){

			data = -1;
			next = null;
		}

		listNode(int data){

			this.data = data;
			next = null;
		}
	}

	public class LLStack{

		listNode top;
		
		LLStack(){

			top = null;
		}

		public void Push(listNode newNode){

			newNode.next = top;
			top = newNode;
		}

		public listNode Pop(){

			listNode temp = top;

			if(temp == null)
				System.out.println("Stack is empty!");
			else
				top = top.next;

			return temp;
		}

		public Boolean isEmpty(){

			if(top == null)
				return true;
			else
				return false;
		}

		public String printStack(){

			String line = "Top -> ";
			listNode ptr = top;
			
			while(ptr != null){

				line += "(";
				line += ptr.data;
				line += ", ";
			
				if(ptr.next == null)
					line+= "NULL) -> ";
				else
					line += ptr.next.data + ") -> ";
				ptr = ptr.next;
			}
		
			line += "NULL\r\n";

			return line;
		}
	}

	public class LLQ{

		listNode head;
		listNode tail;
		
		LLQ(){
			listNode dummy = new listNode();
			head = dummy;
			tail = head;
		}

		public void addTail(LLQ Q, listNode node){

			tail.next = node;
			tail = Q.tail.next;
		}

		public listNode deleteFront(LLQ Q){

			listNode temp = Q.head.next;
			
			if(temp == null)
				System.out.println("The queue is full!");

			else if(temp == Q.tail){

				Q.head.next = Q.head.next.next;
				Q.tail = Q.head;
			}

			else
				Q.head.next = Q.head.next.next;

			return temp;
		}

		public Boolean isEmpty(LLQ Q){

			if(Q.tail != Q.head)
				return false;
			else
				return true;
		}

		public String printQueue(int index){

			String line = "Front(";
			line += index + ") -> ";
			
			listNode ptr = head.next;
			
			while(ptr != null){

				line += "(";
				line += ptr.data;
				line += ", ";

				if(ptr.next == null)
					line += "NULL) -> ";
				else
					line += ptr.next.data + ") -> ";
				ptr = ptr.next;
			}

			line += "NULL\r\n";
			line += index + ") -> ";
			ptr = tail;
			
			if(ptr == null || ptr.data == -1)
				line += "NULL\r\n";
			else
				line += "(" + ptr.data + ", NULL) -> NULL\r\n";

			return line;
		}
	}

	public static void print(String content, String FileName) throws IOException{

		BufferedWriter outFile = new BufferedWriter( new FileWriter(FileName,true) );
		outFile.write(content);
		outFile.close();
	}
	
	public static void main(String[] args) throws IOException{

		RadixSort R = new RadixSort();
		listNode Top;

		Top = R.loadStack(args[0], args[2]);
		R.dumpStack(Top, R.currentDigit, R.currentTable, args[2]);
		R.RxSort(args[2]);

		//Outputing sorted numbers
		String sorted_line = "";
		int skewedData;
		int correctData;
		for(int i = 0; i < R.tableSize; i++){
			if(!R.hashTable[R.currentTable][i].isEmpty(R.hashTable[R.currentTable][i])){

				listNode ptr = R.hashTable[R.currentTable][i].head.next;
				while(ptr != null){

					skewedData   = ptr.data;
					correctData  = skewedData - R.offset;
					sorted_line += correctData;
					sorted_line += "\r\n";
					ptr          = ptr.next;
				}
			}	
		}
	print(sorted_line, args[1]);
	
	}
}
				
