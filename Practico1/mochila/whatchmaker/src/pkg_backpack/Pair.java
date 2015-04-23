package pkg_backpack;

public class Pair<FIRST> implements Comparable<Pair<FIRST>> {

    public final FIRST first;
    public final FIRST second;

    public Pair(FIRST first, FIRST second) {
        this.first = first;
        this.second = second;
    }

    public static <FIRST> Pair<FIRST> of(FIRST first,
            FIRST second) {
        return new Pair<FIRST>(first, second);
    }

    public int compareTo(Pair<FIRST> o) {
        int cmp = compare(first, o.first);
        return cmp == 0 ? compare(second, o.second) : cmp;
    }

    // todo move this to a helper class.
    private static int compare(Object o1, Object o2) {
        return o1 == null ? o2 == null ? 0 : -1 : o2 == null ? +1
                : ((Comparable) o1).compareTo(o2);
    }

    public int hashCode() {
        return 31 * hashcode(first) + hashcode(second);
    }

    // todo move this to a helper class.
    private static int hashcode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        if (this == obj)
            return true;
        return equal(first, ((Pair) obj).first)
                && equal(second, ((Pair) obj).second);
    }

    // todo move this to a helper class.
    private boolean equal(Object o1, Object o2) {
        return o1 == null ? o2 == null : (o1 == o2 || o1.equals(o2));
    }

    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}