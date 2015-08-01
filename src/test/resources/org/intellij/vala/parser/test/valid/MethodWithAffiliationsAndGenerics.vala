class MyClass {

	public unowned T? next_value () {
		void* vi = &this;
		GLib.HashTableIter<unowned T,T>* htp = vi;
		unowned T? value;
		return htp->next (out value, null) ? value : null;
	}
}