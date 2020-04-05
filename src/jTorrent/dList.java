package jTorrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class dList implements List<DecodedValue> {
	
	List<DecodedValue> dList;
	
	public dList() {
		dList = new LinkedList<DecodedValue>();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return dList.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return dList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return dList.contains(o);
	}

	@Override
	public Iterator<DecodedValue> iterator() {
		// TODO Auto-generated method stub
		return dList.iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return dList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return dList.toArray(a);
	}

	@Override
	public boolean add(DecodedValue e) {
		// TODO Auto-generated method stub
		return dList.add(e);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return dList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return dList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends DecodedValue> c) {
		// TODO Auto-generated method stub
		return dList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends DecodedValue> c) {
		// TODO Auto-generated method stub
		return dList.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return dList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return dList.retainAll(c);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		dList.clear();
	}

	@Override
	public DecodedValue get(int index) {
		// TODO Auto-generated method stub
		return dList.get(index);
	}

	@Override
	public DecodedValue set(int index, DecodedValue element) {
		// TODO Auto-generated method stub
		return dList.set(index, element);
	}

	@Override
	public void add(int index, DecodedValue element) {
		// TODO Auto-generated method stub
		dList.add(index, element);
	}

	@Override
	public DecodedValue remove(int index) {
		// TODO Auto-generated method stub
		return dList.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return dList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return dList.lastIndexOf(o);
	}

	@Override
	public ListIterator<DecodedValue> listIterator() {
		// TODO Auto-generated method stub
		return dList.listIterator();
	}

	@Override
	public ListIterator<DecodedValue> listIterator(int index) {
		// TODO Auto-generated method stub
		return dList.listIterator();
	}

	@Override
	public List<DecodedValue> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return dList.subList(fromIndex, toIndex);
	}

	@Override
	public String toString() {
	String output = "";
		
		for (DecodedValue decodedValue : dList) {
			output += "length: " + decodedValue.getLength() + " - " + "contents: " + decodedValue.getContents() + "\n";
		}
		return output;
	}
}
