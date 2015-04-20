class Inner {
	public int method() {
	}
}

class Wrapper {
	public Inner inner = new Inner();
}


int main(string [] args) {
	var obj = new Wrapper();
	obj.inner.method()<caret>
}