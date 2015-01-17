
class MyObject {
	private int field;
}

int main(string [] args) {
	MyObject o = new MyObject();
	o.fie<caret>ld = 13;
	return o.field;
}