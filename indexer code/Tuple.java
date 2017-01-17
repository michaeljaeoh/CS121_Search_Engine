// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

public class Tuple<T> {
	public int id;
	public T freq;

	public Tuple(T freq, int id) {
		this.freq = freq;
		this.id = id;
	}
	@Override
	public String toString() {
		return id + ":" + freq;
	}
}
