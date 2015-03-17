class MyClass {
	public int field;
}

int main(string [] args) {
	var inferred = new MyClass();
	inferred.fie<caret>ld = 22;
	return 0;
}