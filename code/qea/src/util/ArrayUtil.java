package util;

import structure.impl.other.FBindingImpl;
import structure.impl.other.Transition;

public class ArrayUtil {

	/**
	 * Creates a new array of the specified size with a copy of the content of
	 * the specified array
	 * 
	 * @param array
	 *            Original array
	 * @param size
	 *            Size of the array to be created
	 * @return The specified array if its size is equal to <code>size</code>.
	 *         Otherwise, a new array of the specified size with a copy of the
	 *         content of the specified array
	 */
	public static Object[] resize(Object[] array, int size) {
		if (array.length == size) {
			return array;
		}
		Object[] resizedArray = new Object[size];
		int sizeToCopy = array.length < size ? array.length : size;
		System.arraycopy(array, 0, resizedArray, 0, sizeToCopy);
		return resizedArray;
	}

	public static int[] resize(int[] array, int size) {
		if (array.length == size) {
			return array;
		}
		int[] resizedArray = new int[size];
		int sizeToCopy = array.length < size ? array.length : size;
		System.arraycopy(array, 0, resizedArray, 0, sizeToCopy);
		return resizedArray;
	}

	public static FBindingImpl[] resize(FBindingImpl[] array, int size) {
		if (array.length == size) {
			return array;
		}
		FBindingImpl[] resizedArray = new FBindingImpl[size];
		int sizeToCopy = array.length < size ? array.length : size;
		System.arraycopy(array, 0, resizedArray, 0, sizeToCopy);
		return resizedArray;
	}

	public static Transition[] resize(Transition[] array, int size) {
		if (array.length == size) {
			return array;
		}
		Transition[] resizedArray = new Transition[size];
		int sizeToCopy = array.length < size ? array.length : size;
		System.arraycopy(array, 0, resizedArray, 0, sizeToCopy);
		return resizedArray;
	}

	/**
	 * Concatenates the specified arrays into one single array
	 * 
	 * @param array1
	 *            First array
	 * @param array2
	 *            Second array
	 * @return A new array whose size is equal to the sum of the sizes of the
	 *         specified arrays with a copy of their content
	 */
	public static int[] concat(int[] array1, int[] array2) {
		int[] result = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

	public static Transition[] concat(Transition[] array1, Transition[] array2) {
		Transition[] result = new Transition[array1.length + array2.length];
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

}
