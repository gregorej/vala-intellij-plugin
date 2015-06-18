class MyClass {
	void method();
}

int main(string [] args) {
	var wrongName = new MyClass();
	wrong<caret>Name.method();
}