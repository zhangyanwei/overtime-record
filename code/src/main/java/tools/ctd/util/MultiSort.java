package tools.ctd.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

public class MultiSort {

	public static <T> void sort(List<T> list, final String[] properties,
			final boolean[] asc) {

		Collections.sort(list, new Comparator<T>() {
			public int compare(T o1, T o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}

				if (o1 == null) {
					return -1;
				}

				if (o2 == null) {
					return 1;
				}

				for (int i = 0; i < properties.length; i++) {
					String property = properties[i];

					@SuppressWarnings("unchecked")
					Comparator<Object> c = new BeanComparator(property);
					int result = c.compare(o1, o2);

					if (result != 0) {
						return result * (asc[i] ? 1 : -1);
					}
				}

				return 0;
			}
		});
	}

}
