package tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import datamodel.Triple;

public class Shuffle {
	public static Triple[] arrayShuffle(Triple[] paraTriple){
		List tempList=new ArrayList();
		for (int i = 0; i < paraTriple.length; i++) {
			tempList.add(paraTriple[i]);
		}
		Collections.shuffle(tempList);
		Iterator itor=tempList.iterator();
		int i=0;
		while(itor.hasNext()){
			paraTriple[i]=(Triple) itor.next();
			i++;
		}
		return paraTriple;
	}
}
