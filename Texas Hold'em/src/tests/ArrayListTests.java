package tests;

import java.util.ArrayList;

public class ArrayListTests {

	public static void main(String[] args) {
		
		ArrayList<Integer> al = new ArrayList<Integer>();
		al.add(10);
		al.add(5);
		al.add(4);
		al.add(6);
		al.add(5);
		al.add(5);
		al.add(7);
		al.add(11);
		
		System.out.println(al);
		
		for (int x = 0; x < al.size(); x++) {
			if (al.get(x) == 5) {
				al.remove(x);
				x--;
			}
		}
		System.out.println(al);
		
		
		
		

	}

}
