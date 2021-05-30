package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/*Using a Trie data structure to provide auto-complete feature for facility names*/

@Service
public class Autocomplete {  
	
	private Node root;
	
	public Autocomplete() {
		
		root = new Node();
		
	}
	
	private class Node {
		
		HashMap<Character,Node> next;
		boolean f;
		String word;
		
		Node()
		{
			next = new HashMap<>();
			f = false;
			word = "";
		}
	
	}
	
	public void buildTrie(List<String> list) throws IOException
	{
		for(String s : list)
			add(s);
	}	
	
	private void add(String line)
	{
		int n = line .length();
		Node curr = root;
		for(int i = 0; i<n; i++)
		{
			char ch = Character.toLowerCase(line.charAt(i));
			
			if(!(curr.next).containsKey(ch))
				(curr.next).put(ch,new Node());
			
			curr = (curr.next).get(ch);
			
		}
		
		curr.f = true;
		curr.word = line;
	}
	
	public ArrayList<String> complete (String line)
	{
		ArrayList<String> words = new ArrayList<>();
		Node curr = root;
		int n = line.length();
		int i = 0;
		
		while(i<n)
		{
			char ch = Character.toLowerCase(line.charAt(i));
			
			if(!(curr.next).containsKey(ch))
				return new ArrayList<>();
			
			curr = (curr.next).get(ch);
			i++;
		}
		
		return bfs(curr); 
	}
	
	private ArrayList<String> bfs (Node start)
	{
		ArrayList<String> list = new ArrayList<>();
		Node curr = start;
		Queue<Node> q = new LinkedList<>();
		q.add(curr);
		
		while(!q.isEmpty())
		{
		   Node n = q.poll();
		   if(n.f)
			   list.add(n.word);
		   
		   for(char ch : n.next.keySet())
		     q.add((n.next).get(ch));
		}
		
		return list;
	}
	
	public static void main(String args[]) throws IOException
	{
		
	}


}
