class MyClass {
}

MyClass createFromFile() {
	return null;
}

MyClass createFromService() {
	return null;
}

int main(string [] args) {
	var obj = createFromFile() <caret>?? createFromService() ?? new MyClass();
}