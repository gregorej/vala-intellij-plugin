class SomeLibraryClass {
	public int get_size();
}

int main(string [] args) {
	SomeLibraryClass cl = new SomeLibraryClass();
	var a = cl.get_<caret>size();
}