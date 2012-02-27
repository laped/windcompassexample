package dk.laped.windcompass;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

public class EnumUtils {
	public static <E extends Enum<E>> int encode(EnumSet<E> set) {
		int ret = 0;

		for (E val : set) {
			ret |= 1 << val.ordinal();
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> EnumSet<E> decode(int code,
			Class<E> enumType) {
		try {
			E[] values = (E[]) enumType.getMethod("values").invoke(null);
			EnumSet<E> result = EnumSet.noneOf(enumType);
			while (code != 0) {
				int ordinal = Integer.numberOfTrailingZeros(code);
				code ^= Integer.lowestOneBit(code);
				result.add(values[ordinal]);
			}
			return result;
		} catch (IllegalAccessException ex) {
			// Shouldn't happen
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			// Probably a NullPointerException, caused by calling this method
			// from within E's initializer.
			throw (RuntimeException) ex.getCause();
		} catch (NoSuchMethodException ex) {
			// Shouldn't happen
			throw new RuntimeException(ex);
		}
	}
}
